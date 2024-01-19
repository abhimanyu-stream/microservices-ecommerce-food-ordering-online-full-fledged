#Python Script

import mysql.connector

# Database connection parameters
db_config = {
    'host': 'localhost',
    'user': 'root',
    'password': 'root',
    'database': 'fooddeliveryapp'
}

# Establish a database connection
db_connection = mysql.connector.connect(**db_config)
cursor = db_connection.cursor()

# SQL insert command
# MySQL query
insert_command1 = "INSERT INTO role VALUES(1,'ROLE_ADMIN');"
insert_command2 = "INSERT INTO role VALUES(2,'ROLE_USER');"
insert_command3 = "INSERT INTO role VALUES(3,'ROLE_MODERATOR');"

insert_command4 = "INSERT INTO user_roles VALUES(1,1);"



# Execute the insert command
cursor.execute(insert_command1)
cursor.execute(insert_command2)
cursor.execute(insert_command3)
cursor.execute(insert_command4)

# Commit the changes to the database
db_connection.commit()

# Close the cursor and connection
cursor.close()
db_connection.close()
