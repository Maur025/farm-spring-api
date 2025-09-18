package com.kernotec.farm.account.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.account.jpa.enums.AccountFollowProfileStateEnum;
import com.kernotec.farm.activity.jpa.entity.Profile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "account_follow_profiles")
public class AccountFollowProfile extends BaseAuditEntity {

    @Column(name = "followed_at", nullable = false)
    private ZonedDateTime followedAt;

    @Column(name = "unfollowed_at")
    private ZonedDateTime unfollowedAt;

    @Column(name = "follow_state", nullable = false)
    private AccountFollowProfileStateEnum followState;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Account account;

    @Column(name = "profile_id")
    private UUID profileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Profile profile;
}
