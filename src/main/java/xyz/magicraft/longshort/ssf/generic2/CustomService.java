package xyz.magicraft.longshort.ssf.generic2;


import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


public class CustomService<T> extends Generic2Clazz<T>{


	
	@Autowired
	protected Generic2Repository<T> genericRepository; 
	
	@Autowired
	protected Generic2Dao<T> genericDao; 
	
	
	@PersistenceContext 
	protected EntityManager entityManager;
	
	protected Generic2Service<T> genericService;
	
	@PostConstruct
	public void init() {
		
		genericDao.setClazz(clazz);
		genericService.setClazz(clazz);
		
		
	}

}
