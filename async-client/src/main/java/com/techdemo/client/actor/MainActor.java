package com.techdemo.client.actor;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.client.HttpAllDirectives;
import com.techdemo.entrys.CostTimeResult;
import com.techdemo.entrys.EchoResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActor extends AbstractBehavior<MainActor.Request> {

    private final HttpAllDirectives httpAllDirectives;

    private final ActorRef<EchoActor.EchoRequest> echoActor;
    private final ActorRef<EchoResult> echoResponseActor;
    private final ActorRef<CostTimeResult> costTimeResponse;
    private Logger log = LoggerFactory.getLogger(MainActor.class);

    private final String basePath = "akka://service-proxy@127.0.0.1:25520/user/dispatch";

    private MainActor(ActorContext<Request> context) {

        super(context);


        echoActor = context.spawn(EchoActor.create(basePath), "echoActor");

        costTimeResponse = context.spawn(CostTimeResponseActor.create(), "costTimeResponse");

        echoResponseActor = context.spawn(EchoResponseActor.create(basePath, costTimeResponse), "echoResponse");

        httpAllDirectives = new HttpAllDirectives(getContext());

    }

    public static Behavior<Request> create() {

        return Behaviors.setup(MainActor::new);
    }

    @Override
    public Receive<Request> createReceive() {
        return newReceiveBuilder()
                .onMessage(Request.class, this::onEcho)
                .onSignal(PreRestart.class, this::onPreRestart)
                .onSignal(PostStop.class, this::onPostStop)
                .build();
    }

    private Behavior<Request> onPreRestart(Signal signal) {
        log.info("pre restart main {}", signal);
        return this;
    }

    private Behavior<Request> onPostStop(Signal signal) {
        log.info("post stop main {}", signal);

        httpAllDirectives.close();
        return this;
    }

    private Behavior<MainActor.Request> onEcho(Request request) {

        EchoActor.EchoRequest echoRequest = new EchoActor.EchoRequest("hello", echoResponseActor);
        echoActor.tell(echoRequest);

        return this;
    }

    public static class Request {

    }


}
