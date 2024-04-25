package xyz.magicraft.longshort.ssf.demo.define;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({"xyz.magicraft.longshort.ssf.define"})
public class DemoBeanConfig {

}
