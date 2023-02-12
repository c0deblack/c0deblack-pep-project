package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import Model.Account;
import Service.SocialMediaService;
import Util.ConnectionUtil;

public class AccountDAO {
    public Account 
    registerAccount(String username, String password)
    {
        if(username.length() < 1)       return null;
        if(password.length() < 4)       return null;
        if(this.userExists(username))   return null;

        try (Connection connection = ConnectionUtil.getConnection(); )
        {
            String query = "INSERT INTO account (username, password) VALUES ( ?, ? )";
    
            ;
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                
            statement.setString(1, username);
            statement.setString(2, password);
    
            int retVal = statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            while(keys.next()) {
                Integer account_id  = keys.getInt(1);
    
                Account ret = new Account(account_id, username, password);
                return ret;
            }
        } catch (SQLException sqle)
        {
            sqle.printStackTrace();
            throw new Error(sqle.getMessage());
        }
        
        return null;
    }


    public Account 
    getAccountInfoById(int id)
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "SELECT * FROM account WHERE account_id = ? ";
        CachedRowSet result = service.execQuery(query, id);
        List<Account> accounts = new ArrayList<>();
        try
        {
            while(result.next())
            {
                Account returnAccount;
                returnAccount = new Account(result.getInt("account_id"),
                                            result.getString("username"),
                                            result.getString("password"));
                accounts.add(returnAccount);
            }
        } catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }

        if(accounts.size() > 0) return accounts.get(0);
        else return null;    
    }


    public boolean 
    userExists(String username)
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "SELECT * FROM account WHERE username = ?";
        CachedRowSet result = service.execQuery(query, username);
        List<Account> accounts = new ArrayList<>();
        try
        {
            while(result.next())
            {
                Account returnAccount;
                returnAccount = new Account(result.getInt("account_id"),
                                            result.getString("username"),
                                            result.getString("password"));
                accounts.add(returnAccount);
            }
        } catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        return accounts.size() > 0;
    }


    public Account 
    getMatchingUserPassCombo(String username, String password)
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        CachedRowSet result = service.execQuery(query, username, password);
        List<Account> accounts = new ArrayList<>();
        try
        {
            while(result.next())
            {
                Account returnAccount;
                returnAccount = new Account(result.getInt("account_id"),
                                            result.getString("username"),
                                            result.getString("password"));
                accounts.add(returnAccount);
            }
        } catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }

        if(accounts.size() > 0) return accounts.get(0);
        else return null;
    }
}
