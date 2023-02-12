package Exceptions;

public class AccountRegistrationException extends RuntimeException {
    public AccountRegistrationException(String msg)
    {
        super(msg);
    }
}
