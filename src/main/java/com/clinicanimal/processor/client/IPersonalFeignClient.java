package com.clinicanimal.processor.client;

import com.clinicanimal.processor.client.dto.master.enums.EstadoPersonal;
import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.client.dto.master.common.GenericResponseDto;
import com.clinicanimal.processor.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "clinic-master-service", contextId = "personalClient", url = "${master-service.url}", configuration = FeignConfig.class)
public interface IPersonalFeignClient {

    @GetMapping("/api/personal")
    ResponseEntity<GenericResponseDto<List<PersonalResponseDto>>> listarPersonal();

    @GetMapping("/api/personal/{id}")
    ResponseEntity<GenericResponseDto<PersonalResponseDto>> buscarPersonalPorId(@PathVariable Long id);

    @PutMapping("/api/personal/{id}/estado")
    ResponseEntity<GenericResponseDto<PersonalResponseDto>> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPersonal estado
    );
}
