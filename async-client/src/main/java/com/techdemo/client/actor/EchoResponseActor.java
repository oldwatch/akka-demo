package com.techdemo.client.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoResponseActor extends AbstractBehavior<EchoResponseActor.EchoResponse> {

    private Logger log = LoggerFactory.getLogger(EchoActor.class);

    private EchoResponseActor(ActorContext<EchoResponse> context) {
        super(context);

    }

    public static Behavior<EchoResponse> create() {
        return Behaviors.setup(EchoResponseActor::new);
    }

    @Override
    public Receive createReceive() {
        return newReceiveBuilder()
                .onMessage(EchoResponseActor.EchoResponse.class, this::onResponse)
                .build();
    }

    private Behavior<EchoResponseActor.EchoResponse> onResponse(EchoResponseActor.EchoResponse response) {

        log.info("oh,i got echo response {}", response.response);

        return this;
    }

    @Data
    public static class EchoResponse {
        private final String response;
    }


}
