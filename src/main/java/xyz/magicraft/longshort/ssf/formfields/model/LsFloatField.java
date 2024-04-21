package xyz.magicraft.longshort.ssf.formfields.model;



import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class LsFloatField extends LsField{
	
	Float floatValue;
}
