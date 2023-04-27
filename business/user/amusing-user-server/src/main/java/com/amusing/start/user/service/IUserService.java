package com.amusing.start.user.service;

import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.vo.user.UserDetailVo;
import com.amusing.start.user.entity.vo.user.UserListVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Created by 2022/10/2.
 *
 * @author lvqingyu
 */
public interface IUserService {

    /**
     * 用户-分页列表
     *
     * @param name  用户名
     * @param phone 手机号
     * @param page  页码
     * @param size  分页展示条目
     * @return 用户列表
     */
    IPage<UserListVo> list(String name, String phone, Integer page, Integer size);

    /**
     * 用户详情
     *
     * @param userId 用户ID
     * @return 用户信息
     * @throws CustomException 自定义异常信息
     */
    UserDetailVo detail(String userId) throws CustomException;

    /**
     * 删除用户
     *
     * @param userId   用户ID
     * @param updateBy 执行人ID
     * @return true:成功 false:失败
     * @throws CustomException 自定义异常信息
     */
    Boolean del(String userId, String updateBy) throws CustomException;

}
