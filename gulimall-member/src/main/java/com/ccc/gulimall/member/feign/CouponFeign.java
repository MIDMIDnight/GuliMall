package com.ccc.gulimall.member.feign;

import com.ccc.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-coupon")
public interface CouponFeign {

    @RequestMapping("/coupon/coupon/membercoupon")
    public R memberCoupons();

}
