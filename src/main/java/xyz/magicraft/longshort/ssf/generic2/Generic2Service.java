package xyz.magicraft.longshort.ssf.generic2;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.ReflectionUtils;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.sf.jsqlparser.statement.select.Limit;
import xyz.magicraft.longshort.ssf.base.BaseModel;
import xyz.magicraft.longshort.ssf.base.Pagination;
import xyz.magicraft.longshort.ssf.generic.iface.IGenericRepository;


public class Generic2Service<T> extends Generic2Clazz<T>{


	
	@Autowired
	protected Generic2Repository<T> genericRepository; 
	
	@Autowired
	protected Generic2Dao<T> genericDao; 
	
	
	@PersistenceContext 
    EntityManager entityManager;
	
	
//	@Override
//	public void setClazz(Class clazz){
//		super.setClazz(clazz);
//		genericDao.setClazz(clazz);
//		
//		
//		
//
//		System.out.println("set clazz: Service: " + getClass().getGenericSuperclass());
//		
//		
//	}
	

	
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
	
	public T loadByField(String field,String value ) {
		
		if (field == null || value == null ) return null;
		
		T item = genericDao.loadByField(field,value);
		
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
		
		Object fObj;
		try {
			Field field = ReflectionUtils.findRequiredField(getClazz(),StrUtil.toCamelCase(foreign));
			Class fClazz = field.getType();
			fObj = fClazz.getDeclaredConstructor().newInstance();
			System.out.println("field class:" + fClazz.getSimpleName() + fClazz.getDeclaredFields());
			
			Field fUuid = ReflectionUtils.findRequiredField(fClazz ,"uuid");
			fUuid.setAccessible(true);
			fUuid.set(fObj, uuid);
			fUuid.setAccessible(false);
			
		} catch (SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		
		
		return genericDao.loadByForeign(foreign, fObj);
		
		
	}
	
	public Iterable<T> listByForeign(String foreign, UUID uuid){
		
		if (foreign == null || uuid ==null ) return null;
		
    	Object fObj;
		try {
			Field field = ReflectionUtils.findRequiredField(getClazz(),StrUtil.toCamelCase(foreign));
			Class fClazz = field.getType();
			fObj = fClazz.getDeclaredConstructor().newInstance();
			System.out.println("field class:" + fClazz.getSimpleName() + fClazz.getDeclaredFields());
			
			Field fUuid = ReflectionUtils.findRequiredField(fClazz ,"uuid");
			fUuid.setAccessible(true);
			fUuid.set(fObj, uuid);
			fUuid.setAccessible(false);
			
		} catch (SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		
		return genericDao.listByForeign(foreign, fObj);
	}
	
	public Iterable<T> searchByForeign(String foreign, T data){
		
		if (foreign == null || data ==null ) return null;
		
		Object obj;
		try {
			Field field = ReflectionUtils.findRequiredField(getClazz(),StrUtil.toCamelCase(foreign));
			field.setAccessible(true);
			obj = field.get(data);
			field.setAccessible(false);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		System.out.println(obj);
		
		return genericDao.listByForeign(foreign, obj);
	}
	



	public Pagination<T> paginationSearchByOrder( Map<String,Map<String,Object>> condition,String orderField , int page , int size ) {

		System.out.println( "%%%%%%%%%%%%% condition:" + condition.toString());
		
		
		Specification<T> spec = new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
	
		   	 	
		   	 	List<Predicate> predicates = new ArrayList<Predicate>();
		   	 	
		   	 	if (condition != null) {
			   	 	for(String key : condition.keySet()) {
			   	 		
			   	 		Map<String,Object> c = condition.get(key);
			   	 		
			   	 		Object value = c.get("value");
			   	 		String expr = (String)c.get("expression");
			   	 		Boolean strict = (Boolean)c.get("strict");
			   	 		
//			   	 		Predicate predicate1;
			   	 		
//			   	 		if (strict != null && strict) {
//			   	 			predicate1 = builder.isNotNull(root.get(StrUtil.toCamelCase(key)));
//			   	 		}else {
//			   	 			predicate1 = builder.isNull(root.get(StrUtil.toCamelCase(key)));
//			   	 		}
			   	 		
			   	 		System.out.println( "%%%%%%%%%%%%% expr:" + expr);
			   	 		System.out.println( "%%%%%%%%%%%%% value:" + value);
			   	 		
			   	 		Predicate predicate2;
			   	 		if ("start_with".equalsIgnoreCase(expr)) {
			   	 			
			   	 			System.out.println(value + "%");
			   	 			
			   	 			CriteriaBuilder.In in = builder.in((Expression)root.get(StrUtil.toCamelCase(key)));
			   	 		    
			   	 			for (int i=0; i< value.toString().length(); i++) {
			   	 				in.value(value.toString().substring(0,i+1));
			   	 			}
			   	 		    
			   	 			
			   	 			predicate2 =  in;
			   	 			
//			   	 			builder.length(root.get(StrUtil.toCamelCase(key)));
//			   	 			
//			   	 			predicate2 = builder.equal(builder.locate((Expression)value,(Expression)root.get(StrUtil.toCamelCase(key))), 0);
			   	 			
			   	 			
//			   	 			builder.locate((Expression)root.get(StrUtil.toCamelCase(key)),(String)value);
			   	 			
//			   	 			predicate2 = builder.like((Expression)root.get(StrUtil.toCamelCase(key)), value + "%" );
			   	 			
//			   	 			predicate2 = builder.lessThanOrEqualTo( root.get(StrUtil.toCamelCase(key)), (Comparable)value);
			   	 		}else if ("in".equalsIgnoreCase(expr)) {
			   	 			
			   	 			System.out.println(value + "%");
			   	 			
			   	 			CriteriaBuilder.In in = builder.in((Expression)root.get(StrUtil.toCamelCase(key)));
			   	 		    
			   	 			for (String r : (Iterable<String>)value) {
			   	 				in.value(r);
			   	 			}
			   	 		    
			   	 			
			   	 			predicate2 =  in;
			   	 			
			   	 		}else if ("include".equalsIgnoreCase(expr)) {
			   	 			
			   	 			System.out.println(value + "%%%%%%%%%%%%%" + key);
			   	 			

			   	 			
			   	 			return builder.like(root.get(StrUtil.toCamelCase(key)), "%" + value + "%");

			   	 			
			   	 		}else if("foreign".equalsIgnoreCase(expr)) {
			   	 			
			   	 			predicate2 = builder.equal( root.get(StrUtil.toCamelCase(key)), foreignObject(key, UUID.fromString((String)value)));
			   	 		}else if("null".equalsIgnoreCase(expr)) {
			   	 			
			   	 			boolean b = (boolean)value;
			   	 			
			   	 			if (b) {
			   	 				predicate2 = builder.isNull(root.get(StrUtil.toCamelCase(key)));	
			   	 			}else {
			   	 				
			   	 				System.out.println("b: " + key);
			   	 				
			   	 				predicate2 = builder.isNotNull(root.get(StrUtil.toCamelCase(key)));	
			   	 			}
			   	 			
			   	 			
			   	 		} else {
			   	 			predicate2 = builder.equal( root.get(StrUtil.toCamelCase(key)),  value);
			   	 		}
			   	 		
			   	 		
			   	 		predicates.add(predicate2);
			   	 		
//				   	 	if (strict != null && strict) {
//				   	 		predicates.add(builder.and(predicate1,predicate2));
//				   	 	}else {
//					   	 	
//				   	 		predicates.add(builder.or(predicate1,predicate2));
//					   	}
			   	 	}
		   	 	}
		 	   	 		
	   	 		
				
		   	 	Predicate total = builder.and(predicates.toArray(new Predicate[0]));
				
				
				return total;
			}	
		};
		

		Pageable pageable = PageRequest.of(page, size, Direction.DESC,StrUtil.toCamelCase(orderField),"uuid");
		

		Page<T> p =  this.genericRepository.findAll(spec, pageable);
		
	    Pagination<T> pg = new Pagination<T>(page,size,p.getTotalPages(),p.getContent());
	    
		
		return pg;
	    
	}
	


	public Page<T> searchByCondition( Map<String,Map<String,Object>> condition , int page , int size ) {
		
		
		
		Specification<T> spec = new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
	
		   	 	
		   	 	List<Predicate> predicates = new ArrayList<Predicate>();
		   	 	
		   	 	if (condition != null) {
			   	 	for(String key : condition.keySet()) {
			   	 		
			   	 		Map<String,Object> c = condition.get(key);
			   	 		
			   	 		Object value = c.get("value");
			   	 		String expr = (String)c.get("expression");
			   	 		Boolean strict = (Boolean)c.get("strict");
			   	 		
//			   	 		Predicate predicate1;
			   	 		
//			   	 		if (strict != null && strict) {
//			   	 			predicate1 = builder.isNotNull(root.get(StrUtil.toCamelCase(key)));
//			   	 		}else {
//			   	 			predicate1 = builder.isNull(root.get(StrUtil.toCamelCase(key)));
//			   	 		}
			   	 		
			   	 		Predicate predicate2;
			   	 		if ("start_with".equalsIgnoreCase(expr)) {
			   	 			
			   	 			System.out.println(value + "%");
			   	 			
			   	 			CriteriaBuilder.In in = builder.in((Expression)root.get(StrUtil.toCamelCase(key)));
			   	 		    
			   	 			for (int i=0; i< value.toString().length(); i++) {
			   	 				in.value(value.toString().substring(0,i+1));
			   	 			}
			   	 		    
			   	 			
			   	 			predicate2 =  in;
			   	 			
//			   	 			builder.length(root.get(StrUtil.toCamelCase(key)));
//			   	 			
//			   	 			predicate2 = builder.equal(builder.locate((Expression)value,(Expression)root.get(StrUtil.toCamelCase(key))), 0);
			   	 			
			   	 			
//			   	 			builder.locate((Expression)root.get(StrUtil.toCamelCase(key)),(String)value);
			   	 			
//			   	 			predicate2 = builder.like((Expression)root.get(StrUtil.toCamelCase(key)), value + "%" );
			   	 			
//			   	 			predicate2 = builder.lessThanOrEqualTo( root.get(StrUtil.toCamelCase(key)), (Comparable)value);
			   	 		}else if ("in".equalsIgnoreCase(expr)) {
			   	 			
			   	 			System.out.println(value + "%");
			   	 			
			   	 			CriteriaBuilder.In in = builder.in((Expression)root.get(StrUtil.toCamelCase(key)));
			   	 		    
			   	 			for (String r : (Iterable<String>)value) {
			   	 				in.value(r);
			   	 			}
			   	 		    
			   	 			
			   	 			predicate2 =  in;
			   	 			
//			   	 			builder.length(root.get(StrUtil.toCamelCase(key)));
//			   	 			
//			   	 			predicate2 = builder.equal(builder.locate((Expression)value,(Expression)root.get(StrUtil.toCamelCase(key))), 0);
			   	 			
			   	 			
//			   	 			builder.locate((Expression)root.get(StrUtil.toCamelCase(key)),(String)value);
			   	 			
//			   	 			predicate2 = builder.like((Expression)root.get(StrUtil.toCamelCase(key)), value + "%" );
			   	 			
//			   	 			predicate2 = builder.lessThanOrEqualTo( root.get(StrUtil.toCamelCase(key)), (Comparable)value);
			   	 		}else if ("include".equalsIgnoreCase(expr)) {
			   	 			
			   	 			System.out.println(value + "%" + key);
			   	 			

			   	 			
//			   	 			
//			   	 			CriteriaQuery<T> q = builder.createQuery(tc);
//			   	 			
//			   	 			q.select(root).where(builder.isMember(value.toString(), root.<Set<String>>get(StrUtil.toCamelCase(key))));
//			   	 			
//			   	 			
//			   	 			
//			   	 			
//			   	 		query
//				   	      Root<Person> p = query.from(Person.class);
//				   	      q.select(p)
//				   	       .where(cb.isMember("joe",
//				   	                          p.<Set<String>>get("nicknames")));
//				   	 			
				   	 			
			   	 			
			   	 			return builder.like(root.get(StrUtil.toCamelCase(key)), "%" + value + "%");
			   	 			
//			   	 			return builder.isMember(builder.literal(value.toString()), root.<Set<String>>get(StrUtil.toCamelCase(key)));
			   	 			
//			   	 			System.out.println(value + "%");
//			   	 			
//				   	 		Subquery<Integer> subquery = query.subquery(Integer.class);
//			                Root<T> correlated = subquery.correlate(root);
//			                Join<T,String> p = correlated.join(key);
//			                
//			                subquery.select(builder.literal(1));
//			                subquery.where(builder.equal( p ,value));
//			                
//			                return builder.exists(subquery);
//			   	 			


			   	 			
			   	 		}else if("foreign".equalsIgnoreCase(expr)) {
			   	 			
			   	 			predicate2 = builder.equal( root.get(StrUtil.toCamelCase(key)), foreignObject(key, UUID.fromString((String)value)));
			   	 		}else if("null".equalsIgnoreCase(expr)) {
			   	 			
			   	 			boolean b = (boolean)value;
			   	 			
			   	 			if (b) {
			   	 				predicate2 = builder.isNull(root.get(StrUtil.toCamelCase(key)));	
			   	 			}else {
			   	 				
			   	 				System.out.println("b: " + key);
			   	 				
			   	 				predicate2 = builder.isNotNull(root.get(StrUtil.toCamelCase(key)));	
			   	 			}
			   	 			
			   	 			
			   	 		} else {
			   	 			predicate2 = builder.equal( root.get(StrUtil.toCamelCase(key)),  value);
			   	 		}
			   	 		
			   	 		
			   	 		predicates.add(predicate2);
			   	 		
//				   	 	if (strict != null && strict) {
//				   	 		predicates.add(builder.and(predicate1,predicate2));
//				   	 	}else {
//					   	 	
//				   	 		predicates.add(builder.or(predicate1,predicate2));
//					   	}
			   	 	}
		   	 	}
		 	   	 		
	   	 		
				
		   	 	Predicate total = builder.and(predicates.toArray(new Predicate[0]));
				
				
				return total;
			}	
		};
		

