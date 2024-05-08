package xyz.magicraft.longshort.ssf.demo.generic2;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({"xyz.magicraft.longshort.ssf.generic2"})
//@EntityScan("xyz.magicraft.longshort.ssf.demo.generic")
@EnableJpaRepositories("xyz.magicraft.longshort.ssf.demo.generic2")
public class Generic2DemoBeanConfig {

}
