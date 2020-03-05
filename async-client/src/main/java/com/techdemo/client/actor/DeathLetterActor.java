package com.techdemo.client.actor;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;

public class DeathLetterActor extends AbstractBehavior<String> {

    private DeathLetterActor(ActorContext<String> context) {
        super(context);

    }

    @Override
    public Receive<String> createReceive() {
        return null;
    }
}
