# crossnet_login

This app allows users to login using their google account.
The first screen to be loaded is Main activity, it checks if there is connected account and head to profileActivity or otherwise show the login button
Once the login button is pressed, the user can choose an existing google account in his device or add a new one
##For interaction with a backed server,
 public-string-getidtoken() will return an ID token, a JSON Web Token signed by Google that can be used to identify a user to the backend.
