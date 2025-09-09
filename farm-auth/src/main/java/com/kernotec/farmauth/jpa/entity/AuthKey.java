package com.kernotec.farmauth.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "auth_keys")
public class AuthKey extends BaseAuditEntity {

    @Column(name = "realm", nullable = false)
    private String realm;

    @Column(name = "kid", nullable = false)
    private String kid;

    @Column(name = "private_key", nullable = false, columnDefinition = "TEXT")
    private String privateKey;

    @Column(name = "public_key", nullable = false, columnDefinition = "TEXT")
    private String publicKey;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private boolean active;
}
