# Project Title

Political PreparedNess

# Features

Project has 4 screens:
- Launch screen has two buttons for navigating to "Upcoming election" screen or "Find My Representatives" screen.
- Upcoming Elections screen : Upcoming elections retrieved from api and showed to user. Also followed elections retrieved from room db and showed to user. By clicking an item in this list, user is navigated to VoterInfo screen. 
- VoterInfo screen : Details of the clicked election is shown in this screen. User can follow this election by clicking the follow button so that followed elections saved into db and can be seen in the previous screen.
- Representatives screen : User can see representatives w.r.t their address. By clicking "find my location" button, foreground and background permission are requested and location prompted to turned on. If permissions are granted and location is turned on, latitude longitude are found and address is found with the help of geolocation and editText fields are filled automatically. By clicking "find my representatives" button, representatives in your address is shown in the list. MotionLayout is used in this page and provides the bottom of page drags upward and becomes full screen.

### Technology Stack Used in this project

Mvvm, Repository Pattern
Databinding
Retrofit
Moshi
Room
Dagger Hilt
StateFlow
Navigation Components


### Installation

Note : In order to launch this project, you must add API_KEY to local.properties file as the following. API_KEY=A1B2C3 In order to generate an API key, open https://console.developers.google.com -> create a project -> enable Google Civic Api -> copy the api key.

### Screenshots
![image](https://user-images.githubusercontent.com/7634049/112615326-6857e880-8e33-11eb-917d-b4fa52934c90.png)




