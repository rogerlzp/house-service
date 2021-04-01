package com.house.backend.houseservice.generalization;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.house.backend.houseservice.common.CommonRequest;
import com.house.backend.houseservice.common.QueryCondition;
import com.house.backend.houseservice.enums.ConditonTypeEnum;
import com.house.backend.houseservice.enums.Status;
import com.house.backend.houseservice.exception.HouseException;
import com.house.backend.houseservice.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zhengshuqin
 * 泛型通用方法框架
 */
@Slf4j
public abstract class BaseService<K extends CommonRequest, T> {

    @Autowired
    BaseMapper baseMapper;

    BaseServiceMapper<T> baseServiceMapper;

    Class exClass;

    Class<T> tclass;

    public BaseService(BaseServiceMapper<T> baseServiceMapper, Class exClass, Class<T> tclass) {
        this.baseServiceMapper = baseServiceMapper;
        this.exClass = exClass;
        this.tclass = tclass;
    }

    protected abstract void saveValidate(T model);

    public T checkVisitHas(String id) {
        T model = this.selectSingleById(id);
        if (model == null) {
            throw new HouseException("未查询到需要修改的记录,id:" + id);
        }
        return model;
    }

    /**
     * 分页查询记录
     *
     * @param model
     * @param request
     * @return
     */
    public PageInfo<T> getPageInfo(K request, T model) {
        try {
            BeanUtils.copyProperties(request, model);
            PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
            List<T> result;
            if (StringUtils.isNotEmpty(request.getOrderByClause())) {
                result = baseServiceMapper.selectByConditionOrder(model, request.getOrderByClause());
            } else {
                result = baseServiceMapper.selectByCondition(model);
            }
            PageInfo<T> pageInfo = new PageInfo<>(result);
            return pageInfo;
        } catch (Exception e) {
            throw new HouseException(Status.PAGE_ERROR, e);
        }
    }

    /**
     * 新增记录
     *
     * @param model
     * @return
     */
    public int insert(T model) {
        try {
            return baseServiceMapper.insert(model);
        } catch (Exception e) {
            throw new HouseException(Status.INSERT_ERROR, e);
        }
    }

    /**
     * 按条件查询多条返回
     *
     * @param model
     * @return
     */
    public List<T> queryByCondition(T model) {
        try {
            return baseServiceMapper.selectByCondition(model);
        } catch (Exception e) {
            throw new HouseException(Status.LIST_QUERY_ERROR, e);
        }
    }

    /**
     * 单条数据查询
     *
     * @param model
     * @return
     */
    public T querySingleByCondition(T model) {
        try {
            return baseServiceMapper.selectByConditionSingle(model);
        } catch (Exception e) {
            throw new HouseException(Status.ONE_QUERY_ERROR, e);
        }
    }

    /**
     * 根据Id更新数据
     *
     * @param model
     * @return
     */
    public int updateSingleById(T model) {
        try {
            return baseServiceMapper.updateByPrimaryKeySelective(model);
        } catch (Exception e) {
            throw new HouseException(Status.UPDATE_ERROR, e);
        }
    }

    /**
     * 根据id删除数据
     *
     * @param id
     * @return
     */
    public int deleteSingleById(String id) {
        try {
            return baseServiceMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new HouseException(Status.DELETE_ERROR, e);
        }
    }

    /**
     * 根据id删除数据
     *
     * @param id
     * @return
     */
    public T selectSingleById(String id) {
        try {
            return baseServiceMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            throw new HouseException(Status.ONE_QUERY_ERROR, e);
        }
    }

    /**
     * 根据序列名称获取值
     *
     * @param seqName
     * @return
     */
    public String getSeqByName(String seqName) {
        try {
            return baseMapper.querySeqByName(seqName);
        } catch (Exception e) {
            throw new HouseException(Status.SEQ_ERROR, e);
        }
    }

