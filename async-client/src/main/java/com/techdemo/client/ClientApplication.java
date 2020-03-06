package com.techdemo.client;

import akka.actor.typed.ActorSystem;
import com.techdemo.client.actor.MainActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientApplication {

    private static final Logger log = LoggerFactory.getLogger(ClientApplication.class);

    public static void main(String[] argv) {

        log.debug("start...");

        ActorSystem<MainActor.Request> system = ActorSystem.create(MainActor.create(), "main");


    }


}
