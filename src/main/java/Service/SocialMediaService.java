package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Exceptions.AccountRegistrationException;
import Exceptions.MessageAddException;
import Exceptions.MessageUpdateException;
import Model.Account;
import Model.Message;

/**
 * This class interact with the {@link AccountDAO} and {@link MessageDAO} classes directly and
 * provides a unified interface to multiple data sources.
 * <br><br>
 * 
 * <pre>
 * {@link SocialMediaService#SocialMediaService()}
 * {@link SocialMediaService#getService()} 
 * {@link SocialMediaService#registerAccount(String, String)} 
 * {@link SocialMediaService#login(String, String)} 
 * {@link SocialMediaService#getMessages()} 
 * {@link SocialMediaService#getMessagesByUser(int)} 
 * {@link SocialMediaService#getMessage(int)} 
 * {@link SocialMediaService#updateMessage(int, Message)} 
 * {@link SocialMediaService#deleteMessage(int)} 
 * {@link SocialMediaService#addMessage(Message)}
 * </pre>
 */
public class SocialMediaService {
    private static AccountDAO accountDAO;
    private static MessageDAO messageDAO;
    private static SocialMediaService instance = null;

    private
    SocialMediaService()
    {
        accountDAO = new AccountDAO();
        messageDAO = new MessageDAO();
    }

    static public SocialMediaService
    getService()
    {
        if(instance == null)
        {
            instance = new SocialMediaService();
        }
        return instance;
    }

    /*****************************************************
     * Account Services
     *****************************************************/
    public Account
    registerAccount(String username, String password)
    {
        try
        {
            return accountDAO.registerAccount(username, password);
        } catch (AccountRegistrationException are)
        {
            return null;
        }
    }

    public Account
    login(String username, String password)
    {
        return accountDAO.getMatchingUserPassCombo(username, password);
    }


    /*****************************************************
     * Message Services
     *****************************************************/
    public List<Message>
    getMessages()
    {
        return messageDAO.getAllMessages();
    }
    public List<Message>
    getMessagesByUser(int id)
    {
        return messageDAO.getMessagesByAccountId(id);
    }
    public Message
    getMessage(int id)
    {
        return messageDAO.getMessageById(id);
    }
    public Message
    updateMessage(int id, Message newMsg)
    {
        try
        {
            return messageDAO.updateMessageById(id, newMsg);
        }
        catch (MessageUpdateException mue)
        {
            return null;
        }
    }
    public Message
    deleteMessage(int id)
    {
        return messageDAO.deleteMessageById(id);
    }
    public Message
    addMessage(Message newMsg)
    {
        try
        {
            return messageDAO.addMessage(newMsg);
        } catch (MessageAddException mae)
        {
            return null;
        }
    }
}
