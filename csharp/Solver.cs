using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web.Script.Serialization;

namespace FtaSolver
{
    public class Solver
    {
        // REPLACE WITH SERVER ADDRESS
        private const String ApplicationUrl = "http://codingquest.herokuapp.com/";
        // REPLACE WITH YOUR PLAYER ID
        private const String PlayerId = "0125442";

        private static readonly WebClient Client = new WebClient();
        private static readonly JavaScriptSerializer Serializer = new JavaScriptSerializer();

        private static string CreateResponse(IEnumerable<string> answers)
        {
            return Serializer.Serialize(new Dictionary<string, object>
            {
                {"playerId", PlayerId},
                {"answers", answers.ToList()}
            });
        }


        public static void Main()
        {
            var answer = (IAnswer)new EchoAnswer();
            const string category = "Echo";

            const string questionUrl = ApplicationUrl + "game?playerid=" + PlayerId + "&category=" + category;
            var questionString = Client.DownloadString(questionUrl);
            Console.WriteLine("Question from server: " + questionString);
            var questions = Serializer.Deserialize<string[]>(questionString);

            var answers = answer.AnswerQuestions(questions);
            
            var answerString = CreateResponse(answers);
            Console.WriteLine("Answering: " + answerString);
            var response = Client.UploadString(ApplicationUrl + "game", answerString);
            Console.WriteLine("Response from server: " + response);
            
            Console.WriteLine("Press enter");
            Console.In.Read();
        }
    }

    internal interface IAnswer
    {
        IEnumerable<string> AnswerQuestions(IEnumerable<string> questions);
    }

    public class EchoAnswer : IAnswer
    {
        public IEnumerable<string> AnswerQuestions(IEnumerable<string> questions)
        {
            return questions.Select(AnswerSingleQuestion);
        }

        private string AnswerSingleQuestion(string question)
        {
            return "";
        }
    }
}