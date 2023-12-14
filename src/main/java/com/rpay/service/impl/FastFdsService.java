package com.rpay.service.impl;

import com.rpay.common.exception.BusinessException;
import com.rpay.common.template.FastDfsTemplate;
import com.rpay.model.FilePojo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author steven
 */
@Service
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "fds")
public class FastFdsService extends AbstractIFileService {
    @Resource
    private FastDfsTemplate fdsTemplate ;

    @Override
    protected FilePojo uploadFile(MultipartFile file) {
        return fdsTemplate.upload(file);
    }


    @Override
    protected FilePojo uploadFileSharding(MultipartFile file, HttpSession session) {
        throw new BusinessException("分片上传目前只支持OSS，本地模式还未实现！");
    }

    @Override
    protected void deleteFile(String url) {
        fdsTemplate.delete(url);
    }

    @Override
    public void download(String url, HttpServletResponse response) {
        fdsTemplate.download(url, response);
    }
}
