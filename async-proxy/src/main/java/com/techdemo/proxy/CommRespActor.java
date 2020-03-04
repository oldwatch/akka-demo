package com.techdemo.proxy;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.entrys.CostTimeResult;
import com.techdemo.entrys.EchoResult;
import com.techdemo.entrys.RespResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommRespActor extends AbstractBehavior<RespResult> {

    private final Logger log = LoggerFactory.getLogger(CommRespActor.class);

    private CommRespActor(ActorContext<RespResult> context) {
        super(context);
    }

    public static Behavior<RespResult> create() {
        return Behaviors.setup(CommRespActor::new);
    }

    @Override
    public Receive<RespResult> createReceive() {
        return newReceiveBuilder()
                .onMessage(EchoResult.class, this::onEchoResp)
                .onMessage(CostTimeResult.class, this::onCostTimeResp)
                .build();
    }

    private Behavior<RespResult> onEchoResp(EchoResult result) {
        log.info("cost time result {}", result.getResponse());

        return this;
    }

    private Behavior<RespResult> onCostTimeResp(CostTimeResult result) {
        log.info("echo result: {} ", result.getResult());
        return this;
    }
}
