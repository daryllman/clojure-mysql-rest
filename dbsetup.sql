CREATE TABLE IF NOT EXISTS reviews (
	id int(11),
    asin varchar(24),
    helpful varchar(10) DEFAULT NULL,
    overall integer(1) DEFAULT NULL,
    reviewText text(1000),
    reviewTime varchar(11),
    reviewerID varchar(14),
    reviewerName varchar(64),
    summary varchar(255) DEFAULT NULL,
    unixReviewTime integer(10),
    PRIMARY KEY(asin, reviewerID, unixReviewTime)
    );

LOAD DATA LOCAL INFILE 'kindle_reviews.csv' INTO TABLE reviews
	FIELDS TERMINATED BY ','
	ENCLOSED BY '"'
	LINES TERMINATED BY '\n'
	IGNORE 1 ROWS;