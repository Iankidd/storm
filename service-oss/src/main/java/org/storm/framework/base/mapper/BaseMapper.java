package org.storm.framework.base.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper<Te> {

	/**
	 * 保存
	 * @param paramT
	 */
	public Long save(Te paramT);

	/**
	 * 修改
	 * @param paramT
	 * @return
	 */
	public void update(Te paramT);

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public void deleteByIdList(List<Long> ids);

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public void deleteById(long id);

	/**
	 * 根据ID获取实体对象
	 * @param id
	 * @return
	 */
	public Te getById(long id);
	
	/**
	 * 获取数据
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
	 * 获取行数
	 * @param paramMap
	 * @return
	 */
	public long getCount(Map<String, Object> paramMap);
	
	/**
	 * 通过sql获取List
	 * @param sql
	 * @return
	 */
	public List<Te> getListBySql(@Param("sql") String sql);
	
}
