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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FirstTakesAllSolver {
	private static final String BASE_URL="http://www.anderssandbox.com:8080/fta/";
	private static final String PLAYER_ID="0239042";

	public static void main(String[] args) throws Exception {
        String category = "Echo";
        JSONArray question = readQuestions(category);
        JSONArray answerToQuestions  = calculateAnswer(question);
        JSONObject answer = new JSONObject();
        answer.put("playerId", PLAYER_ID);
        answer.put("answers", answerToQuestions);
        System.out.println(answer);
        String res = httpPost(FirstTakesAllSolver.BASE_URL + "game", answer.toString());
        System.out.println(res);
    }

    private static JSONArray calculateAnswer(JSONArray question) {
        return question;
    }

    private static JSONArray readQuestions(String category) throws JSONException, IOException {
        String questionUrl = FirstTakesAllSolver.BASE_URL +
                "game?playerid=" + FirstTakesAllSolver.PLAYER_ID +
                "&category=" +category;
        System.out.println("Reading URL " + questionUrl);
        JSONArray question = new JSONArray(readUrl(new URL(questionUrl)));
        System.out.println(question);
        return question;
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
