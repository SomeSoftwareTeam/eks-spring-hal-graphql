package com.somesoftwareteam.graphql.graphql;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Document;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.DocumentRepository;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.PropertyRepository;
import com.somesoftwareteam.graphql.datasources.mysql.specification.SpecificationBuilder;
import io.leangen.graphql.annotations.GraphQLId;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.somesoftwareteam.graphql.graphql.ResolverUtils.createPageRequest;
import static com.somesoftwareteam.graphql.graphql.ResolverUtils.createSort;

/**
 * https://github.com/marmelab/react-admin/tree/master/packages/ra-data-graphql-simple
 */
@Service
@GraphQLApi
@PreAuthorize("hasAuthority('SCOPE_read:documents')")
public class DocumentResolver {

    private final EntityCreator entityCreator;
    private final MyAclService myAclService;
    private final DocumentRepository documentRepository;
    private final PropertyRepository propertyRepository;
    private final SpecificationBuilder<Document> specificationBuilder;

    public DocumentResolver(EntityCreator entityCreator,
                            MyAclService myAclService, DocumentRepository documentRepository,
                            PropertyRepository propertyRepository, SpecificationBuilder<Document> specificationBuilder) {
        this.entityCreator = entityCreator;
        this.myAclService = myAclService;
        this.documentRepository = documentRepository;
        this.propertyRepository = propertyRepository;
        this.specificationBuilder = specificationBuilder;
    }

    @GraphQLQuery(name = "Document", description = "Get document by primary id")
    public Document document(@GraphQLId @GraphQLNonNull Long id) {
        return documentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @GraphQLQuery(name = "allDocuments", description = "Get document records")
    public List<Document> allDocuments(Integer page, Integer perPage, String sortField, String sortOrder,
                                       DocumentFilter filter) {
        Specification<Document> spec = specificationBuilder.createSpecFromFilter(filter);
        Sort sort = createSort(sortOrder, sortField);
        PageRequest pageRequest = createPageRequest(page, perPage, sort);
        return documentRepository.findAll(spec, pageRequest).getContent();
    }

    @GraphQLQuery(name = "_allDocumentsMeta", description = "Get document records metadata")
    public ListMetadata allDocumentsMeta(Integer page, Integer perPage, DocumentFilter filter) {
        Specification<Document> spec = specificationBuilder.createSpecFromFilter(filter);
        PageRequest pageRequest = createPageRequest(page, perPage);
        Long count = documentRepository.findAll(spec, pageRequest).getTotalElements();
        return new ListMetadata(count);
    }

//    @PreAuthorize("hasAuthority('SCOPE_write:documents')")
//    @GraphQLMutation(name = "updateDocument", description = "Update a document record")
//    public Document updateDocument(@GraphQLId @GraphQLNonNull Long id, String name, String description,
//                                   JsonNode attributes) {
//        Document document = documentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
//        document.setName(name);
//        document.setDescription(description);
//        document.setAttributes(attributes);
//        documentRepository.save(document);
//        return document;
//    }
//
//    @PreAuthorize("hasAuthority('SCOPE_write:documents')")
//    @GraphQLMutation(name = "deleteDocument", description = "Delete a document record")
//    public Document deleteDocument(@GraphQLId @GraphQLNonNull Long id) {
//        Document document = documentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
//        documentRepository.deleteById(id);
//        return document;
//    }
}


