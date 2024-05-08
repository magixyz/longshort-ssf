package xyz.magicraft.longshort.ssf.generic2;


import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import xyz.magicraft.longshort.ssf.base.BaseModel;

@NoRepositoryBean
public interface Generic2Repository<T> extends CrudRepository<T, UUID> {

	

  Page<T> findAll( Pageable pageable);
  

  Page<T> findAll( Specification<T> spec, Pageable pageable);
  

  List<T> findAll(Specification<T> spec);
  
  
//  @Query(value = "select * from ?1 where ?2 = ?3 ",nativeQuery = true)		  
//  List<T> findByForeignUuid(String table, String foreign,UUID uuid);
}