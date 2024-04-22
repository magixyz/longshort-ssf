package xyz.magicraft.longshort.ssf.wechat;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("xyz.magicraft.longshort.ssf.wechat.model")
@EnableJpaRepositories("xyz.magicraft.longshort.ssf.wechat.repository")
public class WechatBeanConfig {

}
