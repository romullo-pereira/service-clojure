(ns service-clojure.server
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [service-clojure.interceptors :as interceptors]
            [service-clojure.routes.task.handlers :as handlers]
            [io.pedestal.interceptor :as i]))

(def routes
  (route/expand-routes
    #{["/hello" :get handlers/hello-fn :route-name :hello-world]
      ["/task" :post handlers/create-task :route-name :create-task]
      ["/task" :get handlers/get-tasks :route-name :get-tasks]
      ["/task/:id" :delete handlers/delete-task :route-name :delete-task]
      ["/task/:id" :patch handlers/update-task :route-name :update-task]}))

(def service-map-base
  {::http/routes routes
   ::http/port   9999
   ::http/type   :jetty
   ::http/join?  false})

(def service-map
  (-> service-map-base
      (http/default-interceptors)
      (update ::http/interceptors conj (i/interceptor interceptors/database-interceptor))))

(defonce server (atom nil))


(defn start-server []
  (http/start (http/create-server service-map)))




