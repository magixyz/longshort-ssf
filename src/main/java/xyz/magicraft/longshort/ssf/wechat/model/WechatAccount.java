package xyz.magicraft.longshort.ssf.wechat.model;


import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import xyz.magicraft.longshort.ssf.base.BaseModel;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class WechatAccount extends BaseModel{
	
	private String openid;
	private String sessionKey;
	private String unionid;
	private String appid;
	private String mobile;
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
