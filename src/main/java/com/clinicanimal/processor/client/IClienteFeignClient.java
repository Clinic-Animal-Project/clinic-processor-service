package com.clinicanimal.processor.client;

import com.clinicanimal.processor.client.dto.master.cliente.ClienteResponseDto;
import com.clinicanimal.processor.client.dto.master.common.GenericResponseDto;
import com.clinicanimal.processor.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "clinic-master-service", contextId = "clienteClient", url = "${master-service.url}", configuration = FeignConfig.class)
public interface IClienteFeignClient {

    @GetMapping("/api/clientes")
    ResponseEntity<GenericResponseDto<List<ClienteResponseDto>>> listarClientes();

    @GetMapping("/api/clientes/{id}")
    ResponseEntity<GenericResponseDto<ClienteResponseDto>> buscarClientePorId(@PathVariable Long id);
}
