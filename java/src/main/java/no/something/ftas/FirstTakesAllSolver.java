package no.something.ftas;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

public class FirstTakesAllSolver {
	private static final String BASE_URL="http://localhost:8081/";
	private static final String PLAYER_ID="1";

	public static void main(String[] args) throws Exception {
		String questionId = "2000001";
		String answer = "024ff09e-0271-4a8b-aeb4-f4cdb8890413";
		new FirstTakesAllSolver().sendAnswer(questionId,answer);
	}

	private void sendAnswer(String questionId, String answer) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(BASE_URL + "game/");
		postMethod.setParameter("gamerId", "verdi");
		
		int result = client.executeMethod(postMethod);
		
		
	}

}
