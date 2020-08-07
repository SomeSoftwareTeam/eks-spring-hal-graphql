package com.somesoftwareteam.graphql.datasources.mysql.repositories;

import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class EntityCreator {

    private final EntityManager entityManager;
    private final AuthenticationFacade authenticationFacade;

    public EntityCreator(AuthenticationFacade authenticationFacade, EntityManager entityManager) {
        this.authenticationFacade = authenticationFacade;
        this.entityManager = entityManager;
    }

    @Transactional
    public <T> T persistEntity(T entity) {
        setOwnerOfEntityIfNecessary(entity);
        entityManager.persist(entity);
        return entity;
    }

    private <T> void setOwnerOfEntityIfNecessary(T entity) {

//        try {
//            Method setOwnerMethod = entity.getClass().getDeclaredMethod("setOwnerId", String.class);
//            String ownerId = authenticationFacade.getCurrentPrincipalName();
//            setOwnerMethod.invoke(entity, ownerId);
//        } catch (NoSuchMethodException e) {
//            System.out.println("No owner property to set on type " + entity.getClass().getName());
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            System.out.println("Failed to set owner property on type " + entity.getClass().getName());
//            e.printStackTrace();
//        }
    }
}
