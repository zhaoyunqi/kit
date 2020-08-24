package com.github.zhaoyunqi.kit.biz;

import com.github.zhaoyunqi.kit.mapper.CommonMapper;
import com.github.zhaoyunqi.kit.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
