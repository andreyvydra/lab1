package itmo.is.lab1.services.storage;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final MinioClient minioClient;

    @Value("${app.storage.bucket:imports}")
    private String bucket;

    public StoredObject storeImportFile(MultipartFile file) {
        ensureBucket();
        String objectKey = buildObjectKey(file.getOriginalFilename());
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException("Failed to upload file to object storage", e);
        }

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status == STATUS_ROLLED_BACK) {
                        try {
                            minioClient.removeObject(
                                    io.minio.RemoveObjectArgs.builder()
                                            .bucket(bucket)
                                            .object(objectKey)
                                            .build()
                            );
                            log.warn("Rolled back import, removed object {}", objectKey);
                        } catch (Exception ex) {
                            log.error("Failed to cleanup object {} after rollback", objectKey, ex);
                        }
                    }
                }
            });
        }

        return new StoredObject(objectKey, file.getOriginalFilename(), file.getContentType(), file.getSize());
    }

    public InputStreamResource getObject(String objectKey) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build()
            );
            return new InputStreamResource(stream);
        } catch (Exception e) {
            throw new StorageException("Failed to download object: " + objectKey, e);
        }
    }

    private void ensureBucket() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new StorageException("Failed to ensure bucket existence", e);
        }
    }

    private String buildObjectKey(String originalName) {
        String safeName = originalName == null ? "file" : originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
        return "imports/" + LocalDate.now() + "/" + UUID.randomUUID() + "_" + safeName;
    }

    @Data
    @AllArgsConstructor
    public static class StoredObject {
        private String objectKey;
        private String originalFileName;
        private String contentType;
        private long size;
    }
}

class StorageException extends RuntimeException {
    StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
