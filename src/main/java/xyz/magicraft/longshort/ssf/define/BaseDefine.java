package xyz.magicraft.longshort.ssf.define;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class BaseDefine<K> {

	
	@Enumerated(EnumType.STRING)
	K key;

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}
	
	

}


