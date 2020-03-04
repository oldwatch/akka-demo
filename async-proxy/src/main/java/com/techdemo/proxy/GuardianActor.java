package com.techdemo.proxy;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.entrys.ReqParam;

public class GuardianActor extends AbstractBehavior<GuardianActor.Start> {


    private final ActorRef<ReqParam> dispatchActor;

    private GuardianActor(ActorContext<Start> context) {
        super(context);

        dispatchActor = context.spawn(DispatchActor.create(), "dispatch");

    }

    public static Behavior<Start> create() {

        return Behaviors.setup(GuardianActor::new);

    }

    @Override
    public Receive<Start> createReceive() {
        return newReceiveBuilder().build();
    }


    public static class Start {

    }


}
