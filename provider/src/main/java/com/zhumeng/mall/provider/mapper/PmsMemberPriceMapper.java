package com.zhumeng.mall.provider.mapper;


import com.zhumeng.api.model.PmsMemberPrice;
import com.zhumeng.api.model.PmsMemberPriceExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface PmsMemberPriceMapper {
    @SelectProvider(type= com.zhumeng.mall.provider.mapper.PmsMemberPriceSqlProvider.class, method="countByExample")
    long countByExample(PmsMemberPriceExample example);

    @DeleteProvider(type= com.zhumeng.mall.provider.mapper.PmsMemberPriceSqlProvider.class, method="deleteByExample")
    int deleteByExample(PmsMemberPriceExample example);

    @Delete({
        "delete from pms_member_price",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into pms_member_price (id, product_id, ",
        "member_level_id, member_price, ",
        "member_level_name)",
        "values (#{id,jdbcType=BIGINT}, #{productId,jdbcType=BIGINT}, ",
        "#{memberLevelId,jdbcType=BIGINT}, #{memberPrice,jdbcType=DECIMAL}, ",
        "#{memberLevelName,jdbcType=VARCHAR})"
    })
    int insert(PmsMemberPrice record);

    @InsertProvider(type= com.zhumeng.mall.provider.mapper.PmsMemberPriceSqlProvider.class, method="insertSelective")
    int insertSelective(PmsMemberPrice record);

    @SelectProvider(type= com.zhumeng.mall.provider.mapper.PmsMemberPriceSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
        @Result(column="product_id", property="productId", jdbcType= JdbcType.BIGINT),
        @Result(column="member_level_id", property="memberLevelId", jdbcType= JdbcType.BIGINT),
        @Result(column="member_price", property="memberPrice", jdbcType= JdbcType.DECIMAL),
        @Result(column="member_level_name", property="memberLevelName", jdbcType= JdbcType.VARCHAR)
    })
    List<PmsMemberPrice> selectByExample(PmsMemberPriceExample example);

    @Select({
            "select",
            "id, product_id, member_level_id, member_price, member_level_name",
            "from pms_member_price",
            "where product_id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="product_id", property="productId", jdbcType= JdbcType.BIGINT),
            @Result(column="member_level_id", property="memberLevelId", jdbcType= JdbcType.BIGINT),
            @Result(column="member_price", property="memberPrice", jdbcType= JdbcType.DECIMAL),
            @Result(column="member_level_name", property="memberLevelName", jdbcType= JdbcType.VARCHAR)
    })
    List<PmsMemberPrice> selectMemberByParentId(Long id);


    @Select({
        "select",
        "id, product_id, member_level_id, member_price, member_level_name",
        "from pms_member_price",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
        @Result(column="product_id", property="productId", jdbcType= JdbcType.BIGINT),
        @Result(column="member_level_id", property="memberLevelId", jdbcType= JdbcType.BIGINT),
        @Result(column="member_price", property="memberPrice", jdbcType= JdbcType.DECIMAL),
        @Result(column="member_level_name", property="memberLevelName", jdbcType= JdbcType.VARCHAR)
    })
    PmsMemberPrice selectByPrimaryKey(Long id);

    @UpdateProvider(type= com.zhumeng.mall.provider.mapper.PmsMemberPriceSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") PmsMemberPrice record, @Param("example") PmsMemberPriceExample example);

    @UpdateProvider(type= com.zhumeng.mall.provider.mapper.PmsMemberPriceSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") PmsMemberPrice record, @Param("example") PmsMemberPriceExample example);

    @UpdateProvider(type= com.zhumeng.mall.provider.mapper.PmsMemberPriceSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(PmsMemberPrice record);

    @Update({
        "update pms_member_price",
        "set product_id = #{productId,jdbcType=BIGINT},",
          "member_level_id = #{memberLevelId,jdbcType=BIGINT},",
          "member_price = #{memberPrice,jdbcType=DECIMAL},",
          "member_level_name = #{memberLevelName,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(PmsMemberPrice record);

    @Insert({
            "<script>" ,
           "  INSERT INTO pms_member_price (product_id, member_level_id, member_price,member_level_name) VALUES " ,
                   "<foreach collection=\"list\" separator=\",\" item=\"item\" index=\"index\"> " ,
                   "( " ,
                   "#{item.productId,jdbcType=BIGINT}, " ,
                   "#{item.memberLevelId,jdbcType=BIGINT}, " ,
                   "#{item.memberPrice,jdbcType=DECIMAL}, " ,
                   "#{item.memberLevelName,jdbcType=VARCHAR} " ,
                  ") " ,
                   "</foreach> ",
             "</script>"})

    //自定义增加会员价格
    int insertList(@Param("list") List<PmsMemberPrice> memberPriceList);
}