package com.harshith.service;

import com.harshith.model.SupportMessage;
import com.harshith.repository.SupportMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportMessageServiceImpl implements SupportMessageService {

    @Autowired
    private SupportMessageRepository repository;

    @Override
    public SupportMessage saveSupportMessage(SupportMessage message) {
        return repository.save(message);
    }

    @Override
    public List<SupportMessage> getAllMessages() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public SupportMessage updateMessageReply(Long id, String adminReply) {
        SupportMessage message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setAdminReply(adminReply); // Use the updated field name "adminReply"
        return repository.save(message); // Save the updated message
    }

    @Override
    public List<SupportMessage> getMessagesByUserId(Long userId) {
        return repository.findAll().stream()
                .filter(msg -> userId.equals(msg.getUserId())) // Filter messages by userId
                .collect(Collectors.toList());
    }
}
