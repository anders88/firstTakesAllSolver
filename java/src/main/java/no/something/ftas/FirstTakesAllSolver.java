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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FirstTakesAllSolver {
	private static final String BASE_URL="http://localhost:8081/";
	private static final String PLAYER_ID="0110905";

	public static void main(String[] args) throws Exception {
        String category = "Echo";
        JSONArray question = new JSONArray(readQuestions(category));
        String answerToQuestions  = calculateAnswer(question);
        String answer = String.format("{\"playerId\" : \"%s\",\"answers\":%s}",PLAYER_ID,answerToQuestions.toString());

        System.out.println(answer);
        String res = httpPost(FirstTakesAllSolver.BASE_URL + "game", answer.toString());
        System.out.println(res);
    }

    private static List<String> calculateAnswer(List<String> questions) {
        return questions;
    }

    private static String calculateAnswer(JSONArray question) throws JSONException {
        List<String> quelist = parseQuestions(question.toString());
        return toJson(calculateAnswer(quelist));
    }

    private static List<String> parseQuestions(String question) throws JSONException {
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

    private static String readQuestions(String category) throws JSONException, IOException {
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
