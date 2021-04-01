package com.house.backend.houseservice.utils;

import com.house.backend.houseservice.exception.HouseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author zhengshuqin
 */
@Slf4j
public class JdbcConnectHelp {
    public static final String DATASOURCE_ = "dataSource_";
    protected static final Hashtable dataSourceHashtable = new Hashtable();


    public static void removeDs(String connCode) {

        DefaultListableBeanFactory beanFactory = SpringContextUtil.getBeanFactory();
        if (beanFactory.containsBean(connCode)) {
            beanFactory.destroySingleton(connCode);
        }
        String dataSourcekey = JdbcConnectHelp.DATASOURCE_ + connCode;
        log.info(">>>>> init database [{}] success ......", dataSourcekey);
        if (beanFactory.containsBean(dataSourcekey)) {
            beanFactory.destroySingleton(dataSourcekey);
        }
        SpringContextUtil.setBeanFactory(beanFactory);
    }

    public static DataSource initDataSource(Integer maxSize, Integer minIde, String connCode, String url, String username, String password, String driverName) {
        int db_max_conn = (maxSize == null ? 20 : maxSize);
        int db_min_conn = (minIde == null ? 5 : minIde);
        String db_url = url;
        String db_username = username;
        String db_password = password;
        String db_classname = driverName;

        if (db_url == null || db_url.length() == 0) {
            log.error("配置的数据库ip地址错误! url{} username{} password{} driverName{}");
            return null;
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(db_classname);
        config.setJdbcUrl(url);
        config.setUsername(db_username);
        config.setPassword(db_password);
        config.setMaximumPoolSize(db_max_conn);//        <!-- 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count) -->
        config.setMinimumIdle(db_min_conn);
        config.setMaxLifetime(1000 * 60 * 10);// <!-- 一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL  wait_timeout参数（show variables like '%timeout%';） -->
        config.setIdleTimeout(1000 * 60 * 10);//        <!-- 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟 -->
        config.setValidationTimeout(1000 * 10);
        config.setConnectionTimeout(1000 * 60 * 2); //等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒
        config.setReadOnly(false);

        config.setPoolName(connCode);//连接池名字

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        // 设置连接超时为8小时
        config.setConnectionTimeout(8 * 60 * 60);
        HikariDataSource dataSource = null;
        if (dataSourceHashtable.get(connCode) != null) {
            dataSource = (HikariDataSource) dataSourceHashtable.get(connCode);
            dataSource.close();
        }
        dataSource = new HikariDataSource(config);
        dataSourceHashtable.put(connCode, dataSource);
        return dataSource;
    }


    /**
     * 获取jdbcTemplate
     *
     * @param url
     * @param username
     * @param password
     * @param driverName
     * @return
     */
    public static JdbcTemplate getJdbcTemplate(String url, String username, String password, String driverName) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        DataSource ds = dataSourceBuilder
                .url(url).username(username).password(password)
                .driverClassName(driverName).build();
        return new JdbcTemplate(ds);
    }


    public static JdbcTemplate getJdbcTemplate(String connCode) {
        JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean(connCode);
        if (Objects.isNull(jdbcTemplate)) {
            throw new HouseException(connCode + "没有初始化");
        }
        return jdbcTemplate;
    }


    public static boolean execute(String connCode, String sql) {
        return execute(connCode, sql, null);
    }

    public static boolean execute(String connCode, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection(connCode);
            if (Objects.isNull(conn)) {
                throw new HouseException(connCode + "获取连接异常");
            }
            ps = conn.prepareStatement(sql);
            //有参数
            if (!Objects.isNull(args)) {
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }
            }
            //执行sql语句
            int i = ps.executeUpdate();
            //返回  true
            return i > 0;
        } catch (SQLException e) {
            log.error("执行sql出问题：", e);
        } finally {
            //关闭资源
            close(conn, ps, null);
        }
        return false;
    }


    /**
     * 查询的通用方法
     *
     * @param connCode
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String connCode, String sql) {
        return executeQuery(connCode, sql, null);
    }

    /**
     * 查询的通用方法
     *
     * @param sql;
     * @param args;
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String connCode, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection(connCode);
            if (Objects.isNull(conn)) {
                throw new HouseException(connCode + "获取连接异常");
            }
            ps = conn.prepareStatement(sql);
            //有可能有参数
            if (!Objects.isNull(args)) {
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }
            }

            //执行sql语句
            rs = ps.executeQuery();
            //创建List集合
            List<Map<String, Object>> list = new ArrayList<>();
            //获取本次查询结果集有多少列
            int count = rs.getMetaData().getColumnCount();
            //while循环
            while (rs.next()) {
                //创建Map集合   获取一个数据封装成一个Map集合
                Map<String, Object> map = new HashMap<>();
                //for循环  遍历所有的列
                for (int i = 0; i < count; i++) {
                    //获取本次查询结果集的列名
                    String name = rs.getMetaData().getColumnLabel(i + 1);
                    map.put(name, rs.getObject(name));
                }
                //把所有的map集合添加到List集合中
                list.add(map);
            }
            //返回值
            return list;
        } catch (SQLException e) {
            log.error("执行sql出问题：", e);
        } finally {
            //关闭资源
            close(conn, ps, rs);
        }
        return null;
    }


    private static Connection getConnection(String connCode) {
        String dataSourcekey = JdbcConnectHelp.DATASOURCE_ + connCode;
        DataSource dataSource = (DataSource) SpringContextUtil.getBean(dataSourcekey);

        if (Objects.isNull(dataSource)) {
            throw new HouseException(connCode + "没有初始化");
        }

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("获取数据库连接出错：", e);
        }

        return null;
    }

    public static void getConnectPoolInfo(String connCode) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(connCode);
        HikariDataSource db = (HikariDataSource) jdbcTemplate.getDataSource();
        log.info("Active:" + db.getHikariPoolMXBean().getActiveConnections());
        log.info("Idle:" + db.getHikariPoolMXBean().getIdleConnections());
        log.info("Await:" + db.getHikariPoolMXBean().getThreadsAwaitingConnection());
        log.info("Total:" + db.getHikariPoolMXBean().getTotalConnections());
    }

    public static void releaseConn(String connCode) throws SQLException {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(connCode);
        DataSource dataSource = jdbcTemplate.getDataSource();
        DataSourceUtils.releaseConnection(dataSource.getConnection(), dataSource);
        log.info("释放数据源{}连接池成功", connCode);
    }

    public static void getConnectPoolInfo(JdbcTemplate jdbcTemplate) {
        HikariDataSource db = (HikariDataSource) jdbcTemplate.getDataSource();
        log.info("Active:" + db.getHikariPoolMXBean().getActiveConnections());
        log.info("Idle:" + db.getHikariPoolMXBean().getIdleConnections());
        log.info("Await:" + db.getHikariPoolMXBean().getThreadsAwaitingConnection());
        log.info("Total:" + db.getHikariPoolMXBean().getTotalConnections());
    }

    public static void releaseConn(JdbcTemplate jdbcTemplate) throws SQLException {
        HikariDataSource dataSource = (HikariDataSource) jdbcTemplate.getDataSource();
        DataSourceUtils.releaseConnection(dataSource.getConnection(), dataSource);
        log.info("释放数据源{}连接池成功", dataSource.getPoolName());
    }


    /**
     * 关闭资源的通用方法
     */
    private static void close(Connection conn, Statement stat, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            log.error("关闭资源的通用方法失败", e);
        }
    }
}
