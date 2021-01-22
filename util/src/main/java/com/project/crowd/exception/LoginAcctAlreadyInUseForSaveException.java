package com.project.crowd.exception;

/**
 * @description:保存用户时，用户名重复异常
 */
public class LoginAcctAlreadyInUseForSaveException extends RuntimeException{

    public LoginAcctAlreadyInUseForSaveException() {
        super();
    }

    public LoginAcctAlreadyInUseForSaveException(String message) {
        super(message);
    }

    public LoginAcctAlreadyInUseForSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAlreadyInUseForSaveException(Throwable cause) {
        super(cause);
    }

    protected LoginAcctAlreadyInUseForSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
