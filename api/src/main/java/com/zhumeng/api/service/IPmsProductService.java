package com.zhumeng.api.service;

import com.zhumeng.api.common.CommonPage;
import com.zhumeng.api.dto.PmsProductQueryParam;
import com.zhumeng.api.model.PmsProduct;

import java.util.List;

/**
 * 创建人：Jason
 * 创建时间：2020/1/15
 * 描述你的类：
 */
public interface IPmsProductService {
    /**
     * 创建商品
     */
    PmsProduct create(PmsProduct produc);
    /**
     * 分页查询商品
     */
    CommonPage list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量删除商品 修改删除标记 并没有真正删除
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);




    /**
     * 批量修改新品状态
     */
    PmsProduct updateNewStatus(Long id, Integer newStatus);

    /**
     * 批量修改商品上架状态
     */
    PmsProduct updatePublishStatus(Long id, Integer publishStatus);

    /**
     * 批量修改商品推荐状态
     */
    PmsProduct updateRecommendStatus(Long id, Integer recommendStatus);

    PmsProduct update(Long id, PmsProduct product);

    /**
     * 根据商品编号获取更新信息
     */
    PmsProduct getUpdateInfo(Long id);
}
