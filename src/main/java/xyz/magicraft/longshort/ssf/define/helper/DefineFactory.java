package xyz.magicraft.longshort.ssf.define.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import cn.hutool.core.util.StrUtil;
import xyz.magicraft.longshort.ssf.define.BaseDefine;
import xyz.magicraft.longshort.ssf.utils.JsonUtil;

public class DefineFactory<K,T extends BaseDefine<K>> {
	
	private Class<K> clazz;

	public List<T> list;
	public Map<K,T> map;
	
	
	private String res;
	private TypeReference<List<T>> ref;
	
	public DefineFactory(Class<K> clazz, String res,TypeReference<List<T>> ref) {
		this.res = res;
		this.ref = ref;
		this.clazz = clazz;
	}
	
	public String load() {
		
		
		
		System.out.println(res);
		System.out.println(ref.getClass().getSimpleName());
		
		list = JsonUtil.parseJsonResource(res , ref );
		

		map = new HashMap<K,T>();
		
		for (T p :list) {
			
			map.put(p.getKey(), p);
		}
		
		return StrUtil.toUnderlineCase(clazz.getSimpleName());
		
	}
	
}
