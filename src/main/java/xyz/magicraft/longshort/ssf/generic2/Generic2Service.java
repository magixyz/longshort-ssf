package xyz.magicraft.longshort.ssf.generic2;


import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.sf.jsqlparser.statement.select.Limit;
import xyz.magicraft.longshort.ssf.base.BaseModel;
import xyz.magicraft.longshort.ssf.base.Pagination;


public class Generic2Service<T> extends Generic2Clazz<T>{


	
	@Autowired
	Generic2Repository<T> genericRepository; 
	
	@Autowired
	Generic2Dao<T> genericDao; 
	
	
	@PersistenceContext 
    EntityManager entityManager;
	
	@PostConstruct
	public void init() {
		
		genericDao.setClazz(clazz);
		
		
	}

	
	public Iterable<T> list() {

		return genericRepository.findAll();
	}
	
	public Iterable<T> listLast(Integer limit) {

		return genericRepository.findAll(PageRequest.of(0, limit, Direction.DESC,"createdDate")).toList();
	}
	

	public T get(UUID uuid ) {
		
		if (uuid == null ) return null;
		
		Optional<T> optional = genericRepository.findById(uuid);
		
		if (optional.isEmpty()) return null;
		
		T item = optional.get();
		
		return item;
		
	}
	
	public T create(T data) {

		return genericRepository.save(data);
		
	}
	
	public void delete(UUID uuid ) {

		genericRepository.deleteById(uuid);		
	
	}

	
	public T loadByForeign(String foreign, UUID uuid) {
		
		if (foreign == null || uuid ==null ) return null;
		
		return genericDao.loadByForeign(foreign, uuid);
		
		
	}
//	
//	
//	public <T> Iterable<T> listByForeign(String page, String foreign, UUID uuid ,Class<T> t) {
//		
//		if (uuid == null || page == null || foreign == null ) return null;
//		
//		
//		String sql= String.format("select * from %s where %s = x'%s'",page,foreign,uuid.toString().replace("-", "")) ;
//
//        System.out.println("sql: " + sql);
//        
//        List list = entityManager.createNativeQuery(sql,t).getResultList();
//        
//        
//        return list;
//
//		
//	}
//
	public T patch(UUID uuid, Field [] fields, T data ) {
		
		if (uuid == null || data == null || fields==null || fields.length == 0 ) return null;

		
		
        try {
        	
        	Optional<T> optional = genericRepository.findById(uuid);


    		if (optional.isEmpty()) return null;

    		
    		T item = optional.get();
    		
    		
    		for (Field f : fields) {
				f.setAccessible(true);
				
				f.set(item , f.get(data));
				
				System.out.println("field new value:" + f.get(data));
				
				f.setAccessible(false);
    		}
			
			
			return genericRepository.save(item);
			
			
			
        } catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

		return null;
		
	}
//	
//	@Transactional
//    @Modifying
//	public boolean patchForeign(String page,UUID uuid, String foreign, UUID foreignUuid ) {
//		
//		
//		String sql= String.format("update %s set %s_uuid = x'%s' where uuid = x'%s'",page,foreign,foreignUuid.toString().replace("-", ""),uuid.toString().replace("-", "")) ;
//
//        System.out.println("sql: " + sql);
//        
//        int ret =  entityManager.createNativeQuery(sql).executeUpdate();
//        
//		return ret > 0;
//		
//	}
//	public Object put( UUID uuid, Object data, Class c) {
//		
//		System.out.println("uuid: " + uuid);
//		System.out.println("data:  " + data.toString());
//		
//		IGenericRepository repo = genericHelper.getRepository(c);
//		
//		try {
//			
//			return repo.save(data);
//			
////		} catch (NoSuchFieldException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (IllegalAccessException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (InstantiationException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (InvocationTargetException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (NoSuchMethodException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} 
//		
//		return null;
//	}
//	

//	
//	public Object import_(List objs, Class c ) {
//
//		IGenericRepository repo = genericHelper.getRepository(c);
//		
//		try {
//			for(Object obj: objs) {
//
//				return repo.save(obj);
//			}
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
//		return null;
//	}
//


//	
//	
//	
//	
////	
//	public Page<Object> list(int page,int size, Class c) {
//
//		IGenericRepository repo = genericHelper.getRepository(c);
//		
//		
//		Pageable pageable = PageRequest.of(page, size, Direction.DESC,"createdDate");
//		
//
//	    Page<Object> objPage = repo.findAll(pageable);
//	    
//	    
//		return objPage;
//	}
//	
//	
//
//	public <T> Iterable searchNullable( Map<String,Object> condition,Class<T> c) {
//		
//
//		IGenericRepository<T> repository = genericHelper.getRepository(c);
//		
//		
//		Specification<T> spec = new Specification<T>() {
//			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//	
//		   	 	
//		   	 	List<Predicate> predicates = new ArrayList<Predicate>();
//		   	 	
//		   	 	if (condition != null) {
//			   	 	for(String key : condition.keySet()) {
//			   	 		
//			   	 		Object value = condition.get(key);
//				   	 	Predicate predicate1 = builder.isNull(root.get(StrUtil.toCamelCase(key)));
//				   	 	Predicate predicate2 = builder.equal( root.get(StrUtil.toCamelCase(key)),  value);
//				   	 	
//			   	 		predicates.add(builder.or(predicate1,predicate2));
//				   	}
//		   	 	}
//		 	   	 		
//	   	 		
//				
//		   	 	Predicate total = builder.and(predicates.toArray(new Predicate[0]));
//				
//				
//				return total;
//			}	
//		};
//		
//		
//
//	    List items = repository.findAll(spec);
//	    
//		return items;
//	}
//	
//
//
//	public <T> Iterable search( Map<String,Object> condition,Class<T> c) {
//		
//
//		IGenericRepository<T> repository = genericHelper.getRepository(c);
//		
//		
//		Specification<T> spec = new Specification<T>() {
//			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//	
//		   	 	
//		   	 	List<Predicate> predicates = new ArrayList<Predicate>();
//		   	 	
//		   	 	if (condition != null) {
//			   	 	for(String key : condition.keySet()) {
//			   	 		
//			   	 		Object value = condition.get(key);
//				   	 	Predicate predicate = builder.equal( root.get(StrUtil.toCamelCase(key)),  value);
//				   	 	
//			   	 		predicates.add(predicate);
//				   	}
//		   	 	}
//		 	   	 		
//	   	 		
//				
//		   	 	Predicate total = builder.and(predicates.toArray(new Predicate[0]));
//				
//				
//				return total;
//			}	
//		};
//		
//		
//
//	    List items = repository.findAll(spec);
//	    
//		return items;
//	}
//	
//	
	public Pagination<T> paginationList( int page,int size) {
		

		Pageable pageable = PageRequest.of(page, size, Direction.DESC,"createdDate");
	    Page<T> orderPage = genericRepository.findAll(pageable);
	    
	    Pagination<T> pg = new Pagination<T>(page,size,orderPage.getTotalPages(),orderPage.getContent());
	    
	    
		return pg;
	}
	
}
