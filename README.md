# Pandora2Spotify
Allows a user to migrate all of their "Thumbs Up" songs to from Pandora to Spotify.

This program utilizes a created a custom wrapper for the Spotify Web API that gives the programmer the ability to do the following: 
- Access a user account
- Create a playlist
- Add tracks to a playlist
- Search for a track


### 0. Getting Started


https://developer.spotify.com/documentation/web-api/quick-start/

Before you can access the Spotify Web API you must have an active Pandora and Spotify account. 

### 1. Accessing the Spotify Web API
To access the Spotfiy API endpoints and private data you must receive authorization. Once authorization is granted, each time a request is made to the server 
you will recieve refresh tokens. All of this is handled though the custom API wrapper.

More information can be found here: https://developer.spotify.com/documentation/general/guides/authorization-guide/


### Current Issues
There appears to be a bug that will not migrate all of your songs if it is greater than 1200 tracks. I am currently working on a fix for it.
