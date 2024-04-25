//https://stackoverflow.com/questions/44007188/deserialize-json-with-spring-unresolved-forward-references-jackson-exception
	
package xyz.magicraft.longshort.ssf.utils;


import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;

import jakarta.persistence.EntityManager;

/**
 * @author fta on 20.12.15.
 */

public class EntityIdResolver
        implements ObjectIdResolver {

    private EntityManager entityManager;

    public EntityIdResolver(
            final EntityManager entityManager) {

        this.entityManager = entityManager;

    }

    @Override
    public void bindItem(
            final ObjectIdGenerator.IdKey id,
            final Object pojo) {

    }

    @Override
    public Object resolveId(final ObjectIdGenerator.IdKey id) {

        return this.entityManager.find(id.scope, id.key);
    }

    @Override
    public ObjectIdResolver newForDeserialization(final Object context) {

        return this;
    }

    @Override
    public boolean canUseFor(final ObjectIdResolver resolverType) {

        return false;
    }

}