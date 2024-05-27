package xyz.magicraft.longshort.ssf.module.admin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import xyz.magicraft.longshort.ssf.base.BaseModel;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@MappedSuperclass
public class LsAdmin extends BaseModel{



	@Column(unique=true)
	private String mobile;

	@Column(unique=true)
	private String email;

	private String password;

	private String token;


	private String lastLoginIp;


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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}



	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}


	@Override
	 public String toString() {
	 	return String.format(
	        "Admin[uuid=%d, mobile='%s', email='%s']",
	        uuid, email);
	 }

}
