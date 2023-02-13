package Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Exceptions.AccountRegistrationException;
import Exceptions.MessageAddException;
import Exceptions.MessageUpdateException;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;


public class SocialMediaService {
    private static AccountDAO accountDAO;
    private static MessageDAO messageDAO;
    private static SocialMediaService instance = null;

    private SocialMediaService()
    {
        accountDAO = new AccountDAO();
        messageDAO = new MessageDAO();
    }

    static public SocialMediaService getService()
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
    public Account registerAccount(String username, String password)
    {
        try
        {
            return accountDAO.registerAccount(username, password);
        } catch (AccountRegistrationException are)
        {
            return null;
        }
    }
    public Account getAccount(int id)
    {
        return accountDAO.getAccountInfoById(id);
    }
    public Account login(String username, String password)
    {
        return accountDAO.getMatchingUserPassCombo(username, password);
    }
    public boolean userExists(String username)
    {
        return accountDAO.userExists(username);
    }



    /*****************************************************
     * Message Services
     *****************************************************/
    public List<Message> getMessages()
    {
        return messageDAO.getAllMessages();
    }
    public List<Message> getMessagesByUser(int id)
    {
        return messageDAO.getMessagesByAccountId(id);
    }
    public Message getMessage(int id)
    {
        return messageDAO.getMessageById(id);
    }
    public Message updateMessage(int id, Message newMsg)
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
    public Message deleteMessage(int id)
    {
        return messageDAO.deleteMessageById(id);
    }
    public Message addMessage(Message newMsg)
    {
        try
        {
            return messageDAO.addMessage(newMsg);
        } catch (MessageAddException mae)
        {
            return null;
        }
    }

    /*****************************************************
     * Utility Database Services
     *****************************************************/
    public void 
    resetDB()
    {
        ConnectionUtil.resetTestDatabase();
    }

}
