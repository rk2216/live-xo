# live-xo
Live XO mini-game

Setup:
Maven Link - https://dlcdn.apache.org/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.zip
Add mvn bin directory to path env variable
Node - v14.17.5
npm - 6.14.14
#UI:
1. cd ./live-games/ui
2. npm install
3. To start - npm start

#Service
1. cd ./live-games/service
2. mvn clean install
3. To start - mvn spring-boot:run

##UI Module:
###cmds to start react application:
cd ./live-games/ui
npm start

###cmd to install react packages
npm install --save <package_name>

Flow:
For host
1. Enter username
2. Select game
3. Copy and send link to joinees

For Joinee
1. Use link to join
2. Enter username
