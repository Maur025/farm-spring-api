package com.kernotec.farmauth.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
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
@Table(name = "tokens")
public class Token extends BaseAuditEntity {

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "token_id", nullable = false)
    private UUID tokenId;

    @Column(name = "issued_at", nullable = false)
    private ZonedDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    private ZonedDateTime expiresAt;

    @Column(name = "expires_in", nullable = false)
    private Long expiresIn;

    @Column(name = "revoked", nullable = false, columnDefinition = "boolean default false")
    private Boolean revoked = false;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private User user;
}
