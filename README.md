# MS3Test
MS3 Java Test

This Project was made as per MS3's coding challenge to make an application that 
-consumes a csv file
-parses said csv file
-puts good record to sqlite database
-puts bad records to csv file
-logs the entire process

How to run:
1. Clone/download the repo
2. Download univocity jar file (https://www.univocity.com/pages/univocity_parsers_download)
3. Download javafx (preferably javafx11) (https://gluonhq.com/products/javafx/)
4. Add jar files to your project library
5. Build and run
(PS. did not use maven, encountered dependency issues which i have not resolved. took me days :/)


Approach:
For this project I used Univocity Parsers (since it claimed it was the fastest),
JavaFX for the simple GUI, and SQLite for the database.

PS. 
-haven't done any major exception handling
-app might not respond while being fed large csv files
-db directory not necessary
