package xyz.magicraft.longshort.ssf.admin;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("xyz.magicraft.longshort.ssf.admin.model")
@EnableJpaRepositories("xyz.magicraft.longshort.ssf.admin.repository")
public class AdminConfig {

}