		Pageable pageable = PageRequest.of(page, size, Direction.DESC,"createdDate");
		
		
		return this.genericRepository.findAll(spec, pageable);
	    
	}
	

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
	
	public Pagination<T> paginationListOrderBy(String field, int page,int size) {
		

		Pageable pageable = PageRequest.of(page, size, Direction.DESC,StrUtil.toCamelCase(field),"uuid");
	    Page<T> orderPage = genericRepository.findAll(pageable);
	    
	    Pagination<T> pg = new Pagination<T>(page,size,orderPage.getTotalPages(),orderPage.getContent());
	    
	    
		return pg;
	}
	
	
	private Object foreignObject(String foreign, UUID uuid) {
		
		if (foreign == null || uuid ==null ) return null;
		
		Object fObj;
		try {
			Field field = ReflectionUtils.findRequiredField(getClazz(),StrUtil.toCamelCase(foreign));
			Class fClazz = field.getType();
			fObj = fClazz.getDeclaredConstructor().newInstance();
			System.out.println("field class:" + fClazz.getSimpleName() + fClazz.getDeclaredFields());
			
			Field fUuid = ReflectionUtils.findRequiredField(fClazz ,"uuid");
			fUuid.setAccessible(true);
			fUuid.set(fObj, uuid);
			fUuid.setAccessible(false);
			
		} catch (SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		
		
		return fObj;
		
		
	}
	
}
