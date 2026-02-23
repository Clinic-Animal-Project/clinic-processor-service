package com.clinicanimal.processor.client.dto.master.common;

public record GenericResponseDto<T>(
        Boolean success,
        String mensaje,
        T data
) {}
