package com.techdemo.entrys;

import akka.actor.typed.ActorRef;

public interface ReqParam extends CborSerializable {

    public ActorRef<? extends RespResult> getFromPath();
}
