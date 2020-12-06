(ns clojure-rest-mysql.reviews.handler
  (:require [clojure-rest-mysql.reviews.model :refer [read-all-reviews
                                                      create-review
                                                      read-reviews
                                                      delete-a-review]]
            [ring.util.response :refer [response]]
            [clojure.data.json :as json]))


; asin char (10)
; helpful varchar (10)
; overall integer (1)
; reviewText text (1000)
; reviewTime varchar (11)
; reviewerID varchar (14)
; reviewerName varchar (64)
; summary varchar (255)
; unixReviewTime char (10)


(defn handle-all-reviews [req]
  (let [ds (:clojure-rest-mysql/ds req)
        reviews (read-all-reviews ds)]
    ;; {:status 200
    ;;  :headers {}
    ;;  :body (list (json/write-str (response reviews)))}))
    {:body (list (json/write-str (response reviews)))}))


(defn handle-reviews [req]
  (let [ds (:clojure-rest-mysql/ds req)
        asin (get-in req [:params "asin"])
        ;max (get-in req [:params "max"])
        selected-reviews (read-reviews ds asin)]
    {:body (json/write-str (response selected-reviews))}))

(defn handle-delete-review [req]
  (let [ds (:clojure-rest-mysql/ds req)
        asin (get-in req [:params "asin"])
        reviewerID (get-in req [:params "reviewerID"])
        unixReviewTime (get-in req [:params "unixReviewTime"])
        deleted-review (delete-a-review ds asin reviewerID unixReviewTime)]
    {:body (list (json/write-str (response deleted-review)))}))


(defn handle-create-review [req]
  (let [ds (:clojure-rest-mysql/ds req)
        asin (get-in req [:params "asin"])
        helpful (get-in req [:params "helpful"])
        overall (get-in req [:params "overall"])
        reviewText (get-in req [:params "reviewText"])
        reviewTime (get-in req [:params "reviewTime"])
        reviewerID (get-in req [:params "reviewerID"])
        reviewerName (get-in req [:params "reviewerName"])
        summary (get-in req [:params "summary"])
        unixReviewTime (get-in req [:params "unixReviewTime"])
        createdReview (create-review ds asin helpful overall reviewText reviewTime reviewerID reviewerName summary unixReviewTime)]

    {:body (json/write-str (response createdReview))}))

