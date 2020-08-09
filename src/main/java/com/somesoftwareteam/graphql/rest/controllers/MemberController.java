package com.somesoftwareteam.graphql.rest.controllers;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.somesoftwareteam.graphql.datasources.auth0.Auth0Wrapper;
import com.somesoftwareteam.graphql.datasources.auth0.Member;
import com.somesoftwareteam.graphql.rest.assemblers.MemberModelAssembler;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@ExposesResourceFor(Member.class)
public class MemberController {

    private final Auth0Wrapper auth0Wrapper;
    private final ModelMapper modelMapper;
    private final MemberModelAssembler memberModelAssembler;

    public MemberController(Auth0Wrapper auth0Wrapper, ModelMapper modelMapper,
                            MemberModelAssembler memberModelAssembler) {
        this.auth0Wrapper = auth0Wrapper;
        this.modelMapper = modelMapper;
        this.memberModelAssembler = memberModelAssembler;
    }

    @GetMapping(value = "/rest/members")
    public ResponseEntity<?> getMembers() throws Auth0Exception {
        List<User> users = auth0Wrapper.getAuth0CoreUsers();
        List<EntityModel<Member>> models = users.stream().map(this::convertToEntityModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models));
    }

    @GetMapping(value = "/rest/members/search/findByNameContains")
    public ResponseEntity<?> getMembers(@RequestParam String input) throws Auth0Exception {
        List<User> users = auth0Wrapper.searchAuth0CoreUsers(input);
        List<EntityModel<Member>> models = users.stream().map(this::convertToEntityModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models));
    }

    private EntityModel<Member> convertToEntityModel(User user) {
        Member member = modelMapper.map(user, Member.class);
        if (user.getIdentities().size() > 0) member.setProvider(user.getIdentities().get(0).getProvider());
        return memberModelAssembler.toModel(member);
    }
}
