package com.techdemo.proxy.test;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import com.techdemo.entrys.*;
import com.techdemo.proxy.DispatchActor;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static junit.framework.TestCase.assertEquals;

public class SmokingTest {

    ActorRef<ReqParam> dispatch;
    private TestKitJunitResource testKit = new TestKitJunitResource();

    @Before
    public void init() {
        dispatch = testKit.spawn(DispatchActor.create());
    }

    @Test
    public void testEcho() {

        TestProbe<EchoResult> echoProbe = testKit.createTestProbe(EchoResult.class);


        dispatch.tell(new EchoEntryParam("hello", echoProbe.getRef()));

        EchoResult result = echoProbe.receiveMessage();

        assertEquals(result.getResponse(), "echo:hello");

    }

    @Test
    public void testCostTime() {

        TestProbe<CostTimeResult> costTimeProbe = testKit.createTestProbe(CostTimeResult.class);

        dispatch.tell(new CostTimeParam(3, costTimeProbe.getRef()));

        CostTimeResult ctResult = costTimeProbe.receiveMessage(Duration.ofSeconds(5));

        assertEquals("OK", ctResult.getResult());


    }


}
