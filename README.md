# SocketFlow-Java

A robust multi-threaded client-server chat application built using Java Sockets. This project demonstrates the evolution from basic synchronous I/O to a concurrent, full-duplex communication system.

## 🚀 Key Features
- **Multithreaded Architecture:** Handles multiple concurrent client connections.
- **Full-Duplex Communication:** Simultaneous message sending and receiving.
- **Resource Management:** Optimized using Java's try-with-resources for leak prevention.

## 🏗️ Architectural Overview
The system utilizes a **Thread-per-Connection** model.
1. **Server:** Listens on port 2000 and spawns a `ClientHandler` thread for every incoming connection.
2. **Client:** Decoupled into a `Reader` thread and a `Writer` thread to prevent UI/Input blocking.

## 🛠️ Concepts Explored
- **Computer Networks:** TCP/IP Handshaking, Port Binding, Byte Streams.
- **Concurrency:** Runnable Interface, Thread Lifecycle, Lambda Expressions.
- **OOPS:** Encapsulation of handler logic, Interface implementation.

## 🚦 How to Run
1. Compile: `javac *.java`
2. Run Server: `java MultiThreadedServer`
3. Run Client: `java ChatClient`