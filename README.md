# Pandora2Spotify
Allows a user to migrate all of their "Thumbs Up" songs to from Pandora to Spotify.

Technologies used: Java, Javascript, Maven, Selenium 

### Custom Spotify Web API Wrapper
This program utilizes a created a custom wrapper for the Spotify Web API that gives the programmer the ability to do the following: 
- Access a user account
- Create a playlist
- Add tracks to a playlist
- Search for a track

The class is included in the program but if you want to experient with it you can find it in a separate repo in my Guthub account: https://github.com/JMCSci/Spotify-Web-API-Wrapper

### Requirements
- Mozilla Firefox 


### 0. Getting Started
Before accessing and making changes to you account outside of Spotify, you must connect your Spotify account to Spotfiy Developer:
https://developer.spotify.com/dashboard/

Once connected, you will receive two VERY important pieces of information: <b>Client ID</b> and <b>Client Secret</b>. The Client ID and Client Secret are used to make requests to the Spotfiy API. Keep these in a safe place, you will need it at the beginning of the program. 

#### IMPORTANT!!! 
<i>Save both the Client ID and Client Secret to a plain text document. The Client ID should be on the first line and the Client Secret on the second line.</i>

### 1. Program Launch
From the command line enter the following: java -jar <LOCATION OF FILE>
  
  Mac example: java -jar /Users/JMCSci/Desktop/Pandora2Spotify.jar 
  
  Windows example: java -jar C:/My Computer/My Folder/Pandora2Spotify.jar

At start of the program you will be prompted to enter the following:
- The location of the Client ID/Client Secret plain text document
- Spotify user name
- Spotify password
- Pandora user name
- Pandora password

Everything after this point will be handled automatically by the program (including access to your web browser for sign-in).

### 2. Accessing the Spotify Web API
To access the Spotfiy API endpoints and the user private data you must receive authorization. Once authorization is granted, each time a request is made to the server you will recieve refresh tokens. The custom API wrapper handles of the requests.

For more information on how the Spotify Web API works: https://developer.spotify.com/documentation/web-api/quick-start/

Spotify Authorization Guide: https://developer.spotify.com/documentation/general/guides/authorization-guide/

<i>FYI: This program uses the <b> Refreshable user authorization code flow </b></i>

### Current Issues
There appears to be a bug that will not migrate all of your songs if it is greater than 1200 tracks. I am currently working on a fix for it.
