# Pandora2Spotify
Allows a user to migrate all of their "Thumbs Up" songs to from Pandora to Spotify.

This program utilizes a created a custom wrapper for the Spotify Web API that gives the programmer the ability to do the following: 
- Access a user account
- Create a playlist
- Add tracks to a playlist
- Search for a track

### Requirements
- Mozilla Firefox 


### 0. Getting Started
Before accessing and making changes to you account outside of Spotify, you must connect your Spotify account to Spotfiy Developer:
https://developer.spotify.com/dashboard/

Once connected, you will receive two VERY important pieces of information: <b>Client ID</b> and <b>Client Secret</b>. The Client ID and Client Secret are used to make requests to the Spotfiy API. Keep these in safe place, you will need it at the beginning of the program. 

### 1. Accessing the Spotify Web API
To access the Spotfiy API endpoints and the users private data you must receive authorization. Once authorization is granted, each time a request is made to the server you will recieve refresh tokens. The user will not need to do anything and all requests are handled through the custom API wrapper.

For more information on how the Spotify Web API works: https://developer.spotify.com/documentation/web-api/quick-start/

Spotify Authorization Guide: https://developer.spotify.com/documentation/general/guides/authorization-guide/

<i>FYI: This program uses the <b> Refreshable user authorization code flow </b></i>

### Current Issues
There appears to be a bug that will not migrate all of your songs if it is greater than 1200 tracks. I am currently working on a fix for it.
