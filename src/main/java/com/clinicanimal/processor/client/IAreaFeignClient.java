package com.clinicanimal.processor.client;

import com.clinicanimal.processor.client.dto.master.area.AreaResponseDto;
import com.clinicanimal.processor.client.dto.master.common.GenericResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "clinic-master-service", contextId = "areaClient", url = "${master-service.url}")
public interface IAreaFeignClient {

    @GetMapping("/api/areas")
    ResponseEntity<GenericResponseDto<List<AreaResponseDto>>> listarAreas();

    @GetMapping("/api/areas/{id}")
    ResponseEntity<GenericResponseDto<AreaResponseDto>> buscarAreaPorId(@PathVariable Long id);
}
