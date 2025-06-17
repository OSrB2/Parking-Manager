package io.github.spring.api_parking_manager.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_movements")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MovementsModel {
  
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "vehicle_id", nullable = false)
  private VehicleModel vehicle;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "enterprise_id", nullable = false)
  private EnterpriseModel enterprise;
  
  @Column(name = "entry_time", nullable = false)
  private LocalDateTime entryTime;
  
  @Column(name = "departure_time")
  private LocalDateTime departureTime;
  
  @Column(name = "status", nullable = false)
  private Status status;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private Vehicle type;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
