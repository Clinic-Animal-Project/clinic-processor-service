package com.clinicanimal.processor.web.controller;

import com.clinicanimal.processor.aplication.service.HorarioService;
import com.clinicanimal.processor.web.common.ApiResponse;
import com.clinicanimal.processor.web.dto.horario.HorarioRequestDto;
import com.clinicanimal.processor.web.dto.horario.HorarioResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;

    // -------------------------------------------------------
    // CONSULTAS
    // -------------------------------------------------------

    @GetMapping
    public ResponseEntity<ApiResponse<List<HorarioResponseDto>>> listar() {
        List<HorarioResponseDto> horarios = horarioService.listar();
        return ResponseEntity.ok(ApiResponse.ok("Horarios obtenidos correctamente", horarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HorarioResponseDto>> buscarPorId(@PathVariable Long id) {
        HorarioResponseDto horario = horarioService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.ok("Horario encontrado", horario));
    }

    // GET /api/horarios/personal/{personalId}
    @GetMapping("/personal/{personalId}")
    public ResponseEntity<ApiResponse<List<HorarioResponseDto>>> listarPorPersonal(
            @PathVariable Long personalId) {
        List<HorarioResponseDto> horarios = horarioService.listarPorPersonal(personalId);
        return ResponseEntity.ok(ApiResponse.ok("Horarios del personal obtenidos", horarios));
    }

    // -------------------------------------------------------
    // CRUD
    // -------------------------------------------------------

    @PostMapping
    public ResponseEntity<ApiResponse<HorarioResponseDto>> crear(
            @Valid @RequestBody HorarioRequestDto dto) {
        HorarioResponseDto horario = horarioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Horario creado correctamente", horario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HorarioResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HorarioRequestDto dto) {
        HorarioResponseDto horario = horarioService.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Horario actualizado correctamente", horario));
    }

    // PATCH en lugar de DELETE para soft-delete (desactivar)
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<Void>> desactivar(@PathVariable Long id) {
        horarioService.desactivar(id);
        return ResponseEntity.ok(ApiResponse.ok("Horario desactivado correctamente", null));
    }
}
