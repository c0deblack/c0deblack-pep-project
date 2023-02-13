package Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import DAO.AccountDAO;
import DAO.MessageDAO;
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
        return accountDAO.registerAccount(username, password);
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
    public List<Message> getMessages(int id)
    {
        return messageDAO.getMessagesByAccountId(id);
    }
    public Message getMessage(int id)
    {
        return messageDAO.getMessageById(id);
    }
    public Message updateMessage(int id, Message newMsg)
    {
        return messageDAO.updateMessageById(id, newMsg);
    }
    public Message deleteMessage(int id)
    {
        return messageDAO.deleteMessageById(id);
    }
    public Message addMessage(Message newMsg)
    {
        return messageDAO.addMessage(newMsg);
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
