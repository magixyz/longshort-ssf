package xyz.magicraft.longshort.ssf.formfields.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import xyz.magicraft.longshort.ssf.base.BaseModel;

@Entity
public class LsField extends BaseModel{

	private String name;
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY,optional = true)
	private LsForm form;
	
}
