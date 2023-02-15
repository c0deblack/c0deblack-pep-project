package Errors.Account;

import Errors.ReadError;

public class GetAccountError extends ReadError {
    public GetAccountError(String msg)
    {
        super(msg);
    }
}
