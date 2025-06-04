package io.github.spring.api_parking_manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AddressModel {

  @Column(name = "street_address", nullable = false)
  private String streetAddress;

  @Column(name = "city", nullable = false)
  private String city;

  @Column(name = "state", nullable = false)
  private String state;

  @Column(name = "postal_code", nullable = false)
  private String postalCode;
}
