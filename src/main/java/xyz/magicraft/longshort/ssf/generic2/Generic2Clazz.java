package xyz.magicraft.longshort.ssf.generic2;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Generic2Clazz<T> {
//
//	Class<T> clazz;
//	
//	
//	public void setClazz(Class<T> clazz) {
//		this.clazz = clazz;
//		
//		Class c = getClass();
//		
//		while(c != Generic2Clazz.class) c = c.getSuperclass();
//		
//		Type t = ((ParameterizedType)(c.getGenericSuperclass())).getActualTypeArguments()[0];
//		
//
//		System.out.println("set clazz: Generic111: " + c.getGenericSuperclass());
//		System.out.println("set clazz: Generic111: " + t);
//		System.out.println("set clazz: Generic111: " + getClass());
//		System.out.println("set clazz: Generic112: " +  ((ParameterizedType)(getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
//	
//		System.out.println("set clazz: Generic112: " + getClazz());
//	}
	
	public Class<T> getClazz() {
		
		Class<?> c = getClass();
		
		while(! (c.getGenericSuperclass() instanceof ParameterizedType)) c = c.getSuperclass();
		
		ParameterizedType pt = (ParameterizedType)c.getGenericSuperclass();
		
		Type t = pt.getActualTypeArguments()[0];
		
		if (t instanceof Class) {
			return (Class<T>)t;
		}else {
			return null;
		}
		
	}
	

}
