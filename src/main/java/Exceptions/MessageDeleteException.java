package Exceptions;

public class MessageDeleteException extends RuntimeException {
    public MessageDeleteException(String msg)
    {
        super(msg);
    }
}