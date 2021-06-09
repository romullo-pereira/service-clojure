(ns service-clojure.interceptors
  (:require [service-clojure.database.atom :as db]
            [clojure.data.json :as json]))

(defn assoc-store [context]
  (update context :request assoc :store db/store))

(def database-interceptor
  {:name  :db-interceptor
   :enter assoc-store})

(defn json-response [context]
  (let [{:keys [response]} context
        parsed-body (json/write-str (:body response))
        headers {"Content-Type" "application/json"}]
    (-> context
        (update :response assoc :body parsed-body)
        (update :response assoc :headers headers))))

(def response-json-interceptor
  {:name :json-interceptor
   :leave json-response})