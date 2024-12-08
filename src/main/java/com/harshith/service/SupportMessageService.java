package com.harshith.service;

import com.harshith.model.SupportMessage;
import java.util.List;

public interface SupportMessageService {

    SupportMessage saveSupportMessage(SupportMessage message);

    List<SupportMessage> getAllMessages();

    SupportMessage updateMessageReply(Long id, String adminReply);

    List<SupportMessage> getMessagesByUserId(Long userId);
}
