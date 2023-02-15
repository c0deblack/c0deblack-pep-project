package Exceptions;

public class AccountLoginException extends RuntimeException {
    public AccountLoginException(String msg)
    {
        super(msg);
    }
}
