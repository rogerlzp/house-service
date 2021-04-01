package com.house.backend.houseservice.generalization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 */
@Getter
@Setter
@NoArgsConstructor
public class QueryCondition {

    private String key;

    private String value;

    private String type;

    public QueryCondition(String key, String value, String type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public QueryCondition(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
