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

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public void logOperation(String userId, String operationType, String targetType, String targetId, String description, String ipAddress, String userAgent) {

        
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
    public List<OperationLogVO> getUserOperationLogs(String userId) {
        List<OperationLog> logs = operationLogRepository.findByUserId(userId);
        return convertToVOs(logs);
    }
    
    /**
     * 根据操作类型获取用户的操作日志
     */
    public List<OperationLogVO> getUserOperationLogsByType(String userId, String operationType) {
        List<OperationLog> logs = operationLogRepository.findByUserIdAndOperationType(userId, operationType);
        return convertToVOs(logs);
    }
    
    /**
     * 获取指定时间范围内的用户操作日志
     */
    public List<OperationLogVO> getUserOperationLogsByTimeRange(String userId, Date startTime, Date endTime) {
        LocalDateTime startDateTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        List<OperationLog> logs = operationLogRepository.findByUserIdAndCreatedTimeBetween(userId, startDateTime, endDateTime);
        return convertToVOs(logs);
    }
    
    /**
     * 统计用户操作次数
     */
    public Long countUserOperations(String userId) {
        return operationLogRepository.countByUserId(userId);
    }
    
    /**
     * 统计用户指定操作类型的次数
     */
    public Long countUserOperationsByType(String userId, String operationType) {
        return operationLogRepository.countByUserIdAndOperationType(userId, operationType);
    }
    
    /**
     * 分页获取用户的操作日志
     */
    public Page<OperationLog> getUserOperationLogs(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        return operationLogRepository.findByUserId(userId, pageable);
    }
    
    /**
     * 分页获取用户指定类型的操作日志
     */
    public Page<OperationLog> getUserOperationLogsByType(String userId, String operationType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        return operationLogRepository.findByUserIdAndOperationType(userId, operationType, pageable);
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
        List<OperationLog> logs = operationLogRepository.findByOperationTypeOrderByCreatedTimeDesc(operationType);
        return convertToVOs(logs);
    }
    
    /**
     * 获取指定时间范围内的所有操作日志（管理员）
     */
    public List<OperationLogVO> getAllOperationLogsByTimeRange(Date startTime, Date endTime) {
        LocalDateTime startDateTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        List<OperationLog> logs = operationLogRepository.findByCreatedTimeBetween(startDateTime, endDateTime);
        return convertToVOs(logs);
    }
    
    /**
     * 清理过期日志
     */
    public int cleanExpiredLogs(int daysToKeep) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -daysToKeep);
        Date cutoffDate = calendar.getTime();
        LocalDateTime cutoffDateTime = cutoffDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        
        List<OperationLog> expiredLogs = operationLogRepository.findByCreatedTimeBefore(cutoffDateTime);
        operationLogRepository.deleteAll(expiredLogs);
        
        return expiredLogs.size();
    }
    
    /**
     * 分页获取所有操作日志（管理员用）
     */
    public Page<OperationLog> getAllOperationLogs(int page, int size, String level, String module, String startDate, String endDate) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        return operationLogRepository.findAll(pageable);
    }
    
    /**
     * 分页获取指定类型的所有操作日志（管理员用）
     */
    public Page<OperationLog> getOperationLogsByType(String operationType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        return operationLogRepository.findByOperationType(operationType, pageable);
    }
    
    /**
     * 转换为VO对象
     */
    public List<OperationLogVO> convertToVOs(List<OperationLog> logs) {
        List<OperationLogVO> vos = new ArrayList<>();
        
        for (OperationLog log : logs) {
            OperationLogVO vo = new OperationLogVO();
            vo.setId(log.getId());
            vo.setUserId(log.getUserId());
            vo.setOperationType(log.getOperationType());
            vo.setTargetType(log.getTargetType());
            vo.setTargetId(log.getTargetId());
            vo.setDescription(log.getDescription());
            vo.setIpAddress(log.getIpAddress());
            vo.setUserAgent(log.getUserAgent());
            vo.setCreateTime(Date.from(log.getCreatedTime().atZone(ZoneId.systemDefault()).toInstant()));
            
            // 获取用户邮箱
            Optional<User> userOptional = userRepository.findById(log.getUserId());
            if (userOptional.isPresent()) {
                vo.setUsername(userOptional.get().getEmail());
            }
            
            // 根据目标类型和ID获取目标名称
            if (log.getTargetType() != null && log.getTargetId() != null) {
                String targetName = getTargetName(log.getTargetType(), log.getTargetId());
                vo.setTargetName(targetName);
            }
            
            vos.add(vo);
        }
        
        return vos;
    }
    
    /**
     * 根据目标类型和ID获取目标名称
     */
    private String getTargetName(String targetType, String targetId) {
        try {
            switch (targetType) {
                case "USER":
                    Optional<User> userOptional = userRepository.findById(targetId);
                    return userOptional.map(User::getEmail).orElse("未知用户");
                case "FILE":
                    // 这里需要根据实际的文件服务来获取文件名
                    // 由于没有文件仓库，暂时返回默认值
                    return "文件_" + targetId;
                case "FOLDER":
                    // 这里需要根据实际的文件夹服务来获取文件夹名
                    // 由于没有文件夹仓库，暂时返回默认值
                    return "文件夹_" + targetId;
                default:
                    return targetType + "_" + targetId;
            }
        } catch (Exception e) {
            return targetType + "_" + targetId;
        }
    }
    
    /**
     * 清空所有日志数据
     */
    public void clearAllLogs() {
        operationLogRepository.deleteAll();
    }
}