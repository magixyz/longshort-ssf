package xyz.magicraft.longshort.ssf.module.message.model;


import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import xyz.magicraft.longshort.ssf.base.BaseModel;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class Sms extends BaseModel{

	private String mobile;
	private String content;
	private String code;
	private boolean consumed;

	@CreatedDate
	@LastModifiedDate
	private Date issuedDate;

	public Sms() {}

	public Sms(String mobile,String code) {
		this.mobile = mobile;
		this.code = code;
	}



	public boolean isConsumed() {
		return consumed;
	}



	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}





	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	 @Override
	 public String toString() {
	 	return String.format(
	        "SMS[uuid=%s, mobile='%s'], code='%s'",
	        uuid, mobile,code);
	 }

}
