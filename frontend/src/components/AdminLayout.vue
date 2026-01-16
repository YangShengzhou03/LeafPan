<template>
  <div class="admin-layout">
    <header class="admin-header">
      <div class="header-left">
        <h1 class="logo">轻羽云盘 - 管理员控制台</h1>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="32" :src="userAvatar">
              <el-icon>
                <User />
              </el-icon>
            </el-avatar>
            <span class="username">{{ store.state.user?.nickname || store.state.user?.email || '管理员' }}</span>
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
      <aside class="admin-sidebar">
        <el-menu :default-active="activeMenu" class="admin-menu" router unique-opened>
          <el-menu-item index="/admin">
            <el-icon>
              <Monitor />
            </el-icon>
            <span>管理员仪表盘</span>
          </el-menu-item>

          <el-menu-item index="/admin/users">
            <el-icon>
              <User />
            </el-icon>
            <span>用户管理</span>
          </el-menu-item>

          <el-menu-item index="/admin/system">
            <el-icon>
              <Setting />
            </el-icon>
            <span>系统设置</span>
          </el-menu-item>

          <el-menu-item index="/admin/logs">
            <el-icon>
              <Document />
            </el-icon>
            <span>操作日志</span>
          </el-menu-item>
        </el-menu>
      </aside>

      <main class="admin-main">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, ArrowDown, Monitor, Setting, Document } from '@element-plus/icons-vue'
import store from '@/utils/store.js'

const router = useRouter()
const route = useRoute()

const activeMenu = computed(() => route.path)

const userAvatar = computed(() => {
  const user = store.state.user
  if (!user) {
    return ''
  }
  if (user.avatar) {
    return `${process.env.VUE_APP_API_URL}/avatar/view/${user.id}`
  }
  return ''
})

const handleCommand = async (command) => {
  if (command === 'logout') {
    await store.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  } else if (command === 'userMode') {
    router.push('/user')
  }
}

onMounted(async () => {
  if (!store.state.user) {
    await store.fetchCurrentUser()
  }
})
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.admin-header {
  height: 60px;
  background-color: #ffffff;
  color: #202124;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  border-bottom: 1px solid #e0e0e0;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
}

.header-left .logo {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1a73e8;
  letter-spacing: 0.3px;
}

.header-right .user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #5f6368;
  padding: 8px 16px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.header-right .user-info:hover {
  background-color: #f1f3f4;
}

.username {
  margin: 0 10px;
  font-size: 14px;
  font-weight: 500;
}

.admin-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.admin-sidebar {
  width: 220px;
  background-color: #ffffff;
  border-right: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;
}

.admin-menu {
  height: 100%;
  border-right: none;
  background-color: transparent;
  padding: 12px 8px;
}

.admin-menu :deep(.el-menu-item) {
  height: 48px;
  line-height: 48px;
  color: #5f6368;
  margin: 2px 8px;
  border-radius: 4px;
  font-weight: 500;
  font-size: 14px;
}

.admin-menu :deep(.el-menu-item:hover) {
  background-color: #f1f3f4;
  color: #1a73e8;
}

.admin-menu :deep(.el-menu-item.is-active) {
  background-color: #e8f0fe;
  color: #1a73e8;
  font-weight: 600;
}

.admin-menu :deep(.el-menu-item .el-icon) {
  font-size: 18px;
  margin-right: 12px;
}

.admin-main {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
}

.admin-main :deep(.el-card) {
  border-radius: 4px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  background-color: #ffffff;
}

.admin-main :deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #e0e0e0;
  background-color: #fafbfc;
}

.admin-main :deep(.card-header) {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
  color: #202124;
}

/* 响应式设计 */
/* 平板设备 */
@media (max-width: 768px) {
  .admin-sidebar {
    width: 64px;
  }

  .admin-header {
    padding: 0 16px;
    height: 56px;
  }

  .header-left .logo {
    font-size: 16px;
  }

  .header-right .user-info .username {
    display: none;
  }

  .admin-menu {
    padding: 12px 4px;
  }

  .admin-menu :deep(.el-menu-item span) {
    display: none;
  }

  .admin-menu :deep(.el-menu-item) {
    justify-content: center;
    margin: 4px 4px;
    padding: 0;
  }

  .admin-menu :deep(.el-menu-item .el-icon) {
    margin-right: 0;
    font-size: 20px;
  }

  .admin-main {
    padding: 16px;
  }
}

@media (max-width: 640px) {
  .admin-header {
    height: 52px;
    padding: 0 12px;
  }

  .header-left .logo {
    font-size: 15px;
  }

  .admin-sidebar {
    width: 56px;
  }

  .admin-menu :deep(.el-menu-item) {
    height: 44px;
    line-height: 44px;
  }

  .admin-menu :deep(.el-menu-item .el-icon) {
    font-size: 18px;
  }

  .admin-main {
    padding: 12px;
  }
}

@media (max-width: 480px) {
  .admin-header {
    padding: 0 10px;
    height: 48px;
  }

  .header-left .logo {
    font-size: 14px;
  }

  .admin-sidebar {
    width: 52px;
  }

  .admin-menu :deep(.el-menu-item .el-icon) {
    font-size: 16px;
  }

  .admin-main {
    padding: 10px;
  }

  .admin-main :deep(.el-card__header) {
    padding: 14px 16px;
  }

  .admin-main :deep(.card-header) {
    font-size: 15px;
  }
}
</style>
