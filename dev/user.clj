(ns user
  (:require
    [service-clojure.server :as s]
    [io.pedestal.http :as http]
    [io.pedestal.test :as test]))


(def service-map
    {::http/routes s/routes
     ::http/port   9999
     ::http/type   :jetty
     ::http/join?  false})

(defn start-server []
  (reset! s/server (http/start (http/create-server service-map))))

(defn stop-server []
  (http/stop @s/server))

(defn restart []
  (stop-server)
  (start-server))

(defn test-request [verb url]
  (test/response-for (::http/service-fn @s/server) verb url))
