import java.sql.Connection;
import java.sql.SQLException;

import Controller.SocialMediaController;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import Util.ConnectionUtil;
import io.javalin.Javalin;

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
        SocialMediaService service = SocialMediaService.getService();
        //service.resetDB();

        println("Print Register Acc Test");
        Account reg_test = service.registerAccount(username, password);
        System.out.println(reg_test);


        username = "NEW_USERe4yheh";
        password = "1234";
        println("Print Register Acc Test2");
        reg_test = service.registerAccount(username, password);
        System.out.println(reg_test);


        //Account acc_test = service.login(username, password);
        //System.out.println(acc_test);

        Message newMsg = new Message(1, "My Message!", System.currentTimeMillis());

        Main.println("Printed Added Message1");
        Message added = service.addMessage(newMsg);
        System.out.println(added);

        newMsg = new Message(2, "My Message!", System.currentTimeMillis());

        Main.println("Printed Added Message2");
        added = service.addMessage(newMsg);
        System.out.println(added);

        println("Print Get Message By Id");
        Message retrieved = service.getMessage(1);
        System.out.println(retrieved);

        Main.println("Print getMessages");
        System.out.println(service.getMessages());

        println("Update a message.");
        newMsg = new Message(0, "Next Message Text", 0);
        Message updatedMsg = service.updateMessage(1, newMsg);
        println(updatedMsg);
        println(service.getMessage(1));
        println(service.getMessages());

        println("Get Messages By User");
        println(service.getMessagesByUser(1));
        println(service.getMessagesByUser(2));




    }
}
