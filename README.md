# Cubbyhole

A cubbyhole 1 is a small hiding place where one can stash things. We now define the cubbyhole protocol that allows users to store one-line-messages on a server. 
To make matters somewhat easier, the server will only store a single message at a time, but keeps and shares it across different connections.
If a new message is put in the cubbyhole, the old message is lost.
We realize the cubbyhole protocol as simple, TCP based text protocol. Each command consists of a single word (casing does not matter) that might be followed by a space and an arbitrary text and is terminated with a newline. 
The following commands should be supported:

PUT < message > Places a new message in the cubbyhole.
GET Takes the message out of the cubbyhole and displays it.
LOOK Displays message without taking it out of the cubbyhole.
DROP Takes the message out of the cubbyhole without displaying it.
HELP Displays some help message.
QUIT Terminates the connection.

# Running server

Use `./run.sh` to run

# Connecting to server

Use `telnet 0.0.0.0 1337`

# State diagram

Cubbyhole server uses only hard state transitions, since both server and client can initiate and remove states (there are no timeouts removing the states, implying soft state)

![state diagram](https://github.com/Gabbe1993/Cubbyhole/blob/master/StateDiagram.png)

