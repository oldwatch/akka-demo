package com.techdemo.client.actor;

import akka.actor.ActorSelection;
import akka.actor.Address;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.entrys.EchoEntryParam;
import com.techdemo.entrys.EchoResult;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoActor extends AbstractBehavior<EchoActor.EchoRequest> {

    private final ActorSelection remotePath;
    private Logger log = LoggerFactory.getLogger(EchoActor.class);

    private EchoActor(ActorContext<EchoRequest> context, String remotePath) {
        super(context);

        this.remotePath = context.classicActorContext().actorSelection(remotePath);
    }

    public static Behavior<EchoRequest> create(String remotePath) {
        return Behaviors.setup((context) -> new EchoActor(context, remotePath));
    }

    @Override
    public Receive<EchoRequest> createReceive() {
        return newReceiveBuilder()
                .onMessage(EchoRequest.class, this::onRequest)
                .build();
    }

    private Behavior<EchoRequest> onRequest(EchoRequest request) {

        Address address = getContext().getSystem().address();

        EchoEntryParam param = new EchoEntryParam(request.request, request.getCallBackRef().path().toStringWithAddress(address));

        remotePath.tell(param, this.getContext().classicActorContext().self());

        return this;


    }

    @Data
    public static class EchoRequest {
        private final String request;

        private final ActorRef<EchoResult> callBackRef;

    }


}
