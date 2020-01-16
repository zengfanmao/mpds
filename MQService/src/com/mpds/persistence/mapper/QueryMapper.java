package com.mpds.persistence.mapper;

import com.mpds.persistence.po.TbUserIMDatas;

public interface QueryMapper {
	
	public int insertUserGps(TbUserIMDatas param) throws Exception;
	
	public int deleteExpireGps(TbUserIMDatas param) throws Exception;
	
	public String selectUserGps(TbUserIMDatas param) throws Exception;
	
	public int updateUserGps(TbUserIMDatas param) throws Exception;
	
	public int updatePdtUserStatus(TbUserIMDatas param) throws Exception;
	
	public String selectGroupName(TbUserIMDatas param) throws Exception;
	
	public int updatePdtUserGroup(TbUserIMDatas param) throws Exception;
}
