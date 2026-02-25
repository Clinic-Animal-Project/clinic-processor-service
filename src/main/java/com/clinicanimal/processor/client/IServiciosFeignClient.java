package com.clinicanimal.processor.client;

import com.clinicanimal.processor.client.dto.master.servicios.ServiciosResponseDto;
import com.clinicanimal.processor.client.dto.master.common.GenericResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "clinic-master-service", contextId = "serviciosClient", url = "${master-service.url}")
public interface IServiciosFeignClient {

    @GetMapping("/api/servicios")
    ResponseEntity<GenericResponseDto<List<ServiciosResponseDto>>> listarServicios();

    @GetMapping("/api/servicios/{id}")
    ResponseEntity<GenericResponseDto<ServiciosResponseDto>> buscarServicioPorId(@PathVariable Long id);
}
