# IRC-For-Me
Copyright (c) 2017 Blair Davis

This is a repository for creating an irc server and client in java.

IRC-For-ME is written in Java and is intended to be deployed via the linux command line.

This is currently a work in progress and is expected to be wrapped up in mid-August.


# How To Run This Application

This application is intended to have a very simple and straight-forward deployment. You can run the entire application via the linux command line with just the commands provided for you below.  

1. Open the linux command line on your computer.  
1. Clone this repository with the following command:    
`git clone https://github.com/brdavis/IRC-For-Me.git `  
1. Compile the application with the following commands:    
`javac Server.javac Server_handler.javac `    
`javac Client.javac Client_handler.javac`    
1. Run the application by starting the server first with the following command:    
`java Server Server_handler`  
1. In a seperate window start the client with the following command:    
`java Client Clinet_handler`

# Useful Links
### IRC Information
* http://www.mirc.com/ircintro.html

### Socket Programming and Network Information

# Status

See the GitHub Development Wiki for pending and completed development tasks.

# Limitations
 * Current limitation is that this project does not contain a GUI (graphic user interface).
 * This project can only be deployed on the linux command line. 

# License
This project is made available under the MIT license. Please see the LICENSE file in this repository for license terms.

# Authors
Blair Davis

# Status reports

## Week 3 Report

  **What I've accomplished so far:**
  
  So far what I've accomplished has primarily been research based. Since this is my first time working with Git and Github I did a few tutorials in order to get acclimated and understand how to use both the site interface and the command line in linux. I then did research primarily on socket programming as well as brushed up on my java. I then began to code and have created a very basic client-server socket connection as the base for my IRC. Finally, I utilized the wiki section of this repository to outline individual project components so that I can keep focused while working on my project.  
  
  **What my goals are to accomplish next:**   
  
  My next goals are to dive heavily into the code of the project for the next few weeks. I hope to develop a fully functional IRC. Additionally, it would be a reach goal to add a GUI to my IRC.  
  
  **Potential obstacles:**    
  
  My potential obstacles are mostly just the learning curve of trying to use new tools while working on a software project independently.   
