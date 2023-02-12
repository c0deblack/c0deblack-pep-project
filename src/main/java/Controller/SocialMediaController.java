package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
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
        app.get("/{account_id}/messages", this::getMessagesByUser);


        return app;
    }

    private void 
    register(Context context) 
    throws JsonMappingException, JsonProcessingException
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
    throws JsonMappingException, JsonProcessingException
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
    throws JsonMappingException, JsonProcessingException
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
    throws JsonMappingException, JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> output = service.getMessages();

        if(output == null)
        {
            context.json(mapper.writeValueAsString(new ArrayList<Message>()));
        }
        else 
        {
            context.json(mapper.writeValueAsString(output));
        }
        context.status(200);
    }


    private void 
    getMessageFromId(Context context) 
    throws JsonMappingException, JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        String id = context.pathParam("message_id");
        Message output = service.getMessage(Integer.valueOf(id));

        if(output == null)
        {
        }
        else 
        {
            context.json(mapper.writeValueAsString(output));
        }
        context.status(200);
    }

    private void 
    deleteMessageById(Context context) 
    throws JsonMappingException, JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        String id = context.pathParam("message_id");
        Message output = service.deleteMessage(Integer.valueOf(id));

        if(output == null)
        {
        }
        else 
        {
            context.json(mapper.writeValueAsString(output));
        }
        context.status(200);
    }


    private void 
    updateMessage(Context context) throws JsonMappingException, JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        String id = context.pathParam("message_id");
        Message output = service.deleteMessage(Integer.valueOf(id));

        if(output == null)
        {
        }
        else 
        {
            context.json(mapper.writeValueAsString(output));
        }
        context.status(200);
    }


    private void 
    getMessagesByUser(Context context) 
    throws JsonMappingException, JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        String id = context.pathParam("account_id");
        List<Message> output = service.getMessages(Integer.valueOf(id));

        if(output == null)
        {
        }
        else 
        {
            context.json(mapper.writeValueAsString(output));
        }
        context.status(200);
    }
}