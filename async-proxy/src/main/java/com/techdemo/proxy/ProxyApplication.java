package com.techdemo.proxy;

import akka.actor.typed.ActorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@SpringBootApplication
public class ProxyApplication {

    private static final Logger log = LoggerFactory.getLogger(ProxyApplication.class);

    public static void main(String[] argv) {

        log.debug("start...");
//        SpringApplication.run(ProxyApplication.class);

        ActorSystem.create(DispatchActor.create(), "dispatch");

    }
}
