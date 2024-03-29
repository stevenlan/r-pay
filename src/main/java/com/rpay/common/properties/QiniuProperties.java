package com.rpay.common.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 七牛云配置资源类
 *
 * @author dinghao
 * @date 2021/3/10
 */
@Getter
@Setter
public class QiniuProperties {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String path;
}
