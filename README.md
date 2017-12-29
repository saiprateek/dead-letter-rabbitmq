# dead-letter-with-RabbitMQ
implementation of RabbitMQ dead letter queue feature with Spring Boot

Te dead letter exchange feature can be implement using topic, funout and direct.
Whenever failure occurs during processing a message fetched from a queue, RabbitMQ checks if there is a dead letter exchange configured for that queue. 

If there is one configured via x-dead-letter-exchange argument then it routes the failed messages to it with the original routing key. This routing key can be overridden via the x-dead-letter-routing-key argument.

When failure happens and message routed to dead letter queue, the Message header map key xdeath contains sufficient information about message failure like :

<h1>queue</h1> - the name of the queue the message was in before it was dead-lettered,<br>
<h1>reason</h1> - reason of rejection or failure<br>
<h1>time</h1> - the date and time the message was dead lettered as a 64-bit AMQP format timestamp,<br>
<h1>exchange</h1> - the exchange the message was published to (note that this will be a dead letter exchange if the message is dead lettered multiple times),<br>
<h1>routing-keys</h1> - the routing keys (including CC keys but excluding BCC ones) the message was published with<br>
<h1>count</h1> - how many times this message was dead-lettered in this queue for this reason<br>

Here Reason and Count could be your best guy to implement your custom logic.


