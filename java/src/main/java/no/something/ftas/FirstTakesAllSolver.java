package no.something.ftas;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class FirstTakesAllSolver {
	private static final String BASE_URL="http://localhost:8081/";
	private static final String PLAYER_ID="1";

	public static void main(String[] args) throws Exception {
        FirstTakesAllSolver firstTakesAllSolver = new FirstTakesAllSolver();
        List<String> questions = firstTakesAllSolver.readQuestions("Echo");
        String res = firstTakesAllSolver.postAnswer(questions);

        System.out.println("Got response");
        System.out.println(res);

    }

    private List<String> readQuestions(String categoryid) throws  Exception {
        String jsonQuestions =  readUrl(new URL(BASE_URL + "game?playerid=" + PLAYER_ID + "&category=" + categoryid));
        Gson gson = new Gson();
        List<String> answers = gson.fromJson(jsonQuestions, new TypeToken<List<String>>() {}.getType());

        return answers;
    }

    private static class PlayerAnswerDto {
        @SuppressWarnings("UnusedDeclaration")
        private String playerId;
        @SuppressWarnings("UnusedDeclaration")
        private List<String> answers;

        private PlayerAnswerDto(String playerId, List<String> answers) {
            this.playerId = playerId;
            this.answers = answers;
        }
    }


    private String readUrl(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();
        return readFromStream(inputStream);
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        try {
            StringBuilder result = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                result.append((char)c);
            }
            return result.toString();
        } finally {
            reader.close();
        }
    }

    private String postJson(String urlstr,String json) throws Exception {
        URL url = new URL(urlstr);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));

        printWriter.append(json);
        printWriter.close();

        String res = readFromStream(conn.getInputStream());

        return res;

    }


    private String postAnswer(List<String> answers) throws Exception {
        PlayerAnswerDto playerAnswerDto = new PlayerAnswerDto(PLAYER_ID, answers);
        Gson gson = new Gson();
        String res = postJson(BASE_URL + "game",gson.toJson(playerAnswerDto));
        return res;
    }

}
