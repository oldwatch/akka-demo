package com.techdemo.stream;

import akka.NotUsed;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.util.ByteString;
import com.techdemo.entrys.EchoResult;
import com.techdemo.stream.actor.EchoActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.PathMatchers.segment;

public class HttpAllDirectives extends AllDirectives {

    private final DemoStream demoSource;

    private final CompletionStage<ServerBinding> binding;
    private final ActorSystem system;
    private final Http http;
    private final ActorRef<EchoActor.EchoRequest> echoActor;
    private Logger log = LoggerFactory.getLogger(HttpAllDirectives.class);

    public HttpAllDirectives(ActorContext actorContext) {

        this.system = actorContext.getSystem();

        this.echoActor = (ActorRef<EchoActor.EchoRequest>) actorContext.getChild("echo").get();

        this.demoSource = new DemoStream();

        akka.actor.ActorSystem classicSystem = system.classicSystem();

        http = Http.get(classicSystem);

        Materializer materializer = Materializer.createMaterializer(system);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                createRoute()
                        .flow(classicSystem, materializer);

        binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 9090), materializer);


    }

    private Route createRoute() {

        return concat(
                path("hello", () ->
                        get(() ->
                                complete("hello,world")
                        )),
                path("stream", () ->
                        get(() ->
                                complete(HttpEntities.create(
                                        ContentTypes.TEXT_PLAIN_UTF8,
                                        demoSource.getSource2()
                                                .map(ByteString::fromString)

                                ))
                        )
                ),
                get(() ->
                        pathPrefix("echo", () ->
                                path(segment(), (echo) -> {
                                    final CompletionStage<EchoResult> future = getEchoResult(echo);
                                    return onSuccess(future, result ->
                                            completeOK(result, Jackson.marshaller()));
                                })))
        );
    }

    private CompletionStage<EchoResult> getEchoResult(String echo) {

        return AskPattern.ask(
                echoActor,
                (reply) -> new EchoActor.EchoRequest(echo, reply),
                Duration.ofSeconds(1),
                system.scheduler()
        );

    }

    public void close() {

        binding
                .thenCompose(ServerBinding::unbind);
//                .thenAccept(unbound->system.terminate());
    }

}
