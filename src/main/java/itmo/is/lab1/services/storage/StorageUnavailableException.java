package itmo.is.lab1.services.storage;

public class StorageUnavailableException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Сервис для работы с файлами недоступен";

    public StorageUnavailableException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}