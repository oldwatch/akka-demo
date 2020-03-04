package com.techdemo.proxy.test;

import akka.actor.ActorPath;
import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import com.techdemo.entrys.*;
import com.techdemo.proxy.DispatchActor;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static junit.framework.TestCase.assertEquals;

public class SmokingTest {

    private Logger log = LoggerFactory.getLogger(SmokingTest.class);

    ActorRef<ReqParam> dispatch;
    private TestKitJunitResource testKit = new TestKitJunitResource();

    @Before
    public void init() {
        dispatch = testKit.spawn(DispatchActor.create());
    }

    @Test
    public void testEcho() {

        TestProbe<EchoResult> echoProbe = testKit.createTestProbe(EchoResult.class);


        dispatch.tell(new EchoEntryParam("hello", echoProbe.getRef().path().toString()));

        ActorPath path = dispatch.path();
        log.info("path : {} ", path.address().toString());
        EchoResult result = echoProbe.receiveMessage();

        assertEquals(result.getResponse(), "echo:hello");

    }

    @Test
    public void testCostTime() {

        TestProbe<CostTimeResult> costTimeProbe = testKit.createTestProbe(CostTimeResult.class);

        dispatch.tell(new CostTimeParam(3, costTimeProbe.getRef().toString()));

        CostTimeResult ctResult = costTimeProbe.receiveMessage(Duration.ofSeconds(5));

        assertEquals("OK", ctResult.getResult());


    }


}
