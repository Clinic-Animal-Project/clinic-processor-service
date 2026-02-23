package com.clinicanimal.processor.client;

import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.client.dto.master.common.GenericResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "clinic-master-service", url = "localhost:8080")
public interface IPersonalFeignClient {

    @GetMapping("/api/personal")
    ResponseEntity<GenericResponseDto<List<PersonalResponseDto>>> listarPersonal();

    @GetMapping("/api/personal/{id}")
    ResponseEntity<GenericResponseDto<PersonalResponseDto>> buscarPersonalPorId(@PathVariable Long id);
}
