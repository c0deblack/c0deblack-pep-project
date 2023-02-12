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
    public static void main(String[] args) {
        //SocialMediaController controller = new SocialMediaController();
        //Javalin app = controller.startAPI();
        //app.start(8080);


        String username = "NEW_USER3453466";
        String password = "1234";
        SocialMediaService service = SocialMediaService.getService();
        service.resetDB();
        Account reg_test = service.registerAccount(username, password);
        System.out.println(reg_test);

        //Account acc_test = service.login(username, password);
        //System.out.println(acc_test);

        Message newMsg = new Message(1, "My Message!", System.currentTimeMillis());

        Connection connection = ConnectionUtil.getConnection();

        Message added = service.addMessage(newMsg);
        System.out.println(added);
        
        System.out.println(service.getMessages());
        Message retrieved = service.getMessage(1);
        System.out.println(retrieved);




    }
}
