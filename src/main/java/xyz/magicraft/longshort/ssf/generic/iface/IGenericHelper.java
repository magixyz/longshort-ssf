package xyz.magicraft.longshort.ssf.generic.iface;

import java.util.List;
import java.util.Map;




public interface IGenericHelper {
	

	<T> List<Class<T>> listModels ();
	

	public <T> Class<T> getClass(String model) ;
	public <T> IGenericRepository<T> getRepository(Class<T> c);
	

	public IGenericInterceptor getInterceptor(Class c);
	
	public <T> T getDataByMsg(Map<String,Object> msg, Class<T> c);
	
	
	
	
	
}

