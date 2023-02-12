package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import Model.Message;
import Service.SocialMediaService;
import Util.ConnectionUtil;

public class MessageDAO {

    public List<Message> 
    getAllMessages()
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "SELECT * FROM message";

        CachedRowSet result = service.execQuery(query);

        List<Message> msg_list = new ArrayList<>();
        try
        {
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
            sqle.printStackTrace();
        }

        if(msg_list.size() > 0) return msg_list;
        else return null;
    }


    public List<Message> 
    getMessagesByAccountId(int id)
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "SELECT * FROM message WHERE posted_by = ?";

        CachedRowSet result = service.execQuery(query, id);
        List<Message> msg_list = new ArrayList<>();
        try
        {
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
            sqle.printStackTrace();
        }

        if(msg_list.size() > 0) return msg_list;
        else return null;
    }


    public Message 
    getMessageById(int id)
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "SELECT * FROM message WHERE message_id = ?";

        CachedRowSet result = service.execQuery(query, id);

        List<Message> added_msg = service.parseMessageResults(result);

        if(added_msg == null) return null;
        if(added_msg.size() == 0) return null;
        else return added_msg.get(0);
    }


    public Message 
    addMessage(Message newMsg) 
    {
        String query = "INSERT INTO message (posted_by, message_text, time_posted_epoch)"
                     + " VALUES ( ?, ?, ? );";
        ;
        try (Connection connection = ConnectionUtil.getConnection())
        {

            String text         = newMsg.getMessage_text();
            Integer posted_by   = newMsg.getPosted_by();
            Long time_posted    = newMsg.getTime_posted_epoch();


            if(text.length() > 255)             return null;
            if(text.length() == 0)              return null;
           // if(service.getAccount(posted_by) == null)  return null;


            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, posted_by);
            statement.setString(2, text);   
            statement.setLong(3, time_posted);

            int retVal = statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                Integer message_id  = keys.getInt(1);

                Message ret_msg = new Message(message_id, posted_by, text, time_posted);
                return ret_msg;
            }
        }catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }

        return null;
    }


    public Message 
    deleteMessageById(int id)
    {
        SocialMediaService service = SocialMediaService.getService();
        Message msg = this.getMessageById(id);
        if(msg == null) return null;

        String query = "DELETE FROM message WHERE message_id = ?";

        CachedRowSet result = service.execQuery(query, id);
        if(result == null) return null;

        return msg;
    }


    public Message 
    updateMessageById(int id, Message newMsg)
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "UPDATE message set message_text  = ? WHERE message_id = ?";

        CachedRowSet result = service.execQuery(query, newMsg.getMessage_text(), id);
        if(result == null) return null;

        return this.getMessageById(id);
    }
    
}
