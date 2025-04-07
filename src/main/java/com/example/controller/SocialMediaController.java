package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
 @RequestMapping("/")
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository messageRepository;

    // Register a new account
    @PostMapping("/register")
    public Account register(@RequestBody Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }

        if (account.getPassword() == null || account.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }

        Optional<Account> existing = accountRepository.findByUsername(account.getUsername());
        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");
        }

        return accountRepository.save(account);
    }

    // Create a message
    @PostMapping("/messages")
    public Message createMessage(@RequestBody Message message) {
        
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text cannot be blank");
        }

        if (message.getMessageText().length() > 255){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message words can't exceed 255 words");
        }

        if (message.getPostedBy() == null || message.getTimePostedEpoch() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields");
        }

        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            return createdMessage;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message FAILED ✗ to be created");
    }

    // Retrieve all messages
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // Delete a message by ID
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
        Integer result = messageService.deleteMessageById(messageId);

        if (result == 1) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok().build();
        }
    }
    

    // Update message text by ID
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
        if (messageService.updateMessage(messageId, message.getMessageText())) {
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.badRequest().body(0);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);

        if (account == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<Message> messages = messageRepository.findByPostedBy(accountId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        return message.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.ok().build());
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount.isPresent() && optionalAccount.get().getPassword().equals(password)) {
            return ResponseEntity.ok(optionalAccount.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
