package com.techdemo.entrys;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StreamEntry implements CborSerializable {


    private String uuid;

    private String pk;

    private List<FieldInfo> fieldInfoList;

    private OperateType operate;

    public enum OperateType {

        Insert, Update, Delete;
    }
}
