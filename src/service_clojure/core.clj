(ns service-clojure.core
  (:require [service-clojure.server :as server]))

(defn -main [& args]
  ((try
     (server/start-server)
     (catch Exception e
       (println "Server Start error" (.getMessage e))))))
