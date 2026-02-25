package com.clinicanimal.processor.web.controller;

import com.clinicanimal.processor.aplication.service.AsistenciaService;
import com.clinicanimal.processor.domain.model.estado.EstadoAsistencia;
import com.clinicanimal.processor.web.common.ApiResponse;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaSalidaDto;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaRequestDto;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    // -------------------------------------------------------
    // CONSULTAS
    // -------------------------------------------------------

    @GetMapping
    public ResponseEntity<ApiResponse<List<AsistenciaResponseDto>>> listar() {
        List<AsistenciaResponseDto> asistencias = asistenciaService.listar();
        return ResponseEntity.ok(ApiResponse.ok("Asistencias obtenidas correctamente", asistencias));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AsistenciaResponseDto>> buscarPorId(@PathVariable Long id) {
        AsistenciaResponseDto asistencia = asistenciaService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.ok("Asistencia encontrada", asistencia));
    }

    // GET /api/asistencias/horario/{horarioId}
    @GetMapping("/horario/{horarioId}")
    public ResponseEntity<ApiResponse<List<AsistenciaResponseDto>>> listarPorHorario(
            @PathVariable Long horarioId) {
        List<AsistenciaResponseDto> asistencias = asistenciaService.listarPorHorario(horarioId);
        return ResponseEntity.ok(ApiResponse.ok("Asistencias del horario obtenidas", asistencias));
    }

    // GET /api/asistencias/fecha?fecha=2025-01-15
    @GetMapping("/fecha")
    public ResponseEntity<ApiResponse<List<AsistenciaResponseDto>>> listarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<AsistenciaResponseDto> asistencias = asistenciaService.listarPorFecha(fecha);
        return ResponseEntity.ok(ApiResponse.ok("Asistencias de la fecha " + fecha, asistencias));
    }

    // -------------------------------------------------------
    // REGISTRO
    // -------------------------------------------------------

    @PostMapping
    public ResponseEntity<ApiResponse<AsistenciaResponseDto>> registrar(
            @Valid @RequestBody AsistenciaRequestDto dto) {
        AsistenciaResponseDto asistencia = asistenciaService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Asistencia registrada correctamente", asistencia));
    }

    // PATCH /api/asistencias/{id}/salida
    @PatchMapping("/{id}/salida")
    public ResponseEntity<ApiResponse<AsistenciaResponseDto>> registrarSalida(
            @PathVariable Long id,
            @Valid @RequestBody AsistenciaSalidaDto dto) {
        AsistenciaResponseDto asistencia = asistenciaService.registrarSalida(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Hora de salida registrada correctamente", asistencia));
    }

    // -------------------------------------------------------
    // CAMBIO DE ESTADO MANUAL
    // -------------------------------------------------------

    // PATCH /api/asistencias/{id}/estado?estado=FUERA_DE_SERVICIO
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<AsistenciaResponseDto>> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoAsistencia estado) {
        AsistenciaResponseDto asistencia = asistenciaService.actualizarEstado(id, estado);
        return ResponseEntity.ok(ApiResponse.ok("Estado de asistencia actualizado a " + estado, asistencia));
    }
}
