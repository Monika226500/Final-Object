package com.zhumeng.mall.provider.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.dto.PmsProductQueryParam;
import com.zhumeng.api.model.*;
import com.zhumeng.api.service.IBrandService;
import com.zhumeng.api.service.IPmsProductService;
import com.zhumeng.api.service.IProductCategoryService;
import com.zhumeng.mall.provider.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/5/16:33
 * 描述你的类：
 */

@Service(
        version = "1.0.0",
        interfaceName = "com.zhumeng.api.service.IPmsProductService",
        interfaceClass = IPmsProductService.class,
        timeout = 120000
)
public class ProductServiceImpl implements IPmsProductService {
    @Autowired
    private PmsProductMapper pdao;

    @Autowired
    private PmsMemberPriceMapper memberPriceMapper;//会员价格 批量加入

    @Autowired
    private PmsProductLadderMapper productLadderMapper;//阶梯价格 批量加入

    @Autowired
    private PmsProductFullReductionMapper productFullReductionMapper;//满减价格

    @Autowired
    private PmsSkuStockMapper skuStockMapper;//库存

    @Autowired
    private PmsProductAttributeValueMapper productAttributeValueMapper;

    @Autowired
    private CmsSubjectProductRelationMapper subjectProductRelationMapper;//关联主题
    @Autowired  //关联优选
    private CmsPrefrenceAreaProductRelationMapper prefrenceAreaProductRelationMapper;


