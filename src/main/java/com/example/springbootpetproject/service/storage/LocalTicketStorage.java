package com.example.springbootpetproject.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/** Билеты в локальной папке: для запуска на своей машине и в docker compose. */
@Component
@ConditionalOnProperty(name = "app.tickets.storage", havingValue = "local", matchIfMissing = true)
public class LocalTicketStorage implements TicketStorage {

    private final Path root;

    public LocalTicketStorage(@Value("${pdf.path}") String pdfPath) {
        this.root = Path.of(pdfPath);
    }

    @Override
    public boolean exists(String key) {
        return Files.exists(resolve(key));
    }

    @Override
    public void save(String key, byte[] pdf) {
        try {
            Path file = resolve(key);
            Files.createDirectories(file.getParent());
            Files.write(file, pdf);
        } catch (IOException e) {
            throw new UncheckedIOException("cannot save ticket " + key, e);
        }
    }

    @Override
    public byte[] load(String key) {
        try {
            return Files.readAllBytes(resolve(key));
        } catch (IOException e) {
            throw new UncheckedIOException("cannot read ticket " + key, e);
        }
    }

    @Override
    public Optional<URI> downloadUrl(String key, String fileName) {
        return Optional.empty();
    }

    private Path resolve(String key) {
        Path file = root.resolve(key).normalize();
        if (!file.startsWith(root.normalize())) {
            throw new IllegalArgumentException("invalid ticket key " + key);
        }
        return file;
    }
}
