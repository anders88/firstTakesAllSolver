package no.something.ftas;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class FirstTakesAllSolver {
	private static final String BASE_URL="http://localhost:8081/";
	private static final String PLAYER_ID="1";

	public static void main(String[] args) throws Exception {
		//String questionId = "2000001";
		//String answer = "024ff09e-0271-4a8b-aeb4-f4cdb8890413";
		//new FirstTakesAllSolver().sendAnswer(questionId,answer);
        //new FirstTakesAllSolver().testNew();
        List<String> questions = new FirstTakesAllSolver().readQuestions("Echo");
        System.out.println(questions);
        System.out.println(questions.size());

    }

    private List<String> readQuestions(String categoryid) throws  Exception {
        String jsonQuestions =  readUrl(new URL(BASE_URL + "game?playerid=" + PLAYER_ID + "&category=" + categoryid));
        Gson gson = new Gson();
        List<String> answers = gson.fromJson(jsonQuestions, new TypeToken<List<String>>() {}.getType());

        return answers;
    }

    private String readUrl(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        Reader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
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

    private String postJson(String url,String json) {
        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpPost request = new HttpPost(BASE_URL + "game/something");
            StringEntity params =new StringEntity("details={\"name\":\"myname\",\"age\":\"20\"} ");

            request.setEntity(params);

            httpClient.execute(request);

            return null;
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

	private String sendAnswer(String questionId, String answer) throws ClientProtocolException, IOException {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(BASE_URL + "game/"); 

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("gamerId", PLAYER_ID));
		params.add(new BasicNameValuePair("questionId", questionId));
		params.add(new BasicNameValuePair("answer", answer));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		httpPost.setEntity(entity);
		
		HttpResponse result = defaultHttpClient.execute(httpPost);
		
		InputStream content = result.getEntity().getContent();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content));
		
		StringBuilder res=new StringBuilder();
		while(bufferedReader.ready()) {
			res.append(bufferedReader.readLine());
		}
		
		System.out.println(res);
		return res.toString();
		
		
	}

}
