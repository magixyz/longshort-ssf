package xyz.magicraft.longshort.ssf.generic2;



import java.util.List;

import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


@Component
public class Generic2Dao<T> extends Generic2Clazz<T>{
	

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    public T loadByField(String field,Object value) {
    	
    	
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    	
    	CriteriaQuery<T> query = cb.createQuery(getClazz());


        Root<T> root = query.from(getClazz());

        Path<T> p = root.get(StrUtil.toCamelCase(field));
        Predicate predicate = cb.equal(p, value);
        query.where(predicate);
    	
    	
    	TypedQuery<T> q = entityManager.createQuery(query);
    	
    	try {
			return q.getSingleResult();
		} catch (NoResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
    	
    }
    
    
    public T loadByForeign(String field,Object value) {
    	
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    	
    	CriteriaQuery<T> query = cb.createQuery(getClazz());


        Root<T> root = query.from(getClazz());

        Path<T> p = root.get(field);
        Predicate predicate = cb.equal(p, value);
        query.where(predicate);
    	
    	
    	TypedQuery<T> q = entityManager.createQuery(query);
    	
    	return q.getSingleResult();
    	
    }
    
    public List<T> listByForeign(String field,Object value) {

    	
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    	
    	CriteriaQuery<T> query = cb.createQuery(getClazz());

    	System.out.println("clazz: " + getClazz().getSimpleName());
    	

        Root<T> root = query.from(getClazz());

        Path<T> p = root.get(StrUtil.toCamelCase(field));
        Predicate predicate = cb.equal(p, value);
        query.where(predicate);
    	
    	
    	TypedQuery<T> q = entityManager.createQuery(query);
    	
    	return q.getResultList();
    	
    }
}