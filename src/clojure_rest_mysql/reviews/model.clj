(ns clojure-rest-mysql.reviews.model
  (:require [next.jdbc :as jdbc]))

; Kindle Reviews Dataset columns
; asin - ID of the product, eg B00FA64PK
; helpful - helpfulness rating of the review, eg 2/3 -- note that its raw format in csv is [2,3] - need to format
; overall - rating of the product, eg 3
; reviewText - text of the review (heading), can be very long
; reviewTime - time of the review (raw) - eg. 01 6, 2014 --need to convert later like this SELECT STR_TO_DATE ("01 5,2017", "%m %d, %Y");
; reviewerID - ID of the reviewer, eg A3SPTOKDG7WBLN
; reviewerName - name of the reviewer, eg ubavka seirovska
; summary - summary of the review (description) eg Not bad, not exceptional
; unixReviewTime - unix timestamp, eg 1388966400 --need to convert later like this mysql> SELECT FROM_UNIXTIME(1447430881); -> '2015-11-13 10:08:01'
;;; To check data types: https://www.w3schools.com/sql/sql_datatypes.asp

; CRUD OPERATIONS for kindle_reviews mysql database
; - READ all reviews
; - READ reviews of a specified book
; - CREATE a review for a specified book
; - Update the (written) review for a specified book
; - DELETE a (written) review for a specified book - user can only delete its own review

(def db {:dbtype "mysql" :dbname "kindle-reviews"})
(def ds (jdbc/get-datasource db))


; Load data into mysql from csv


; Insert sample data
(defn load-sample-data [ds]
  (jdbc/execute! ds ["INSERT INTO reviews (asin, helpful, overall, reviewText, reviewTime, reviewerID, reviewerName, summary, unixReviewTime)
                      VALUES (?,?,?,?,?,?,?,?,?), (?,?,?,?,?,?,?,?,?), (?,?,?,?,?,?,?,?,?)"
                     "B000F83SZQ" "[0, 0]" "5" "I enjoy vintage books and movies" "04 21, 2014" "A1F6404F1VG29J" "jack brown" "I enjoy vintage books and movies summary" "1398038400"
                     "B000FDJ0FS" "[2, 9]" "3" "I love this grape" "02 16, 2014" "A1CQ8WG6CUDBNV" "danny cake" "I love this grape summary" "1392508800"
                     "B000GFK7L6" "[3, 4]" "3" "cool stuff" "11 3, 2009" "A3GXR6CHHPX0JS" "Zach green" "cool stuff summary" "1257206400"]))

; Create 'reviews' Table
(defn create-table [ds] ;change to drop table if exists;
  (jdbc/execute! ds ["
                      CREATE TABLE IF NOT EXISTS reviews (
                      asin char(10) DEFAULT NULL,
                      helpful varchar(10) DEFAULT NULL,
                      overall integer(1) DEFAULT NULL,
                      reviewText text(1000) DEFAULT NULL,
                      reviewTime varchar(11) DEFAULT NULL,
                      reviewerID varchar(14) DEFAULT NULL,
                      reviewerName varchar(64) DEFAULT NULL,
                      summary varchar(255) DEFAULT NULL,
                      unixReviewTime integer(10) DEFAULT NULL,
                      PRIMARY KEY(asin, reviewerID, unixReviewTime)
                      )"])
  (println "Created Reviews Table")
  (load-sample-data ds)
  (println "Loaded some sample data"))



; Insert
(defn create-review [ds asin helpful overall reviewText reviewTime reviewerID reviewerName summary unixReviewTime]
  (println "Inserting a row")
  (jdbc/execute!
   ds
   ["INSERT INTO reviews (asin, helpful, overall, reviewText, reviewTime, reviewerID, reviewerName, summary, unixReviewTime)
                VALUES (?,?,?,?,?,?,?,?,?)"
    asin helpful overall reviewText reviewTime reviewerID reviewerName summary unixReviewTime]))



; Read All - wont really be used. Just for testing purpose
(defn read-all-reviews [ds]
  (println "Reading all reviews")
  (jdbc/execute! ds ["SELECT * from reviews"]))


; Read a review
(defn read-a-review [ds asin reviewerID unixReviewTime]
  (println "Reading a reviews")
  (jdbc/execute! ds ["SELECT * from reviews WHERE asin=? AND reviewerID=? AND unixReviewTime=?" asin reviewerID  unixReviewTime]))