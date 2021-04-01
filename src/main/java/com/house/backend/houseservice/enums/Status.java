package com.house.backend.houseservice.enums;

import lombok.Getter;

/**
 * status enum
 *
 * @author zhengshuqin
 */
@Getter
public enum Status {
    /*返回status类型枚举*/
    SUCCESS("0000", "success"),
    /*common*/
    UPDATE_ERROR("1001", "更新数据失败"),
    INSERT_ERROR("1002", "新增数据失败"),
    DELETE_ERROR("1003", "删除数据失败"),
    PAGE_ERROR("1004", "分页查询数据失败"),
    LIST_QUERY_ERROR("1005", "查询List数据失败"),
    ONE_QUERY_ERROR("1006", "查询单条数据失败"),
    INVALID_OPER("1007", "非法操作"),
    SEQ_ERROR("1008", "获取序列错误"),
    CONDITION_QUERY_ERROR("1009", "条件查询出错"),
    CONDITION_SET_ERROR("1010", "查询条件设置出错"),
    BUILD_MODEL_ERROR("1011", "创建对象失败"),
    USER_ERROR("1012", "未查询到用户信息"),
    /*backend*/
    USER_NAME_NULL("2001", "user name is null"),
    USER_NAME_PASSWD_ERROR("2002", "user name or password error"),
    LOGIN_SESSION_FAILED("2003", "create session failed!"),
    LOGIN_SUCCESS("2004", "login success"),
    USER_LOGIN_FAILURE("2005", "user login failure"),
    IP_IS_EMPTY("2006", "ip is empty"),
    CODE_AUTHORIZATION_TOKEN_IS_NULL("AU0001", "token校验失败，禁止访问！"),
    CODE_AUTHORIZATION_GET_USERINFO("AU0002", "获取用户信息失败，请稍后再试"),
    CODE_AUTHORIZATION_SYSKEY_IS_NULL("AU0003", "获取用户系统信息失败，请稍后再试"),
    CODE_BUSINESS_LOGIN_INVALID("BU0001", "登录失效，请重新登录。"),
    DB_TEST_ERROR("2007", "数据库测试连接失败"),
    DB_TYPE_ERROR("2008", "不支持的数据库类型"),
    SSO_SESSION_FAILED("2009", "域登录超时，请重新登录!"),
    SSO_POST_FAILED("2010", "域登录接口调用失败!"),
    COMP_ADD_ERROR_URL("2011", "组件地址不能重复"),
    /*data-sync*/
    REPEATABLE_DATA("3001", "已有相同目标表与目标库的同步申请还未执行完毕，请稍后提交!"),
    DATATRANS_ERROR("3002", "数据传输失败"),
    DBTYPE_ERROR("3003", "错误的数据库类型"),
    FILEEXPORT_INIT_ERROR("3004", "文件导出日初始化错误"),
    TABLE_SCAN_ERROR("3005", "建表语句解析失败"),
    FILE_IMPORT_ERROR("3006", "结构化文件导入失败"),
    FILE_NOTEXIST_ERROR("3007", "文件不存在"),
    /*git相关*/
    GIT_GETFILE_ERROR("4001", "git获取文件失败"),
    GIT_CONFIG_ERROR("4002", "git环境配置错误"),
    GIT_COMMIT_ERROR("4003", "git提交文件失败"),
    GIT_ACTION_ERROR("4004", "git提交action非法"),
    GIT_MATCHFILE_ERROR("4005", "指定git路径，文件名未匹配到文件，请检查报表路径设置"),
    /*cpt文件相关*/
    CPT_CONN_ERROR("4999","服务器连接失败"),
    CPT_COPY_ERROR("4998","cpt文件提交到帆软服务器异常"),
    CPT_QUERY_ERROR("4997","文件获取异常"),
    CPT_LOCAL_ERROR("4996","cpt本地备份文件操作异常"),

    /*portal相关*/
    RPT_AUTH_ERROR_HAS("5000","已申请报表权限"),
    /**
     * 参数错误
     */
    ERR_EXPORT("9999", "上报数据导出出错"),
    ERR_EMPTY_FILTTYPE("1000", "文件类型为空"),
    ERR_EMPTY_TRADEDATE("1001", "文件类型为空"),
    ERR_FILE_DOWNLOAD("1002", "数据下载出错"),
    /**
     * 流程错误
     */
    ERR_PROC_DOING("2001", "别的机器正在处理，无需处理"),
    ERR_DAYINIT_CHECK("2002", "日初始化错误，有未完成的任务"),
    API_TIMEOUT("1020", "接口超时，请稍后重试"),

    /**
     * 文件导出错误
     */
    EXPORT_FILE_EXIST("1018", "尚有文件导出中，请稍等重试"),
    EXPORT_FILE_NOT_FINISH("1019", "文件尚未导出完成，请稍后下载"),
    EXPORT_FILE_FAILED("1016", "导出文件失败"),

    /**
     * 数据库错误
     */
    ERR_UPDATE_DATA("3001", "更新数据出错"),
    ERR_INSERT_DATA("3002", "新增数据出错");

    private String code;
    private String msg;

    Status(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static boolean checkStatus(String code) {
        for (Status status : Status.values()) {
            if (status.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
