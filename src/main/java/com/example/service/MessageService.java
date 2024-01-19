package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidInputException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import jdk.jfr.Timestamp;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    
    /**
     * ## 3: Our API should be able to process the creation of new messages.
     * 
     * The creation of the message will be successful if and only if the message_text is not blank, 
     * is not over 255 characters, and posted_by refers to a real, existing user. 
     * 
     * @param message The message to insert into the database, no message_id
     * @return Message The saved message with message_id
     * @throws InvalidInputException if the message fields do not meet requirements
    */
    public Message postMessage(Message message) throws InvalidInputException{
        if(message != null && message.getMessage_text() != null && message.getPosted_by() != null
            && message.getMessage_text().length() <= 255 && message.getMessage_text().length() > 0) 
        {
            // throws RuntimeException on insert if posted_by does not exist as an account_id in account tbl.
            // TODO should this explicitly check if the posted_by account exists?  currently it works as is.
            Message newMessage = messageRepository.save(message);
            return newMessage;
        }
        else { 
            throw new InvalidInputException(); 
        }
    }

    /**
     * ## 4: Our API should be able to retrieve all messages.
     * 
     * @return List<Messages> a list containing all messages retrieved from the database. 
     *                        It is expected for the list to simply be empty if there are no messages. 
     */
    public List<Message> getMesssages() {
        return messageRepository.findAll();
    }

    /**
     * ## 5: Our API should be able to retrieve a message by its ID.
     * 
     * @param message_id message identifier
     * @return Message The identified message retrieved from the database.  Null if does not exist.
     */
    public Message getMessage(int message_id) {
          Optional<Message> messageO = messageRepository.findById(message_id);
        if(messageO.isPresent()) {
            return messageO.get();
        }
        else {
            return null;
        }
    }


    /**
     * ## 6: Our API should be able to delete a message identified by a message ID.
     * 
     * @param message_id message identifier
     * @return Integer number of rows updated (if message deleted (1) or (0) if no record existed)
     */
    public Integer deleteMessage(Integer message_id) {
        if(messageRepository.existsById(message_id)) {
            messageRepository.deleteById(message_id);
            return 1;
        }
        return 0;
    }

    /**
     * ## 7: Our API should be able to update a message text identified by a message ID.
     * 
     * The update of a message should be successful if and only if the message id already exists and the 
     * new message_text is not blank and is not over 255 characters. 
     * 
     * @param message_id
     * @param message_text
     * @return Integer the number of rows updated (0 or 1)
     * @throws InvalidInputException if the message_id does not exist or the message_text does not
     *         meed requirements.
     */
    public Integer updateMessage(Integer message_id, String message_text) throws InvalidInputException {
        Message message = messageRepository.getById(message_id);
        if(message != null && message_text != null && message_text.length() <= 255 && message_text.length() > 0) {
            message.setMessage_text(message_text);
            messageRepository.save(message);
            return Integer.valueOf(1);
        }
        else {
            throw new InvalidInputException();
        }
    }

    /**
     * ## 8: Our API should be able to retrieve all messages written by a particular user.
     * 
     * @param account_id unique identifier for accounts
     * @return List<Messages> all messages posted by the identified account, empty if none exist.
     */
    public List<Message> getAccountMessages(int account_id) {
        List<Message> messages = messageRepository.findAllMessagesByPostedBy(account_id);
        return messages;
    }
}
