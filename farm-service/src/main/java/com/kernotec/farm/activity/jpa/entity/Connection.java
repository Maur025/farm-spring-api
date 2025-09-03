package com.kernotec.farm.activity.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.entity.RequestState;
import com.kernotec.farm.activity.jpa.enums.FriendActionEnum;
import com.kernotec.farm.activity.jpa.enums.FriendTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "connections")
public class Connection extends BaseAuditEntity {

    @Column(name = "potential_friend_account_id", nullable = false)
    private UUID potentialFriendAccountId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "potential_friend_account_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    private Account potentialFriendAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private FriendActionEnum action;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FriendTypeEnum type;

    @Column(name = "request_state_id", nullable = false)
    private UUID requestStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_state_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private RequestState requestState;

    @Column(name = "activity_id", nullable = false)
    private UUID activityId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Activity activity;

    @Column(name = "activity_type_id", nullable = false)
    private UUID activityTypeId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_type_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private ActivityType activityType;
}
