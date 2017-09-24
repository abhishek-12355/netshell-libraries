package com.netshell.libraries.utilities.factory.exception;

/**
 * @author Abhishek
 * Created on 8/22/2015.
 */
public class FactoryException extends RuntimeException {
    private static final long serialVersionUID = 5565534586481805043L;

    public FactoryException() {
        super();
    }

    public FactoryException(String message) {
        super(message);
    }

    public FactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public FactoryException(Throwable cause) {
        super(cause);
    }

    protected FactoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
