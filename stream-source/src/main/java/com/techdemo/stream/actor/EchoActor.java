package com.techdemo.stream.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.entrys.EchoResult;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoActor extends AbstractBehavior<EchoActor.EchoRequest> {

    private Logger log = LoggerFactory.getLogger(EchoActor.class);

    private EchoActor(ActorContext<EchoRequest> context) {
        super(context);
    }

    public static Behavior<EchoRequest> create() {

        return Behaviors.setup(EchoActor::new);
    }

    @Override
    public Receive<EchoRequest> createReceive() {
        return newReceiveBuilder()
                .onMessage(EchoRequest.class, this::echo)
                .build();
    }

    private Behavior<EchoRequest> echo(EchoRequest param) {

        param.reply.tell(new EchoResult("echo:" + param.request));

        return this;
    }

    @Data
    public static class EchoRequest {

        private final String request;

        private final ActorRef<EchoResult> reply;
    }

}
