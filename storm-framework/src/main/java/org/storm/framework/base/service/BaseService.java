package org.storm.framework.base.service;

import com.github.pagehelper.Page;
import org.storm.framework.base.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author pengxw
 * 
 * @param <Te>
 */
public interface BaseService<Te> {
	
	public BaseMapper<Te> getBaseMapper();
	public Class getMpClass();

	/**
	 * 保存
	 * 
	 * @param paramT
	 */
	public Long save(Te paramT);
	
	/**
	 * 批量保存
	 * 
	 * @param list
	 */
	public void saveBatch(List<Te> list);

	/**
	 * 修改
	 * 
	 * @param paramT
	 * @return
	 */
	public Te update(Te paramT);

	/**
	 * 修改
	 * 
	 * @param list
	 * @return
	 */
	public void updateBatch(List<Te> list) ;

	/**
	 * 删除
	 * 
	 * @param ids
	 * @return
	 */
	public boolean deleteBatch(List<Long> ids);
	
	/**
	 * 删除
	 * 
	 * @param ids
	 * @return
	 */
	public void deleteByIdList(List<Long> ids);

	/**
	 * 删除
	 * 
	 * @param ids
	 * @return
	 */
	public void deleteByIds(String ids);
	
	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	public void deleteById(long id);

	/**
	 * 根据ID获取实体对象
	 * 
	 * @param id
	 * @return
	 */
	public Te getById(long id);

	/**
	 * 获取数据
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Te> getList(Map<String, Object> paramMap);
	
	/**
	 * 获取全部数据
	 * @return
	 */
	public List<Te> getAll();
	
	/**
	 * 获取分页数据
	 * 
	 * @param paramMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<Te> getPageList(Map<String, Object> paramMap, int pageNo, int pageSize);

	/**
	 * 获取行数
	 * 
	 * @param paramMap
	 * @return
	 */
	public long getCount(Map<String, Object> paramMap);
	
	/**
	 * 通过sql获取List
	 * @param sql
	 * @return
	 */
	public List<Te> getListBySql(String sql);
	
}
