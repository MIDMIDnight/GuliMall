package com.ccc.gulimall.member.dao;

import com.ccc.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author ccc
 * @email 2219444584@qq.com
 * @date 2022-10-27 15:17:15
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
