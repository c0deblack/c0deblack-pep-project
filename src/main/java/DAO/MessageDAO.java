package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Errors.Message.MessageAddError;
import Errors.Message.MessageReadError;
import Errors.Message.MessageDeleteError;
import Errors.Message.MessageUpdateError;
import Errors.ReadError;
import Exceptions.*;
import Interface.InterfaceDAO;
import Model.Message;
import Util.ConnectionUtil;

/**
 * This class handles interaction with the message table which is modeled by the {@link Message} class.
 * <br/><br/>
 * It includes the following methods to get, add, and update messages:
 *
 * <br/><br/>
 *
 * <pre>
 * </pre>
 */
public class MessageDAO implements InterfaceDAO<Message> {

    public List<Message> 
    getMessagesByAccountId(int id) throws MessageReadError {
        String query = "SELECT * FROM message WHERE posted_by = ?";

        List<Message> msg_list = new ArrayList<>();
        try
        {
            Connection connection = ConnectionUtil.getConnection();
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
            throw new MessageReadError(msg + sqle.getMessage());
        }

        if(msg_list.size() > 0) return msg_list;
        else return null;
    }

    @Override
    public Message
    add(Message message)
            throws MessageAddError,
            MessageInvalidLengthException,
            MessageInvalidAuthorIDException
    {
        String text         = message.getMessage_text();
        int posted_by       = message.getPosted_by();
        long time_posted    = message.getTime_posted_epoch();


        String msg = "Failed adding message: ";
        if(text.length() > 255)             throw new MessageInvalidLengthException(msg + "Text length too long.");
        if(text.length() == 0)              throw new MessageInvalidLengthException(msg + "Text cannot be 0.");

        try
        {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement;

            String query = "SELECT * FROM account WHERE account_id = ? ;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, posted_by);

            ResultSet account_ids = statement.executeQuery();

            if(! account_ids.next())
            {
                throw new MessageInvalidAuthorIDException(msg + "No matching account ID for posted_by ID.");
            }

            query = "INSERT INTO message (posted_by, message_text, time_posted_epoch)"
                    + " VALUES ( ?, ?, ? );";

            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, posted_by);
            statement.setString(2, text);
            statement.setLong(3, time_posted);

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                int message_id  = keys.getInt(1);
                return new Message(message_id, posted_by, text, time_posted);
            }
        }catch (SQLException sqle)
        {
            throw new MessageAddError("Failed to add new message to database: " + sqle.getMessage());
        }
        return null;    }

    @Override
    public Message
    get(int id)
            throws MessageReadError
    {
        String query = "SELECT * FROM message WHERE message_id = ?";

        List<Message> msg_list = new ArrayList<>();
        try
        {
            Connection connection = ConnectionUtil.getConnection();
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
            throw new MessageReadError(msg + sqle.getMessage());
        }

        if(msg_list.size() > 0) return msg_list.get(0);
        else return null;
    }

    @Override
    public List<Message>
    getAll()
            throws MessageReadError
    {
        String query = "SELECT * FROM message; ";
        List<Message> msg_list = new ArrayList<>();
        try
        {
            Connection connection = ConnectionUtil.getConnection();
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
            throw new MessageReadError("Failed getting messages: " + sqle.getMessage());
        }

        if(msg_list.size() > 0) return msg_list;
        else return null;
    }

    @Override
    public Message
    update(Message message)
            throws MessageInvalidLengthException,
            MessageInvalidAuthorIDException,
            MessageUpdateError
    {
        String text         = message.getMessage_text();
        int id              = message.getMessage_id();

        try
        {
            Connection connection = ConnectionUtil.getConnection();

            String msg = "Failed adding message: ";
            if(text.length() > 255)             throw new MessageInvalidLengthException(msg + "Text length too long.");
            if(text.length() == 0)              throw new MessageInvalidLengthException(msg + "Text cannot be 0.");

            String query = "SELECT * FROM message WHERE message_id = ?;";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet message_ids = statement.executeQuery();
            if(! message_ids.next())
            {
                //msg += Objects.requireNonNullElse(this.getAllMessages(), "");
                throw new MessageInvalidAuthorIDException(msg + "Message ID ["+id+"] does not exist.");
            }

            int message_id      = message_ids.getInt("message_id");
            int posted_by       = message_ids.getInt("posted_by");
            long time_posted    = message_ids.getLong("time_posted_epoch");

            query = "UPDATE message SET message_text  = ? WHERE message_id = ?;";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, text);
            statement.setInt(2, message_id);
            //statement.setLong(3, time_posted);

            statement.execute();

            return new Message(message_id, posted_by, text, time_posted);
        }catch (SQLException sqle)
        {
            String msg = "Failed to update message in database with ID ["+id+"]: ";
            throw new MessageUpdateError(msg + sqle.getMessage());
        }
    }

    @Override
    public Message
    delete(Message message)
            throws MessageDeleteError,
            MessageInvalidIDException
    {
        int id = message.getMessage_id();

        Message msg = null;

        try {
            msg = this.get(id);
        } catch (MessageReadError e) {
            throw new MessageDeleteError("Message Delete Error: " + e.getMessage());
        }

        if(msg == null) return null;

        String query = "DELETE FROM message WHERE message_id = ?";
        try
        {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException sqle)
        {
            throw new MessageDeleteError("SQL error while attempting to delete message with ID ["+id+"]: " + sqle.getMessage());
        }
        return msg;
    }
}
