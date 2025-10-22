package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.OperationLog;
import com.yangshengzhou.backend.repository.OperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OperationLogService {
    
    @Autowired
    private OperationLogRepository operationLogRepository;
    
    /**
     * 记录操作日志
     */
    public OperationLog logOperation(Long userId, String operationType, String description, String ipAddress) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setOperationType(operationType);
        log.setDescription(description);
        log.setIpAddress(ipAddress);
        log.setOperationTime(new Date());
        
        return operationLogRepository.save(log);
    }
    
    /**
     * 获取用户的操作日志
     */
    public Page<OperationLog> getUserOperationLogs(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findByUserId(userId, pageable);
    }
    
    /**
     * 获取用户指定类型的操作日志
     */
    public Page<OperationLog> getUserOperationLogsByType(Long userId, String operationType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findByUserIdAndOperationType(userId, operationType, pageable);
    }
    
    /**
     * 获取指定时间范围内的操作日志
     */
    public List<OperationLog> getOperationLogsByTimeRange(Date startTime, Date endTime) {
        return operationLogRepository.findByOperationTimeBetween(startTime, endTime);
    }
    
    /**
     * 获取用户在指定时间范围内的操作日志
     */
    public List<OperationLog> getUserOperationLogsByTimeRange(Long userId, Date startTime, Date endTime) {
        return operationLogRepository.findByUserIdAndOperationTimeBetween(userId, startTime, endTime);
    }
    
    /**
     * 获取所有操作日志（管理员用）
     */
    public Page<OperationLog> getAllOperationLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findAll(pageable);
    }
    
    /**
     * 获取指定类型的所有操作日志（管理员用）
     */
    public Page<OperationLog> getOperationLogsByType(String operationType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findByOperationType(operationType, pageable);
    }
}