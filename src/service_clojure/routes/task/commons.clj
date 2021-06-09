(ns service-clojure.routes.task.commons)

(defn get-uuid []
  (java.util.UUID/randomUUID))

(defn string->uuid [string]
  (java.util.UUID/fromString string))


(def ok 200)
(def created 201)
(def no-content 204)
(def bad-request 400)
(def not-found 404)
