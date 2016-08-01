Simple Akka/Scala application that uses remote actors to pass messages around.

**How to run**

gradle runGreeter: Runs the greeter actor system

gradle runReceiver: Runs the receiver/responder actor system

Tweak greeter.conf and receiver.conf for different ports. Different hostnames not supported at the moment.
