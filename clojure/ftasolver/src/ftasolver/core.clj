(ns ftasolver.core
(:use 
   [cheshire.core :only [generate-string parse-string]]
   )
	)



(def playerid "1")
(def server-address "http://localhost:8081/")

(defn read-questions [category]
	(parse-string (slurp (str server-address "game?playerid=" playerid "&category=" category)))
	)

(defn -main [& m]
	(read-questions "Echo")
	)