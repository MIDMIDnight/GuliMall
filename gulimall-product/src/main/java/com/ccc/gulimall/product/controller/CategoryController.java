package com.ccc.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.ccc.common.utils.PageUtils;
import com.ccc.common.utils.R;
import com.ccc.gulimall.product.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccc.gulimall.product.entity.CategoryEntity;
import com.ccc.gulimall.product.service.CategoryService;


/**
 * 商品三级分类
 *
 * @author ccc
 * @email 2219444584@qq.com
 * @date 2022-10-27 10:39:19
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /***
     * @description: 以树形结构返回目录
     * @param:
     * @return: com.ccc.common.utils.R
     * @author 陈南田
     * @date: 10/30/2022 4:00 PM
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:category:list")
    public R list(){
//        CategoryServiceImpl categoryService1 = new CategoryServiceImpl();
        List<CategoryEntity> categoryEntities = categoryService.listWithTree();
//        List<CategoryEntity> list = categoryService.list();
//        List<CategoryEntity> categoryEntities = categoryService.listWithTree();
        return R.ok().put("data",categoryEntities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
   // @RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
  //  @RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
