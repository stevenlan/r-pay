package com.rpay.common.template;

import com.rpay.common.properties.FsServerProperties;
import com.rpay.common.utils.FileUtil;
import com.rpay.model.FilePojo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;

/**
 * fastDfs的封装模板
 * @author steven
 */

@Slf4j
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "fds")
public class FastDfsTemplate implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    @Resource
    private FsServerProperties fileProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        ClientGlobal.initByProperties(fileProperties.getFds());

        logger.info("conf success! conf: classpath:fdfs_client.conf");
    }

    @SneakyThrows
    public FilePojo upload(MultipartFile file) {
        FilePojo pojo = FileUtil.buildFilePojo(file);
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getTrackerServer() ;

        StorageClient1 storageClient = new StorageClient1(trackerServer);
//          NameValuePair nvp = new NameValuePair("age", "18");
        NameValuePair nvp [] = new NameValuePair[]{
                new NameValuePair("name", file.getName()),
                new NameValuePair("type", file.getContentType())
        };
        String url = storageClient.upload_file1("group1", file.getBytes(), pojo.getSuffix(), nvp);

        pojo.setUrl(url);
        logger.debug(url);
        return pojo ;
    }

    @SneakyThrows
    public void delete(String url) {
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getTrackerServer() ;

        StorageClient1 storageClient = new StorageClient1(trackerServer);
        storageClient.delete_file1(url) ;
    }

    @SneakyThrows
    public void download(String url, HttpServletResponse response) {
        byte[] bytes = download(url) ;

        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        //性能考虑可以分片输出
        out.write(bytes, 0, bytes.length);
        out.flush();
        out.close();
    }

    @SneakyThrows
    public byte[] download(String url) {
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getTrackerServer() ;

        StorageClient1 storageClient = new StorageClient1(trackerServer);
        return storageClient.download_file1(url) ;
    }
}
