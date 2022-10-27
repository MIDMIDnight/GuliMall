package com.ccc.gulimall.product.dao;

import com.ccc.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author ccc
 * @email 2219444584@qq.com
 * @date 2022-10-27 10:39:19
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
