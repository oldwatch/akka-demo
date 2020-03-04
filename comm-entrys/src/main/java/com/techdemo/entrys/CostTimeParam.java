package com.techdemo.entrys;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostTimeParam implements ReqParam {

    private int cost;

    private String fromPath;

}
