package com.somesoftwareteam.graphql.datasources.mysql.handlers;

import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * https://docs.spring.io/spring-data/rest/docs/current/reference/html/#events
 */
@Component
@RepositoryEventHandler
public class GlobalRepositoryEventListeners {

    private final AuthenticationFacade authenticationFacade;
    private final Validator validator;

    public GlobalRepositoryEventListeners(AuthenticationFacade authenticationFacade, Validator validator) {
        this.authenticationFacade = authenticationFacade;
        this.validator = validator;
    }

    @HandleBeforeCreate
    public void handleEntitySave(Object entity) {
        setOwnerId(entity);
//        validate(entity);
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

//    private void validate(Object entity) {
//        Set<ConstraintViolation<Object>> violations = validator.validate(entity);
//        violations.forEach(v -> System.out.println(v.getMessage()));
//    }
}
