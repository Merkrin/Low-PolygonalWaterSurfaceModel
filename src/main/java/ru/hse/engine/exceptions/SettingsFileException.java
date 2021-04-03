package ru.hse.engine.exceptions;

public class SettingsFileException extends Exception{
    public SettingsFileException() {
    }

    public SettingsFileException(String message) {
        super(message);
    }

    public SettingsFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public SettingsFileException(Throwable cause) {
        super(cause);
    }

    public SettingsFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
