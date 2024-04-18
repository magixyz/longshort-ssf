package xyz.magicraft.longshort.ssf.framework.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;


public interface IGenericRepository<T> extends CrudRepository<T, UUID> {

	

  Page<T> findAll( Pageable pageable);
  

  Page<T> findAll( Specification<T> spec, Pageable pageable);
  

  List<T> findAll(Specification<T> spec);
  
  
//  @Query(value = "select * from ?1 where ?2 = ?3 ",nativeQuery = true)		  
//  List<T> findByForeignUuid(String table, String foreign,UUID uuid);
}