package Errors.Message;

import Errors.ReadError;

public class MessageReadError extends ReadError {
    public MessageReadError(String msg)
    {
        super(msg);
    }
}