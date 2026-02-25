package com.clinicanimal.processor.web.controller;

import com.clinicanimal.processor.aplication.service.CitaService;
import com.clinicanimal.processor.domain.model.estado.EstadoCita;
import com.clinicanimal.processor.web.common.ApiResponse;
import com.clinicanimal.processor.web.dto.cita.CitaEncolarDto;
import com.clinicanimal.processor.web.dto.cita.CitaReprogramarDto;
import com.clinicanimal.processor.web.dto.cita.CitaRequestDto;
import com.clinicanimal.processor.web.dto.cita.CitaResponseDto;
import com.clinicanimal.processor.web.dto.cita.CitaUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    // -------------------------------------------------------
    // CONSULTAS
    // -------------------------------------------------------

    @GetMapping
    public ResponseEntity<ApiResponse<List<CitaResponseDto>>> listar() {
        List<CitaResponseDto> citas = citaService.listar();
        return ResponseEntity.ok(ApiResponse.ok("Citas obtenidas correctamente", citas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaResponseDto>> buscarPorId(@PathVariable Long id) {
        CitaResponseDto cita = citaService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.ok("Cita encontrada", cita));
    }

    // GET /api/citas/estado?estado=PROGRAMADA
    @GetMapping("/estado")
    public ResponseEntity<ApiResponse<List<CitaResponseDto>>> listarPorEstado(
            @RequestParam EstadoCita estado) {
        List<CitaResponseDto> citas = citaService.listarPorEstado(estado);
        return ResponseEntity.ok(ApiResponse.ok("Citas en estado " + estado, citas));
    }

    // GET /api/citas/veterinario/{idVeterinario}/bandeja
    @GetMapping("/veterinario/{idVeterinario}/bandeja")
    public ResponseEntity<ApiResponse<List<CitaResponseDto>>> listarBandejaVeterinario(
            @PathVariable Long idVeterinario) {
        List<CitaResponseDto> citas = citaService.listarBandejaVeterinario(idVeterinario);
        return ResponseEntity.ok(ApiResponse.ok("Bandeja del veterinario obtenida", citas));
    }

    // GET /api/citas/mascota/{idMascota}
    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<ApiResponse<List<CitaResponseDto>>> listarPorMascota(
            @PathVariable Long idMascota) {
        List<CitaResponseDto> citas = citaService.listarPorMascota(idMascota);
        return ResponseEntity.ok(ApiResponse.ok("Historial de citas de la mascota", citas));
    }

    // GET /api/citas/cliente/{idCliente}
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<ApiResponse<List<CitaResponseDto>>> listarPorCliente(
            @PathVariable Long idCliente) {
        List<CitaResponseDto> citas = citaService.listarPorCliente(idCliente);
        return ResponseEntity.ok(ApiResponse.ok("Historial de citas del cliente", citas));
    }

    // -------------------------------------------------------
    // CRUD
    // -------------------------------------------------------

    @PostMapping
    public ResponseEntity<ApiResponse<CitaResponseDto>> crear(
            @Valid @RequestBody CitaRequestDto dto) {
        CitaResponseDto cita = citaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cita creada correctamente", cita));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CitaUpdateDto dto) {
        CitaResponseDto cita = citaService.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Cita actualizada correctamente", cita));
    }

    // -------------------------------------------------------
    // CAMBIOS DE ESTADO
    // -------------------------------------------------------

    // PROGRAMADA → EN_COLA
    @PatchMapping("/{id}/encolar")
    public ResponseEntity<ApiResponse<CitaResponseDto>> encolar(
            @PathVariable Long id,
            @Valid @RequestBody CitaEncolarDto dto) {
        CitaResponseDto cita = citaService.encolarCita(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Cita encolada correctamente", cita));
    }

    // EN_COLA → EN_PROGRESO
    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<ApiResponse<CitaResponseDto>> iniciar(@PathVariable Long id) {
        CitaResponseDto cita = citaService.iniciarCita(id);
        return ResponseEntity.ok(ApiResponse.ok("Cita iniciada correctamente", cita));
    }

    // EN_PROGRESO → TERMINADA
    @PatchMapping("/{id}/terminar")
    public ResponseEntity<ApiResponse<CitaResponseDto>> terminar(@PathVariable Long id) {
        CitaResponseDto cita = citaService.terminarCita(id);
        return ResponseEntity.ok(ApiResponse.ok("Cita terminada correctamente", cita));
    }

    // TERMINADA → PAGADA
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<ApiResponse<CitaResponseDto>> pagar(@PathVariable Long id) {
        CitaResponseDto cita = citaService.registrarPago(id);
        return ResponseEntity.ok(ApiResponse.ok("Pago registrado correctamente", cita));
    }

    // PROGRAMADA o EN_COLA → CANCELADO
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<CitaResponseDto>> cancelar(@PathVariable Long id) {
        CitaResponseDto cita = citaService.cancelarCita(id);
        return ResponseEntity.ok(ApiResponse.ok("Cita cancelada correctamente", cita));
    }

    // EN_COLA → PROGRAMADA
    @PatchMapping("/{id}/reprogramar")
    public ResponseEntity<ApiResponse<CitaResponseDto>> reprogramar(
            @PathVariable Long id,
            @Valid @RequestBody CitaReprogramarDto dto) {
        CitaResponseDto cita = citaService.reprogramarCita(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Cita reprogramada correctamente", cita));
    }
}

