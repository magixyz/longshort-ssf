package xyz.magicraft.longshort.ssf.demo.generic2;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import xyz.magicraft.longshort.ssf.base.BaseModel;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Generic2DemoModel extends BaseModel{

}
