package com.somesoftwareteam.graphql.rest.controllers;

import com.somesoftwareteam.graphql.datasources.aws.AmazonWrapper;
import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Document;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.rest.assemblers.DocumentModelAssembler;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RepositoryRestController
public class DocumentController {

    private final AmazonWrapper amazonWrapper;
    private final MyAclService myAclService;
    private final EntityCreator entityCreator;
    private final DocumentModelAssembler documentModelAssembler;

    public DocumentController(AmazonWrapper amazonWrapper, MyAclService myAclService, EntityCreator entityCreator,
                              DocumentModelAssembler documentModelAssembler) {
        this.amazonWrapper = amazonWrapper;
        this.myAclService = myAclService;
        this.entityCreator = entityCreator;
        this.documentModelAssembler = documentModelAssembler;
    }

    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_write:documents')")
    public ResponseEntity<?> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        String url = this.amazonWrapper.uploadFile(file);
        Document document = new Document(null, url, JacksonUtil.toJsonNode("{}"));
        Document newDocument = entityCreator.setOwnerAndPersistEntity(document);
        myAclService.createAccessControlList(Document.class, newDocument.getId());
        EntityModel<Document> model = documentModelAssembler.toModel(document);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
}
