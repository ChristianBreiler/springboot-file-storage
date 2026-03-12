package com.example.springbootfilestorage.scripts.schedules;

import com.example.springbootfilestorage.dao.Message;
import com.example.springbootfilestorage.repository.MessageRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// Delete old messages older than 2 weeks every Monday at 1 AM (00:01:00)
@Service
public class DeleteOldMessagesJob {
    private final MessageRepository messageRepository;

    public DeleteOldMessagesJob(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Scheduled(cron = "0 0 1 ? * MON")
    public void deleteOldMessages() {
        LocalDate today = LocalDate.now();
        List<Message> oldMessages = messageRepository.findAllByCreatedAtAfter(today.minusWeeks(2));
        messageRepository.deleteAll(oldMessages);
        if (oldMessages.isEmpty()) System.out.println("No old messages deleted");
        else oldMessages.forEach(message -> {
            System.out.println("Deleted message: " + message.getMessage());
        });
    }
}
