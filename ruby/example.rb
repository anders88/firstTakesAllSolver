# remember to run `bundle`

require './ftas'

ftas = FirstTakesAllSolver.new(2428597)

questions = ftas.get_questions_from_category("Echo")

# do work
answers = questions

ftas.send_answers(answers)
