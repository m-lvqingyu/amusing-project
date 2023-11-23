package com.amusing.start.platform.mapper;

import com.amusing.start.platform.entity.pojo.AliPayAsyncNotifyInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/30
 */
public interface AliPayAsyncNotifyInfoMapper {

    Integer insert(AliPayAsyncNotifyInfo aliPayAsyncNotifyInfo);

    Integer updateById(@Param("id") Long id, @Param("status") Integer status);

    AliPayAsyncNotifyInfo getByNotifyId(@Param("notifyId") String notifyId);


}
