Android
=======
This repo contains sample Android sample applications from 
the Android tutorial book found here: 
http://www.bignerdranch.com/we-write/android-programming.html.
I highly recommend the book to anyone who knows java
and is interested in learning Android.  The authors provide 
a step by step guide for writing all of these sample apps,
and they thoroughly explain each step in
a way that is accessible to programmers with no Android 
experience.  The apps are pretty basic, but they certainly
helped me develop familiarity key concepts of Android 
programming.  

## CriminalIntent
CriminalIntent is a pretty cool app.  The launcher activity
is a ListView that uses an ArrayAdapter to provide a scrollable
interface for viewing the crime log.  The crime log is backed
up on an locally stored JSONFile, so the current state of the crime
log will persist forever (or at least untill the app is 
uninstalled).

![List](https://github.com/p-tricky/Android/blob/master/images/CriminalIntent_1.jpg)

A new crime can be created from the options menu or an old
crime can be edited by selecting it from the list. Both of 
these actions will create a CriminalFragment.

![CriminalFragment](https://github.com/p-tricky/Android/blob/master/images/CriminalIntent_2.jpg)

The Criminal Fragment has several cool features. It provides 
access to the phone's camera.

![Camera](https://github.com/p-tricky/Android/blob/master/images/CriminalIntent_3.jpg)
![Update1](https://github.com/p-tricky/Android/blob/master/images/CriminalIntent_4.jpg)

Users can identify suspects from their contacts.

![Suspects](https://github.com/p-tricky/Android/blob/master/images/CriminalIntent_5.jpg)
![Update2](https://github.com/p-tricky/Android/blob/master/images/CriminalIntent_6.jpg)

The CriminalFragment also uses chooser intents to send crime
reports using any of the phones messaging applications.

![Report](https://github.com/p-tricky/Android/blob/master/images/CriminalIntent_7.jpg)
![Update3](https://github.com/p-tricky/Android/blob/master/images/CriminalIntent_8.jpg)

## DragAndDraw
DragAndDraw is a simple app with a single custom view that is
specifically designed to handle touch events.  The user can
draw pink boxes accross the screen by dragging.

![Picasso](https://github.com/p-tricky/Android/blob/master/images/DragAndDraw_1.jpg)

## GeoQuiz
The code for GeoQuiz is the first and most simple
application of the tutorial.  It introduces the
reader to application life-cycles, Android's 
Model-View-Controller architecture, and methods for passing
data between activities and persisting data across screen
rotations.  

![Main1](https://github.com/p-tricky/Android/blob/master/images/GeoQuiz_1.jpg)
![Cheat](https://github.com/p-tricky/Android/blob/master/images/GeoQuiz_2.jpg)
![Main2](https://github.com/p-tricky/Android/blob/master/images/GeoQuiz_3.jpg)

## PhotoGallery
PhotoGallery is a flickr client for Android.  It introduces
concepts like AsyncTasks, Loopers, and WebViews.  

Performing a search from the options menu creates 
an AsyncTask that uses the flickr API's querry method to
download urls correpsonding to flickr thumbnail photos. A looper
thread retrieves the thumbnails and provides handlers to the
PhotoGallery GridView fragment. The GridView then uses the handler
to populate its child views with the thumbnail.

![GridView](https://github.com/p-tricky/Android/blob/master/images/PhotoGallery_1.jpg)

When one of the thumbnails is selected, a WebView fragment
is created to view the thumbnail.

![WebView](https://github.com/p-tricky/Android/blob/master/images/PhotoGallery_2.jpg)


## RunActivity
RunActivity uses location data to track a user's runs.  It 
incorporates cool android features like the location manager,
broadcast receivers, local database storage and asynchronous
data loaders, and the Google Maps API.

The app opens to a list view of runs.

![RunList](https://github.com/p-tricky/Android/blob/master/images/RunActivity_1.jpg)

Selecting a run from the list opens a new view that displays
relevent run data. 

![RunFragment](https://github.com/p-tricky/Android/blob/master/images/RunActivity_2.jpg)

From here the user can choose to start (or resume) collecting 
location data for the selected run. In respone, Android's built-in
location manager will start a pending intent that provides
location data to the BroadcastReceiver.  The BroadcastReceiver
will update the database will the location info even when the
application is paused.  

Selecting the Map button creates a google map.  A Loader queries
the database on a background thread and uses callbacks to provide
location data to the map.  

![Map](https://github.com/p-tricky/Android/blob/master/images/RunActivity_3.jpg)
`
