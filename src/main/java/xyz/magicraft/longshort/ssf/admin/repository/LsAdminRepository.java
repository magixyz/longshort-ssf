package xyz.magicraft.longshort.ssf.admin.repository;



import xyz.magicraft.longshort.ssf.generic.iface.IGenericRepository;


public interface LsAdminRepository<T> extends IGenericRepository<T> {

	  
	T findByMobile(String mobile);
	T findByEmail(String email);

}