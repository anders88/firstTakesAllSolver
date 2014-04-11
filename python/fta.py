import json
from urllib import urlencode, urlopen

# Insert your specific values
SERVER = "http://localhost:8081"
PLAYER_ID = "1234567"


def get_questions(category):
    """Fetch questions from server"""
    response = urlopen(SERVER + '/game?' + urlencode({'category': category, 'playerid': PLAYER_ID}))
    if 200 <= response.getcode() <= 299:
        return json.load(response)
    else:
        raise Exception('Error #%d: %r' % (response.getcode(), response.read()))


def post_answers(answers):
    """Post answers to server"""
    response = urlopen(SERVER + '/game', json.dumps({"playerId": PLAYER_ID, "answers": answers}))
    if 200 <= response.getcode() <= 299:
        return json.load(response)
    else:
        raise Exception('Error #%d: %r' % (response.getcode(), response.read()))


def solve_echo(question):
    return question


def main():
    questions = get_questions('Echo')
    print 'Questions:', questions
    answers = [solve_echo(q) for q in questions]
    print 'Answers:  ', answers
    result = post_answers(answers)
    print 'Result:   ', result


if __name__ == '__main__':
    main()
