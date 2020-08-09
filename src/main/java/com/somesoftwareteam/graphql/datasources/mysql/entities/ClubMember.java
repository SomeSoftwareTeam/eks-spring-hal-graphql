package com.somesoftwareteam.graphql.datasources.mysql.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * https://thorben-janssen.com/generate-uuids-primary-keys-hibernate/
 * https://www.maxenglander.com/2017/09/01/optimized-uuid-with-hibernate.html
 */
@Entity
@Table(name = "club_member")
public class ClubMember {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    private Club club;

    @Column(name = "club_id")
    private UUID clubId;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    private String memberId;

    public ClubMember() {
    }

    public ClubMember(UUID clubId, String memberId) {
        this.clubId = clubId;
        this.memberId = memberId;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public UUID getClubId() {
        return clubId;
    }

    public void setClubId(UUID clubId) {
        this.clubId = clubId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
