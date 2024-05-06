package xyz.magicraft.longshort.ssf.generic.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import xyz.magicraft.longshort.ssf.base.BaseModel;
import xyz.magicraft.longshort.ssf.base.Pagination;
import xyz.magicraft.longshort.ssf.generic.iface.IGenericHelper;
import xyz.magicraft.longshort.ssf.generic.iface.IGenericRepository;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;

@Service
public class GenericService {


	
	@Autowired
	IGenericHelper genericHelper; 
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@PersistenceContext 
    EntityManager entityManager;

	

	public Object get(UUID uuid,Class c ) {
		
		if (uuid == null || c == null ) return null;
		
		IGenericRepository repo = genericHelper.getRepository(c);
		
        try {
        	
        	Optional<Object> optional = repo.findById(uuid);
        	

    		if (optional.isEmpty()) return null;
    		
    		Object item = optional.get();
    		
    		return item;
			
			
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

		return null;
		
	}
	
	public <T> Object loadByForeign(String page, String foreign, UUID uuid ,Class<T> t) {
		
		if (page == null || foreign == null || uuid ==null ) return null;
		
//		
		String sql= String.format("select * from %s where %s = x'%s'",page,foreign,uuid.toString().replace("-", "")) ;

        System.out.println("sql: " + sql);
        
        Object obj = entityManager.createNativeQuery(sql,t).getSingleResult();
//        
        
        return obj;
	}
	
	
	public <T> Iterable<T> listByForeign(String page, String foreign, UUID uuid ,Class<T> t) {
		
		if (uuid == null || page == null || foreign == null ) return null;
		
		
		String sql= String.format("select * from %s where %s = x'%s'",page,foreign,uuid.toString().replace("-", "")) ;

        System.out.println("sql: " + sql);
        
        List list = entityManager.createNativeQuery(sql,t).getResultList();
        
        
        return list;

		
		
		

//		GenericRepository<T> repository = genericHelper.getRepository(page);
//		
//		
//        try {
//        	
//        	Iterable<T> list = repository.findByForeignUuid(page, foreign, uuid);
//        	
//
//        	return list;
//			
//			
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
//
//		return null;
		
	}

	public Object patch(UUID uuid, Field [] fields, Object data,Class c) {
		
		if (uuid == null || data == null ) return null;

		IGenericRepository repo = genericHelper.getRepository(c);
		
		System.out.println(repo.getClass());
		
		
        try {
        	
        	Optional<Object> optional = repo.findById(uuid);

        	System.out.println("patch uuid:" + uuid);
        	System.out.println("patch target" + (optional.isEmpty()?"empty":"exist") );
        	

    		if (optional.isEmpty()) return null;

        	System.out.println("patch uuid:" + uuid);
    		
    		Object item = optional.get();
    		
    		
    		for (Field f : fields) {
//	    		Field f = c.getDeclaredField( StrUtil.toCamelCase(field) );
				f.setAccessible(true);
				
				if (f.getType().isAssignableFrom(BaseModel.class)) {
					
					if (f.get(data) == null) {
						f.set( item, null);
					}else {
						IGenericRepository refRepo = genericHelper.getRepository(f.getType());
						Optional<Object> refOptional = refRepo.findById( ((BaseModel)f.get(data)).getUuid());
						if (refOptional.isPresent()) {
							f.set(item,refOptional.get());
						}
					}
					
					
				}else {
					f.set(item , f.get(data));
						
				}
				
				System.out.println("field new value:" + f.get(data));
				
				f.setAccessible(false);
    		}
			
			
			return repo.save(item);
			
			
			
        } catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

		return null;
		
	}
	
	@Transactional
    @Modifying
	public boolean patchForeign(String page,UUID uuid, String foreign, UUID foreignUuid ) {
		
		
		String sql= String.format("update %s set %s_uuid = x'%s' where uuid = x'%s'",page,foreign,foreignUuid.toString().replace("-", ""),uuid.toString().replace("-", "")) ;

        System.out.println("sql: " + sql);
        
        int ret =  entityManager.createNativeQuery(sql).executeUpdate();
        
		return ret > 0;
		
	}
	public Object put( UUID uuid, Object data, Class c) {
		
		System.out.println("uuid: " + uuid);
		System.out.println("data:  " + data.toString());
		
		IGenericRepository repo = genericHelper.getRepository(c);
		
		try {
			
			return repo.save(data);
			
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} 
		
		return null;
	}
	

	public Object post(Object data, Class c ) {

		IGenericRepository repo = genericHelper.getRepository(c);
		
		try {
			
			return repo.save(data);
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Object import_(List objs, Class c ) {

		IGenericRepository repo = genericHelper.getRepository(c);
		
		try {
			for(Object obj: objs) {

				return repo.save(obj);
			}
			
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public Object delete(UUID uuid, Class c ) {
		IGenericRepository repo = genericHelper.getRepository(c);
		
		try {

			repo.deleteById(uuid);
			
			return uuid;
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Iterable list(Class c) {

		IGenericRepository repo = genericHelper.getRepository(c);
		
	    Iterable items = repo.findAll();
	    
		return items;
	}
	
	
	
	
//	
	public Page<Object> list(int page,int size, Class c) {

		IGenericRepository repo = genericHelper.getRepository(c);
		
		
		Pageable pageable = PageRequest.of(page, size, Direction.DESC,"createdDate");
		

	    Page<Object> objPage = repo.findAll(pageable);
	    
	    
		return objPage;
	}
	
	


	public <T> Iterable search( Map<String,Object> condition,Class<T> c) {
		

		IGenericRepository<T> repository = genericHelper.getRepository(c);
		
		
		Specification<T> spec = new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
	
		   	 	
		   	 	List<Predicate> predicates = new ArrayList<Predicate>();
		   	 	
		   	 	if (condition != null) {
			   	 	for(String key : condition.keySet()) {
			   	 		
			   	 		Object value = condition.get(key);
				   	 	Predicate predicate = builder.equal( root.get(StrUtil.toCamelCase(key)),  value);
				   	 	
			   	 		predicates.add(predicate);
				   	}
		   	 	}
		 	   	 		
	   	 		
				
		   	 	Predicate total = builder.and(predicates.toArray(new Predicate[0]));
				
				
				return total;
			}	
		};
		
		

	    List items = repository.findAll(spec);
	    
	    
		
//		 Pagination<Object> pg = new Pagination<Object>(page,size,orderPage.getTotalPages(),orderPage.getContent());
		 
		 return items;
	}
	
	
	public <T> Pagination<T> search( Map<String,String> filter , int page,int size, Class<T> t) {

		IGenericRepository<T> repository = genericHelper.getRepository(t);
		
		
		if (filter.containsKey("_page")) {
			page = Integer.valueOf( filter.get("_page"));
		}
	
		if (filter.containsKey("_size")) {
			size = Integer.valueOf( filter.get("_size"));
		}
		 	
		Specification<T> spec = new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
	
		   	 	
		   	 	List<Predicate> predicates = new ArrayList<Predicate>();
		   	 	
		   	 	
				
		   	 	Predicate total = builder.and(predicates.toArray(new Predicate[0]));
				
				
				return total;
			}
		};	
		
		Pageable pageable = PageRequest.of(page, size, Direction.DESC,"createdDate");
	    Page<T> orderPage = repository.findAll(pageable);
	    
	    Pagination<T> pg = new Pagination<T>(page,size,orderPage.getTotalPages(),orderPage.getContent());
	    
	    
		return pg;
	}
	
}
