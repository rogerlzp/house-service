package com.house.backend.houseservice.generalization;

import com.github.pagehelper.PageInfo;
import com.house.backend.houseservice.common.AjaxResult;
import com.house.backend.houseservice.common.CommonRequest;
import com.house.backend.houseservice.common.PageResult;
import com.house.backend.houseservice.enums.OperTypeEnum;
import com.house.backend.houseservice.enums.Status;
import com.house.backend.houseservice.exception.HouseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author zhengshuqin
 */
@RestController
@Slf4j
public abstract class BaseController<K extends CommonRequest, T> {

    @Autowired
    BaseService<K, T> baseService;

    private Class<T> mclass;

    public BaseController(Class<T> mclass) {
        this.mclass = mclass;
    }

    /**
     * 输出参数过滤 分页查询使用
     *
     * @param obj
     */
    protected abstract void filterAfter(T obj);

    /**
     * 输入参数过滤 新增更新使用
     *
     * @param obj
     * @param operType
     */
    protected abstract void filterBefore(K obj, OperTypeEnum operType);

    private Consumer<T> filterAfter = this::filterAfter;

    @PostMapping("/pageInfo")
    public PageResult pageInfo(@RequestBody K request) {
        log.info("request->{}", request.toString());
        //查询分页信息
        PageInfo<T> pageInfo = baseService.getPageInfo(request, buildModel());
        log.info("pageNum->{},total->{}", pageInfo.getPageNum(), pageInfo.getTotal());
        //抽象过滤值
        pageInfo.getList().forEach(filterAfter);
        log.info("filter obj success");
        return PageResult.ok(pageInfo);
    }

    @PostMapping("/getOne")
    public AjaxResult getOne(@RequestBody K request) {
        log.info("request->{}", request.toString());
        T conditon = buildModel();
        BeanUtils.copyProperties(request, conditon);
        T info = baseService.querySingleByCondition(conditon);
        filterAfter.accept(info);
        return AjaxResult.ok(info);
    }

    @PostMapping("/addOrUpdateOne")
    public AjaxResult<String> addOrUpdateOne(@RequestBody K request) {
        log.info("request->{}", request.toString());
        T model = buildModel();
        String msg;
        if (OperTypeEnum.TYPE_I.getValue().equals(request.getOperType())) {
            //处理前过滤
            this.filterBefore(request, OperTypeEnum.TYPE_I);
            //属性拷贝
            BeanUtils.copyProperties(request, model);
            //业务验证
            baseService.saveValidate(model);
            //保存数据
            baseService.insert(model);
            this.filterAfter(model);
            msg = "新增数据成功";
        } else if (OperTypeEnum.TYPE_U.getValue().equals(request.getOperType())) {
            //处理前过滤
            this.filterBefore(request, OperTypeEnum.TYPE_U);
            //属性拷贝
            BeanUtils.copyProperties(request, model);
            //更新记录
            baseService.updateSingleById(model);
            this.filterAfter(model);
            msg = "更新数据成功";
        } else {
            throw new HouseException(Status.INVALID_OPER);
        }
        return AjaxResult.ok(msg);

    }

    @PostMapping("/addOrUpdate")
    public AjaxResult<T> addOrUpdate(@RequestBody K request) {
        log.info("request->{}", request.toString());
        T model = buildModel();
        if (OperTypeEnum.TYPE_I.getValue().equals(request.getOperType())) {
            //处理前过滤
            this.filterBefore(request, OperTypeEnum.TYPE_I);
            //属性拷贝
            BeanUtils.copyProperties(request, model);
            //业务验证
            baseService.saveValidate(model);
            //保存数据
            baseService.insert(model);
            this.filterAfter(model);
        } else if (OperTypeEnum.TYPE_U.getValue().equals(request.getOperType())) {
            //处理前过滤
            this.filterBefore(request, OperTypeEnum.TYPE_U);
            //属性拷贝
            BeanUtils.copyProperties(request, model);
            //更新记录
            baseService.updateSingleById(model);
            this.filterAfter(model);
        } else {
            throw new HouseException(Status.INVALID_OPER);
        }
        return AjaxResult.ok(model);

    }

    @PostMapping("/deleteBatch")
    public AjaxResult deleteBatch(@RequestBody K request) {
        log.info("request->{}", request.toString());
        for (String key : request.getKeys().split(",")) {
            baseService.deleteSingleById(key);
        }
        T model = buildModel();
        BeanUtils.copyProperties(request, model);
        this.filterAfter(model);
        return AjaxResult.ok("删除数据成功");
    }

    @PostMapping("/queryByExamplePage")
    public PageResult queryByExamplePage(@RequestBody K request) {
        log.info("request->{}", request.toString());
        PageInfo<T> pageInfo = baseService.queryByExamplePage(request);
        //抽象过滤值
        pageInfo.getList().forEach(filterAfter);
        return PageResult.ok(pageInfo);
    }

    @PostMapping("/queryByExample")
    public AjaxResult queryByExample(@RequestBody K request) {
        log.info("request->{}", request.toString());
        List<T> listInfo = baseService.queryByExample(request);
        //抽象过滤值
        listInfo.forEach(filterAfter);
        return AjaxResult.ok(listInfo);
    }

    @PostMapping("/queryByKeyWords")
    public PageResult queryByKeyWords(@RequestBody K request) {
        log.info("request->{}", request.toString());
        PageInfo<T> pageInfo = baseService.queryBykeyWords(request);
        //抽象过滤值
        pageInfo.getList().forEach(filterAfter);
        return PageResult.ok(pageInfo);
    }

    @PostMapping("/queryListByCon")
    public AjaxResult queryListByCon(@RequestBody K request) {
        T model = buildModel();
        if (request != null) {
            log.info("request->{}", request.toString());
            BeanUtils.copyProperties(request, model);
        }

        List<T> listInfo = baseService.queryByCondition(model);
        //抽象过滤值
        listInfo.forEach(filterAfter);
        return AjaxResult.ok(listInfo);
    }

    protected T buildModel() {
        try {
            return mclass.newInstance();
        } catch (Exception e) {
            throw new HouseException(Status.BUILD_MODEL_ERROR, e);
        }
    }
}
