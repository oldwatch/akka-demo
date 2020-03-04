package com.techdemo.entrys;

import akka.actor.ActorPath;
import lombok.Data;

@Data
public class EchoEntryParam implements ReqParam {

    private final String request;

    private final ActorPath fromPath;
}
