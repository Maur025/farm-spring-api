package com.kernotec.farm.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "accounts")
public class Account extends BaseAuditEntity {

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "observation", length = 500)
  private String observation;

  @Column(name = "person_id", nullable = false)
  private UUID personId;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id", referencedColumnName = "id", insertable = false, updatable = false)
  private Person person;

  @Column(name = "chip_id", nullable = false)
  private UUID chipId;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "chip_id", referencedColumnName = "id", insertable = false, updatable = false)
  private Chip chip;

  @Column(name = "social_network_id", nullable = false)
  private UUID socialNetworkId;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "social_network_id", referencedColumnName = "id", insertable = false, updatable = false)
  private SocialNetwork socialNetwork;
}
