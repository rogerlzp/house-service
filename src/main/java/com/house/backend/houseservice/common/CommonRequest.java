package com.house.backend.houseservice.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * @author liyatao
 * 分页查询参数
 */
@Setter
@Getter
@ToString
public class CommonRequest {

    private int pageSize = 100;

    private int currentPage = 1;

    private String keys;

    private String operType;

    private String keyWords;

    private String orderByClause;


    /**
     * 动态查询条件,例：
     * {
     * "conditions":[{
     * "key":"connCode",
     * "value":"like %TEST%"
     * },{
     * "key":"driverName",
     * "value":"= oracle.jdbc.OracleDriver",
     * "type":"and"
     * }
     * ,{
     * "key":"dbUrl",
     * "value":"= oracle.jdbc.OracleDriver",
     * "type":"or"
     * }]
     * }
     */
    private List<QueryCondition> conditions;

}
