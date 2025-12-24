package com.yangshengzhou.backend.event;

import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class StorageUpdateEventListener {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 异步处理存储更新事件
     */
    @Async
    @EventListener
    public void handleStorageUpdate(StorageUpdateEvent event) {
        try {
            User user = userRepository.findById(event.getUserId()).orElse(null);
            if (user != null) {
                if (event.isIncrease()) {
                    // 增加存储使用量
                    user.setUsedStorage(user.getUsedStorage() + event.getFileSize());
                } else {
                    // 减少存储使用量
                    user.setUsedStorage(Math.max(0, user.getUsedStorage() - event.getFileSize()));
                }
                userRepository.save(user);
            }
        } catch (Exception e) {
            // 处理存储更新事件失败，静默处理
        }
    }
}