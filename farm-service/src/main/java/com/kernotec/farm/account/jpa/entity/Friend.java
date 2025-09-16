package com.kernotec.farm.account.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.parametric.jpa.entity.FriendState;
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
@Table(name = "friends")
public class Friend extends BaseAuditEntity {

    @Column(name = "accepted_at", nullable = false)
    private ZonedDateTime acceptedAt;

    @Column(name = "ended_at")
    private ZonedDateTime endedAt;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Account account;

    @Column(name = "friend_account_id", nullable = false)
    private UUID friendAccountId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "friend_account_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Account friendAccount;

    @Column(name = "friend_state_id", nullable = false)
    private UUID friendStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "friend_state_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private FriendState friendState;
}
