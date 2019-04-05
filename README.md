# FlightPuntos
App for testing flight search with Puntos Colombia.

# Implementation

The app archictecture is MVC. Im using SQLiteDatabase to Register, Login, and Mock the Flights Information. Any user can register and login with the registered account. Is validating correct email, correct name lenght, and some other validations.
Models will be created but no implemented. Instead, the mock will live in the Databasehelper for testing purposes. Also i implemented a profile, and security module, so user is able to edit his information, and change his password with previous validations.

# Techonologies
#### Android Studio IDE
#### JAVA
#### SQLite

# In Dev

Still in developing mode, migration to Firebase for Login and Register. Checkin times. DeadLine: April 7th 2019.

# Data for Testing the book reservation

## Oneway

("Montreal", "Vancouver", "2019-7-28")
("New York", "Miami", "2019-8-15")
("Bogota", "New York", "2019-7-28")
("Medellin", "Miami", "2019-8-25")
("Los Angeles", "Seatle", "2019-8-26")
("Cartagena", "Miami", "2019-8-27")
("Cali", "New York", "2019-8-28", "2019-8-28")


## RoundTrip

("Bogota", "Miami", "2019-8-10", "2019-8-10")
("Miami", "Bogota", "2019-8-12", "2019-8-12")
("Medellin", "New York", "2019-8-25", "2018-8-25")
("New York", "Medellin", "2019-9-27", "2018-9-27")

# Author
### Juan Camilo Sandoval Devia - Multimedia Engineer

# License and Legal
#### Im not the owner of the images, benchmark, or anything related to Puntos Colombia. Any reproduction of this material will be illegal. This app is for testing purposes only
