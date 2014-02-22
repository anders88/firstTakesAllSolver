var http = require("http");

var PLAYER_ID = "0150835"; // Your given player id
var HOSTNAME = "localhost"; // FTA-Server address
var PORT = 8081; // FTA-server-port


function getQuestions(category, callback) {
	var getoptions = {
	  hostname: HOSTNAME,
	  port: PORT,
	  path: '/game?category=' + category + '&playerid=' + PLAYER_ID,
	  method: 'GET'
	};


	var req = http.request(getoptions, function(res) {
	  console.log('STATUS: ' + res.statusCode);
	  console.log('HEADERS: ' + JSON.stringify(res.headers));
	  res.setEncoding('utf8');
	  res.on('data', function (chunk) {
	    console.log('QUESTIONBODY: ' + chunk);
	  	callback(JSON.parse(chunk));
	  });
	});

	req.on('error', function(e) {
	  console.log('problem with request: ' + e.message);
	});


	req.end();
}

function answerQuestions(answers) {
	var data = JSON.stringify({
		playerId: PLAYER_ID,
		answers: answers
	});
	console.log("Answering " + data);

	var postoptions = {
	  hostname: HOSTNAME,
	  port: PORT,
	  path: '/game',
	  method: 'POST'
	};


	var req = http.request(postoptions, function(res) {
	  console.log('STATUS: ' + res.statusCode);
	  console.log('HEADERS: ' + JSON.stringify(res.headers));
	  res.setEncoding('utf8');
	  res.on('data', function (chunk) {
	    console.log('ANSWERBODY: ' + chunk);
	  });
	});

	req.on('error', function(e) {
	  console.log('problem with request: ' + e.message);
	});

	// write data to request body
	req.write(data);
	req.end();
} 

function computeAnswer(question) {
	// Do your implementation here
	return question;
}

getQuestions("Echo",function(questions) {
	var answers = [];
	for (var i=0;i<questions.length;i++) {
		var answer = computeAnswer(questions[i]);
		answers.push(answer);
	}
	answerQuestions(answers);
});




