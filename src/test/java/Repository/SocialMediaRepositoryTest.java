package Repository;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.*;

public class SocialMediaRepositoryTest {

    @Mock
    private AccountService accountService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private SocialMediaRepository repository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
         repository = new SocialMediaRepository(accountService, messageService);
    }

    @Test
    public void getRepositoryTest() {
        var repo = SocialMediaRepository.getRepository();
        assertNotNull(repo);
        assertTrue(repo instanceof SocialMediaRepository);
    }

    @Test
    public void ProcessNewAccountTest() {
        Account input = new Account("user", "pass");
        Account expected = new Account(1, "user", "pass");

        when(accountService.registerAccount(input)).thenReturn(expected);
        System.out.println(accountService.registerAccount(input));

        var result = repository.processNewAccount(input);
        assertEquals(expected, result);
    }

    @Test
    public void ProcessLoginTest() {
        Account input = new Account("user", "pass");
        Account expected = new Account(1, "user", "pass");

        when(accountService.login(input)).thenReturn(expected);

        var result = repository.processLogin(input);
        assertEquals(expected, result);
    }

    @Test
    public void checkIfUserExistTest() {
        ProcessNewAccountTest();
        Account input = new Account("user", "pass");
        Account expected = new Account(1, "user", "pass");

        when(accountService.checkIfUserExists(input.username)).thenReturn(expected);


        var result = repository.checkIfUserExist(input.username);
        assertEquals(expected, result);
    }

    @Test
    public void processNewMessageTest() {
        ProcessNewAccountTest();
        Message input = new Message(1, "My message", 1293848293);
        Message expected = new Message(1, 1, "My message", 1293848293);

        when(messageService.addMessage(input)).thenReturn(expected);
        var result = repository.processNewMessage(input);
        assertEquals(expected, result);
    }

    @Test
    public void getAllMessagesTest() {
        processNewMessageTest();

        List<Message> expected = new ArrayList<>();
        Message msg = new Message(1, 1, "My message", 1293848293);
        expected.add(msg);

        when(messageService.getMessages()).thenReturn(expected);
        var result = repository.getAllMessages();

        assertEquals(expected, result);
    }

    @Test
    public void getSingleMessageTest() {
        processNewMessageTest();

        Message expected = new Message(1, 1, "My message", 1293848293);

        when(messageService.getMessage(1)).thenReturn(expected);
        var result = repository.getSingleMessage(1);

        assertEquals(expected, result);
    }

    @Test
    public void processDeleteMessage() {
        processNewMessageTest();

        Message expected = new Message(1, 1, "My message", 1293848293);

        when(messageService.getMessage(1)).thenReturn(expected);
        var result = repository.getSingleMessage(1);

        assertEquals(expected, result);
    }

    @Test
    public void processUpdateMessageTest() {
        processNewMessageTest();

        Message expected = new Message(1, 1, "New Message", 1293848293);

        when(messageService.getMessage(1)).thenReturn(expected);
        var result = repository.getSingleMessage(1);

        result.setMessage_text("New Message");

        when(messageService.updateMessage(result)).thenReturn(expected);

        result = repository.processUpdateMessage(result);

        assertEquals(expected, result);
    }

    @Test
    public void getMessagesFromUserTest() {
        processNewMessageTest();

        List<Message> expected = new ArrayList<>();
        Message msg = new Message(1, 1, "My message", 1293848293);
        expected.add(msg);

        when(messageService.getMessagesByUser(1)).thenReturn(expected);
        var result = repository.getMessagesFromUser(1);

        assertEquals(expected, result);
    }
}