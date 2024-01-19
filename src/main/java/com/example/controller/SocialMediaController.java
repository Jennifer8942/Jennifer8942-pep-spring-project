package com.example.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * Endpoints and handlers for your controller using Spring. The endpoints use the @GET/POST/PUT/DELETE/etc 
 * Mapping annotations where applicable as well as the @ResponseBody and @PathVariable annotations. 
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
     * The registration will be successful if it meets the requirments of AccountService.Register().
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
     * Process User logins.
     * As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
     * The login will be sucessful if the username and password meet the requirements in AccountService.Login()
     *
     * @param account created vrom the request body which will contain a JSON representation of an Account.
     * @return ResponseEntity 
     * - If successful, the response body should contain a JSON of the account in the response body, including its account_id. 
     *   The response status should be 200 OK, which is the default. 
     * - If the login is not successful, the response status should be 401. (Unauthorized)
     */
    @PostMapping(value="/login")
    public @ResponseBody ResponseEntity<Account> login(@RequestBody Account account) {
        try {
            Account newAccount = accountService.login(account);
            return ResponseEntity.status(200).body(newAccount);
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * Process the creation of new messages.
     * As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages.
     * The creation of the message will be successful if the message meets the requirements in MessageService.postMessage
     *
     * @param message created from the request body which will contain a JSON representation of a message,
     *  but will not contain a message_id.
     * @return ResponseEntity 
     * - If successful, the response body should contain a JSON of the message, including its message_id. 
     *   The response status should be 200, which is the default. The new message should be persisted to the database.
     * - If the creation of the message is not successful, the response status should be 400. (Client error)
     */
    @PostMapping(value="/messages")
    public @ResponseBody ResponseEntity<Message> postMessage(@RequestBody Message message) {
        if(message != null){ 
            try {
                Message newMessage = messageService.postMessage(message);
                return ResponseEntity.status(200).body(newMessage);
            } 
            catch(RuntimeException e) {
                return ResponseEntity.status(400).build();
            }
        }
        else {
            return ResponseEntity.status(400).build();
        }
    }

    /**
     * Retrieve all messages.
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
     * 
     * @return ResponseEntity
     * - The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
     * - It is expected for the list to simply be empty if there are no messages. 
     *   The response status should always be 200, which is the default.
     */
    @GetMapping(value="/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMesssages() {
        List<Message> messages = messageService.getMesssages();
        return ResponseEntity.status(200).body(messages);
    }

    /**
     * Retrieve a message by its ID.
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.
     * 
     * @param message_id as a path variable
     * @return ResponseEntity
     * The response body should contain a JSON representation of the message identified by the message_id. 
     * It is expected for the response body to simply be empty if there is no such message. 
     * The response status should always be 200, which is the default.
     */
    @GetMapping(value="/messages/{message_id}")
    public @ResponseBody ResponseEntity<Message> getMessage(@PathVariable int message_id) {
        Message newMessage = messageService.getMessage(message_id);
        if(newMessage != null ) {
            return ResponseEntity.status(200).body(newMessage);
        } else {
            return ResponseEntity.status(200).build();
        }
    }
    
    /**
     * Delete a message identified by a message ID.
     * 
     * As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.
     * The deletion of an existing message should remove an existing message from the database.
     * 
     * @param message_id as a path variable
     * @return ResponseEntity
     * - If the message existed, the response body should contain the number of rows updated (1). 
     * - If the message did not exist, the response body should be empty. 
     * The response status should be 200, which is the defaultThis is because the DELETE verb is intended to be idempotent, 
     * ie, multiple calls to the DELETE endpoint should respond with the same type of response.
     */
    @DeleteMapping(value="/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> deleteMessage(@PathVariable Integer message_id) {
        int retVal = messageService.deleteMessage(message_id);
        if(retVal > 0) {
            return ResponseEntity.status(200).body(1);
        }
        else {
            return ResponseEntity.status(200).build();
        }
    }

    /**
     * Update a message text identified by a message ID.
     * As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. 
     * The update of a message should be successful if and only if the parameters meet the requirements 
     * in MessageService.updateMessage. The message existing in the database should have the updated message_text.
     * 
     * @param message_id as a path variable. A unique identifier for the message (primary key)
     * @param message_text The request body should contain json name value pairs with the value for
     *        message_text. The request body can not be guaranteed to contain any other information. 
     * @return ResponseEnity
     * - If the update is successful, the response body should contain the number of rows updated (1), and the response 
     *   status should be 200, which is the default. 
     * - If the update of the message is not successful for any reason, the response status should be 400. (Client error)
     */
    @PatchMapping(value="/messages/{message_id}") 
    public @ResponseBody ResponseEntity<Integer> updateMessage(@PathVariable Integer message_id, @RequestBody Map<String,String> message_text) {
        String text = null;
        if(message_text != null) {
            text = (String) message_text.get("message_text");
        }
        try {
            Integer rows = messageService.updateMessage(message_id, text);
            if(rows > 0) {
                return ResponseEntity.status(200).body(rows);
            } else {
                return ResponseEntity.status(400).build();
            }   
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(400).build();
        }
    }

    /**
     * Retrieve all messages written by a particular user.
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.
     * 
     * @param account_id as a path variable
     * @return ResponseEntity
     * - The response body should contain a JSON representation of a list containing all messages posted by a particular user, 
     *   which is retrieved from the database. 
     * - It is expected for the list to simply be empty if there are no messages. 
     * The response status should always be 200, which is the default. 
     */
    @GetMapping(value="/accounts/{account_id}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAccountMessages(@PathVariable Integer account_id) {
        List<Message> messages = messageService.getAccountMessages(account_id);
        return ResponseEntity.status(200).body(messages);
    }
}
