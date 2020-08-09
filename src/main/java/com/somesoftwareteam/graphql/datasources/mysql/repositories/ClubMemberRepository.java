package com.somesoftwareteam.graphql.datasources.mysql.repositories;

import com.somesoftwareteam.graphql.datasources.mysql.entities.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Security provides Spring Data integration that allows referring to the current user within your queries:
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#data
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#common-expressions
 */
@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, UUID>, JpaSpecificationExecutor<ClubMember> {
}
