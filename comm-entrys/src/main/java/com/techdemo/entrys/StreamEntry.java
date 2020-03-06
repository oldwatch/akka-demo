package com.techdemo.entrys;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StreamEntry implements CborSerializable {

    public static StreamEntry NULL = new StreamEntry();

    static {
        NULL.uuid = "_NULL_";
    }

    private String uuid;

    private String pk;

    private List<FieldInfo> fieldInfoList;

    private OperateType operate;

    public enum OperateType {

        Insert, Update, Delete;
    }
}
