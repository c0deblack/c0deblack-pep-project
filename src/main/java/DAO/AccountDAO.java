package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import Errors.Account.AddAccountError;
import Errors.Account.LoginError;
import Errors.AddError;
import Errors.ReadError;
import Exceptions.AccountLoginException;
import Errors.Account.GetAccountError;
import Interface.InterfaceDAO;
import Model.Account;
import Util.ConnectionUtil;

/**
 * This class presents an interface for interacting with the account table.
 *  * <br/><br/>
 *  The {@link Account} class models that data stored in the table.
 *   <br/><br/>
 *
 *   This class provides the following methods:
 *   <pre>

 *   </pre>
 */
public class AccountDAO implements InterfaceDAO<Account> {
    public Account
    login(Account account)
            throws GetAccountError
    {
        String username = account.getUsername();
        String password = account.getPassword();

        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        try
        {
            Connection connection = ConnectionUtil.getConnection();
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
            throw new LoginError(sb + sqle.getMessage());
        }
        return null;
    }

    @Override
    public Account
    add(Account account)
            throws AddAccountError {
        String username = account.getUsername();
        String password = account.getPassword();

        if(username.length() < 1)       return null;
        if(password.length() < 4)       return null;

        try
        {
            Connection connection = ConnectionUtil.getConnection();
            String query = "INSERT INTO account (username, password) VALUES ( ?, ? );";

            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                int account_id  = keys.getInt(1);
                return new Account(account_id, username, password);
            }
        } catch (SQLException sqle)
        {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            formatter.format("SQL error occurred when attempting to register user with username [%s], and password [%s]: ", username, password);
            throw new AddAccountError(sb + sqle.getMessage());
        }
        return null;
    }

    @Override
    public Account
    get(int id)
            throws GetAccountError
    {
        String query = "SELECT * FROM account WHERE account_id = ? ";
        try
        {
            Connection connection = ConnectionUtil.getConnection();
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

            formatter.format("SQL error occurred while getting username with ID %d", id);
            throw new GetAccountError(sb + sqle.getMessage());
        }
        return null;
    }

    @Override
    public List<Account>
    getAll()
            throws GetAccountError {
        List<Account> outputList = new ArrayList<>();

        String query = "SELECT * FROM account;";
        try
        {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet result = statement.executeQuery();
            if(result.next()) {
                int id = result.getInt(1);
                String username = result.getString(2);
                String password = result.getString(3);
                outputList.add(new Account(id, username, password));
            }
        } catch (SQLException sqle)
        {
            String msg = "SQL error occurred while getting a list of users: ";
            throw new GetAccountError(msg + sqle.getMessage());
        }
        return outputList;
    }

    @Override
    public Account update(Account account) {
        // TODO: Add AccountDAO.update()
        return null;
    }

    @Override
    public Account delete(Account account) {
        // TODO: Add AccountDAO.delete()
        return null;
    }
}
