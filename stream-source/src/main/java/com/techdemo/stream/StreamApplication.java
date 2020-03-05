package com.techdemo.stream;

import akka.actor.typed.ActorSystem;
import com.techdemo.stream.actor.MainActor;

public class StreamApplication {

    public static void main(String[] args) {

        final ActorSystem<MainActor.Request> system = ActorSystem.create(MainActor.create(), "stream");


    }


}
