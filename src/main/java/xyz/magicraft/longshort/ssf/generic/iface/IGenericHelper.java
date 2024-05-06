package xyz.magicraft.longshort.ssf.generic.iface;

import java.util.List;
import java.util.Map;



public interface IGenericHelper {
	

	List<Class> listModels ();
	

	public Class getClass(String model) ;
	public IGenericRepository getRepository(Class c);
	

	public IGenericInterceptor getInterceptor(Class c);
	
	public Object getDataByMsg(Map<String,Object> msg, Class c);
	
	
	
	
	
}

