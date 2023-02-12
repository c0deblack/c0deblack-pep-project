package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import Exceptions.AccountRegistrationException;
import Model.Account;
import Service.SocialMediaService;

public class AccountDAO {
    public Account registerAccount(String username, String password)
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "INSERT INTO account (username, password) VALUES ( ?, ? )";

        ResultSet result = service.updateQuery(query, username, password);
        if(result == null)
        {
            throw new AccountRegistrationException("Update returned a null ResultSet!");
        }
        else
        {
            try 
            {
                result.next();
                int account_id = (int) result.getLong("account_id");
                Account retAccount = new Account(account_id, username, password);
                return retAccount;

            } catch (SQLException sqle)
            {
                sqle.printStackTrace();
            }
        }
        
        return null;
    }
    public Account getAccountInfoById(int id)
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "SELECT * FROM account WHERE account_id = ? ";
        ResultSet result = service.execQuery(query, id);
        return service.parseAccountResultSet(result);
    }

    public Account getMatchingUserPassCombo(String username, String password)
    {
        SocialMediaService service = SocialMediaService.getService();
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        ResultSet result = service.execQuery(query, username, password);
        return service.parseAccountResultSet(result);
    }


}
