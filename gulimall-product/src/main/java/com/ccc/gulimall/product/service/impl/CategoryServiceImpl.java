package com.ccc.gulimall.product.service.impl;

import com.ccc.common.utils.PageUtils;
import com.ccc.common.utils.Query;
import com.ccc.gulimall.product.entity.AttrGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ccc.gulimall.product.dao.CategoryDao;
import com.ccc.gulimall.product.entity.CategoryEntity;
import com.ccc.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    final
    CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {

        List<CategoryEntity> categoryEntities =baseMapper.selectList(null);

        List<CategoryEntity> categoryEntityList = categoryEntities.stream()
                .filter(categoryEntity -> categoryEntity.getCatLevel() == 1).map(menu -> {
                    menu.setChildren(getMenuChildren(menu, categoryEntities));
                    return menu;
                })
//                .sorted((menu1, menu2) -> {
//                    return (menu1.getSort()==null?0:menu1.getSort()) -( menu2.getSort()==null?0: menu2.getSort());
//                })
                .collect(Collectors.toList());
        return categoryEntityList;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1.检查当前删除的菜单是否在其他地方被引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCategoryIds(Long catelogId) {
        List<Long> categoryIds=new ArrayList<>();
        List<Long> categoryPath = findCategoryPath(catelogId, categoryIds);
        Collections.reverse(categoryPath);
        return (Long[]) categoryPath.toArray(new Long[categoryPath.size()]);
    }
    private List<Long> findCategoryPath(Long catId,List<Long> categoryIds){
        categoryIds.add(catId);
        CategoryEntity byId = this.getById(catId);
        if (byId.getParentCid()!=0){
             findCategoryPath(byId.getParentCid(), categoryIds);
        }
        return categoryIds;

    }
    private List<CategoryEntity> getMenuChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == root.getCatId())
                .map(categoryEntity -> {
                    categoryEntity.setChildren(getMenuChildren(categoryEntity, all));
                    return categoryEntity;
                })
//                .sorted((menu1, menu2) -> {
//                    return (menu1.getSort()==null?0:menu1.getSort()) -( menu2.getSort()==null?0: menu2.getSort());
//                })
                .collect(Collectors.toList());
        return children;

    }

}