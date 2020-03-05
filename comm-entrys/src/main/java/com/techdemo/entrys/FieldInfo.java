package com.techdemo.entrys;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FieldInfo {

    private String fieldName;

    private String newValue;

    private String oldValue;

    private FieldType type;

    public enum FieldType {
        Number, Decimal, String, Date;
    }
}
