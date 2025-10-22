package com.yangshengzhou.backend.service;

import com.yangshengzhou.backend.entity.OperationLog;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.vo.OperationLogVO;
import com.yangshengzhou.backend.repository.OperationLogRepository;
import com.yangshengzhou.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Calendar;

@Service
public class OperationLogService {
    
    @Autowired
    private OperationLogRepository operationLogRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 记录操作日志
     */
    public void logOperation(Long userId, String operationType, String targetType, Long targetId, String targetName, String description, String ipAddress, String userAgent) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setOperationType(operationType);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setDescription(description);
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        
        operationLogRepository.save(log);
    }
    
    /**
     * 获取用户的操作日志
     */
    public List<OperationLogVO> getUserOperationLogs(Long userId) {
        List<OperationLog> logs = operationLogRepository.findByUserId(userId);
        return convertToVOs(logs);
    }
    
    /**
     * 根据操作类型获取用户的操作日志
     */
    public List<OperationLogVO> getUserOperationLogsByType(Long userId, String operationType) {
        List<OperationLog> logs = operationLogRepository.findByUserIdAndOperationType(userId, operationType);
        return convertToVOs(logs);
    }
    
    /**
     * 获取指定时间范围内的用户操作日志
     */
    public List<OperationLogVO> getUserOperationLogsByTimeRange(Long userId, Date startTime, Date endTime) {
        List<OperationLog> logs = operationLogRepository.findByUserIdAndOperationTimeBetween(userId, startTime, endTime);
        return convertToVOs(logs);
    }
    
    /**
     * 获取所有操作日志（管理员）
     */
    public List<OperationLogVO> getAllOperationLogs() {
        List<OperationLog> logs = operationLogRepository.findAll();
        return convertToVOs(logs);
    }
    
    /**
     * 获取指定操作类型的所有操作日志（管理员）
     */
    public List<OperationLogVO> getAllOperationLogsByType(String operationType) {
        List<OperationLog> logs = operationLogRepository.findByOperationType(operationType);
        return convertToVOs(logs);
    }
    
    /**
     * 获取指定时间范围内的所有操作日志（管理员）
     */
    public List<OperationLogVO> getAllOperationLogsByTimeRange(Date startTime, Date endTime) {
        List<OperationLog> logs = operationLogRepository.findByOperationTimeBetween(startTime, endTime);
        return convertToVOs(logs);
    }
    
    /**
     * 统计用户操作次数
     */
    public Long countUserOperations(Long userId) {
        return operationLogRepository.countByUserId(userId);
    }
    
    /**
     * 统计用户指定操作类型的次数
     */
    public Long countUserOperationsByType(Long userId, String operationType) {
        return operationLogRepository.countByUserIdAndOperationType(userId, operationType);
    }
    
    /**
     * 清理过期日志
     */
    public int cleanExpiredLogs(int daysToKeep) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -daysToKeep);
        Date cutoffDate = calendar.getTime();
        
        List<OperationLog> expiredLogs = operationLogRepository.findByOperationTimeBefore(cutoffDate);
        operationLogRepository.deleteAll(expiredLogs);
        
        return expiredLogs.size();
    }
    
    /**
     * 分页获取用户的操作日志
     */
    public Page<OperationLog> getUserOperationLogs(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findByUserId(userId, pageable);
    }
    
    /**
     * 分页获取用户指定类型的操作日志
     */
    public Page<OperationLog> getUserOperationLogsByType(Long userId, String operationType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findByUserIdAndOperationType(userId, operationType, pageable);
    }
    
    /**
     * 分页获取所有操作日志（管理员用）
     */
    public Page<OperationLog> getAllOperationLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findAll(pageable);
    }
    
    /**
     * 分页获取指定类型的所有操作日志（管理员用）
     */
    public Page<OperationLog> getOperationLogsByType(String operationType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findByOperationType(operationType, pageable);
    }
    
    /**
     * 转换为VO对象
     */
    private List<OperationLogVO> convertToVOs(List<OperationLog> logs) {
        List<OperationLogVO> vos = new ArrayList<>();
        
        for (OperationLog log : logs) {
            OperationLogVO vo = new OperationLogVO();
            vo.setId(log.getId());
            vo.setUserId(log.getUserId());
            vo.setOperationType(log.getOperationType());
            vo.setTargetType(log.getTargetType());
            vo.setTargetId(log.getTargetId());
            vo.setTargetName(log.getTargetName());
            vo.setDescription(log.getDescription());
            vo.setIpAddress(log.getIpAddress());
            vo.setUserAgent(log.getUserAgent());
            vo.setCreateTime(log.getOperationTime());
            
            // 获取用户名
            Optional<User> userOptional = userRepository.findById(log.getUserId());
            if (userOptional.isPresent()) {
                vo.setUsername(userOptional.get().getUsername());
            }
            
            vos.add(vo);
        }
        
        return vos;
    }
}