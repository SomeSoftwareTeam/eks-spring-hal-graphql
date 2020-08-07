package com.somesoftwareteam.graphql.rest.controllers;

import com.somesoftwareteam.graphql.datasources.aws.AmazonS3Wrapper;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Document;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.DocumentRepository;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.PropertyRepository;
import com.somesoftwareteam.graphql.rest.assemblers.DocumentModelAssembler;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
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

import java.util.Objects;

/**
 * https://spring.io/guides/gs/uploading-files/
 */
@RepositoryRestController
public class DocumentController {

    private final AmazonS3Wrapper amazonWrapper;
    private final AuthenticationFacade authenticationFacade;
    private final DocumentModelAssembler documentModelAssembler;
    private final DocumentRepository documentRepository;
    private final PropertyRepository propertyRepository;

    public DocumentController(AmazonS3Wrapper amazonWrapper, AuthenticationFacade authenticationFacade,
                              DocumentModelAssembler documentModelAssembler, DocumentRepository documentRepository,
                              PropertyRepository propertyRepository) {
        this.amazonWrapper = amazonWrapper;
        this.authenticationFacade = authenticationFacade;
        this.documentModelAssembler = documentModelAssembler;
        this.documentRepository = documentRepository;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_write:documents')")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "propertyId", required = false) Long propertyId,
                                        @RequestParam("name") String name,
                                        @RequestParam("description") String description) {

        String url = this.amazonWrapper.uploadFile(file);

        Property property = Objects.isNull(propertyId)
                ? null
                : propertyRepository.findById(propertyId).orElseThrow(ResourceNotFoundException::new);

        String ownerId = authenticationFacade.getCurrentPrincipalName();

        Document document = new Document(name, ownerId, url, description, JacksonUtil.toJsonNode("{}"), property);

        documentRepository.save(document);

        EntityModel<Document> model = documentModelAssembler.toModel(document);

        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
}
