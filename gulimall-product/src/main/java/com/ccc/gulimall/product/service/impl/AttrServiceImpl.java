package com.ccc.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ccc.common.constant.ProductConstant;
import com.ccc.common.utils.PageUtils;
import com.ccc.common.utils.Query;
import com.ccc.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.ccc.gulimall.product.dao.AttrGroupDao;
import com.ccc.gulimall.product.dao.CategoryDao;
import com.ccc.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.ccc.gulimall.product.entity.AttrGroupEntity;
import com.ccc.gulimall.product.entity.CategoryEntity;
import com.ccc.gulimall.product.service.CategoryService;
import com.ccc.gulimall.product.vo.AttrGroupRelationVo;
import com.ccc.gulimall.product.vo.AttrRespVo;
import com.ccc.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ccc.gulimall.product.dao.AttrDao;
import com.ccc.gulimall.product.entity.AttrEntity;
import com.ccc.gulimall.product.service.AttrService;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryService categoryService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        //??????????????????
        this.save(attrEntity);
        if (attr.getAttrType()== ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //??????????????????
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            //mp???????????????????????????????????????????????????????????????????????????????????????entity?????????id
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }


    }

    @Override
    public PageUtils qurryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<>();
        attrEntityQueryWrapper.eq("attr_type","base".equalsIgnoreCase( type)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if (catelogId != 0) {
            attrEntityQueryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");

        if (!StringUtils.isEmpty(key)) {
            attrEntityQueryWrapper.and((wrapper) -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });

        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), attrEntityQueryWrapper);
        PageUtils pageUtils = new PageUtils(page);

        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            AttrAttrgroupRelationEntity attr_id = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));


            if (attr_id != null) {
                Long attrGroupId = attr_id.getAttrGroupId();
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                if (attrGroupEntity!=null){
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }

            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        })).collect(Collectors.toList());
        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        //????????????
        AttrEntity attrEntity = this.baseMapper.selectById(attrId);
        BeanUtils.copyProperties(attrEntity,attrRespVo);
        AttrAttrgroupRelationEntity RelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
        if (RelationEntity!=null){
        attrRespVo.setAttrGroupId(RelationEntity.getAttrGroupId());
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(RelationEntity.getAttrGroupId());
        if (attrGroupEntity!=null){
        attrRespVo.setAttrName(attrGroupEntity.getAttrGroupName());}
        }
        //??????????????????
        Long catelogId = attrEntity.getCatelogId();
        Long[] categoryIds = categoryService.findCategoryIds(catelogId);
        attrRespVo.setCatelogPath(categoryIds);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        attrRespVo.setCatelogName(categoryEntity.getName());

        return attrRespVo;
    }

    @Override
    public void updateAttr(AttrVo attr) {

        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);

        this.updateById(attrEntity);
        //1?????????????????????
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attr.getAttrId());

        Long count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attr.getAttrId()));

     if (count > 0) {
        attrAttrgroupRelationDao.update(relationEntity,
                    new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
       } else {
         attrAttrgroupRelationDao.insert(relationEntity);
      }
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities = attrAttrgroupRelationDao.selectList
                (new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));

        List<Long> attrIds = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        //??????attrIds???????????????????????????
        //Collection<AttrEntity> attrEntities = this.listByIds(attrIds);

        //??????attrIds???????????????????????????null?????????
        if (attrIds == null || attrIds.size() == 0) {
            return null;
        }

        List<AttrEntity> attrEntityList = this.baseMapper.selectBatchIds(attrIds);

        return attrEntityList;
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        attrAttrgroupRelationDao.deleteBatchRelation(entities);
    }

    @Override
    public PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params) {

        //1?????????????????????????????????????????????????????????????????????
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        //?????????????????????id
        Long catelogId = attrGroupEntity.getCatelogId();

        //2????????????????????????????????????????????????????????????
        //2.1????????????????????????????????????
        List<AttrGroupEntity> groupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));

        //??????????????????attrGroupId
        List<Long> collect = groupEntities.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());


        //2.2?????????????????????????????????
        List<AttrAttrgroupRelationEntity> groupId = attrAttrgroupRelationDao.selectList
                (new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));

        List<Long> attrIds = groupId.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //2.3??????????????????????????????????????????????????????
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());

        if (attrIds != null && attrIds.size() > 0) {
            queryWrapper.notIn("attr_id", attrIds);
        }

        //???????????????????????????????????????
        String key = (String) params.get("key");
        if (!org.apache.commons.lang.StringUtils.isEmpty(key)) {
            queryWrapper.and((w) -> {
                w.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);

        PageUtils pageUtils = new PageUtils(page);


        return pageUtils;


    }

}