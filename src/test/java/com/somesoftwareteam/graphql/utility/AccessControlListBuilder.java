package com.somesoftwareteam.graphql.utility;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AccessControlListBuilder {

    @Autowired
    private MyAclService myAclService;

    @Transactional
    public AccessControlListBuilder configureAccessControlList(String username, Class<?> entityClass, Long entityId) {

        myAclService.createNewSecurityIdentityIfNecessary(username);

        // Prepare the information we'd like in our access control entry (ACE)
        ObjectIdentity oi = new ObjectIdentityImpl(entityClass, entityId);
        Sid sid = new PrincipalSid(username);

        // Create or update the relevant ACL
        MutableAcl acl;
        try {
            acl = (MutableAcl) myAclService.readAclById(oi);
        } catch (NotFoundException nfe) {
            acl = myAclService.createAcl(oi);
        }

        // Now grant some permissions via an access control entry (ACE)
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, sid, true);
        myAclService.updateAcl(acl);

        return this;
    }
}
