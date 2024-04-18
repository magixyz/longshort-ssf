package xyz.magicraft.longshort.ssf.demo;


import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import xyz.magicraft.longshort.ssf.framework.model.BaseModel;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class DemoModel extends BaseModel{

}
