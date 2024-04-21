package xyz.magicraft.longshort.ssf.account;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("xyz.magicraft.longshort.ssf.account.model")
@EnableJpaRepositories("xyz.magicraft.longshort.ssf.account.repository")
public class AccountConfig {

}
