package xyz.magicraft.longshort.ssf.module.client.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import xyz.magicraft.longshort.ssf.base.BaseModel;
import xyz.magicraft.longshort.ssf.module.client.Channel;
import xyz.magicraft.longshort.ssf.module.client.Env;
import xyz.magicraft.longshort.ssf.module.client.Life;
import xyz.magicraft.longshort.ssf.module.client.Platform;


@MappedSuperclass
public class LsAppVersion extends BaseModel{



	
	private String version;


	@Enumerated(EnumType.STRING)
	private Env env;

	@Enumerated(EnumType.STRING)
	private Platform platform; 
	

	@Enumerated(EnumType.STRING)
	private Channel channel;
	

	@Enumerated(EnumType.STRING)
	private Life life = Life.submited;


	
	@Column(name="package")
	private String binary;
	private String asset;
	
	private String link;
	
	private Boolean focus;
	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	public Env getEnv() {
		return env;
	}

	public void setEnv(Env env) {
		this.env = env;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Life getLife() {
		return life;
	}

	public void setLife(Life life) {
		this.life = life;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public String getBinary() {
		return binary;
	}

	public void setBinary(String binary) {
		this.binary = binary;
	}
	

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Boolean getFocus() {
		return focus;
	}

	public void setFocus(Boolean focus) {
		this.focus = focus;
	}



	


}
