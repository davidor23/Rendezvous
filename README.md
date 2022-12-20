# Rendezvous
Favourite location app
This is an Android app written in Kotlin that allows a user to add a rendezvous (a place and time to meet with someone) to a database. The app has the following features:

Add a rendezvous: The user can input the name, location, date, and time of the rendezvous, and can also add a photo of the location. The location is selected using the Google Places API, and the user's current location is determined using the Fused Location Provider API.

View rendezvous details: The user can view the details of a rendezvous, including the name, location, date, time, and photo.

Edit a rendezvous: The user can edit the details of a rendezvous by clicking on it in the main screen and making changes in the Add Rendezvous screen.

Delete a rendezvous: The user can delete a rendezvous by clicking on it in the main screen and then clicking the delete button.

Save the rendezvous to the database: When the user clicks the save button, the app stores the rendezvous details in a SQLite database using the Android DatabaseHandler class.

The app also requests permissions to access the user's location and external storage (to save the photo of the location), and uses the Dexter library to handle the permissions request process.