    /**
     * 根据Example条件查询数据
     *
     * @param request
     */
    public PageInfo<T> queryByExamplePage(K request) {
        try {
            //反射获取example查询对象
            Object example = exClass.newInstance();
            Method exMethod = example.getClass().getMethod("createCriteria");
            Object obj = exMethod.invoke(example);
            //根据前台传入条件构建查询参数
            if (request.getConditions() != null) {
                for (QueryCondition condition : request.getConditions()) {
                    if (ConditonTypeEnum.Or.getValue().equalsIgnoreCase(condition.getType())) {
                        exMethod = example.getClass().getMethod("or");
                        obj = exMethod.invoke(example);
                    }
                    //condition格式 key:[connCode] value:[like '%TEST%']
                    String[] values = condition.getValue().split(" ");
                    String condString = values[0];
                    String methodName = "and" + upperFirstLatter(condition.getKey()) + ConditonTypeEnum.getEnumDescIgnoreCase(condString);
                    if (ConditonTypeEnum.getParamNumIgnoreCase(condString) == 1) {
                        String condValue = values[1];
                        if (condString.equals(ConditonTypeEnum.In.getValue())) {
                            Method andMethod = obj.getClass().getMethod(methodName, List.class);
                            andMethod.invoke(obj, Arrays.asList(condValue.split(",")));
                        } else {
                            Method andMethod = obj.getClass().getMethod(methodName, String.class);
                            andMethod.invoke(obj, condValue);
                        }
                    } else if (ConditonTypeEnum.getParamNumIgnoreCase(condString) == 0) {
                        Method andMethod = obj.getClass().getMethod(methodName, String.class);
                        andMethod.invoke(obj);
                    } else {
                        throw new HouseException(Status.CONDITION_SET_ERROR);
                    }
                }
            }
            //拼接order by
            if (StringUtils.isNotEmpty(request.getOrderByClause())) {
                Method orderMethod = example.getClass().getMethod("setOrderByClause", String.class);
                orderMethod.invoke(example, request.getOrderByClause());
            }
            PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
            List<T> result = baseServiceMapper.selectByExample(example);
            PageInfo<T> pageInfo = new PageInfo<>(result);
            return pageInfo;
        } catch (Exception e) {
            throw new HouseException(Status.CONDITION_QUERY_ERROR, e);
        }
    }

    /**
     * 根据Example条件查询数据
     *
     * @param request
     */
    public List<T> queryByExample(K request) {
        try {
            //反射获取example查询对象
            Object example = exClass.newInstance();
            Method exMethod = example.getClass().getMethod("createCriteria");
            Object obj = exMethod.invoke(example);
            //根据前台传入条件构建查询参数
            if (request.getConditions() != null) {
                for (QueryCondition condition : request.getConditions()) {
                    if (ConditonTypeEnum.Or.getValue().equalsIgnoreCase(condition.getType())) {
                        exMethod = example.getClass().getMethod("or");
                        obj = exMethod.invoke(example);
                    }
                    //condition格式 key:[connCode] value:[like '%TEST%']
                    String[] values = condition.getValue().split(" ");
                    String condString = values[0];
                    String methodName = "and" + upperFirstLatter(condition.getKey()) + ConditonTypeEnum.getEnumDescIgnoreCase(condString);
                    if (ConditonTypeEnum.getParamNumIgnoreCase(condString) == 1) {
                        Method andMethod = obj.getClass().getMethod(methodName, String.class);
                        String condValue = values[1];
                        andMethod.invoke(obj, condValue);
                    } else if (ConditonTypeEnum.getParamNumIgnoreCase(condString) == 0) {
                        Method andMethod = obj.getClass().getMethod(methodName);
                        andMethod.invoke(obj);
                    } else {
                        throw new HouseException(Status.CONDITION_SET_ERROR);
                    }
                }
            }
            if (StringUtils.isNotEmpty(request.getOrderByClause())) {
                //拼接order by
                Method orderMethod = example.getClass().getMethod("setOrderByClause", String.class);
                orderMethod.invoke(example, request.getOrderByClause());
            }
            List<T> result = baseServiceMapper.selectByExample(example);
            return result;
        } catch (Exception e) {
            throw new HouseException(Status.CONDITION_QUERY_ERROR, e);
        }
    }

    /**
     * 根据关键字模糊查询数据
     *
     * @param request
     */
    public PageInfo<T> queryBykeyWords(K request) {
        try {
            //反射获取example查询对象
            String keyWords = request.getKeyWords();
            Object example = exClass.newInstance();
            Field[] fields = tclass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().equals(String.class)) {
                    Method exMethod = example.getClass().getMethod("or");
                    Object obj = exMethod.invoke(example);
                    String key = field.getName();
                    String methodName = "and" + upperFirstLatter(key) + "Like";
                    Method andConnCodeLike = obj.getClass().getMethod(methodName, String.class);
                    andConnCodeLike.invoke(obj, "%" + keyWords + "%");
                }
            }
            PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
            List<T> result = baseServiceMapper.selectByExample(example);
            PageInfo<T> pageInfo = new PageInfo<>(result);
            return pageInfo;
        } catch (Exception e) {
            throw new HouseException(Status.CONDITION_QUERY_ERROR, e);
        }
    }

    private String upperFirstLatter(String letter) {
        return letter.substring(0, 1).toUpperCase() + letter.substring(1);
    }

    /**
     * 执行普通sql
     *
     * @param sql
     * @return
     */
    public List<Map<String, Object>> executeSql(String sql) {
        return baseMapper.executeSql(sql);
    }
}
