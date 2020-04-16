package com.sjt.sso.core.exception;

/**
 * @author sjt
 * 2020-04-16 15:24:24
 */
public class SsoException extends RuntimeException{

    private static final long serialVersionUID =42L;

    public SsoException(String msg) {
        super(msg);
    }

    public SsoException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SsoException(Throwable cause) {
        super(cause);
    }


}
