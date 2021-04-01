package com.house.backend.houseservice.service;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2018/4/13.
 */
@Slf4j
public abstract class AbstractDataSimBiz {
//    @Autowired
//    BaseMapper baseMapper;
//    @Autowired
//    UserService userService;

//    /**
//     * 记录系统操作日志
//     *
//     * @param sysOperateLog
//     */
//    public void writeLog(SysOperateLog sysOperateLog) {
//        sysOperateLog.setId(this.getSeqByName(SequenceName.SEQ_SYS_OPERATE_LOG));
//        if (StringUtils.isEmpty(sysOperateLog.getReturnCode())) {
//            sysOperateLog.setReturnCode(RetCodeEnum.RETCODE_0000.getRetCode());
//        }
//        if (StringUtils.isEmpty(sysOperateLog.getReturnMessage())) {
//            sysOperateLog.setReturnMessage(RetCodeEnum.RETCODE_0000.getRetMsg());
//        }
//        if (sysOperateLog.getReturnMessage() != null && sysOperateLog.getReturnMessage().length() > 500) {
//            sysOperateLog.setReturnMessage(sysOperateLog.getReturnMessage().substring(0, 500));
//        }
//        if (sysOperateLog.getDetailMessage() != null && sysOperateLog.getDetailMessage().length() > 500) {
//            sysOperateLog.setDetailMessage(sysOperateLog.getDetailMessage().substring(0, 500));
//        }
//        sysOperateLog.setCreateTime(DateUtils.getCurrentOsTime());
////        sysOperateLog.setCreateUser(userService.getUserInfoDefault().getUserName());
////        sysOperateLog.setTradeDate(DateUtils.getNowDate(DateUtils.DATE_FORMAT_YYYYMMDD));
////        sysOperateLogExtendMapper.insertSelective(sysOperateLog);
//    }


//    /**
//     * 根据序列名称获取值
//     *
//     * @param seqNamq
//     * @return
//     */
//    public String getSeqByName(String seqNamq) {
//        try {
//            return baseMapper.querySeqByName(seqNamq);
//        } catch (Exception e) {
//            throw new DataSimException(Status.SEQ_ERROR, e);
//        }
//    }


}

