package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    // Create a new message by the user
    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return null;
        }

        Optional<Account> account = accountService.getAccountById(message.getPostedBy());
        if (account.isEmpty()) {
            return null;
        }
        return messageRepository.save(message);
    }

    // Retrieve all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Retrieve message by ID
    public Optional<Message> getMessageById(Integer messageId){
        return messageRepository.findById(messageId);
    }

    // Delete a message by ID
    public Integer deleteMessageById(Integer messageId){
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    // Retrieve messages by account ID
    public List<Message> getMessagesByAccountId(Integer accountId) {
        return accountRepository.findById(accountId)
                .map(account -> messageRepository.findByPostedBy(account.getAccountId()))
                .orElseGet(List::of);
    }

    public boolean updateMessage(Integer messageId, String messageText) {
        // TODO Auto-generated method stub
        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {
            return false;
        }

        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            Message existingMessage = optionalMessage.get();
            existingMessage.setMessageText(messageText);
            messageRepository.save(existingMessage);
            return true;
        }

        return false;
        
    }
}
