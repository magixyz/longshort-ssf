package xyz.magicraft.longshort.ssf.module.message.model;

import java.util.Date;

import jakarta.persistence.MappedSuperclass;
import xyz.magicraft.longshort.ssf.base.BaseModel;


@MappedSuperclass
public class LsNotice extends LsMessage{


	Boolean closed;
	Boolean executed;

	
	
	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	public Boolean getExecuted() {
		return executed;
	}

	public void setExecuted(Boolean executed) {
		this.executed = executed;
	}
	
	
	

}