    @Autowired
    private CacheManager cacheManager;
    @CachePut(cacheNames = {"Product"},key = "#result.id")
    @Override
    public PmsProduct create(PmsProduct product) {
        //增加商品
        clearProductListBuffer();
        //开始做增加
        //创建商品
        //增加主要的图片资源getPic
        //增加其他图片进入相册字段 第一幅是主图片，其他是从图片，存到相册字段，用","隔开

        pdao.insertSelective(product);
        Long productId=product.getId();
        //id号可以取到

        relateAndInsertList(memberPriceMapper,product.getMemberPriceList(),product.getId());
        //阶梯价格
        relateAndInsertList(productLadderMapper, product.getProductLadderList(), productId);
        //满减价格
        relateAndInsertList(productFullReductionMapper, product.getProductFullReductionList(), productId);
        //处理sku的编码
        handleSkuStockCode(product.getSkuStockList(),productId);
        //添加sku库存信息
        relateAndInsertList(skuStockMapper, product.getSkuStockList(), productId);
        //添加商品参数,添加自定义商品规格
        relateAndInsertList(productAttributeValueMapper, product.getProductAttributeValueList(), productId);
        //关联专题
        relateAndInsertList(subjectProductRelationMapper, product.getSubjectProductRelationList(), productId);
        //关联优选
        relateAndInsertList(prefrenceAreaProductRelationMapper, product.getPrefrenceAreaProductRelationList(), productId);
        return product;
    }
    //单独生成每个商品在库存中的SKU代码
    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if(org.springframework.util.CollectionUtils.isEmpty(skuStockList)){
            return;
        }
        for(int i=0;i<skuStockList.size();i++){
            PmsSkuStock skuStock = skuStockList.get(i);
            if(StringUtils.isEmpty(skuStock.getSkuCode())){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                //日期
                sb.append(sdf.format(new Date()));
                //四位商品id
                sb.append(String.format("%04d", productId));
                //三位索引id
                sb.append(String.format("%03d", i+1));
                skuStock.setSkuCode(sb.toString());
            }
        }
    }


    /**
     * 建立和插入关系表操作
     *
     * @param dao       可以操作的dao
     * @param dataList  要插入的数据
     * @param productId 建立关系的id
     *                               memberPriceDao, productParam.getMemberPriceList(), productId
     *                  利用反射技术执行dao中的insertList方法  ，把dataList的每笔数据保存进数据库，
     *                  并关联productId给每个对象
     */
    private void relateAndInsertList(Object dao, List dataList, Long productId) {
        //通用型增加数据方法
        if(CollectionUtils.isEmpty(dataList)){
            return;
        }
        for (Object item:dataList){
            try {
                Method m=item.getClass().getMethod("setProductId",Long.class);
                //关联产品id外键
                m.invoke(item,(Long)productId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Method m=dao.getClass().getMethod("insertList",List.class);
            m.invoke(dao,dataList);
        } catch (Exception e) {
            System.out.printf("创建产品出错:%s",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }


    @Cacheable(cacheNames = {"ProductList"},
            unless = "#result==null",
            //T(String).valueOf(#name).concat('-').concat(#password))
            //得有字母占位，否则 多条件查询 条件不一样 会出现雷同的结果
            key="T(String).valueOf(#pageNum+'-'+#pageSize)" +
                    ".concat(#productQueryParam.keyword!=null?#productQueryParam.keyword:'k')" +
                    ".concat(#productQueryParam.verifyStatus!=null?#productQueryParam.verifyStatus:'vs') "+
                    ".concat(#productQueryParam.publishStatus!=null?#productQueryParam.publishStatus:'ps') "+
                    ".concat(#productQueryParam.productSn!=null?#productQueryParam.productSn:'psn') "+
                    ".concat(#productQueryParam.productCategoryId!=null?#productQueryParam.productCategoryId:'pc') "+
                    ".concat(#productQueryParam.brandId!=null?#productQueryParam.brandId:'b')"
        )


    @Override
    public CommonPage list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        //必须先写
        PmsProductExample example=new PmsProductExample();
        PmsProductExample.Criteria criteria=example.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if(productQueryParam.getPublishStatus()!=null){
            criteria.andPublishStatusEqualTo(productQueryParam.getPublishStatus());
        }
        if(productQueryParam.getVerifyStatus()!=null){
            criteria.andVerifyStatusEqualTo(productQueryParam.getVerifyStatus());
        }
        if(productQueryParam.getBrandId()!=null){
            criteria.andBrandIdEqualTo(productQueryParam.getBrandId());
        }
        if(productQueryParam.getProductSn()!=null){
            criteria.andProductSnEqualTo(productQueryParam.getProductSn());
        }
        if(productQueryParam.getProductCategoryId()!=null){
            criteria.andProductCategoryIdEqualTo(productQueryParam.getProductCategoryId());
        }
        if(!StringUtil.isEmpty(productQueryParam.getKeyword())){
            criteria.andNameLike("%"+productQueryParam.getKeyword()+"%");
        }
        //执行该条件
        List list=pdao.selectByExample(example);
        return CommonPage.restPage(list);
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        //建立查询条件
        clearProductListBuffer();
        PmsProductExample example=new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        PmsProduct pmsProduct=new PmsProduct();
        pmsProduct.setDeleteStatus(deleteStatus);

        return pdao.updateByExampleSelective(pmsProduct,example);
    }

    @Override
    public PmsProduct updateNewStatus(Long id, Integer newStatus) {
        return null;
    }

    @Override
    @CachePut(cacheNames = "Product",key = "#id")
    //修改单笔redis的缓存
    public PmsProduct updatePublishStatus(Long id, Integer publishStatus) {
        clearProductListBuffer();
        //修改mysql缓存
        PmsProduct product=this.getUpdateInfo(id);
        product.setPublishStatus(publishStatus);
        pdao.updateByPrimaryKeySelective(product);
        return product;
    }

    @Override
    public PmsProduct updateRecommendStatus(Long id, Integer recommendStatus) {
        return null;
    }

    @CachePut(cacheNames = {"Product"},key = "#id")
    @Override
    public PmsProduct update(Long id, PmsProduct product) {
        clearProductListBuffer();
        product.setId(id);
        pdao.updateByPrimaryKeySelective(product);
        //修改从表
        //会员价格
        PmsMemberPriceExample example=new PmsMemberPriceExample();
        example.createCriteria().andProductIdEqualTo(id);
        memberPriceMapper.deleteByExample(example);
        relateAndInsertList(memberPriceMapper,product.getMemberPriceList(),product.getId());

        //阶梯价格
        PmsProductLadderExample ladderExample = new PmsProductLadderExample();
        ladderExample.createCriteria().andProductIdEqualTo(id);
        productLadderMapper.deleteByExample(ladderExample);//先删除 在关联
        relateAndInsertList(productLadderMapper, product.getProductLadderList(), id);
        //满减价格
        PmsProductFullReductionExample fullReductionExample = new PmsProductFullReductionExample();
        fullReductionExample.createCriteria().andProductIdEqualTo(id);
        productFullReductionMapper.deleteByExample(fullReductionExample);
        relateAndInsertList(productFullReductionMapper, product.getProductFullReductionList(), id);
        //修改sku库存信息
        PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
        skuStockExample.createCriteria().andProductIdEqualTo(id);
        skuStockMapper.deleteByExample(skuStockExample);
        handleSkuStockCode(product.getSkuStockList(),id);
        relateAndInsertList(skuStockMapper, product.getSkuStockList(), id);
        //修改商品参数,添加自定义商品规格
        PmsProductAttributeValueExample productAttributeValueExample = new PmsProductAttributeValueExample();
        productAttributeValueExample.createCriteria().andProductIdEqualTo(id);
        productAttributeValueMapper.deleteByExample(productAttributeValueExample);
        relateAndInsertList(productAttributeValueMapper, product.getProductAttributeValueList(), id);
        //关联专题
        CmsSubjectProductRelationExample subjectProductRelationExample = new CmsSubjectProductRelationExample();
        subjectProductRelationExample.createCriteria().andProductIdEqualTo(id);
        subjectProductRelationMapper.deleteByExample(subjectProductRelationExample);
        relateAndInsertList(subjectProductRelationMapper, product.getSubjectProductRelationList(), id);
        //关联优选
        CmsPrefrenceAreaProductRelationExample prefrenceAreaExample = new CmsPrefrenceAreaProductRelationExample();
        prefrenceAreaExample.createCriteria().andProductIdEqualTo(id);
        prefrenceAreaProductRelationMapper.deleteByExample(prefrenceAreaExample);
        relateAndInsertList(prefrenceAreaProductRelationMapper, product.getPrefrenceAreaProductRelationList(), id);

        return product;
    }

    @Cacheable(cacheNames = "Product",key = "#id",unless = "#result==null")
    @Override
    public PmsProduct getUpdateInfo(Long id) {
        //根据id取出缓存对象
        return pdao.getUpdateInfo(id);
    }

    private void clearProductListBuffer(){
        cacheManager.getCache("ProductList").clear();
        //只有删除，删除所有ProductList为前缀的缓存
    }
}
