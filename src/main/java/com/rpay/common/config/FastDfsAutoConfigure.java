package com.rpay.common.config;

import com.rpay.common.properties.FsServerProperties;
import com.rpay.common.template.FastDfsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author steven
 */
@Configuration
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "fds")
@Import(FastDfsTemplate.class)
@RequiredArgsConstructor
public class FastDfsAutoConfigure {

    private final FsServerProperties fileProperties;
}
