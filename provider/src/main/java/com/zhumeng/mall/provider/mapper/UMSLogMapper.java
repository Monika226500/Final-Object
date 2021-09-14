package com.zhumeng.mall.provider.mapper;

import com.zhumeng.api.model.UMSLog;
import com.zhumeng.api.model.UMSLogExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface UMSLogMapper {
    @SelectProvider(type= com.zhumeng.mall.provider.mapper.UMSLogSqlProvider.class, method="countByExample")
    long countByExample(UMSLogExample example);

    @DeleteProvider(type= com.zhumeng.mall.provider.mapper.UMSLogSqlProvider.class, method="deleteByExample")
    int deleteByExample(UMSLogExample example);

    @Delete({
        "delete from ums_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into ums_log (id, create_time, ",
        "update_time, cost_time, ",
        "ip, name, request_type, ",
        "request_url, userid, ",
        "log_type, request_param)",
        "values (#{id,jdbcType=BIGINT}, #{createTime,jdbcType=VARCHAR}, ",
        "#{updateTime,jdbcType=VARCHAR}, #{costTime,jdbcType=INTEGER}, ",
        "#{ip,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{requestType,jdbcType=VARCHAR}, ",
        "#{requestUrl,jdbcType=VARCHAR}, #{userid,jdbcType=INTEGER}, ",
        "#{logType,jdbcType=INTEGER}, #{requestParam,jdbcType=LONGVARCHAR})"
    })
    int insert(UMSLog record);

    @InsertProvider(type= com.zhumeng.mall.provider.mapper.UMSLogSqlProvider.class, method="insertSelective")
    int insertSelective(UMSLog record);

    @SelectProvider(type= com.zhumeng.mall.provider.mapper.UMSLogSqlProvider.class, method="selectByExampleWithBLOBs")
    @Results({
        @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
        @Result(column="create_time", property="createTime", jdbcType= JdbcType.VARCHAR),
        @Result(column="update_time", property="updateTime", jdbcType= JdbcType.VARCHAR),
        @Result(column="cost_time", property="costTime", jdbcType= JdbcType.INTEGER),
        @Result(column="ip", property="ip", jdbcType= JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType= JdbcType.VARCHAR),
        @Result(column="request_type", property="requestType", jdbcType= JdbcType.VARCHAR),
        @Result(column="request_url", property="requestUrl", jdbcType= JdbcType.VARCHAR),
        @Result(column="userid", property="userid", jdbcType= JdbcType.INTEGER),
        @Result(column="log_type", property="logType", jdbcType= JdbcType.INTEGER),
        @Result(column="request_param", property="requestParam", jdbcType= JdbcType.LONGVARCHAR)
    })
    List<UMSLog> selectByExampleWithBLOBs(UMSLogExample example);

    @SelectProvider(type= com.zhumeng.mall.provider.mapper.UMSLogSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
        @Result(column="create_time", property="createTime", jdbcType= JdbcType.VARCHAR),
        @Result(column="update_time", property="updateTime", jdbcType= JdbcType.VARCHAR),
        @Result(column="cost_time", property="costTime", jdbcType= JdbcType.INTEGER),
        @Result(column="ip", property="ip", jdbcType= JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType= JdbcType.VARCHAR),
        @Result(column="request_type", property="requestType", jdbcType= JdbcType.VARCHAR),
        @Result(column="request_url", property="requestUrl", jdbcType= JdbcType.VARCHAR),
        @Result(column="userid", property="userid", jdbcType= JdbcType.INTEGER),
        @Result(column="log_type", property="logType", jdbcType= JdbcType.INTEGER)
    })
    List<UMSLog> selectByExample(UMSLogExample example);

    @Select({
        "select",
        "id, create_time, update_time, cost_time, ip, name, request_type, request_url, ",
        "userid, log_type, request_param",
        "from ums_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
        @Result(column="create_time", property="createTime", jdbcType= JdbcType.VARCHAR),
        @Result(column="update_time", property="updateTime", jdbcType= JdbcType.VARCHAR),
        @Result(column="cost_time", property="costTime", jdbcType= JdbcType.INTEGER),
        @Result(column="ip", property="ip", jdbcType= JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType= JdbcType.VARCHAR),
        @Result(column="request_type", property="requestType", jdbcType= JdbcType.VARCHAR),
        @Result(column="request_url", property="requestUrl", jdbcType= JdbcType.VARCHAR),
        @Result(column="userid", property="userid", jdbcType= JdbcType.INTEGER),
        @Result(column="log_type", property="logType", jdbcType= JdbcType.INTEGER),
        @Result(column="request_param", property="requestParam", jdbcType= JdbcType.LONGVARCHAR)
    })
    UMSLog selectByPrimaryKey(Long id);

    @UpdateProvider(type= com.zhumeng.mall.provider.mapper.UMSLogSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") UMSLog record, @Param("example") UMSLogExample example);

    @UpdateProvider(type= com.zhumeng.mall.provider.mapper.UMSLogSqlProvider.class, method="updateByExampleWithBLOBs")
    int updateByExampleWithBLOBs(@Param("record") UMSLog record, @Param("example") UMSLogExample example);

    @UpdateProvider(type= com.zhumeng.mall.provider.mapper.UMSLogSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") UMSLog record, @Param("example") UMSLogExample example);

    @UpdateProvider(type= com.zhumeng.mall.provider.mapper.UMSLogSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UMSLog record);

    @Update({
        "update ums_log",
        "set create_time = #{createTime,jdbcType=VARCHAR},",
          "update_time = #{updateTime,jdbcType=VARCHAR},",
          "cost_time = #{costTime,jdbcType=INTEGER},",
          "ip = #{ip,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "request_type = #{requestType,jdbcType=VARCHAR},",
          "request_url = #{requestUrl,jdbcType=VARCHAR},",
          "userid = #{userid,jdbcType=INTEGER},",
          "log_type = #{logType,jdbcType=INTEGER},",
          "request_param = #{requestParam,jdbcType=LONGVARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(UMSLog record);

    @Update({
        "update ums_log",
        "set create_time = #{createTime,jdbcType=VARCHAR},",
          "update_time = #{updateTime,jdbcType=VARCHAR},",
          "cost_time = #{costTime,jdbcType=INTEGER},",
          "ip = #{ip,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "request_type = #{requestType,jdbcType=VARCHAR},",
          "request_url = #{requestUrl,jdbcType=VARCHAR},",
          "userid = #{userid,jdbcType=INTEGER},",
          "log_type = #{logType,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UMSLog record);
}