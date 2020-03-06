package com.techdemo.stream.actor;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.http.javadsl.Http;
import akka.http.javadsl.common.EntityStreamingSupport;
import akka.http.javadsl.common.JsonEntityStreamingSupport;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import akka.stream.Materializer;
import akka.stream.typed.javadsl.ActorSink;
import akka.util.ByteString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techdemo.entrys.StreamEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionStage;

public class ClientActor extends AbstractBehavior<ClientActor.Start> {

    final ObjectMapper mapper = new ObjectMapper();
    final Materializer materializer;
    final Unmarshaller<ByteString, StreamEntry> unmarshaller = Jackson.byteStringUnmarshaller(StreamEntry.class);
    final JsonEntityStreamingSupport jsonSupport = EntityStreamingSupport.json();
    private final ActorRef<StreamEntry> outputActor;
    private Logger log = LoggerFactory.getLogger(ClientActor.class);

    private ClientActor(ActorContext<Start> context) {
        super(context);

        outputActor = context.spawn(OutputActor.create(), "output");


        materializer = Materializer.createMaterializer(context.getSystem());

    }

    public static Behavior<Start> create() {
        return Behaviors.setup(ClientActor::new);
    }

    @Override
    public Receive<Start> createReceive() {
        return newReceiveBuilder()
                .onMessage(Start.class, this::onStart)
                .onSignal(PreRestart.class, this::onPreRestart)
                .onSignal(PostStop.class, this::onPostStop)

                .build();
    }

    private Behavior<Start> onPreRestart(Signal signal) {
        log.info("pre restart main {}", signal);


        return this;
    }

    private Behavior<Start> onPostStop(Signal signal) {
        log.info("post stop main {}", signal);

        return this;
    }

    private Behavior<Start> onStart(Start a) {

        CompletionStage<HttpResponse> responseFuture = Http.get(getContext().getSystem().classicSystem())
                .singleRequest(HttpRequest.create("http://localhost:9090/stream"));

        responseFuture.thenAccept((response) -> {

            response.entity().getDataBytes()
                    .via(jsonSupport.framingDecoder())
                    .mapAsync(1, bytes -> unmarshaller.unmarshal(bytes, materializer))
                    .runWith(ActorSink.actorRef(outputActor, StreamEntry.NULL, (throwable) -> {
                        return StreamEntry.NULL;
                    }), materializer);

        });
        return this;
    }

    public static class Start {

    }
}
