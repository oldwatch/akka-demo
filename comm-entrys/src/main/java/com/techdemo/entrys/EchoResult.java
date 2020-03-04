package com.techdemo.entrys;

import lombok.Data;

@Data
public class EchoResult implements RespResult {

    private final String response;
}
