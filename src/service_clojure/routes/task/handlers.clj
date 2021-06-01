(ns service-clojure.routes.task.handlers
  (:require [service-clojure.routes.task.commons :as commons]
            [clojure.data.json :as json]))

(defn hello-fn [request]
  {:status 200 :body (str "Hello World "
                          (get-in request [:query-params :name]
                                  "Everybody!"))})

(defn make-response [status body]
  {:status  status
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str body)})

(defn create-task [{:keys [query-params store]}]
  (let [{:keys [name status]} query-params
        id (commons/get-uuid)
        task (commons/make-task id name status)]
    (swap! store assoc id task)
    (make-response commons/ok {:message "Task created"
                                       :task    task})))

(defn update-task [{:keys [path-params query-params store]}]
  (let [{:keys [id]} path-params
        {:keys [name status]} query-params
        uuid (commons/string->uuid id)
        task (commons/make-task uuid name status)]
    (swap! store assoc uuid task)
    (make-response commons/created {:message "Task updated"
                                :task    task})))

(defn get-tasks [{:keys [store]}]
  (if-not (empty? (deref store))
    (make-response commons/ok @store)
    (make-response commons/no-content [])))

(defn delete-task [{:keys [path-params store]}]
  (let [{:keys [id]} path-params]
    (->> id
         commons/string->uuid
         (swap! store dissoc))
    (make-response 200 {:message "Task deleted"})))
