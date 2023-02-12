import Controller.SocialMediaController;
import Model.Account;
import Service.SocialMediaService;
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

        String username = "NEW_USER";
        String password = "1234";
        SocialMediaService service = SocialMediaService.getService();
        service.registerAccount(username, password);
        Account acc_test = service.login(username, password);
        System.out.println(acc_test);

    }
}
