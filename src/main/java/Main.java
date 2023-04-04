import Controller.SocialMediaController;
import Model.Account;
import Model.Message;
import Repository.SocialMediaRepository;
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
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);
    }
}
