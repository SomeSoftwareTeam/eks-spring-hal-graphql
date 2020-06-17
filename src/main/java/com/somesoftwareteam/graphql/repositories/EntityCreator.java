package com.somesoftwareteam.graphql.repositories;

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
        setOwnerPropertyOfEntityIfNecessary(entity);
        entityManager.persist(entity);
        return entity;
    }

    private <T> void setOwnerPropertyOfEntityIfNecessary(T entity) {

        try {
            Method setOwnerMethod = entity.getClass().getDeclaredMethod("setOwner", String.class);
            String owner = authenticationFacade.getCurrentPrincipalName();
            setOwnerMethod.invoke(entity, owner);
        } catch (NoSuchMethodException e) {
            System.out.println("No owner property to set on type " + entity.getClass().getName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println("Failed to set owner property on type " + entity.getClass().getName());
            e.printStackTrace();
        }
    }
}
