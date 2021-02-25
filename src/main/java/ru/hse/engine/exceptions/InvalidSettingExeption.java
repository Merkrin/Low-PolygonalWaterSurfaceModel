package ru.hse.engine.exceptions;

public class InvalidSettingExeption extends Exception {
    public InvalidSettingExeption() {
        super();
    }

    public InvalidSettingExeption(String message) {
        super(message);
    }

    public InvalidSettingExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSettingExeption(Throwable cause) {
        super(cause);
    }

    protected InvalidSettingExeption(String message, Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
