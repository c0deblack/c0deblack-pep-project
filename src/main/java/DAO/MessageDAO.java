package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import Exceptions.GetAllMessagesException;
import Exceptions.MessageAddException;
import Exceptions.MessageDeleteException;
import Exceptions.MessageUpdateException;
import Model.Message;
import Service.SocialMediaService;
import Util.ConnectionUtil;

public class MessageDAO {

    public List<Message> 
    getAllMessages()
    {
        String query = "SELECT * FROM message; ";
        List<Message> msg_list = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            while(result.next())
            {
                Message returnMessage;
                returnMessage = new Message(result.getInt("message_id"),
                                            result.getInt("posted_by"),
                                            result.getString("message_text"),
                                            result.getLong("time_posted_epoch"));
                //return returnMessage;
                msg_list.add(returnMessage);
            }
        } catch (SQLException sqle)
        {
            throw new GetAllMessagesException("Failed getting messages: " + sqle.getMessage());
        }

        if(msg_list.size() > 0) return msg_list;
        else return null;
    }


    public List<Message> 
    getMessagesByAccountId(int id)
    {
        String query = "SELECT * FROM message WHERE posted_by = ?";

        List<Message> msg_list = new ArrayList<>();
        try(Connection connection = ConnectionUtil.getConnection();)
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            while(result.next())
            {
                Message returnMessage;
                returnMessage = new Message(result.getInt("message_id"),
                                            result.getInt("posted_by"),
                                            result.getString("message_text"),
                                            result.getLong("time_posted_epoch"));
                //return returnMessage;
                msg_list.add(returnMessage);
            }
        } catch (SQLException sqle)
        {
            String msg = "Failed getting messages owned by Account ID ["+id+"]: ";
            throw new GetAllMessagesException(msg + sqle.getMessage());
        }

        if(msg_list.size() > 0) return msg_list;
        else return null;
    }


    public Message 
    getMessageById(int id)
    {
        String query = "SELECT * FROM message WHERE message_id = ?";

        List<Message> msg_list = new ArrayList<>();
        try(Connection connection = ConnectionUtil.getConnection();)
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            while(result.next())
            {
                Message returnMessage;
                returnMessage = new Message(result.getInt("message_id"),
                        result.getInt("posted_by"),
                        result.getString("message_text"),
                        result.getLong("time_posted_epoch"));
                //return returnMessage;
                msg_list.add(returnMessage);
            }
        } catch (SQLException sqle)
        {
            String msg = "Failed getting messages with ID ["+id+"]: ";
            throw new GetAllMessagesException(msg + sqle.getMessage());
        }

        if(msg_list.size() > 0) return msg_list.get(0);
        else return null;
    }


    public Message 
    addMessage(Message newMsg) 
    {
        String query = "INSERT INTO message (posted_by, message_text, time_posted_epoch)"
                     + " VALUES ( ?, ?, ? );";
        try (Connection connection = ConnectionUtil.restartConnection(); )
        {
            SocialMediaService service = SocialMediaService.getService();

            String text         = newMsg.getMessage_text();
            int posted_by       = newMsg.getPosted_by();
            long time_posted    = newMsg.getTime_posted_epoch();

            String msg = "Failed adding message: ";
            if(text.length() > 255)             throw new MessageAddException(msg + "Text length too long.");
            if(text.length() == 0)              throw new MessageAddException(msg + "Text cannot be 0.");
            //if(service.getAccount(posted_by) == null)  throw new MessageAddException(msg + "Account does not exist.");


            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, posted_by);
            statement.setString(2, text);   
            statement.setLong(3, time_posted);

            int retVal = statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                int message_id  = keys.getInt(1);

                return new Message(message_id, posted_by, text, time_posted);
            }
        }catch (SQLException sqle)
        {
            throw new MessageAddException("Failed to add new message to database: " + sqle.getMessage());
        }
        return null;
    }


    public Message 
    deleteMessageById(int id)
    {
        Message msg = this.getMessageById(id);
        if(msg == null) return null;

        String query = "DELETE FROM message WHERE message_id = ?";
        try(Connection connection = ConnectionUtil.getConnection();)
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            int retVal = statement.executeUpdate();
        } catch (SQLException sqle)
        {
            throw new MessageDeleteException("Failed to delete message with ID ["+id+"]: " + sqle.getMessage());
        }
        return msg;
    }


    public Message 
    updateMessageById(int id, Message newMsg)
    {
        String query = "UPDATE message set message_text  = ? WHERE message_id = ?";
        try (Connection connection = ConnectionUtil.getConnection(); )
        {
            SocialMediaService service = SocialMediaService.getService();
            String text         = newMsg.getMessage_text();
            int posted_by       = newMsg.getPosted_by();
            long time_posted    = newMsg.getTime_posted_epoch();

            String msg = "Failed adding message: ";
            if(text.length() > 255)             throw new MessageUpdateException(msg + "Text length too long.");
            if(text.length() == 0)              throw new MessageUpdateException(msg + "Text cannot be 0.");
            //if(service.getAccount(posted_by) == null)  throw new MessageUpdateException(msg + "Account does not exist.");


            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, posted_by);
            statement.setString(2, text);
            statement.setLong(3, time_posted);

            int retVal = statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                int message_id  = keys.getInt(1);

                return new Message(message_id, posted_by, text, time_posted);
            }
        }catch (SQLException sqle)
        {
            String msg = "Failed to update message in database with ID ["+id+"]: ";
            throw new MessageUpdateException(msg + sqle.getMessage());
        }
        return null;
    }
    
}
