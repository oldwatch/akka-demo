package com.techdemo.entrys;


import akka.actor.ActorPath;
import lombok.Data;

@Data
public class CostTimeParam implements ReqParam {

    private final int cost;

    private final ActorPath fromPath;

}
