package io.github.spring.api_parking_manager.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_enterprise")
@Data
@EntityListeners(AuditingEntityListener.class)

public class EnterpriseModel {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "cnpj", nullable = false)
  @CNPJ
  private String cnpj;
  
  @Column(name = "address", nullable = false)
  private AddressModel address;

  @Column(name = "motorcyle_spaces", nullable = false)
  private Integer motorcycleSpaces;

  @Column(name = "car_spaces", nullable = false)
  private Integer carSpaces;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
