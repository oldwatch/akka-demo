package com.techdemo.stream;

import akka.actor.typed.ActorSystem;
import com.techdemo.stream.actor.ClientActor;

public class StreamClient {


    public static void main(String[] args) {
        final ActorSystem<ClientActor.Start> system = ActorSystem.create(ClientActor.create(), "client");

        system.tell(new ClientActor.Start());


    }
}
