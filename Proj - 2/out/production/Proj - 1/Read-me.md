Assumptions: 

- Key entered for the store should be string and should not contain any spaces in between.
- Value entered for the store should be integer.
- Time to live for the client expecting response from the server is 5 seconds.
- Client sends a uniques message id which is appended in the request message. To handle unrequested 
packet.
- All user input commands should be in uppercase (PUT/DELETE/GET). Server will say Invalid command if user
tries to enter otherwise.
- Store is initialized with seed data of multiple values.

How to start server application:
- start the serverApplication file which provides the RMI to the client and registers in the registry.

How to start client application:

- UI can be started with different modes "ui", "thread", "exit"
- ui takes to interactive mode.
- thread runs multiple thread of client to run the application.
- exit quits the client application.

Steps to use:
- Every time an operation is performed on the client or server, the log 
will be ClientLog.log and ServerLog.log in corresponding packages.
- Please enter the user commands in the client application terminal.
```agsl
PUT a 2
GET a
DELETE a
```

