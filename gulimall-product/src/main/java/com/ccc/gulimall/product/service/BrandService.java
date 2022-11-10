package com.ccc.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccc.common.utils.PageUtils;
import com.ccc.gulimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author ccc
 * @email 2219444584@qq.com
 * @date 2022-10-27 10:39:19
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);

}

