(ns clojure-rest-mysql.reviews.handler
  (:require [clojure-rest-mysql.reviews.model :refer [read-all-reviews
                                                      create-review
                                                      read-a-review]]
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
    {:status 200
     :headers {}
    ;  :body (str reviews)}))
     :body (list (json/write-str (response reviews)))}))

(defn handle-a-review [req]
  (let [ds (:clojure-rest-mysql/ds req)
        asin (get-in req [:params "asin"])
        reviewerID (get-in req [:params "reviewerID"])
        unixReviewTime (get-in req [:params "unixReviewTime"])
        selected-review (read-a-review ds asin reviewerID unixReviewTime)]
    {:status 200
     :headers {}
    ;  :body (str reviews)}))
     :body (list (json/write-str (response selected-review)))}))


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
        ; review-asin (create-review ds asin, helpful overall reviewText reviewTime reviewerID reviewerName summary unixReviewTime)
        ]
    (create-review ds asin helpful overall reviewText reviewTime reviewerID reviewerName summary unixReviewTime)
    {:status 200
    ;  :headers {"Location" "/reviews"} ;redirect back to /reviews
    ;  :body (str "Successfully inserted a review into " review-asin)
     :body (str "Successfully inserted a review on book asin" "(" asin ")" " from reviewerID" "(" reviewerID ")")}))

