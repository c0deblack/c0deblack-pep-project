package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * This class encapsulates the Javalin Web Server APIs. It defines numerous enpoints and callbacks
 * for handling use interaction with them.
 *
 * <br><br>
 *
 * The following endpoints and callbacks are defined:
 *
 * <pre>
 *         app.post("/register", {@link SocialMediaController#register(Context)} );
 *         app.post("/login", {@link SocialMediaController#login(Context)} );
 *         app.post("/messages", {@link SocialMediaController#postMessage(Context)} );
 *         app.get("/messages", {@link SocialMediaController#getMessage(Context)} );
 *         app.get("/messages/{message_id}", {@link SocialMediaController#getMessageFromId(Context)} );
 *         app.delete("/messages/{message_id}", {@link SocialMediaController#deleteMessageById(Context)} );
 *         app.patch("/messages/{message_id}", {@link SocialMediaController#updateMessage(Context)} );
 *         app.get("/accounts/{account_id}/messages", {@link SocialMediaController#getMessagesByUser(Context)} );
 * </pre>
 */
public class SocialMediaController {
    SocialMediaService service = SocialMediaService.getService();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::register);
        app.post("/login", this::login);
        app.post("/messages", this::postMessage);
        app.get("/messages", this::getMessage);
        app.get("/messages/{message_id}", this::getMessageFromId);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUser);


        return app;
    }

    private void 
    register(Context context) 
    throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account recieved = mapper.readValue(context.body(), Account.class);
        Account output = service.registerAccount(recieved.getUsername(), recieved.getPassword());

        if(output == null) context.status(400);
        else 
        {
            context.json(mapper.writeValueAsString(output));
            context.status(200);
        }
    }


    private void 
    login(Context context) 
    throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account recieved = mapper.readValue(context.body(), Account.class);
        Account output = service.login(recieved.getUsername(), recieved.getPassword());

        if(output == null) context.status(401);
        else 
        {
            context.json(mapper.writeValueAsString(output));
            context.status(200);
        }
    }


    private void 
    postMessage(Context context) 
    throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message recieved = mapper.readValue(context.body(), Message.class);
        Message output = service.addMessage(recieved);

        if(output == null) context.status(400);
        else 
        {
            context.json(mapper.writeValueAsString(output));
            context.status(200);
        }
    }


    private void 
    getMessage(Context context) 
    throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> output = service.getMessages();

        context.json(mapper.writeValueAsString(
                Objects.requireNonNullElseGet(output, () -> new ArrayList<Message>())));
        context.status(200);
    }


    private void 
    getMessageFromId(Context context) 
    throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        String id = context.pathParam("message_id");
        Message output = service.getMessage(Integer.parseInt(id));

        if(output != null)
            context.json(mapper.writeValueAsString(output));

        context.status(200);
    }

    private void 
    deleteMessageById(Context context) 
    throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        String id = context.pathParam("message_id");
        Message output = service.deleteMessage(Integer.parseInt(id));

        if(output != null)
            context.json(mapper.writeValueAsString(output));
        context.status(200);
    }


    private void 
    updateMessage(Context context)
    throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message received = mapper.readValue(context.body(), Message.class);
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message output = service.updateMessage(id, received);

        if(output == null)
        {
            context.status(400);
        }
        else 
        {
            context.json(mapper.writeValueAsString(output));
            context.status(200);
        }
    }


    private void 
    getMessagesByUser(Context context)
    {
        String id = context.pathParam("account_id");
        List<Message> output = service.getMessagesByUser(Integer.parseInt(id));

        context.json(Objects.requireNonNullElseGet(output, () -> new ArrayList<Message>()));

        context.status(200);
    }
}