package com.zhaoyunqi.kit.biz;

import com.zhaoyunqi.kit.mapper.CommonMapper;
import com.zhaoyunqi.kit.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础业务类
 *
 *
 * @version 2018/1/13.
 */
public abstract class BusinessBiz<M extends CommonMapper<T>, T> extends BaseBiz<M, T> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void insertSelective(T entity) {
        EntityUtils.setCreatAndUpdatInfo(entity);
        super.insertSelective(entity);
    }

    @Override
    public void updateById(T entity) {
        EntityUtils.setUpdatedInfo(entity);
        super.updateById(entity);
    }

    @Override
    public void updateSelectiveById(T entity) {
        EntityUtils.setUpdatedInfo(entity);
        super.updateSelectiveById(entity);
    }
}
