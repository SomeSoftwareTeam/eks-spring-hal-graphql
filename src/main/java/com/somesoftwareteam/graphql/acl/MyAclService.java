package com.somesoftwareteam.graphql.acl;

import com.somesoftwareteam.graphql.entities.Fixture;
import com.somesoftwareteam.graphql.entities.Property;
import com.somesoftwareteam.graphql.entities.Verification;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * https://docs.spring.io/spring-security/site/docs/3.0.x/reference/domain-acls.html
 * https://github.com/spring-projects/spring-security/tree/master/acl
 * https://www.baeldung.com/spring-security-acl
 */
@Component
public class MyAclService extends JdbcMutableAclService {

    private final AuthenticationFacade authenticationFacade;
    private final JdbcTemplate jdbcTemplate;

    public MyAclService(AuthenticationFacade authenticationFacade, DataSource dataSource, LookupStrategy lookupStrategy,
                        AclCache aclCache) {
        super(dataSource, lookupStrategy, aclCache);
        this.authenticationFacade = authenticationFacade;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public void createNewSecurityIdentityIfNecessary(String securityIdentity) {
        try {
            readSid(securityIdentity);
        } catch (DataAccessException e) {
            insertSid(securityIdentity);
        }
    }

    @Transactional
    public List<String> getAllSecurityIdentities() {
        return jdbcTemplate.queryForList("select sid from acl_sid", String.class);
    }

    @Transactional
    public List<String> getAllAclClasses() {
        return jdbcTemplate.queryForList("select class from acl_class", String.class);
    }

    @Transactional
    public void createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(Fixture fixture) {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(Fixture.class, fixture.getId());
        String currentPrincipalName = authenticationFacade.getCurrentPrincipalName();
        createNewSecurityIdentityIfNecessary(currentPrincipalName);
        createOrUpdateAccessControlListToIncludeSecurityIdentity(currentPrincipalName, objectIdentity);
    }

    @Transactional
    public void createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(Property property) {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(Property.class, property.getId());
        String currentPrincipalName = authenticationFacade.getCurrentPrincipalName();
        createNewSecurityIdentityIfNecessary(currentPrincipalName);
        createOrUpdateAccessControlListToIncludeSecurityIdentity(currentPrincipalName, objectIdentity);
    }

    @Transactional
    public void createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(Verification verification) {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(Verification.class, verification.getId());
        String currentPrincipalName = authenticationFacade.getCurrentPrincipalName();
        createNewSecurityIdentityIfNecessary(currentPrincipalName);
        createOrUpdateAccessControlListToIncludeSecurityIdentity(currentPrincipalName, objectIdentity);
    }

    private void insertSid(String securityIdentity) {
        jdbcTemplate.update("insert into acl_sid (principal, sid) values (?, ?)", true, securityIdentity);
    }

    private void readSid(String securityIdentity) {
        Long id = jdbcTemplate.queryForObject(
                "select id from acl_sid where sid = ?", new String[]{securityIdentity}, Long.class);
    }

    private void createOrUpdateAccessControlListToIncludeSecurityIdentity(String username, ObjectIdentity objectIdentity) {

        // Prepare the information we'd like in our access control entry (ACE)
        Sid sid = new PrincipalSid(username);

        // Create or update the relevant ACL
        MutableAcl acl;
        try {
            acl = (MutableAcl) readAclById(objectIdentity);
        } catch (NotFoundException nfe) {
            acl = createAcl(objectIdentity);
        }

        // Now grant some permissions via an access control entry (ACE)
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, sid, true);
        updateAcl(acl);
    }
}
