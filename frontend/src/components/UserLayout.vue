<template>
    <div class="user-layout">
        <div class="user-header">
            <div class="header-content">
                <div class="logo-container">
                    <h1>枫叶网盘</h1>
                </div>
            </div>
            <div class="header-actions">
                <div class="user-dropdown" :class="{ 'active': isDropdownOpen }">
                    <div class="user-info" @click="toggleDropdown">
                        <div class="user-avatar">
                            <i class="icon-user"></i>
                        </div>
                        <div class="user-details">
                            <span class="username">{{ currentUser?.username || '用户名' }}</span>
                        </div>
                        <i class="dropdown-arrow" :class="{ 'rotate': isDropdownOpen }"></i>
                    </div>
                    <transition name="dropdown">
                        <div class="dropdown-menu" v-show="isDropdownOpen">
                            <div class="dropdown-item" @click="handleLogout">
                                <i class="icon-logout"></i>
                                <span>退出登录</span>
                            </div>
                        </div>
                    </transition>
                </div>
            </div>
        </div>
        <div class="user-content">
            <div class="user-content-nav">
                <div class="nav-items">
                    <router-link to="/user" class="nav-item" active-class="active" exact-active-class="active">
                        <i class="icon-files"></i>
                        <span>我的文件</span>
                    </router-link>
                    <router-link to="/user/dashboard" class="nav-item" active-class="active">
                        <i class="icon-dashboard"></i>
                        <span>仪表盘</span>
                    </router-link>
                    <router-link to="/user/shared" class="nav-item" active-class="active">
                        <i class="icon-share"></i>
                        <span>共享文件</span>
                    </router-link>
                    <router-link to="/user/trash" class="nav-item" active-class="active">
                        <i class="icon-recycle"></i>
                        <span>回收站</span>
                    </router-link>
                    <div class="nav-divider"></div>
                    <router-link to="/user/settings" class="nav-item" active-class="active">
                        <i class="icon-settings"></i>
                        <span>设置</span>
                    </router-link>
                </div>
                <div class="nav-footer">
                    <div class="storage-info">
                        <div class="storage-bar">
                            <div class="storage-used" :style="{ width: storagePercentage + '%' }"></div>
                        </div>
                        <div class="storage-text">
                            {{ usedStorage }} / {{ totalStorage }}
                        </div>
                    </div>
                </div>
            </div>
            <div class="user-content-container">
                <router-view v-slot="{ Component }">
                    <transition name="fade" mode="out-in">
                        <component :is="Component" />
                    </transition>
                </router-view>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import store from '@/utils/store.js'

// 控制下拉菜单的显示状态
const isDropdownOpen = ref(false)

// 切换下拉菜单
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

// 点击页面其他地方关闭下拉菜单
const closeDropdown = (event) => {
  if (!event.target.closest('.user-dropdown')) {
    isDropdownOpen.value = false
  }
}

// 计算存储百分比
const storagePercentage = computed(() => {
  const { totalStorageGB, usedStorageGB } = store.state.storageInfo
  return Math.min(100, Math.round((usedStorageGB / totalStorageGB) * 100))
})

// 格式化存储显示
const totalStorage = computed(() => {
  return `${store.state.storageInfo.totalStorageGB} GB`
})

const usedStorage = computed(() => {
  return `${store.state.storageInfo.usedStorageGB.toFixed(1)} GB`
})

// 获取用户信息
const currentUser = computed(() => {
  return store.state.user
})

// 退出登录
const handleLogout = async () => {
  isDropdownOpen.value = false
  await store.logout()
}

// 刷新存储信息
const refreshStorageInfo = async () => {
  try {
    await store.fetchStorageInfo()
  } catch (error) {
    console.error('刷新存储信息失败:', error)
  }
}

// 组件挂载时刷新存储信息
onMounted(() => {
  refreshStorageInfo()
  // 添加全局点击事件监听器，用于关闭下拉菜单
  document.addEventListener('click', closeDropdown)
})
</script>

<style scoped>
.user-layout {
    height: 100vh;
    display: flex;
    flex-direction: column;
    font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
    color: #333;
}

.user-header {
    padding: 15px 25px;
    background-color: #fff;
    color: #333;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    position: sticky;
    top: 0;
    z-index: 100;
}

.header-content {
    display: flex;
    flex-direction: column;
}

.logo-container {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
}

.logo-container i {
    font-size: 28px;
    margin-right: 10px;
}

.logo-container h1 {
    font-size: 24px;
    font-weight: 600;
    margin: 0;
    color: #409EFF;
}

.subtitle {
    font-size: 14px;
    opacity: 0.9;
    margin: 0;
    color: #606266;
}

.header-actions {
    display: flex;
    align-items: center;
}

.user-dropdown {
    position: relative;
}

.user-info {
    display: flex;
    align-items: center;
    margin-right: 20px;
    cursor: pointer;
    padding: 5px;
    border-radius: 4px;
    transition: background-color 0.3s ease;
}

