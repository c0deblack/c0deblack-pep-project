package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Repository.SocialMediaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
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
    SocialMediaRepository repository = SocialMediaRepository.getRepository();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.reflectClientOrigin = true;
                });
            });
        });

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
        Account received = mapper.readValue(context.body(), Account.class);

        if(repository.checkIfUserExist(received.username) == null)
        {
            Account output = repository.processNewAccount(received.getUsername(), received.getPassword());

            if(output != null)
            {
                context.json(mapper.writeValueAsString(output));
                context.status(200);
                return;
            }
        }
        context.status(400);
    }


    private void 
    login(Context context) 
    throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account received = mapper.readValue(context.body(), Account.class);
        Account output = repository.processLogin(received);

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
        Message received = mapper.readValue(context.body(), Message.class);
        Message output = repository.processNewMessage(received);

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
        List<Message> output = repository.getAllMessages();

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
        Message output = repository.getSingleMessage(Integer.parseInt(id));

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
        Message output = repository.processDeleteMessage(Integer.parseInt(id));

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
        received.setMessage_id(id);
        Message output = repository.processUpdateMessage(received);

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
        List<Message> output = repository.getMessagesFromUser(Integer.parseInt(id));

        context.json(Objects.requireNonNullElseGet(output, () -> new ArrayList<Message>()));

        context.status(200);
    }
}