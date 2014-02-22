package no.something.ftas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


class Solver {

    public List<String> calculateAnswer(List<String> questions) {
        List<String> answers = new ArrayList<>();
        for (String q : questions) {
            answers.add(echo(q));
        }
        return answers;
    }

    public static String echo(String q) {
        return "";
    }
}

public class FirstTakesAllSolver {

    private static final String BASE_URL="http://codingquest.herokuapp.com/";
	private static final String PLAYER_ID="1";

	public static void main(String[] args) throws Exception {
        String question= readQuestions("Echo");
        String answerToQuestions  = toJson(new Solver().calculateAnswer(parseQuestions(question)));
        String answer = buildJsonAnswer(answerToQuestions);

        System.out.println(answer);
        String res = httpPost(FirstTakesAllSolver.BASE_URL + "game", answer.toString());
        System.out.println(res);
    }

    private static String buildJsonAnswer(String answerToQuestions) {
        return String.format("{\"playerId\" : \"%s\",\"answers\":%s}",PLAYER_ID,answerToQuestions);
    }

    private static List<String> parseQuestions(String question)  {
        List<String> quelist=new ArrayList<>();
        String [] parts = question.split(",");
        for (String part : parts) {
            String q = part.substring(part.indexOf("\"")+1,part.lastIndexOf("\""));
            quelist.add(q);
        }
        return quelist;
    }

    private static String toJson(List<String> quelist) {
        StringBuilder result = new StringBuilder("[");
        boolean first = true;
        for (String entry : quelist) {
            if (!first) {
                result.append(",");
            }
            first = false;
            result.append("\"");
            result.append(entry);
            result.append("\"");

        }
        result.append("]");
        return result.toString();
    }

    private static String readQuestions(String category) throws IOException {
        String questionUrl = FirstTakesAllSolver.BASE_URL +
                "game?playerid=" + FirstTakesAllSolver.PLAYER_ID +
                "&category=" +category;
        System.out.println("Reading URL " + questionUrl);
        String question = fetchQuestions(questionUrl);
        System.out.println(question);
        return question;
    }

    private static String fetchQuestions(String questionUrl) throws IOException {
        return readUrl(new URL(questionUrl));
    }

    private static String readUrl(URL url) throws IOException {
        return toString(url.openConnection().getInputStream());
    }

    private static String httpPost(String url, String answer) throws Exception {
        URLConnection conn = new URL(url).openConnection();
        conn.setDoOutput(true);
        try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"))) {
            printWriter.append(answer);
        }
        return toString(conn.getInputStream());
    }

    private static String toString(InputStream inputStream) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
            StringBuilder result = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                result.append((char)c);
            }
            return result.toString();
        }
    }
}
