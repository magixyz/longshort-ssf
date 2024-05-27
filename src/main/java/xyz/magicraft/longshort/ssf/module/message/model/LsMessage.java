package xyz.magicraft.longshort.ssf.module.message.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.MappedSuperclass;
import xyz.magicraft.longshort.ssf.base.BaseModel;


@MappedSuperclass
public class LsMessage extends BaseModel{


	public enum Way{sms,email,notify,popup} 
	

	private String title;
	private String content;

	private Date startDate;
	private Date endDate;

	private Way way;
	

	private Boolean disabled;
	



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	


	public Way getWay() {
		return way;
	}

	public void setWay(Way way) {
		this.way = way;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	
	

}
