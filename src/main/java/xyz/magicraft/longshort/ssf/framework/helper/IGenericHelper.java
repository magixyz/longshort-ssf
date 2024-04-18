package xyz.magicraft.longshort.ssf.framework.helper;

import java.util.Map;

import xyz.magicraft.longshort.ssf.framework.repository.IGenericRepository;



public interface IGenericHelper {
	

	public IGenericRepository getRepository(Class c);
	
	public Object getDataByMsg(Map<String,Object> msg, Class c);
	
	
	public Class getClass(String page) ;
	
	
	
}

