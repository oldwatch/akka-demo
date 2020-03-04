package com.techdemo.proxy;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import com.techdemo.entrys.CostTimeParam;
import com.techdemo.entrys.EchoEntryParam;
import com.techdemo.entrys.ReqParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatchActor extends AbstractBehavior<ReqParam> {


    private static final ServiceKey<ReqParam> serviceKey = ServiceKey.create(ReqParam.class, "proxyService");
    private final ActorRef<CostTimeProxyActor.CostTimeRequest> costTimeActor;
    private final ActorRef<EchoProxyActor.EchoRequest> echoActor;
    private final ActorRef<RemoteCallActor.RemoteCallReq> remoteProxyActor;
    private Logger log = LoggerFactory.getLogger(DispatchActor.class);

    private DispatchActor(ActorContext<ReqParam> context) {
        super(context);

        costTimeActor = context.spawn(
                Behaviors.supervise(
                        CostTimeProxyActor.create()
                ).onFailure(SupervisorStrategy.restart()), "costTime");

        echoActor = context.spawn(
                Behaviors.supervise(
                        EchoProxyActor.create()
                ).onFailure(SupervisorStrategy.restart()), "echo");

        remoteProxyActor = context.spawn(RemoteCallActor.create(), "remoteProxy");

    }

    public static Behavior<ReqParam> create() {

        return Behaviors.setup(
                context -> {
                    context.getSystem().receptionist()
                            .tell(Receptionist.register(serviceKey, context.getSelf()));
                    return new DispatchActor(context);
                }
        );
    }

    @Override
    public Receive<ReqParam> createReceive() {
        return newReceiveBuilder()
                .onMessage(EchoEntryParam.class, this::onEcho)
                .onMessage(CostTimeParam.class, this::onCost)
                .build();
    }

    private Behavior<ReqParam> onEcho(EchoEntryParam echo) {


        echoActor.tell(new EchoProxyActor.EchoRequest(echo, remoteProxyActor));

        return this;
    }


    private Behavior<ReqParam> onCost(CostTimeParam costTime) {

        costTimeActor.tell(new CostTimeProxyActor.ExecRequest(costTime, remoteProxyActor));

        return this;
    }
}
