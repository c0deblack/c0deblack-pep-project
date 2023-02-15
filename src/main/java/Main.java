import Model.Account;
import Model.Message;
import Repository.SocialMediaRepository;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    private static void print(Object ...o)
    {
        for(Object O : o)
        {
            System.out.print(O.toString());
        }
    }
    private static void println(Object ...o)
    {
        for(Object O : o)
        {
            print(O.toString());
        }
        print("\n");
    }
    public static void main(String[] args) {
        //SocialMediaController controller = new SocialMediaController();
        //Javalin app = controller.startAPI();
        //app.start(8080);


        String username = "NEW_USER3453466";
        String password = "1234";
        SocialMediaRepository repository = SocialMediaRepository.getRepository();
        //service.resetDB();

        println("Print Register Acc Test");
        Account account = new Account(username, password);
        Account reg_test = repository.processNewAccount(account);
        System.out.println(reg_test);


        username = "NEW_USERe4yheh";
        password = "1234";
        println("Print Register Acc Test2");
        reg_test = repository.processNewAccount(new Account(username, password));
        System.out.println(reg_test);


        //Account acc_test = service.login(username, password);
        //System.out.println(acc_test);

        Message newMsg = new Message(1, "My Message!", System.currentTimeMillis());

        Main.println("Printed Added Message1");
        Message added = repository.processNewMessage(newMsg);
        System.out.println(added);

        newMsg = new Message(2, "My Message!", System.currentTimeMillis());

        Main.println("Printed Added Message2");
        added = repository.processNewMessage(newMsg);
        System.out.println(added);

        println("Print Get Message By Id");
        Message retrieved = repository.getSingleMessage(1);
        System.out.println(retrieved);

        Main.println("Print getMessages");
        System.out.println(repository.getAllMessages());

        println("Update a message.");
        newMsg = new Message(1, "Next Message Text", 0);
        newMsg.setMessage_id(1);
        Message updatedMsg = repository.processUpdateMessage(newMsg);
        println(updatedMsg);
        println(repository.getSingleMessage(1));
        println(repository.getAllMessages());

        println("Get Messages By User");
        println(repository.getMessagesFromUser(1));
        println(repository.getMessagesFromUser(2));
    }
}
