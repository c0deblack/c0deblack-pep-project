package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


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



    /*****************************************************
     * Message Services
     *****************************************************/
    List<Message> getMessages()
    {
        return messageDAO.getAllMessages();
    }
    List<Message> getMessages(int id)
    {
        return messageDAO.getMessagesByAccountId(id);
    }
    Message getMessage(int id)
    {
        return messageDAO.getMessageById(id);
    }
    Message updateMessage(int id, Message newMsg)
    {
        return messageDAO.updateMessageById(id, newMsg);
    }
    Message deleteMessage(int id)
    {
        return messageDAO.deleteMessageById(id);
    }
    Message addMessage(Message newMsg)
    {
        return messageDAO.addMessage(newMsg);
    }

    /*****************************************************
     * Utility Database Services
     *****************************************************/
    public ResultSet query(boolean doUpdate, String query, Object ...args)
    {
        try(Connection connection = ConnectionUtil.getConnection())
        {
            // use different statements for update and execute queries
            PreparedStatement preparedStatement;
            if(doUpdate == false) 
            {
                preparedStatement = connection.prepareStatement(query);
            } else 
            {
                preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            }
            
            // add positional arguments to the statemnet
            if(args.length > 0)
            {
                for(int index = 0; index < args.length; ++index)
                {
                    int position = index + 1;
                    if(args[index] instanceof String)
                    {
                        preparedStatement.setString(position, (String)args[index]);
                    }
                    if(args[index] instanceof Integer)
                    {
                        preparedStatement.setInt(position, (Integer)args[index]);
                    }
                }
            }

            if(doUpdate == false)
            {
                ResultSet result = preparedStatement.executeQuery();
                return result;
            } else
            {
                int update_result = preparedStatement.executeUpdate();
                ResultSet result = preparedStatement.getGeneratedKeys();
                return result;
            }

        } 
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        return null;
    }

    public ResultSet updateQuery(String query, Object ...args)
    {
        return this.query(true, query, args);
    }
    public ResultSet execQuery(String query, Object ...args)
    {
        return this.query(false, query, args);
    }


    /**
     * Helper function that gets an Account from a ResultSet.
     * @param accountResult
     * @return Account
     */
    public Account parseAccountResultSet(ResultSet accountResult)
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

    /**
     * Helper function that gets a list of one or more messages from a result set.
     * @param accountResult
     * @return
     */
    List<Message> parseMessgeResults(ResultSet accountResult)
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
