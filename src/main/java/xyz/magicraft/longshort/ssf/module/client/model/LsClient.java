package xyz.magicraft.longshort.ssf.module.client.model;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import xyz.magicraft.longshort.ssf.base.BaseModel;
import xyz.magicraft.longshort.ssf.module.client.Platform;


@MappedSuperclass
public class LsClient extends BaseModel{
	
	@Column(unique=true)
	private String secret;
	
	private String platformInfo;

	private Instant lastCheckinDate;
	
    
    private Platform platform;
    
    

	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}


	public String getPlatformInfo() {
		return platformInfo;
	}


	public void setPlatformInfo(String platformInfo) {
		this.platformInfo = platformInfo;
	}


	public Instant getLastCheckinDate() {
		return lastCheckinDate;
	}


	public void setLastCheckinDate(Instant lastCheckinDate) {
		this.lastCheckinDate = lastCheckinDate;
	}




	public Platform getPlatform() {
		return platform;
	}


	public void setPlatform(Platform platform) {
		this.platform = platform;
	}





}
