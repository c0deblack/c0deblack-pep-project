package Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
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

    public CachedRowSet 
    getCachedRowSet(String query) throws SQLException
    {
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet rowSet = factory.createCachedRowSet();
        rowSet.setCommand(query);
        rowSet.setUsername("sa");
        rowSet.setPassword("sa");
        rowSet.setUrl("jdbc:h2:./h2/db");

        return rowSet;
    }

    public CachedRowSet 
    query(boolean doUpdate, String query, Object ...args)
    {
        try
        {
            CachedRowSet rowSet = this.getCachedRowSet(query);
            
            // add positional arguments to the statemnet
            if(args.length > 0)
            {
                for(int index = 0; index < args.length; ++index)
                {
                    int position = index + 1;
                    if(args[index] instanceof String)
                    {
                        rowSet.setString(position, (String)args[index]);
                    }
                    if(args[index] instanceof Integer)
                    {
                        rowSet.setInt(position, (Integer)args[index]);
                    }
                    if(args[index] instanceof Long)
                    {
                        rowSet.setLong(position, (Long)args[index]);
                    }
                }
            }
            rowSet.execute();
            return rowSet;
        } 
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        return null;
    }

    public CachedRowSet 
    updateQuery(String query, Object ...args)
    {
        return this.query(true, query, args);
    }

    public CachedRowSet 
    execQuery(String query, Object ...args)
    {
        return this.query(false, query, args);
    }


    /**
     * Helper function that gets an Account from a ResultSet.
     * @param accountResult
     * @return Account
     */
    public Account 
    parseAccountResultSet(CachedRowSet accountResult)
    {
        try
        {
            while(accountResult.next())
            {
                Account returnAccount;
                returnAccount = new Account(accountResult.getInt("account_id"),
                                            accountResult.getString("username"),
                                            accountResult.getString("password"));
                return returnAccount;
            }
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        return null;
    }

    public List<Message> 
    parseMessageResults(CachedRowSet accountResult)
    {
        try
        {
            List<Message> messageList = new ArrayList<>();
            while(accountResult.next())
            {
                Message returnMessage;
                returnMessage = new Message(accountResult.getInt("message_id"),
                                            accountResult.getInt("posted_by"),
                                            accountResult.getString("message_text"),
                                            accountResult.getLong("time_posted_epoch"));
                //return returnMessage;
                messageList.add(returnMessage);
            }
            return messageList;
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        return null;
    }
}
