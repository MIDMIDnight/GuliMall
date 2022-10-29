package com.ccc.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccc.common.utils.PageUtils;
import com.ccc.gulimall.member.entity.MemberReceiveAddressEntity;

import java.util.Map;

/**
 * 会员收货地址
 *
 * @author ccc
 * @email 2219444584@qq.com
 * @date 2022-10-27 15:17:15
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

