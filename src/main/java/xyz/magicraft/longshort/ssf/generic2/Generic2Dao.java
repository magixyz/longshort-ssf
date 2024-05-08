package xyz.magicraft.longshort.ssf.generic2;



import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
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

    
    T loadByForeign(String field,Object value) {
    	
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    	
    	CriteriaQuery<T> query = cb.createQuery(clazz);


        Root<T> root = query.from(clazz);

        Path<T> p = root.get(field);
        Predicate predicate = cb.equal(p, value);
        query.where(predicate);
    	
    	
    	TypedQuery<T> q = entityManager.createQuery(query);
    	
    	return q.getSingleResult();
    	
    }
}