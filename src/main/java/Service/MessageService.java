package Service;

import DAO.MessageDAO;
import Errors.Message.MessageAddError;
import Errors.Message.MessageDeleteError;
import Errors.Message.MessageReadError;
import Errors.Message.MessageUpdateError;
import Errors.ReadError;
import Exceptions.MessageInvalidAuthorIDException;
import Exceptions.MessageInvalidIDException;
import Exceptions.MessageInvalidLengthException;
import Model.Message;

import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO;
    private static MessageService instance;

    MessageService()
    {
        this.messageDAO = new MessageDAO();
    }

    public static MessageService getService() {
        if (instance == null)
        {
            instance = new MessageService();
        }
        return instance;
    }
    public List<Message>
    getMessages()
    {
        try {
            return messageDAO.getAll();
        } catch (ReadError e) {
            throw new RuntimeException(e);
        }
    }
    public List<Message>
    getMessagesByUser(int id)
    {
        try {
            return messageDAO.getMessagesByAccountId(id);
        } catch (MessageReadError e) {
            throw new RuntimeException(e);
        }
    }
    public Message
    getMessage(int id)
    {
        try
        {
            return messageDAO.get(id);
        }
        catch (MessageReadError e)
        {
            throw new RuntimeException(e);
        }
    }
    public Message
    updateMessage(Message newMsg)
    {
        try
        {
            return messageDAO.update(newMsg);
        }
        catch (MessageInvalidAuthorIDException | MessageInvalidLengthException e)
        {
            return null;
        }
        catch (MessageUpdateError e)
        {
            throw new RuntimeException(e);
        }
    }
    public Message
    deleteMessage(int id)
    {
        Message message = new Message();
        message.setMessage_id(id);

        try {
            return messageDAO.delete(message);
        }
        catch (MessageInvalidIDException e)
        {
            return null;
        }
        catch (MessageDeleteError e)
        {
            throw new RuntimeException(e);
        }
    }
    public Message
    addMessage(Message newMsg)
    {
        try
        {
            return messageDAO.add(newMsg);
        }
        catch (MessageInvalidLengthException | MessageInvalidAuthorIDException e)
        {
            return null;
        } catch (MessageAddError e)
        {
            throw new RuntimeException(e);
        }
    }
}
