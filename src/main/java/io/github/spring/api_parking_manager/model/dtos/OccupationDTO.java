package io.github.spring.api_parking_manager.model.dtos;

public record OccupationDTO(
  int totalVacancies,
  long vacanciesFilled,
  long freeVacancies
) {
  public OccupationDTO(int totalVacancies, long vacanciesFilled) {
    this(totalVacancies, vacanciesFilled, totalVacancies - vacanciesFilled);
  }
}
