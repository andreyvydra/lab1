package itmo.is.lab1.services.common;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import itmo.is.lab1.services.exceptions.MinioDeleteException;
import itmo.is.lab1.services.exceptions.MinioUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public void uploadFile(String objectName, MultipartFile file) throws MinioUploadException {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (MinioException e) {
            throw new MinioUploadException("Ошибка при загрузке файла в MinIO: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new MinioUploadException("Неизвестная ошибка при загрузке файла в MinIO: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String objectName) throws MinioDeleteException {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (MinioException e) {
            throw new MinioDeleteException("Ошибка при удалении файла из MinIO: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new MinioDeleteException("Неизвестная ошибка при удалении файла из MinIO: " + e.getMessage(), e);
        }
    }
}