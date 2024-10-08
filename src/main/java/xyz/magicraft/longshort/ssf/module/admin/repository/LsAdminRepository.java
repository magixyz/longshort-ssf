package xyz.magicraft.longshort.ssf.module.admin.repository;



import xyz.magicraft.longshort.ssf.generic.iface.IGenericRepository;


public interface LsAdminRepository<T> extends IGenericRepository<T> {

	  
	T findByMobile(String mobile);
	T findByEmail(String email);

}