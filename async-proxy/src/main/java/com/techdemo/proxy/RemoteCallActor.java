package com.techdemo.proxy;

import akka.actor.ActorSelection;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.entrys.RespResult;
import lombok.Data;

public class RemoteCallActor extends AbstractBehavior<RemoteCallActor.RemoteCallReq> {


    private RemoteCallActor(ActorContext<RemoteCallReq> context) {
        super(context);
    }

    public static Behavior<RemoteCallReq> create() {
        return Behaviors.setup(RemoteCallActor::new);
    }

    @Override
    public Receive<RemoteCallReq> createReceive() {

        return newReceiveBuilder().onMessage(RemoteCallReq.class, this::doRemoteCall).build();
    }

    private Behavior<RemoteCallReq> doRemoteCall(RemoteCallReq request) {

        String path = request.path;

        ActorSelection selection = this.getContext().classicActorContext().actorSelection(path);

        selection.tell(request.result, this.getContext().classicActorContext().self());

        return this;

    }

    @Data
    public static class RemoteCallReq {
        private final String path;

        private final RespResult result;
    }
}
