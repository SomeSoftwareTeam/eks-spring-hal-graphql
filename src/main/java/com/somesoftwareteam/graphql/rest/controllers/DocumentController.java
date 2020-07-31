package com.somesoftwareteam.graphql.rest.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.somesoftwareteam.graphql.datasources.aws.AmazonWrapper;
import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Document;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.PropertyRepository;
import com.somesoftwareteam.graphql.rest.assemblers.DocumentModelAssembler;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * https://spring.io/guides/gs/uploading-files/
 */
@RepositoryRestController
public class DocumentController {

    private final AmazonWrapper amazonWrapper;
    private final DocumentModelAssembler documentModelAssembler;
    private final EntityCreator entityCreator;
    private final MyAclService myAclService;
    private final PropertyRepository propertyRepository;

    public DocumentController(AmazonWrapper amazonWrapper, DocumentModelAssembler documentModelAssembler,
                              EntityCreator entityCreator, MyAclService myAclService,
                              PropertyRepository propertyRepository) {
        this.amazonWrapper = amazonWrapper;
        this.documentModelAssembler = documentModelAssembler;
        this.entityCreator = entityCreator;
        this.myAclService = myAclService;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_write:documents')")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("propertyId") Long propertyId,
                                        @RequestParam("name") String name,
                                        @RequestParam("description") String description) {
        String url = this.amazonWrapper.uploadFile(file);
        Property property = propertyRepository.findById(propertyId).orElseThrow(ResourceNotFoundException::new);
        Document document = new Document(name, null, url, description, JacksonUtil.toJsonNode("{}"), property);
        Document newDocument = entityCreator.setOwnerAndPersistEntity(document);
        myAclService.createAccessControlList(Document.class, newDocument.getId());
        EntityModel<Document> model = documentModelAssembler.toModel(document);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
}
