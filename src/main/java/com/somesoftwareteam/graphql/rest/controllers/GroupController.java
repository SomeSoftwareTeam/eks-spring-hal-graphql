package com.somesoftwareteam.graphql.rest.controllers;

import com.somesoftwareteam.graphql.datasources.auth0.Auth0Wrapper;
import com.somesoftwareteam.graphql.datasources.auth0.Group;
import com.somesoftwareteam.graphql.rest.assemblers.GroupModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ExposesResourceFor(Group.class)
public class GroupController {

    private final Auth0Wrapper auth0Wrapper;
    private final GroupModelAssembler groupModelAssembler;

    public GroupController(Auth0Wrapper auth0Wrapper, GroupModelAssembler groupModelAssembler) {
        this.auth0Wrapper = auth0Wrapper;
        this.groupModelAssembler = groupModelAssembler;
    }

    @GetMapping(value = "/rest/groups")
    public ResponseEntity<?> getGroups() throws IOException {
        List<Group> users = auth0Wrapper.getAuth0AuthorizationExtensionGroups();
        List<EntityModel<Group>> models = users.stream().map(this::convertToEntityModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models));
    }

    @GetMapping(value = "/rest/groups/search/findByNameContains")
    public ResponseEntity<?> findByNameContains(@RequestParam String input) throws IOException {
        List<Group> groups = auth0Wrapper.getAuth0AuthorizationExtensionGroups();
        List<EntityModel<Group>> models = groups.stream()
                .filter(g -> g.getName().contains(input)).map(this::convertToEntityModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models));
    }

    private EntityModel<Group> convertToEntityModel(Group group) {
        return groupModelAssembler.toModel(group);
    }
}
