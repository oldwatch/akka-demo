package com.techdemo.client.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.entrys.CostTimeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CostTimeResponseActor extends AbstractBehavior<CostTimeResult> {

    private Logger log = LoggerFactory.getLogger(CostTimeResponseActor.class);

    private CostTimeResponseActor(ActorContext<CostTimeResult> context) {
        super(context);
    }

    public static Behavior<CostTimeResult> create() {
        return Behaviors.setup(CostTimeResponseActor::new);
    }

    @Override
    public Receive<CostTimeResult> createReceive() {
        return newReceiveBuilder()
                .onMessage(CostTimeResult.class, this::onResponse)
                .build();
    }

    private Behavior<CostTimeResult> onResponse(CostTimeResult response) {

        log.info("receive cost time response {} ", response.getResult());

        return this;

    }


}
