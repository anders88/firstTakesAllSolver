import httplib
import json

# Insert your spesific values
SERVER="localhost:8081"
PLAYER_ID="0125340"

def computeAnswer(question):
	# Make your implementation here
	return question

# Getting questions
category = "Echo"
conn = httplib.HTTPConnection(SERVER)
conn.request("GET", "/game?category=" + category + "&playerid=" + PLAYER_ID)
r1 = conn.getresponse()
questionstr = r1.read();
questions = json.loads(questionstr)
print questions

# Computing answers
answers=[]
for q in questions:
	answers.append(computeAnswer(q))
answerjson = json.dumps({"playerId" : PLAYER_ID, "answers": answers})
print answerjson

# Posting answers to server
postconn = httplib.HTTPConnection(SERVER)
postconn.request("POST", "/game",answerjson);
postresp = postconn.getresponse()
status = postresp.read();
print status