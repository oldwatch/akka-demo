package com.techdemo.proxy;

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

public class EchoProxyActor extends AbstractBehavior<EchoProxyActor.EchoRequest> {

    private Logger log = LoggerFactory.getLogger(EchoProxyActor.class);

    private EchoProxyActor(ActorContext<EchoRequest> context) {
        super(context);
    }

    public static Behavior<EchoRequest> create() {

        return Behaviors.setup(EchoProxyActor::new);
    }

    @Override
    public Receive<EchoRequest> createReceive() {
        return newReceiveBuilder()
                .onMessage(EchoProxyActor.EchoRequest.class, this::echo)
                .build();
    }

    private Behavior<EchoRequest> echo(EchoProxyActor.EchoRequest request) {

        EchoEntryParam param = request.param;

        RemoteCallActor.RemoteCallReq req = new RemoteCallActor.RemoteCallReq(param.getFromPath(), new EchoResult("echo:" + param.getRequest()));
        request.getFrom().tell(req);

        return this;
    }

    @Data
    public static class EchoRequest {

        private final EchoEntryParam param;

        private final ActorRef<RemoteCallActor.RemoteCallReq> from;

    }
}
