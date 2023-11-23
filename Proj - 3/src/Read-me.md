**Assumptions:** 

- Key entered for the store should be string and should not contain any spaces in between.
- Value entered for the store should be integer.
- Time to live for the client expecting response from the server is 5 seconds.
- Client sends a uniques message id which is appended in the request message. To handle unrequested 
packet.
- All user input commands should be in uppercase (PUT/DELETE/GET). Server will say Invalid command if user
tries to enter otherwise.
- Store is initialized with seed data of multiple values.
- Server should be started before the client.

How to start server application:
- start the serverApplication file which provides the RMI of the participants to the client and 
registers in the registry.
- The server serverApplication should be started with command line arguments i.e a list of port 
numbers of different participants.

**How to start client application:**
- The client will be expecting the port number of the server i.e participant inorder to connect,
select one of the earlier provided command line argument ports.
- After server is connected, the client mode should be selected.
- UI can be started with different modes "ui", "thread", "exit"
- ui takes to interactive mode.
- thread runs multiple thread of client to run the application.
- exit quits the client application.

**Steps to use:**
- Every time an operation is performed on the client or server, the log 
will be ClientLog.log and ServerLog.log in corresponding packages.
- Please enter the user commands in the client application terminal.
```agsl
PUT a 2
GET a
DELETE a
```
- The logs contains the data of the server information, error, warnings,
also the store details of each server.

