package com.example.springbootfilestorage.service;

import com.example.springbootfilestorage.dao.Message;
import com.example.springbootfilestorage.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(String msg) {
        Message message = new Message();
        message.setMessage(msg);
        message.setCreatedAt(LocalDate.now());
        messageRepository.save(message);
    }

    public List<Message> findAllMessagesWithMax(int maxMessages) {
        return messageRepository.findAllMessagesWithLimit(maxMessages);
    }
}