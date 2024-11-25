# P2P Chat and VoIP Application using UDP in Java

**This repository was forked.**

This repository contains the codebase of the **Peer-to-Peer (P2P) Chat and VoIP application** which was developed as the main assignment for the **Computer Networks II** course. This course is taught as part of the curiculum of the **Electrical and Computer Engineering** department of the **Aristotle University of Thessaloniki (AUTH)** during the **2024-2025** academic year.

## 📚 Project Overview

The purpose of this project is to create a simple Peer-to-Peer (P2P) application that enables real-time chat and Voice over IP (VoIP) communication utilizing the UDP protocol in Java. This application will be based on  `java.net` to handle network communications, providing a foundational understanding of network programming, concurrency, and instant messaging and multimedia dataexchange between two peers.

## 🛠️ Technologies Used

- **Programming Language:** Java
- **Libraries & Frameworks:**
  - `java.net` for networking
  - `javax.sound` for audio capture and playback
- **Protocols:**
  - UDP (User Datagram Protocol) for communication
- **Tools:**
  - Git for version control
  - Maven for build automation and project management for Java projects

## ✨ Features

- Instant messaging and voice data exchange using UDP
- Simplistic Graphic User Interface (GUI) instead of command line implementation
- Multithreaded architecture to enable concurent instant messaging and VoIP functionalities

## 🛠️ Pre-requisites 

Please consider installing and using the following applications and tools for the assignment:

- Java 23 (although it is expected to work with any version from 1.8 and beyond): [link](https://www.oracle.com/java/technologies/downloads/)
- Apache Maven: [link](https://maven.apache.org/download.cgi)
- Git: [link](https://git-scm.com/downloads)

## 🚀 Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/ioannisam/Chat-VoIP.git
   ```
2. **Navigate to project's directory**
   ```bash
   cd /path/to/Chat-VoIP
   ```
3. **Compile and Run:**
   ```bash
   mvn compile
   ```
   ```bash
   mvn exec:java -Dexec.mainClass="com.cn2.communication.App"
   ```
**In App.java replace RECEIVER_IP with the actual ip of your intended receiver!**

If the compilation is successful the GUI of the application will be displayed on your screen. The GUI is presented below: 

<p align="center">
  <img src="https://github.com/user-attachments/assets/713e81b5-f7bc-44ec-8d6c-56fa77288c2d" alt="cn2-cnv-1">
</p>

As can be seen by the figure above, the application contains a Text Field that is used for writing the messages that need to be sent to the other peer, a Text Area for displaying the messages that have been sent or received, and two buttons. The "Send" button is responsible for sending the messages that are typed into the Text Field. The "Call" button is responsible for initiating a VoIP call with the remote peer (i.e., for starting listening for voice packets and sending voice packets captured from the computer's microphone).


## ⚠️ **ATTENTION:**
>- **Firewall and Network Configuration:** Ensure that your firewall settings allow UDP traffic on the specified ports. Incorrect configurations may prevent peers from connecting.
>- **Java Version:** This application requires Java **1.8** or higher. Ensure that your `JAVA_HOME` environment variable is correctly set to the installed Java version.
>- **Audio Hardware:** Proper functioning of VoIP features depends on the availability and correct configuration of audio input/output devices (e.g., microphone and speakers).

## 📄 Useful Material

- The original description of the assignment can be found [here](https://sites.google.com/view/computer-networks-ii/%CE%B5%CF%81%CE%B3%CE%B1%CF%83%CE%AF%CE%B5%CF%82)
- Additional content for the assignement will be uploaded [here](https://sites.google.com/view/computer-networks-ii)
