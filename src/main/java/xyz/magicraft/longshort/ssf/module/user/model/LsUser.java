package xyz.magicraft.longshort.ssf.module.user.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import xyz.magicraft.longshort.ssf.base.BaseModel;



@MappedSuperclass
public class LsUser extends BaseModel{


	@Column(unique=true)
	private String mobile;

	@Column(unique=true)
	private String email;

	@JsonIgnore
	@Column()
	private String passwd;

	@JsonIgnore
	private String code;
	
	

	private Date lastLoginDate;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}




	 public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}




	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	
	
	

	@Override
	 public String toString() {
	 	return String.format(
	        "User[uuid=%s, mobile='%s', email='%s']",
	        uuid.toString(), mobile,email);
	 }

}
