package org.storm.framework.base.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.storm.framework.base.model.Entity;
import org.storm.framework.base.service.BaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class BaseServiceImpl<Te extends Entity> implements BaseService<Te> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Long save(Te paramT) {
        if (paramT.getId() != null && paramT.getId() > 0) {
            this.getBaseMapper().update(paramT);
        } else {
            this.getBaseMapper().save(paramT);
        }
        return paramT.getId();
    }

    @Override
    public Te update(Te paramT) {
        this.getBaseMapper().update(paramT);
        return paramT;
    }

    @Override
    public void deleteByIdList(List<Long> ids) {
        this.getBaseMapper().deleteByIdList(ids);
    }

    @Override
    public void deleteByIds(String ids) {
        StringTokenizer token = new StringTokenizer(ids, ",");
        List<Long> list = new ArrayList<>();
        while (token.hasMoreTokens()) {
            String x = token.nextToken();
            list.add(Long.parseLong(x));
        }
        this.getBaseMapper().deleteByIdList(list);
    }

    @Override
    public void deleteById(long id) {
        this.getBaseMapper().deleteById(id);
    }

    @Override
    public Te getById(long id) {
        return this.getBaseMapper().getById(id);
    }

    @Override
    public List<Te> getList(Map<String, Object> paramMap) {
        return this.getBaseMapper().getList(paramMap);
    }

    @Override
    public List<Te> getAll() {
        return this.getBaseMapper().getAll();
    }

    @Override
    public long getCount(Map<String, Object> paramMap) {
        return this.getBaseMapper().getCount(paramMap);
    }

    @Override
    public Page<Te> getPageList(Map<String, Object> paramMap, int pageNo, int pageSize) {
        if (pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize <= 0) {
            pageSize = 15;
        }
        logger.info("pageNo:" + pageNo + ",pageSize:" + pageSize);
        PageHelper.startPage(pageNo, pageSize, false);
        Page<Te> page = (Page<Te>) this.getBaseMapper().getList(paramMap);
        long count = this.getBaseMapper().getCount(paramMap);
        page.setTotal(count);
        return page;
    }

    /**
     * batch操作 sava,update list
     */
    @Override
    public void saveBatch(List<Te> list) {
        for (Te te : list) {
            getBaseMapper().save(te);
        }
    }

    @Override
    public boolean deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            getBaseMapper().deleteById(id);
        }
        return true;
    }

    @Override
    public void updateBatch(List<Te> list) {
        for (Te t : list) {
            getBaseMapper().update(t);
        }
    }

    @Override
    public List<Te> getListBySql(String sql) {
        List<Te> list = getBaseMapper().getListBySql(sql);
        logger.info("getListBySql:" + sql);
        return list;
    }
}
