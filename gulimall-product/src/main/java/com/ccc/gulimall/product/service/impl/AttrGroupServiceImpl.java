package com.ccc.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccc.common.utils.PageUtils;
import com.ccc.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ccc.gulimall.product.dao.AttrGroupDao;
import com.ccc.gulimall.product.entity.AttrGroupEntity;
import com.ccc.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {

            String key = (String) params.get("key");
            //select * from pms_attr_group where catelog_id= ? and(attr_group_id=key or attr_group_name like key)
            QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<AttrGroupEntity>();
            if (!StringUtils.isEmpty(key)) {
                queryWrapper.and((obj) -> {
                    obj.eq("attr_group_id", key).like("attr_group_name", key);
                });
            }
        if (catId==0){
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<AttrGroupEntity>());
            return new PageUtils(page);
        }else {
            queryWrapper.eq("catelog_id",catId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), queryWrapper);
            return new PageUtils(page);
        }


        }






}