# IRC-For-Me
Copyright (c) 2017 Blair Davis

This Github repository contains all of the files pertaining to the apllication 'IRC-For-ME'. This application is a very simple chatting applicatiom that enables you to talk with your friends online (so long as one person runs the server provided). It currently is implemented as an internet relay chat (IRC), which is a particular type of chat application because it has to follow a designated protocol that delineates particular functionality, or commands. So far, this application has all of the basic bare bones of an IRC up and running. In order to use the application follow the instructions for the 'How To Run This Application' section below. Once the application is running, all users connected to the application server can talk to each other in chatrooms called channels. Users can create channels, join existing channels, and leave channels. A complete list of all implemented commands is shown below.  

The commands implemented in IRC-For-ME are as follows:  

/HELP - shows a general list of commands to user  
/JOIN - enables user to join a channel   
/LIST - shows a list of all current channels   
/LEAVE - enables user to leave a channel   
/QUIT - enables user to exit IRC-for-me application   
/NICK - enables user to change his/her screen name   
/AWAY - enables user to create an away message   
/WHOIS - displays information about a user in the channel  
/TOPIC - enables channel operator to view or change the topic of the channel   
/NAMES - shows a list of users in a channel, no channels specified shows all users   


IRC-For-ME is currently an open source project that is a work in progress. Just as an explicit clarification, this application is written in Java and is intended to be deployed via the linux command line.

# License
This project is made available under the MIT license. Please see the 'LICENSE' file in this repository for the terms of the license. 

# Author and Contact Information
Author: Blair Davis  
E-mail: bdavis@pdx.edu

# How To Run This Application

This application is intended to have a very simple and straight-forward deployment. You can run the entire application via the linux command line with just the commands provided for you below.  

1. Open the linux command line on your computer.  
1. Clone this repository with the following command:    
`git clone https://github.com/brdavis/IRC-For-Me.git ` 
1. Move to source_code folder with the following command:  
`cd source_code`
1. Compile the application with the following commands:    
`javac Server.java Server_handler.java Server_channel.java`    
`javac Client.java Client_handler.java`    
1. Run the application by starting the server first with the following command:    
`java Server Server_handler Server_channel`  
1. In a seperate window start the client with the following command:    
`java Client Clinet_handler`
 Start all new clients in this manner.
 
 System Requirements: Linux OS with Java installed  
 Reference: https://www.java.com/en/download/help/download_options.xml

# Useful Links
### IRC Information
* https://en.wikipedia.org/wiki/List_of_Internet_Relay_Chat_commands#LIST
* http://www.mirc.com/ircintro.html
* http://www.geekshed.net/commands/user/

### Socket Programming and Network Information
* http://www.oracle.com/technetwork/java/socket-140484.html#server
* https://docs.oracle.com/javase/tutorial/networking/overview/networking.html
* https://docs.oracle.com/javase/tutorial/networking/sockets/
* https://www.tutorialspoint.com/java/java_networking.htm
* https://www.javatpoint.com/URLConnection-class

# Status and Development Documentation
* To get more information about the development of 'IRC-For-Me' look into the docs folder in this repository for a brief overview 
* Feel free to look at the comments within the source code to understand current coding implementation
* Additionally see the <a href = https://github.com/brdavis/IRC-For-Me/projects/1> project list </a> for pending and completed development tasks.

# Limitations
 * Current limitation is that this project does not contain a GUI (graphic user interface).
 * This project can only be deployed on the linux command line. 
