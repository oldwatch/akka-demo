package com.techdemo.proxy;

import akka.actor.typed.ActorSystem;
import akka.cluster.Cluster;
import com.techdemo.entrys.ReqParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainEntrance {

    private static final Logger log = LoggerFactory.getLogger(MainEntrance.class);

    public static void main(String[] argv) {
        log.info("starting...");
        ActorSystem<ReqParam> system = ActorSystem.create(DispatchActor.create(), "dispatch");

        Cluster cluster = Cluster.get(system);

        log.info("finish");
    }
}
