package xyz.magicraft.longshort.ssf.generic.helper;

import jakarta.servlet.http.HttpSession;

public interface IGenericInterceptor {
	

	public void post(Object obj,Class c,HttpSession session);
	public void get(Object obj,Class c,HttpSession session);
	public void list(Object obj,Class c,HttpSession session);
	public void delete(Object obj,Class c,HttpSession session);
	
	
}