.user-avatar {
    width: 42px;
    height: 42px;
    border-radius: 50%;
    background: linear-gradient(135deg, #409EFF, #79bbff);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 12px;
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
    transition: all 0.3s ease;
}

.user-avatar:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
}

.user-avatar i {
    color: white;
    font-size: 18px;
    margin: 0;
}

.user-details {
    display: flex;
    flex-direction: column;
}

.username {
    font-weight: 600;
    color: #303133;
    font-size: 14px;
}

.user-email {
    font-size: 12px;
    color: #909399;
}

.dropdown-arrow {
    width: 0;
    height: 0;
    border-left: 5px solid transparent;
    border-right: 5px solid transparent;
    border-top: 5px solid #909399;
    margin-left: 8px;
    transition: transform 0.3s ease;
}

.dropdown-arrow.rotate {
    transform: rotate(180deg);
}

.dropdown-menu {
    position: absolute;
    top: 100%;
    right: 0;
    margin-top: 12px;
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    min-width: 180px;
    z-index: 1000;
    overflow: hidden;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.8);
}

.dropdown-item {
    display: flex;
    align-items: center;
    padding: 12px 20px;
    cursor: pointer;
    transition: all 0.3s ease;
    color: #606266;
}

.dropdown-item:hover {
    background-color: #f5f7fa;
    color: #F56C6C;
}

.dropdown-item i {
    margin-right: 12px;
    font-size: 16px;
}

.user-content {
    flex: 1;
    display: flex;
    overflow: hidden;
}

.user-content-nav {
    width: 240px;
    background-color: #fff;
    border-right: 1px solid #f0f2f5;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    box-shadow: 2px 0 12px rgba(0, 0, 0, 0.04);
    transition: all 0.3s ease;
}

.nav-items {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 16px 0;
}

.nav-item {
    display: flex;
    align-items: center;
    padding: 14px 24px;
    cursor: pointer;
    text-decoration: none;
    color: #333;
    transition: all 0.3s ease;
    position: relative;
    font-weight: 500;
    border-radius: 0 24px 24px 0;
    margin-right: 12px;
}

.nav-item:hover {
    color: #409EFF;
    background-color: rgba(64, 158, 255, 0.08);
}

.nav-item.active {
    color: #409EFF;
    font-weight: 600;
    background-color: rgba(64, 158, 255, 0.1);
}

.nav-item.active::before {
    content: '';
    position: absolute;
    left: 0;
    top: 50%;
    height: 60%;
    width: 4px;
    background-color: #409EFF;
    transform: translateY(-50%);
    border-radius: 0 4px 4px 0;
}

.nav-item i {
    font-size: 18px;
    width: 24px;
    margin-right: 15px;
    text-align: center;
    color: #606266;
    transition: color 0.3s ease;
}

.nav-item:hover i {
    color: #409EFF;
}

.nav-divider {
    height: 1px;
    background-color: #e9ecef;
    margin: 10px 20px;
}

.nav-footer {
    padding: 20px;
    border-top: 1px solid #f0f2f5;
    background-color: rgba(64, 158, 255, 0.02);
}

.storage-info {
    width: 100%;
}

.storage-bar {
    height: 8px;
    background-color: #f0f2f5;
    border-radius: 4px;
    overflow: hidden;
    margin-bottom: 12px;
    box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

.storage-used {
    height: 100%;
    background: linear-gradient(90deg, #409EFF, #79bbff);
    border-radius: 4px;
    transition: width 0.5s ease;
    box-shadow: 0 0 10px rgba(64, 158, 255, 0.3);
}

.storage-text {
    font-size: 12px;
    color: #606266;
    text-align: center;
    font-weight: 500;
}

.user-content-container {
    flex: 1;
    padding: 10px;
    overflow-y: auto;
    background-color: #f8fafc;
    transition: all 0.3s ease;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}

/* 下拉菜单动画 */
.dropdown-enter-active,
.dropdown-leave-active {
    transition: all 0.3s ease;
    transform-origin: top right;
}

.dropdown-enter-from,
.dropdown-leave-to {
    opacity: 0;
    transform: scale(0.95) translateY(-10px);
}

/* 添加卡片效果 */
.user-content-container > div {
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    padding: 24px;
    min-height: 92%;
    transition: all 0.3s ease;
}

.user-content-container > div:hover {
    box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

/* 响应式设计 */
@media (max-width: 768px) {
    .user-header {
        padding: 12px 15px;
    }
    
    .logo-container h1 {
        font-size: 20px;
    }
    
    .subtitle {
        font-size: 12px;
    }
    
    .user-info span {
        display: none;
    }
    
    .logout-btn span {
        display: none;
    }
    
    .user-content-nav {
        width: 220px;
    }
    
    .user-content-container {
        padding: 10px;
    }
    
    /* 添加移动端卡片效果 */
    .user-content-container > div {
        padding: 12px;
        border-radius: 8px;
    }
}
</style>
