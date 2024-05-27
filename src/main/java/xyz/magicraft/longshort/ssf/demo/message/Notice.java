package xyz.magicraft.longshort.ssf.demo.message;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import xyz.magicraft.longshort.ssf.module.message.model.LsNotice;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Notice extends LsNotice{

}
