<template>
  <div class="admin-system">
    <el-card class="system-card">
      <template #header>
        <div class="card-header">
          <span>系统设置</span>
          <el-button type="primary" disabled>
            保存设置
          </el-button>
        </div>
      </template>
      
      <div class="system-content">
        <div class="placeholder-content">
          <el-icon size="64" color="#909399"><Plus /></el-icon>
          <h3>系统设置功能开发中</h3>
          <p>该功能正在规划开发，敬请期待...</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Plus } from '@element-plus/icons-vue'
</script>

<style scoped>
.admin-system {
  padding: 20px;
}

.system-card {
  min-height: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.placeholder-content {
  text-align: center;
  color: #909399;
  padding: 80px 0;
}

.placeholder-content h3 {
  margin: 16px 0 8px;
  font-size: 18px;
  font-weight: 500;
}

.placeholder-content p {
  margin: 0;
  font-size: 14px;
}
</style>