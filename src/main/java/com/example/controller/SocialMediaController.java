package com.example.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DataConflictException;
import com.example.exception.InvalidInputException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {

    MessageService messageService;
    AccountService accountService;

    public SocialMediaController(MessageService messageService, AccountService accountService) {
        this.messageService = messageService;
        this.accountService = accountService;
    }

    /**
     * Process new User registrations.
     * As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. 
     * The registration will be successful if and only if the username is not blank, the password is at least 
     * 4 characters long, and an Account with that username does not already exist. 
     * 
     * @param account The Account object created from the request body which will contain a representation of a JSON 
     *                Account, but will not contain an account_id.
     * @return ResponseEntity  
     *  -If all conditions are met, the response body should contain a JSON of the Account, including 
     *   its account_id. The response status should be 200 OK, which is the default. The new account should 
     *   be persisted to the database.
     * - If the registration is not successful due to a duplicate username, the response status should be 409. (Conflict)
     * - If the registration is not successful for some other reason, the response status should be 400. (Client error)
     */
    @PostMapping(value= "/register")
    public @ResponseBody ResponseEntity<Account> register(@RequestBody Account account) {
        try {
            Account newAccount = accountService.register(account);
            return ResponseEntity.status(200).body(newAccount);
        }
        catch(DataConflictException e) {
            return ResponseEntity.status(409).build();
        }
        catch(InvalidInputException e) {
            return ResponseEntity.status(400).build();
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(400).build();
        }
    }


    /**
     * ## 2: Our API should be able to process User logins.
     * 
     * As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
     * The request body will contain a JSON representation of an Account.

- The login will be successful if and only if the username and password provided in the request body JSON 
match a real account existing on the database. If successful, the response body should contain a JSON of the account in the response body, including its account_id. The response status should be 200 OK, which is the default.
- If the login is not successful, the response status should be 401. (Unauthorized)
*/
    @PostMapping(value="/login")
    public @ResponseBody ResponseEntity<Account> login(@RequestBody Account account) {
        //TODO
        try {
            Account newAccount = accountService.login(account);
            return ResponseEntity.status(200).body(newAccount);
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * ## 3: Our API should be able to process the creation of new messages.
     * 
     * As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.

- The creation of the message will be successful if and only if the message_text is not blank, is not over 255 characters, and posted_by refers to a real, existing user. If successful, the response body should contain a JSON of the message, including its message_id. The response status should be 200, which is the default. The new message should be persisted to the database.
- If the creation of the message is not successful, the response status should be 400. (Client error)
*/
    @PostMapping(value="/messages")
    public @ResponseBody ResponseEntity<Message> postMessage(@RequestBody Message message) {
        //TODO
        try {
            Message newMessage = messageService.postMessage(message);
            return ResponseEntity.status(200).body(newMessage);
        } 
        catch(RuntimeException e) {
            return ResponseEntity.status(400).build();
        }
    }

    /**
     * ## 4: Our API should be able to retrieve all messages.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

- The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
*/
    @GetMapping(value="/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMesssages() {
        //TODO
        List<Message> messages = messageService.getMesssages();
        return ResponseEntity.status(200).body(messages);
    }

    /**
     * ## 5: Our API should be able to retrieve a message by its ID.

As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

- The response body should contain a JSON representation of the message identified by the message_id. It is expected for 
the response body to simply be empty if there is no such message. The response status should always be 200, which is the default.

*/
    @GetMapping(value="/messages/{message_id}")
    public @ResponseBody ResponseEntity<Message> getMessage(@PathVariable int message_id) {
        //TODO
        Message newMessage = messageService.getMessage(message_id);
        return ResponseEntity.status(200).body(newMessage);
    }
    

    /**
     * ## 6: Our API should be able to delete a message identified by a message ID.
     * 
     * As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

- The deletion of an existing message should remove an existing message from the database. If the message existed,
 the response body should contain the number of rows updated (1). The response status should be 200, which is the default.
- If the message did not exist, the response status should be 200, but the response body should be empty. 
This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.

*/
    @DeleteMapping(value="/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> deleteMessage(@PathVariable int messasge_id) {
        // TODO
        int retVal = messageService.deleteMessage(messasge_id);
        if(retVal == 1) {
            return ResponseEntity.status(200).body(1);
        }
        else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        
    }

    /**
     * ## 7: Our API should be able to update a message text identified by a message ID.
     * As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. 
     * The request body should contain a new message_text values to replace the message identified by message_id. 
     * The request body can not be guaranteed to contain any other information.
     * 
     * The update of a message should be successful if and only if the message id already exists and the new message_text 
     * is not blank and is not over 255 characters. If the update is successful, the response body should contain the number 
     * of rows updated (1), and the response status should be 200, which is the default. The message existing on the database 
     * should have the updated message_text.
     * If the update of the message is not successful for any reason, the response status should be 400. (Client error)
     * 
     * @param message_id A unique identifier for the message (primary key)
     * @return Message
     */
    @PatchMapping(value="/messages/{message_id}") 
    public @ResponseBody ResponseEntity<Message> updateMessage(@PathVariable int message_id, @RequestBody String message_text) {
        //TODO
        try {
            Message newMessage = messageService.updateMessage(message_id, message_text);
            return ResponseEntity.status(200).body(newMessage);
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(400).build();
        }
    }

    /**
     * ## 8: Our API should be able to retrieve all messages written by a particular user.

As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.

- The response body should contain a JSON representation of a list containing all messages posted by a particular user, 
which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.

   */
    @GetMapping(value="/accounts/{account_id}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAccountMessages(@PathVariable int account_id) {
        //TODO
        List<Message> messages = messageService.getAccountMessages(account_id);
        return ResponseEntity.status(200).body(messages);
    }

}
