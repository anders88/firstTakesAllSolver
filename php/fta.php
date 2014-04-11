<?php
define('PLAYER_ID', '1234567');
define('URL', 'http://localhost:8081');

function getQuestions($category) {
	$query = http_build_query(array(
		'category' => $category,
		'playerid' => PLAYER_ID));
	$url = URL . '/game?' . $query;
	return json_decode(file_get_contents($url));
}

function postAnswers($answers) {
	$params = array('http' => array(
		'method' => 'POST',
		'content' => json_encode(array(
			'playerId' => PLAYER_ID,
			'answers' => $answers
		))
	));
	$ctx = stream_context_create($params);
	$response = file_get_contents(URL . '/game', false, $ctx);
	return json_decode($response);
}

function solve_echo($question) {
	return $question;
}

// Fetch questions
$questions = getQuestions('Echo');
echo 'Questions: ' . json_encode($questions) . "\n";

// Compute answers
$answers = array();
foreach ($questions as $question) {
	array_push($answers, solve_echo($question));
}
echo 'Answers:   ' . json_encode($answers) . "\n";

// Send answers
$result = postAnswers($answers);
echo 'Result:    ' . json_encode($result) . "\n";
?>
