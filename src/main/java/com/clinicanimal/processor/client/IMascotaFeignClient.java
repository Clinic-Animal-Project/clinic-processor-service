package com.clinicanimal.processor.client;

import com.clinicanimal.processor.client.dto.master.mascota.MascotaResponseDto;
import com.clinicanimal.processor.client.dto.master.common.GenericResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "clinic-master-service", contextId = "mascotaClient", url = "${master-service.url}")
public interface IMascotaFeignClient {

    @GetMapping("/api/mascotas")
    ResponseEntity<GenericResponseDto<List<MascotaResponseDto>>> listarMascotas();

    @GetMapping("/api/mascotas/{id}")
    ResponseEntity<GenericResponseDto<MascotaResponseDto>> buscarMascotaPorId(@PathVariable Long id);
}
