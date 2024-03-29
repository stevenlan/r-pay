package com.rpay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rpay.model.Auth;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限表mapper接口
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Mapper
public interface AuthMapper extends BaseMapper<Auth> {
}
