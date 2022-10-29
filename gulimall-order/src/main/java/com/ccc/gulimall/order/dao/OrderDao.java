package com.ccc.gulimall.order.dao;

import com.ccc.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author ccc
 * @email 2219444584@qq.com
 * @date 2022-10-27 15:31:18
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
