package com.techdemo.proxy;

import akka.actor.ActorPath;
import akka.actor.typed.*;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.techdemo.entrys.CostTimeParam;
import com.techdemo.entrys.CostTimeResult;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CostTimeProxyActor extends AbstractBehavior<CostTimeProxyActor.CostTimeRequest> {


    private Logger log = LoggerFactory.getLogger(CostTimeProxyActor.class);

    private CostTimeProxyActor(ActorContext<CostTimeRequest> context) {
        super(context);
    }

    public static Behavior<CostTimeRequest> create() {
        return Behaviors.setup(CostTimeProxyActor::new);
    }

    @Override
    public Receive<CostTimeRequest> createReceive() {
        return newReceiveBuilder()
                .onMessage(ExecRequest.class, this::onCostTime)
                .onMessage(WrapperRequest.class, this::onResponse)
                .onSignal(PreRestart.class, this::onPreRestart)
                .onSignal(PostStop.class, this::onPostStop)
                .build();
    }

    private Behavior<CostTimeRequest> onPreRestart(Signal signal) {
        log.info("pre restart costtime {}", signal);
        return this;
    }

    private Behavior<CostTimeRequest> onPostStop(Signal signal) {
        log.info("post stop costtime {}", signal);
        return this;
    }

    private Behavior<CostTimeRequest> onCostTime(ExecRequest request) {

        CostTimeParam param = request.param;

        CompletionStage<String> futureResult = CompletableFuture
                .completedStage(param.getCost())
                .thenApplyAsync((costTime) -> {
                    try {
                        for (int i = 0; i < costTime; i++) {

                            log.info("start {}", i);
                            Thread.sleep(1000l);

                        }
                        return "OK";
                    } catch (InterruptedException e) {
                        return e.getMessage();
                    }
                });

        getContext().pipeToSelf(
                futureResult,
                (ok, exception) -> {
                    if (exception == null) {
                        return new WrapperRequest(ok, request.param.getFromPath(), request.from);
                    } else {
                        return new WrapperRequest(exception.getMessage(), request.param.getFromPath(), request.from);
                    }
                });

        return this;

    }

    private Behavior<CostTimeRequest> onResponse(WrapperRequest wrapperRequest) {

        CostTimeResult result = new CostTimeResult(wrapperRequest.result);
        wrapperRequest.from.tell(new RemoteCallActor.RemoteCallReq(wrapperRequest.getActorPath(), result));

        return this;
    }

    public interface CostTimeRequest {

    }

    @Data
    public static class ExecRequest implements CostTimeRequest {

        private final CostTimeParam param;

        private final ActorRef<RemoteCallActor.RemoteCallReq> from;
    }

    @Data
    public static class WrapperRequest implements CostTimeRequest {

        private final String result;

        private final ActorPath actorPath;

        private final ActorRef<RemoteCallActor.RemoteCallReq> from;
    }

}
