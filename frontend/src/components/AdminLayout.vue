<template>
  <div class="admin-layout">
    <!-- 顶部导航栏 -->
    <header class="admin-header">
      <div class="header-left">
        <h1 class="logo">枫叶网盘 - 管理员控制台</h1>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="32" :src="userAvatar">
              <el-icon><User /></el-icon>
            </el-avatar>
            <span class="username">{{ store.user?.username || '管理员' }}</span>
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="userMode">切换到用户模式</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <div class="admin-container">
      <!-- 侧边栏 -->
      <aside class="admin-sidebar">
        <el-menu
          :default-active="activeMenu"
          class="admin-menu"
          router
          unique-opened
        >
          <el-menu-item index="/admin">
            <el-icon><Monitor /></el-icon>
            <span>管理员仪表盘</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/system">
            <el-icon><Setting /></el-icon>
            <span>系统设置</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/logs">
            <el-icon><Document /></el-icon>
            <span>操作日志</span>
          </el-menu-item>
        </el-menu>
      </aside>

      <!-- 主内容区域 -->
      <main class="admin-main">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, ArrowDown, Monitor, Setting, Document } from '@element-plus/icons-vue'
import store from '@/utils/store.js'

const router = useRouter()
const route = useRoute()

// 当前激活的菜单项
const activeMenu = computed(() => route.path)

// 用户头像
const userAvatar = computed(() => {
  return store.user?.avatar || ''
})

// 处理下拉菜单命令
const handleCommand = (command) => {
  if (command === 'logout') {
    // 退出登录
    store.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  } else if (command === 'userMode') {
    // 切换到用户模式
    router.push('/user')
  }
}

// 组件挂载时刷新存储信息
onMounted(() => {
  // 可以在这里添加管理员专属的初始化逻辑
})
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.admin-header {
  height: 60px;
  background-color: #545c64;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-left .logo {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
}

.header-right .user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #fff;
}

.username {
  margin: 0 8px;
}

.admin-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.admin-sidebar {
  width: 220px;
  background-color: #f5f5f5;
  border-right: 1px solid #e6e6e6;
}

.admin-menu {
  height: 100%;
  border-right: none;
}

.admin-main {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f9f9f9;
}
</style>
