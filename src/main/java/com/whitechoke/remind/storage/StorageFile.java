package com.whitechoke.remind.storage;

import lombok.Builder;

import java.nio.file.Path;

@Builder
public record StorageFile(
        Long id,
        Path path
) {
}
