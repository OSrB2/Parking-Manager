package io.github.spring.api_parking_manager.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.spring.api_parking_manager.exception.ResponseError;
import io.github.spring.api_parking_manager.model.dtos.MovementsRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.MovementsResponseDTO;
import io.github.spring.api_parking_manager.service.MovementsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
@Tag(name = "Movements")
public class MovementsController {
  
  private final MovementsService movementsService;

  @PostMapping
  @Operation(summary = "Register a new movement", description = "Create and register a new movement with a vehicle and a enterprise in the system.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Movement successfully registered",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = MovementsRequestDTO.class)
      )),
    @ApiResponse(responseCode = "400", description = "Bad request",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "422", description = "Validation error",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "404", description = "Vehicle or Enterprise not found with the specified ID.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<MovementsResponseDTO> register(@RequestBody @Valid MovementsRequestDTO requestDTO) {
    MovementsResponseDTO response = movementsService.registerEntry(
        requestDTO.vehicleId(),
        requestDTO.enterpriseId()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

  @GetMapping
  @Operation(summary = "List all movements", description = "Retrieve a list of all movements currently registered in the system.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of movements sccessfully retrieved.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = MovementsResponseDTO.class)
      )),
    @ApiResponse(responseCode = "400", description = "Bad request",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "404", description = "Not found",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<List<MovementsResponseDTO>> listAll() {
    return ResponseEntity.ok(movementsService.listAllMovements());
  }

  @GetMapping("/active")
  @Operation(summary = "List all active movements", description = "Retrieve a list of all active movements currently registered in the system.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of active movements sccessfully retrieved.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = MovementsResponseDTO.class)
      )),
    @ApiResponse(responseCode = "400", description = "Bad request",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "404", description = "Not found",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<List<MovementsResponseDTO>> listAllActive() {
    return ResponseEntity.ok(movementsService.listAllActiveMovements());
  }

  @GetMapping("/finished")
  @Operation(summary = "List all finished movements", description = "Retrieve a list of all finished movements currently registered in the system.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of finished movements sccessfully retrieved.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = MovementsResponseDTO.class)
      )),
    @ApiResponse(responseCode = "400", description = "Bad request",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "404", description = "Not found",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<List<MovementsResponseDTO>> listAllFinished() {
    return ResponseEntity.ok(movementsService.listAllFinishedMovements());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find a movement by ID", description = "Returns detailed information of a specific movement identified by its unique UUID.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Movement retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = MovementsResponseDTO.class)
      )),
    @ApiResponse(responseCode = "400", description = "Bad request",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "404", description = "Movement not found with the specified ID.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<Optional<MovementsResponseDTO>> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(movementsService.findMovementById(id));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Record the closure of a movement ", description = "Record the closure of a specific movement identified by its unique UUID.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Closing and exit registered successfully.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = MovementsResponseDTO.class)
      )),
    @ApiResponse(responseCode = "400", description = "Bad request",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "404", description = "Movement not found with the specified ID.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<MovementsResponseDTO> exitRegister(@PathVariable UUID id) {
    return ResponseEntity.ok(movementsService.registerExit(id));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete movement by ID", description = "In this endpoint it is possible to delete a movement, jus by informing the ID in the URL.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Movement delete successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = MovementsResponseDTO.class)
      )),
    @ApiResponse(responseCode = "400", description = "It is not allowed to delete an active movement.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "404", description = "Movement not found with the specified ID.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<Void> deleteMovements(@PathVariable UUID id) {
    movementsService.deleteMovementById(id);
    return ResponseEntity.noContent().build();
  }
}
