package com.harshith.service;

import com.harshith.model.SupportMessage;
import com.harshith.repository.SupportMessageRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok annotation for constructor injection
public class SupportMessageServiceImpl implements SupportMessageService {

    private final SupportMessageRepository repository;

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
                .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + id));
        message.setAdminReply(adminReply);
        return repository.save(message);
    }

    @Override
    public List<SupportMessage> getMessagesByUserId(Long userId) {
        // This is more efficient than loading all messages into memory
        return repository.findByUserId(userId);
    }
}
