package com.somesoftwareteam.graphql.datasources.mysql.handlers;

import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * https://docs.spring.io/spring-data/rest/docs/current/reference/html/#events
 */
@Component
@RepositoryEventHandler
public class GlobalRepositoryEventHandlers {

    private final AuthenticationFacade authenticationFacade;

    public GlobalRepositoryEventHandlers(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @HandleBeforeCreate
    public void handleBeforeCreate(Object entity) {
        setOwnerId(entity);
    }

    private void setOwnerId(Object entity) {
        try {
            Method setOwnerMethod = entity.getClass().getDeclaredMethod("setOwnerId", String.class);
            String ownerId = authenticationFacade.getCurrentPrincipalName();
            setOwnerMethod.invoke(entity, ownerId);
        } catch (NoSuchMethodException e) {
            System.out.println("No owner id property to set on type " + entity.getClass().getName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println("Failed to set owner id property on type " + entity.getClass().getName());
            e.printStackTrace();
        }
    }
}
