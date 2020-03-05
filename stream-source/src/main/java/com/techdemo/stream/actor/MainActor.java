package com.techdemo.stream.actor;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.stream.HttpAllDirectives;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActor extends AbstractBehavior<MainActor.Request> {


    private final HttpAllDirectives httpAllDirectives;
    private final ActorRef<EchoActor.EchoRequest> echoActor;
    private Logger log = LoggerFactory.getLogger(MainActor.class);

    private MainActor(ActorContext<Request> context) {

        super(context);

        echoActor = getContext().spawn(EchoActor.create(), "echo");

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

        log.info("hello ");

        return this;
    }

    public static class Request {

    }

}
