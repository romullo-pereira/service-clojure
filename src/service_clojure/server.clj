(ns service-clojure.server
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [service-clojure.interceptors :as interceptors]
            [service-clojure.routes.task.handlers :as handlers]))

(def routes
  (route/expand-routes
    #{["/hello" :get handlers/hello-fn :route-name :hello-world]
      ["/task" :post [interceptors/database-interceptor handlers/create-task] :route-name :create-task]
      ["/task" :get [interceptors/database-interceptor handlers/get-tasks] :route-name :get-tasks]
      ["/task/:id" :delete [interceptors/database-interceptor handlers/delete-task] :route-name :delete-task]
      ["/task/:id" :patch [interceptors/database-interceptor handlers/update-task] :route-name :update-task]}))

(def service-map
  {::http/routes routes
   ::http/port   9999
   ::http/type   :jetty
   ::http/join?  true})

(defonce server (atom nil))


(defn start-server []
  (http/start (http/create-server service-map)))




