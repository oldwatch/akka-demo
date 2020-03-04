package com.techdemo.entrys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EchoEntryParam implements ReqParam {

    private String request;

    private String fromPath;
}
