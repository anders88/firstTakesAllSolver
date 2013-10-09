require 'http'
require 'json'
require 'pp'

class FirstTakesAllSolver
  URL = "http://www.anderssandbox.com:8080/fta/game"

  attr_reader :player_id

  def initialize(player_id)
    @player_id = player_id
  end

  def get_questions_from_category(category)
    pp JSON.load(HTTP.get(URL, :params => { :playerid => player_id, :category => category }))
  end

  def send_answers(answers)
    pp JSON.load(HTTP.post(URL, :body => JSON.dump({ :playerId => player_id, :answers => answers })))
  end
end
