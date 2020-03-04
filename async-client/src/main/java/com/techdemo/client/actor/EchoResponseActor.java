package com.techdemo.client.actor;

import akka.actor.ActorSelection;
import akka.actor.Address;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.entrys.CostTimeParam;
import com.techdemo.entrys.CostTimeResult;
import com.techdemo.entrys.EchoResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoResponseActor extends AbstractBehavior<EchoResult> {

    private Logger log = LoggerFactory.getLogger(EchoActor.class);

    private final ActorRef<CostTimeResult> costTimeResponse;

    private final ActorSelection selection;

    private EchoResponseActor(ActorContext<EchoResult> context, String remotePath, ActorRef<CostTimeResult> costTimeResponse) {
        super(context);

        this.costTimeResponse = costTimeResponse;

        this.selection = context.classicActorContext().actorSelection(remotePath);
    }

    public static Behavior<EchoResult> create(String remotePath, ActorRef<CostTimeResult> costTimeResponse) {
        return Behaviors.setup((context) -> new EchoResponseActor(context, remotePath, costTimeResponse));
    }

    @Override
    public Receive createReceive() {
        return newReceiveBuilder()
                .onMessage(EchoResult.class, this::onResponse)
                .build();
    }

    private Behavior<EchoResult> onResponse(EchoResult response) {


        Address address = getContext().getSystem().address();

        CostTimeParam param = new CostTimeParam(3, costTimeResponse.path().toStringWithAddress(address));

        log.info("now ,i call remote cost time actor ");

        selection.tell(param, this.getContext().classicActorContext().self());

        log.info("finish cost time actor call");

        return this;
    }



}
