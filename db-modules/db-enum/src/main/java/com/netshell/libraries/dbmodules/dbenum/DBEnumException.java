package com.netshell.libraries.dbmodules.dbenum;

/**
 * @author Abhishek
 * Created on 12/13/2015.
 */
public final class DBEnumException extends RuntimeException {
    private static final long serialVersionUID = -495323689460753367L;

    public DBEnumException(final String message) {
        super(message);
    }

    public DBEnumException(final String s, final Exception e) {
        super(s, e);
    }

}
