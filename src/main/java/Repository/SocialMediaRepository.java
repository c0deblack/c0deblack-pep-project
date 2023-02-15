package Repository;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

public class SocialMediaRepository {
    private final AccountService accountService;
    private final MessageService messageService;
    private static SocialMediaRepository instance = null;

    public SocialMediaRepository()
    {
        this.accountService = AccountService.getService();
        this.messageService = MessageService.getService();
    }
    public SocialMediaRepository(AccountService accServ, MessageService messServ)
    {
        this.accountService = accServ;
        this.messageService = messServ;
    }
    public static SocialMediaRepository
    getRepository()
    {
        if (instance == null)
        {
            instance = new SocialMediaRepository();
        }
        return instance;
    }

    /**
     * Register a new account to the database.
     * <br><br>
     * <pre>
     * <b>Pre-Condition:</b>
     *
     * Account parameter contains:
     *  1) valid username and password fields values
     *  2) username ! blank
     *  3) len(password) > 4
     *  4) no id field value
     *
     * <b>Post-Condition:</b>
     *
     *  On success: New account is added to the DB.
     *              The added account is returned.
     *
     *  On failure: Null is returned.
     *  </pre>
     * @param account An instance of an {@link Account} object.
     * @return The {@link Account} added to the DB or null on failure.
     */
    public Account
    processNewAccount(Account account)
    {

        return accountService.registerAccount(account);
    }
    public Account
    processNewAccount(String username, String password)
    {

        return accountService.registerAccount(new Account(username, password));
    }


    /**
     *  Process a login request.
     *  <br><br>
     *  <pre>
     *  <b>Pre-Condition:</b>
     *
     *  Account parameter contains:
     *   1) valid username and password fields values
     *   2) username ! blank
     *   3) len(password) > 4
     *   4) no id field value
     *
     *  <b>Post-Condition:</b>
     *
     *   On success: Matching account data is returned.
     *
     *   On failure: Null is returned.
     *   </pre>
     * @param account An instance of an {@link Account} object.
     * @return The {@link Account} that matches the input or null.
     */
    public Account
    processLogin(Account account)
    {
        return accountService.login(account);
    }
    public Account
    processLogin(String username, String password)
    {
        return accountService.login(new Account(username, password));
    }

    /**
     *  Check if a username exists in the database.
     *  <br><br>
     *  <pre>
     *  <b>Pre-Condition:</b>
     *      None
     *
     *  <b>Post-Condition:</b>
     *
     *   If exists: The account is returned
     *
     *   If Not Exists: Return null
     *
     *   On failure: Null is returned.
     *   </pre>
     * @param username A string of the username to search for in the DB.
     * @return The {@link Account} if found or Null if the username is not in the database.
     */
    public Account
    checkIfUserExist(String username)
    {
        return accountService.checkIfUserExists(username);
    }

    /**
     *  Add a new message to the database
     *  <br><br>
     *  <pre>
     *  <b>Pre-Condition:</b>
     *
     *   1) 0 < Message.text.length < 255
     *   2) Message.posted_by matches an actual account_id in the databae
     *
     *  <b>Post-Condition:</b>
     *
     *   On success: New message is added to the database.
     *               The same message object is returned with all fields populated.
     *
     *   On failure: Null is returned.
     *   </pre>
     * @param message A {@link Message} that will be added to the database.
     * @return A {@link Message} that has been added to the database or null on failure.
     */
    public Message
    processNewMessage(Message message)
    {

        return messageService.addMessage(message);
    }

    /**
     *  Get a list of messages stored in the database.
     *  <br><br>
     *  <pre>
     *  <b>Pre-Condition:</b>
     *      None
     *
     *  <b>Post-Condition:</b>
     *      A List of 0 or more Messages
     *   </pre>
     * @return {@link List<Message>} containing all messages from the database.
     */
    public List<Message>
    getAllMessages()
    {
        return messageService.getMessages();
    }

    /**
     *  Get a message by its message ID.
     *  <br><br>
     *  <pre>
     *  <b>Pre-Condition:</b>
     *
     *      A message with a valid ID exists in the database.
     *
     *  <b>Post-Condition:</b>
     *
     *   On success: A matching message is returned.
     *
     *   On failure: Null is returned.
     *   </pre>
     * @param id A int that represents the message_id of a message in the database.
     * @return Null on failure or a matching {@link Message} on success.
     */
    public Message
    getSingleMessage(int id)
    {
        return messageService.getMessage(id);
    }

    /**
     *  Delete a message from the database with a matching ID.
     *  <br><br>
     *  <pre>
     *  <b>Pre-Condition:</b>
     *
     *      A message with a matching ID in the database.
     *
     *  <b>Post-Condition:</b>
     *
     *   On success: The matching message is deleted.
     *               The deleted message is returned.
     *
     *   On failure: Null is returned.
     *   </pre>
     * @param id A int that represents the message_id of a message in the database.
     * @return Null on failure or a matching {@link Message} on success.
     */
    public Message
    processDeleteMessage(int id)
    {
        return messageService.deleteMessage(id);
    }

    /**
     *  Update the message text of a message with a matching message_id.
     *  <br><br>
     *  <pre>
     *  <b>Pre-Condition:</b>
     *
     *      1) A message with a matching message ID exists in the database.
     *      2) The message text in the new message is between 1 and 255.
     *
     *  <b>Post-Condition:</b>
     *
     *   On success: The text of a matching message in the database is updated to match the new message text.
     *               The new message is returned.
     *
     *   On failure: Null is returned.
     *   </pre>
     * @param message A {@link Message} object containing the new message text.
     * @return Null on failure or the updated {@link Message} on success.
     */
    public Message
    processUpdateMessage(Message message)
    {
        return messageService.updateMessage(message);
    }

    /**
     *  Get a list of messages posted by a user.
     *  <br><br>
     *  <pre>
     *  <b>Pre-Condition:</b>
     *      id is a valid account_id.
     *
     *  <b>Post-Condition:</b>
     *
     *   On success: A list of messages is returned.
     *
     *   On failure: An empty list is returned.
     *   </pre>
     * @param id The account_id of a user {@link Account}
     * @return A list of 0 or more messages.
     */
    public List<Message>
    getMessagesFromUser(int id)
    {
        return messageService.getMessagesByUser(id);
    }
}
