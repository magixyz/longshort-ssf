package xyz.magicraft.longshort.ssf.define.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DefineRepository {
	
	@Autowired
	IDefineHelper defineHelper;
	
	Map<String,DefineFactory> map = new HashMap<String, DefineFactory>();

	@PostConstruct
	void init() {
		
		List<DefineFactory> list = defineHelper.listFactory();
		
		for (DefineFactory fac: list) {
		
			String key = fac.load();
			
			map.put(key, fac);
			
		}
		
		
	}
	
	public DefineFactory find(String k) {
		return map.get(k);
	}
}
