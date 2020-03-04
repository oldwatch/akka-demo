package com.techdemo.client.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.entrys.CostTimeResult;
import com.techdemo.entrys.EchoResult;

public class MainActor extends AbstractBehavior<MainActor.Request> {

    private final ActorRef<EchoActor.EchoRequest> echoActor;
    private final ActorRef<EchoResult> echoResponseActor;
    private final ActorRef<CostTimeResult> costTimeResponse;
    private final String basePath = "akka://service-proxy@127.0.0.1:25520/user/dispatch";

    private MainActor(ActorContext<Request> context) {

        super(context);


        echoActor = context.spawn(EchoActor.create(basePath), "echoActor");

        costTimeResponse = context.spawn(CostTimeResponseActor.create(), "costTimeResponse");

        echoResponseActor = context.spawn(EchoResponseActor.create(basePath, costTimeResponse), "echoResponse");
    }

    public static Behavior<Request> create() {

        return Behaviors.setup(MainActor::new);
    }

    @Override
    public Receive<Request> createReceive() {
        return newReceiveBuilder()
                .onMessage(Request.class, this::onEcho)
                .build();
    }

    private Behavior<MainActor.Request> onEcho(Request request) {

        EchoActor.EchoRequest echoRequest = new EchoActor.EchoRequest("hello", echoResponseActor);
        echoActor.tell(echoRequest);

        return this;
    }

    public static class Request {

    }


}
