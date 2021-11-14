package com.amusing.start.product.mapper;

import com.amusing.start.product.pojo.ProductMessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMessageInfoMapper {

    int insertSelective(ProductMessageInfo record);

    ProductMessageInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductMessageInfo record);

    int updateStatus(@Param("txId") String txId,
                     @Param("status") Integer status,
                     @Param("resultStatus") Integer resultStatus);

}