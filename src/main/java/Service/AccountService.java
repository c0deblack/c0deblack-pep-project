package Service;

import DAO.AccountDAO;
import Errors.Account.AddAccountError;
import Errors.Account.GetAccountError;
import Model.Account;

import java.util.List;
import java.util.Objects;

public class AccountService {

    private final AccountDAO accountDAO;
    private static AccountService instance = null;

    AccountService()
    {
        this.accountDAO = new AccountDAO();
    }
    public static AccountService
    getService()
    {
        if(instance == null)
        {
            instance = new AccountService();
        }
        return instance;
    }

    public Account
    registerAccount(Account account)
    {
        try
        {
            return accountDAO.add(account);
        } catch (AddAccountError e)
        {
            throw new RuntimeException(e);
        }
    }

    public Account
    login(Account account)
    {
        try
        {
            return accountDAO.login(account);
        } catch (GetAccountError e)
        {
            throw new RuntimeException(e);
        }
    }

    public Account
    checkIfUserExists(String username)
    { // TODO: Implement this in the DAO using SQL for a major boost in efficiency
        List<Account> accounts;
        try {
            accounts = accountDAO.getAll();
            for(var acc : accounts)
            {
                if(Objects.equals(acc.getUsername(), username)) return acc;
            }

        } catch (GetAccountError e) {
            throw new RuntimeException(e);
        }

        return null;
    }


}
