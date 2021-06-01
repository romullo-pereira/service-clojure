(ns service-clojure.interceptors
  (:require [service-clojure.database.atom :as db]))

(defn assoc-store [context]
  (update context :request assoc :store db/store))

(def database-interceptor
  {:name  :db-interceptor
   :enter assoc-store})
