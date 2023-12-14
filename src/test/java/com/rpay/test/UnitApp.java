package com.rpay.test;

import com.rpay.FreeFsApplication;
import com.rpay.common.properties.FsServerProperties;
import com.rpay.common.template.FastDfsTemplate;
import com.rpay.model.User;
import com.rpay.service.impl.UserServiceImpl;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FreeFsApplication.class},properties = {"spring.profiles.active=dev"})
@AutoConfigureMockMvc
public class UnitApp {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    @Autowired
    private UserServiceImpl userService ;
    @Autowired
    private FastDfsTemplate template ;
    @Resource
    private FsServerProperties fileProperties;

    @Test
    public void getList() {
        List<User> list = userService.list() ;
        logger.info(list.toString());
    }

    @Test
    public void runEnd() throws IOException, MyException {
        File f = ResourceUtils.getFile("classpath:fdfs_client.conf");
        Assert.assertNotNull(f);
        Assert.assertEquals(f.getName(),"fdfs_client.conf");

        Properties p = new Properties() ;
        p.load(new FileInputStream(f));
        ClientGlobal.initByProperties(p);

        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getTrackerServer() ;

        StorageClient storageClient = new StorageClient(trackerServer);
//          NameValuePair nvp = new NameValuePair("age", "18");
        NameValuePair nvp [] = new NameValuePair[]{
                new NameValuePair("age", "18"),
                new NameValuePair("sex", "male")
        };
        String fileIds[] = storageClient.upload_file(f.getPath(), "conf", nvp);

        Assert.assertNotNull(fileIds);
        //Assert.assertEquals(fileIds.length,2);
        logger.info(fileIds[0]+ "/" + fileIds[1]); ;
    }

    @Test
    public void testDown() throws IOException, MyException {
        //group1/M00/00/00/wKgfMmGT7AGAL7uyAAABC5k_LBU99.conf
        byte[] bytes = template.download("group1/M00/00/00/wKgfMmGT7AGAL7uyAAABC5k_LBU99.conf") ;
        Assert.assertNotNull(bytes);
        Assert.assertTrue(bytes.length > 0);
    }

}
