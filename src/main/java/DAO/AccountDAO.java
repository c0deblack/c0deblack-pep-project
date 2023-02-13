package DAO;

import java.rmi.AccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import javax.sql.rowset.CachedRowSet;

import Exceptions.AccountException;
import Exceptions.AccountRegistrationException;
import Model.Account;
import Service.SocialMediaService;
import Util.ConnectionUtil;

public class AccountDAO {
    public Account 
    registerAccount(String username, String password)
    {
        if(username.length() < 1)       return null;
        if(password.length() < 4)       return null;
        //if(this.userExists(username))   return null;

        try
        {
            Connection connection = ConnectionUtil.getConnection();
            String query = "INSERT INTO account (username, password) VALUES ( ?, ? );";

            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                
            statement.setString(1, username);
            statement.setString(2, password);
    
            int retVal = statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                int account_id  = keys.getInt(1);

                return new Account(account_id, username, password);
            }
        } catch (SQLException sqle)
        {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            formatter.format("Failed to register user with username [%s], and password [%s]: ", username, password);
            throw new AccountRegistrationException(sb.toString() + sqle.getMessage());
        }
        return null;
    }


    public Account 
    getAccountInfoById(int id)
    {
        String query = "SELECT * FROM account WHERE account_id = ? ";
        try (Connection connection = ConnectionUtil.getConnection(); )
        {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if(result.next()) {
                return new Account(result.getInt(1),
                                    result.getString(2),
                                    result.getString(3));
            }
        } catch (SQLException sqle)
        {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            formatter.format("Failed to get username with ID %d", id);
            throw new AccountException(sb.toString() + sqle.getMessage());
        }
        return null;
    }


    public boolean 
    userExists(String username)
    {
        String query = "SELECT * FROM account WHERE username = ?";
        try (Connection connection = ConnectionUtil.getConnection(); )
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();
            if(result.next()) {
                return false;
            }
        } catch (SQLException sqle)
        {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            formatter.format("Failed to check if user exists with username [%s]: ", username);
            throw new AccountException(sb.toString() + sqle.getMessage());
        }
        return false;
    }


    public Account 
    getMatchingUserPassCombo(String username, String password)
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        try (Connection connection = ConnectionUtil.getConnection(); )
        {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();
            if(result.next()) {
                return new Account(result.getInt(1),
                        result.getString(2),
                        result.getString(3));
            }
        } catch (SQLException sqle)
        {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            String msg = "Failed to check User/Pass Combination with username [%s], and password [%s]: ";
            formatter.format(msg, username, password);
            throw new AccountException(sb.toString() + sqle.getMessage());
        }
        return null;
    }
}
