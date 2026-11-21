package com.example.springbootpetproject.service.storage;

import java.net.URI;
import java.util.Optional;

/**
 * Хранилище готовых PDF-билетов.
 * <p>
 * Раньше PDF писались в локальную папку контейнера. На Fargate диск задачи временный
 * и у каждой задачи свой, поэтому файл пропадал при перезапуске и не был виден
 * соседней задаче. В AWS используется {@link S3TicketStorage}, локально — {@link LocalTicketStorage}.
 */
public interface TicketStorage {

    boolean exists(String key);

    void save(String key, byte[] pdf);

    byte[] load(String key);

    /**
     * Временная ссылка для скачивания напрямую из хранилища.
     * Пусто, если хранилище так не умеет — тогда файл отдаёт само приложение.
     */
    Optional<URI> downloadUrl(String key, String fileName);
}
