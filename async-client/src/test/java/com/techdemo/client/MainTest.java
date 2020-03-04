package com.techdemo.client;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import com.techdemo.client.actor.EchoResponseActor;
import com.techdemo.entrys.CostTimeResult;
import com.techdemo.entrys.EchoResult;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static junit.framework.TestCase.assertEquals;

public class MainTest {

    private final String basePath = "akka://service-proxy@127.0.0.1:25520/user/dispatch";
    private TestKitJunitResource testKit = new TestKitJunitResource();

    @Before
    public void init() {


    }

    @Test
    public void testCostTime() throws InterruptedException {

        TestProbe<CostTimeResult> costTimeProbe = testKit.createTestProbe(CostTimeResult.class);


        ActorRef<EchoResult> echoResponse = testKit.spawn(EchoResponseActor.create(basePath, costTimeProbe.getRef()));

        EchoResult resp = new EchoResult("abc");

        echoResponse.tell(resp);


        CostTimeResult response = costTimeProbe.receiveMessage(Duration.ofSeconds(5));
        assertEquals("OK", response.getResult());

    }

}
