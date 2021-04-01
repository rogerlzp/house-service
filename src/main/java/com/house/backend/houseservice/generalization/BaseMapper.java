package com.house.backend.houseservice.generalization;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author zhengshuqin
 */
public interface BaseMapper {
    /**
     * 根据seqName查询序列值
     *
     * @param seqName
     * @return
     */
    @Select("select ${seqName}.nextval from dual")
    // 尾佣新增序列时，在循环中不会更新序列，故增加注解，清空缓存，以便于获取序列
    @Options(useCache=false ,flushCache= Options.FlushCachePolicy.TRUE)
    String querySeqByName(@Param("seqName") String seqName);


    /**
     * 执行sql
     * @param checkSql
     * @return
     */
    @Select("${checkSql}")
    List<Map<String, Object>> executeSql(String checkSql);

}
