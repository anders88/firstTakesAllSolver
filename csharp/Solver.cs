using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace FirstTakesAllSolver
{
    public class Solver
    {
        // REPLACE WITH SERVER ADDRESS
        private const String APPLICATION_URL = "http://localhost:8081/";

        // REPLACE WITH YOUR PLAYER ID
        private const String PLAYER_ID = "1";

        public Solver()
        {
        }

        public string ReadUrl(string url)
        {
            var client = new WebClient();
            var data = client.OpenRead(url);
            using (StreamReader sr = new StreamReader(data))
            {
                return sr.ReadToEnd();
            }
        }

        public List<string> readQuestions(String category) {
            String json = ReadUrl(APPLICATION_URL + "game?playerid=" + PLAYER_ID + "&category=" + category);
            Console.Out.WriteLine("QESTIONS");
            Console.Out.WriteLine(json);

            List<string> questions = new List<string>();

            int pos = json.IndexOf("\"");

            while (pos != -1)
            {
                int endpos = json.IndexOf("\"", pos + 1);
                String question = json.Substring(pos+1, endpos - pos - 1);
                questions.Add(question);
                pos = json.IndexOf("\"", endpos + 1);
            }


            return questions;
        }

        public String postjson(String url,String json)
        {
            var httpWebRequest = (HttpWebRequest)WebRequest.Create(url);
            httpWebRequest.ContentType = "text/json";
            httpWebRequest.Method = "POST";

            using (var streamWriter = new StreamWriter(httpWebRequest.GetRequestStream()))
            {
                streamWriter.Write(json);
                streamWriter.Flush();
                streamWriter.Close();

                var httpResponse = (HttpWebResponse)httpWebRequest.GetResponse();
                using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
                {
                    var result = streamReader.ReadToEnd();
                    return result;
                }
            }
            
        }

        public String answer(List<string> answers)
        {
            StringBuilder json = new StringBuilder("{\"playerId\":\"");
            json.Append(PLAYER_ID);
            json.Append("\", \"answers\":[");
            bool first = true;
            foreach (string answer in answers)
            {
                if (!first)
                {
                    json.Append(",");
                }
                first = false;
                json.Append("\"");
                json.Append(answer);
                json.Append("\"");
            }

            json.Append("]}");
            Console.Out.WriteLine("POSTING");
            Console.Out.WriteLine(json);
            String response = postjson(APPLICATION_URL + "game",json.ToString());
            return response;
        }


        public static void Main()
        {
            Solver solver = new Solver();
            List<String> questions = solver.readQuestions("Echo");
            String response = solver.answer(questions);
            Console.Out.WriteLine("RESPONSE");
            Console.Out.WriteLine(response);
            Console.In.Read();

        }
    }
}