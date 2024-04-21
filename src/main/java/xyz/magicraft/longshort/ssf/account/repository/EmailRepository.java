package xyz.magicraft.longshort.ssf.account.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import xyz.magicraft.longshort.ssf.account.model.Email;



public interface EmailRepository extends CrudRepository<Email,UUID> {

	
}
