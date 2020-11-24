(ns clojure-rest-mysql.core
  (:require [clojure-rest-mysql.reviews.model :as reviews]
            [clojure-rest-mysql.reviews.handler :refer [handle-all-reviews
                                                        handle-create-review
                                                        handle-reviews
                                                        handle-delete-review]])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]
            [next.jdbc :as jdbc]))

; Initialise database -> datasource
(def db {:dbtype "mysql" :dbname "kindle-reviews" :user "root" :password "1234" :serverTimezone "UTC"})
(def ds (jdbc/get-datasource db))


(defn root [req]
  {:body "
          <h1>Available endpoints (kindle-reviews) - MySQL</h1>
          <p>
          <b>/api/all_reviews:</b>
          (this api will not be used) To retrieve all the available reviews from database
          </p>
          
          <p>
          <b>/api/get_reviews:</b>
          Get all reviews of a particular book
          </p>
          
          <p>
          <b>/api/create_review:</b>
          Add a review to a particular book - requires asin, helpful overall, reviewText, reviewTime, reviewerID, reviewerName, summary, unixReviewTime
          </p>
          
          <p>
          <b>/api/delete_review:</b>
          Remove a review to a particular book - requires asin, reviewerID, unixReviewTime
          </p>
          <hr/>
          <h2>Table Model:</h2>
          <p>asin char(10)</p>
          <p>helpful varchar(10) DEFAULT NULL</p>
          <p>overall integer(1) DEFAULT NULL</p>
          <p>reviewText text(1000)</p>
          <p>reviewTime varchar(11)</p>
          <p>reviewerID varchar(14)</p>
          <p>reviewerName varchar(64)</p>
          <p>summary varchar(255) DEFAULT NULL</p>
          <p>unixReviewTime integer(10)</p>
          <p>PRIMARY KEY(asin, reviewerID, unixReviewTime)</p>
          "})

; REST routes
(defroutes routes
  (GET "/" [] root) ; Shows available API endpoints
  (GET "/request" [] handle-dump)

  (GET "/api/all_reviews" [] handle-all-reviews) ; this api will not be used - just to quickly display all reviews (if db is set up correctly)
  (POST "/api/get_reviews" [] handle-reviews) ; Get all review of a particular book
  (POST "/api/create_review" [] handle-create-review) ; Add a review to a particular book
  (POST "/api/delete_review" [] handle-delete-review) ; Remove a review to a particular book - requires asin, reviewerID, unixReviewTime

  (not-found "Page not found"))

(defn wrap-db [hdlr]
  (fn [req]
    (hdlr (assoc req :clojure-rest-mysql/ds ds))))

(defn wrap-server [hdlr]
  (fn [req]
    (assoc-in (hdlr req) [:headers "Server"] "Kindle Reviews MySQL Server")))

;; (def app
;;   (wrap-server (wrap-db (wrap-params routes)))))
(def app
  (wrap-server (wrap-db (wrap-json-params routes))))

(defn -main [port]
  (reviews/create-table ds)
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (reviews/create-table ds)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))