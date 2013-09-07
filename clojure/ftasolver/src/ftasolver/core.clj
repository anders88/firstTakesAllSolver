(ns ftasolver.core
(:use [cheshire.core :only [generate-string parse-string]]))



(def playerid "1")
(def server-address "http://localhost:8081/")

(defn read-questions [category]
	(parse-string (slurp (str server-address "game?playerid=" playerid "&category=" category)))
)

(defn post-json [address json]
	(let [connection (doto (.openConnection (new java.net.URL address)) (.setDoOutput true))]
		(doto (new java.io.PrintWriter (new java.io.OutputStreamWriter (.getOutputStream connection) "utf-8"))
			(.append json) (.close))
		(slurp (.getInputStream connection))
	)
)

(defn answer-json [answers]
	(generate-string {
		:playerId playerid
		:answers answers
		})
	)

(defn post-answers [answers]
	(post-json (str server-address "game") (answer-json answers))
	)

(defn -main [& m]
	(post-answers (read-questions "Echo"))
	)