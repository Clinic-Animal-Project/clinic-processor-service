package com.clinicanimal.processor.client;

import com.clinicanimal.processor.client.dto.master.roles.RolesResponseDto;
import com.clinicanimal.processor.client.dto.master.common.GenericResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "clinic-master-service", contextId = "rolesClient", url = "${master-service.url}")
public interface IRolesFeignClient {

    @GetMapping("/api/roles")
    ResponseEntity<GenericResponseDto<List<RolesResponseDto>>> listarRoles();

    @GetMapping("/api/roles/{id}")
    ResponseEntity<GenericResponseDto<RolesResponseDto>> buscarRolPorId(@PathVariable Long id);
}
