package com.techdemo.stream.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.entrys.StreamEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputActor extends AbstractBehavior<StreamEntry> {

    private Logger log = LoggerFactory.getLogger(OutputActor.class);

    private OutputActor(ActorContext<StreamEntry> context) {
        super(context);
    }

    public static Behavior<StreamEntry> create() {
        return Behaviors.setup(OutputActor::new);
    }

    @Override
    public Receive<StreamEntry> createReceive() {
        return newReceiveBuilder()
                .onMessage(StreamEntry.class, this::onEntry)
                .build();
    }

    private Behavior<StreamEntry> onEntry(StreamEntry a) {

        log.info("receive entry {} ", a.getPk());

        return this;
    }
}
