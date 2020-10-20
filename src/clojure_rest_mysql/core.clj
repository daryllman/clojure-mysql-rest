(ns clojure-rest-mysql.core
  (:require [clojure-rest-mysql.reviews.model :as reviews]
            [clojure-rest-mysql.reviews.handler :refer [handle-all-reviews
                                                        handle-create-review]])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]
            [next.jdbc :as jdbc]))

; Initialise database -> datasource
(def db {:dbtype "mysql" :dbname "kindle-reviews" :user "root" :password "1234" :serverTimezone "UTC"})
(def ds (jdbc/get-datasource db))


(defn greet [req]
  {:status 200
   :body "Hello world"
   :headers {}})
(defn yo [req]
  (let [name (get-in req [:route-params :name])]
    {:status 200
     :body (str "Yo " name "!")
     :headers {}}))

; REST routes
(defroutes routes
  (GET "/" [] greet)
  (GET "/request" [] handle-dump)
  (GET "/yo/:name" [] yo)

  (GET "/reviews" [] handle-all-reviews)
  (POST "/reviews" [] handle-create-review)

  (not-found "Page not found"))

(defn wrap-db [hdlr]
  (fn [req]
    (hdlr (assoc req :clojure-rest-mysql/ds ds))))

(defn wrap-server [hdlr]
  (fn [req]
    (assoc-in (hdlr req) [:headers "Server"] "Kindle Reviews MySQL Server")))

(def app
  (wrap-server (wrap-db (wrap-params routes))))

(defn -main [port]
  (reviews/create-table ds)
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (reviews/create-table ds)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))