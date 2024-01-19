#!/bin/bash

# MySQL credentials
DB_USER='root'
DB_PASS='root'
DB_NAME='fooddeliveryapp'

# MySQL query
SQL_QUERY1="INSERT INTO role VALUES(1,'ROLE_ADMIN');"
SQL_QUERY2="INSERT INTO role VALUES(2,'ROLE_USER');"
SQL_QUERY3="INSERT INTO role VALUES(3,'ROLE_MODERATOR');"
SQL_QUERY4="INSERT INTO role VALUES(3,'ROLE_MODERATOR');"
SQL_QUERY5="INSERT INTO user_roles VALUES(1,1);"


# Execute MySQL command
mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" -e "$SQL_QUERY1"
mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" -e "$SQL_QUERY2"
mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" -e "$SQL_QUERY3"
mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" -e "$SQL_QUERY4"
mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" -e "$SQL_QUERY5"