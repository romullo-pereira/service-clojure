(ns service-clojure.routes.task.handlers
  (:require [service-clojure.routes.task.commons :as commons]
            [clojure.data.json :as json]))

(defn hello-fn [request]
  {:status 200 :body (str "Hello World "
                          (get-in request [:query-params :name]
                                  "Everybody!"))})

#_(defn make-response [status body]
    {:status  status
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str body)})

(defn make-task [id name status]
  {:id id :name name :status status})


(defn create-task [{:keys [query-params store]}]
  (let [{:keys [name status]} query-params
        id (commons/get-uuid)
        task (make-task id name status)]
    (swap! store assoc id task)
    {:status 200 :body task}))

(defn update-task [{:keys [path-params query-params store]}]
  (let [{:keys [id]} path-params
        {:keys [name status]} query-params
        uuid (commons/string->uuid id)
        task (make-task uuid name status)]
    (swap! store assoc uuid task)
    {:status commons/created :body {:message "Task updated"
                                    :task    task}}))

(defn get-tasks [{:keys [store]}]
  (if-not (empty? (deref store))
    {:status commons/ok :body @store}
    {:status commons/no-content :body []}))

(defn delete-task [{:keys [path-params store]}]
  (let [{:keys [id]} path-params]
    (->> id
         commons/string->uuid
         (swap! store dissoc))
    {:status commons/ok :body {:message "Task deleted"}}))
