package xyz.magicraft.longshort.ssf.module.message;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("xyz.magicraft.longshort.ssf.module.message.model")
@EnableJpaRepositories("xyz.magicraft.longshort.ssf.module.message.repository")
public class MessageConfig {

}
