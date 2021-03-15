package ru.hse.engine.exceptions;

public class CommandLineArgumentsException extends Exception{
    public CommandLineArgumentsException() {
        super();
    }

    public CommandLineArgumentsException(String message) {
        super(message);
    }

    public CommandLineArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandLineArgumentsException(Throwable cause) {
        super(cause);
    }

    protected CommandLineArgumentsException(String message, Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
