package com.house.backend.houseservice.utils;

import com.github.pagehelper.Page;
import com.house.backend.houseservice.enums.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 jdbcTemplate 封装类
 **/
@Slf4j
public class JdbcTemplateSupport extends JdbcTemplate {
    public JdbcTemplateSupport() {
    }

    public JdbcTemplateSupport(DataSource dataSource) {
        super(dataSource);
    }

    public JdbcTemplateSupport(DataSource dataSource, boolean lazyInit) {
        super(dataSource, lazyInit);
    }

    public <T> PageBean<T> queryForPage(String sql, Page<T> pagination, RowMapper<T> rowMapper) throws DataAccessException {

        return queryForPage(sql, pagination, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                return;
            }
        }, rowMapper);
    }


    public <T> PageBean<T> queryForPage(String sql, Page<T> pagination, PreparedStatementSetter var2, RowMapper<T> var3) throws DataAccessException {

        PageBean<T> result = new PageBean<T>();

        //获取记录条数
        String countSql = "select count(1) as count from (" + sql + ") temp";
        log.info("countSql {}", countSql);
        List<Integer> countList = super.query(countSql, var2, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Integer(resultSet.getInt("count"));
            }
        });


        result.setTotal(countList.get(0));
        result.setCurrentPage(pagination.getPageNum());
        result.setPageSize(pagination.getPageSize());

        int pageCount = result.getSize() % result.getPageSize();
        result.setPages(pageCount == 0 ? (result.getSize() / result.getPageSize()) : (result.getSize() / result.getPageSize() + 1));

        sql += parseLimit(result);
        log.info("queryLimitSql {}", sql);

        List<T> data = super.query(sql, var2, var3);
        result.setList(data);
        return result;
    }

    public  PageBean<List<Map<String, Object>>> queryForPageNew(String sql,  Page<Map<String, Object>> pagination) throws DataAccessException {

        return queryForPageNew(sql, pagination, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                return;
            }
        });
    }


    public PageBean<List<Map<String, Object>>> queryForPageNew(String sql, Page<Map<String, Object>> pagination, PreparedStatementSetter var2) throws DataAccessException {

        PageBean<List<Map<String, Object>>> result = new PageBean<List<Map<String, Object>>>();

        //获取记录条数
        String countSql = "select count(1) as count from (" + sql + ") temp";
        log.info("countSql {}", countSql);
        List<Integer> countList = super.query(countSql, var2, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Integer(resultSet.getInt("count"));
            }
        });


        result.setTotal(countList.get(0));
        result.setCurrentPage(pagination.getPageNum());
        result.setPageSize(pagination.getPageSize());

        int pageCount = result.getSize() % result.getPageSize();
        result.setPages(pageCount == 0 ? (result.getSize() / result.getPageSize()) : (result.getSize() / result.getPageSize() + 1));

        sql += parseLimit(result);
        log.info("queryLimitSql {}", sql);

        List<Map<String, Object>> dataMap= super.queryForList(sql);
        result.setMapList(dataMap);
        return result;
    }

    private <T> String parseLimit(PageBean<T> pagination) {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" ");
        stringBuffer.append("AND RN >");
       stringBuffer.append((pagination.getCurrentPage()-1) * pagination.getPageSize());
        stringBuffer.append(" ");
        stringBuffer.append("AND RN<=");
        stringBuffer.append((pagination.getCurrentPage()) * pagination.getPageSize());

        return stringBuffer.toString();
    }
}