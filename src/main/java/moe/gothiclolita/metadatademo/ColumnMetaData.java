package moe.gothiclolita.metadatademo;

import lombok.Data;

@Data
public class ColumnMetaData {

    String name;
    String type;
    String comment;
    String defaultValue;
    String isNullable;
//    String isPrimaryKey;
//    String isUnique;
}
