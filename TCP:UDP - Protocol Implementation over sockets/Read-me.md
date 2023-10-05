Assumptions: 

- Key entered for the store should be string and should not contain any spaces in between.
- Value entered for the store should be integer. 
- Time to live for the client expecting response from the server is 5 seconds.
- Client sends a uniques message id which is appended in the request message. To handle unrequested 
packet.
- Server will respond to the request with unique client id sent by client and this is appended to 
the response followed by ```#```. 
- Client and server communication should be in through same protocol i.e either TCP or UDP.
- All user input commands should be in uppercase (PUT/DELETE/GET). Server will say Invalid command if user
tries to enter otherwise.
- Store is initialized with seed data of values from a - 1, b - 1, c - 1, d - 1, e - 1.

How to start server application:

- Provide commandline arguments ``` <port_number>```.
    
How to start client application:

- Provide commandline arguments ```<host_name>``` and ``` <port_number>```.

Steps to use:
- Please provide the mode of protocol either UDP or TCP for both client and server.
- Every time after the packet is sent or received either from client or server, the log 
will be ClientLog.log and ServerLog.log in corresponding packages.
- Please enter the user commands in the client application terminal.
```agsl
PUT a 2
GET a
DELETE a
```

