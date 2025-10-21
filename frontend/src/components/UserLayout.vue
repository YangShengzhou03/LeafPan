<template>
    <div class="user-layout">
        <div class="user-header">
            <div class="header-content">
                <div class="logo-container">
                    <h1>枫叶网盘</h1>
                </div>
            </div>
            <div class="header-actions">
                <div class="user-info">
                    <div class="user-avatar">
                        <i class="icon-user"></i>
                    </div>
                    <div class="user-details">
                        <span class="username">{{ currentUser?.username || '用户名' }}</span>
                    </div>
                </div>
                <button class="logout-btn" @click="handleLogout">
                    <i class="icon-logout"></i>
                    <span>退出</span>
                </button>
            </div>
        </div>
        <div class="user-content">
            <div class="user-content-nav">
                <div class="nav-items">
                    <router-link to="/files" class="nav-item" active-class="active">
                        <i class="icon-files"></i>
                        <span>我的文件</span>
                    </router-link>
                    <router-link to="/dashboard" class="nav-item" active-class="active">
                        <i class="icon-dashboard"></i>
                        <span>仪表盘</span>
                    </router-link>
                    <router-link to="/shared" class="nav-item" active-class="active">
                        <i class="icon-share"></i>
                        <span>共享文件</span>
                    </router-link>
                    <router-link to="/recycle" class="nav-item" active-class="active">
                        <i class="icon-recycle"></i>
                        <span>回收站</span>
                    </router-link>
                    <div class="nav-divider"></div>
                    <router-link to="/settings" class="nav-item" active-class="active">
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

// 存储信息
const totalStorageGB = ref(5) // 默认5GB
const usedStorageGB = ref(1.2) // 已使用1.2GB

// 计算存储百分比
const storagePercentage = computed(() => {
  return Math.min(100, Math.round((usedStorageGB.value / totalStorageGB.value) * 100))
})

// 格式化存储显示
const totalStorage = computed(() => {
  return `${totalStorageGB.value} GB`
})

const usedStorage = computed(() => {
  return `${usedStorageGB.value.toFixed(1)} GB`
})

// 获取用户信息
const currentUser = computed(() => {
  return store.state.user
})

// 退出登录
const handleLogout = async () => {
  await store.logout()
}
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

.user-info {
    display: flex;
    align-items: center;
    margin-right: 20px;
}

.user-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-color: #409EFF;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 12px;
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

.logout-btn {
    display: flex;
    align-items: center;
    background-color: transparent;
    border: none;
    color: #F56C6C;
    padding: 6px 12px;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.3s ease;
}

.logout-btn:hover {
    background-color: rgba(245, 108, 108, 0.1);
}

.logout-btn i {
    margin-right: 6px;
}

.user-content {
    flex: 1;
    display: flex;
    overflow: hidden;
}

.user-content-nav {
    width: 220px;
    background-color: #fff;
    border-right: 1px solid #e9ecef;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
}

.nav-items {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 10px 0;
}

.nav-item {
    display: flex;
    align-items: center;
    padding: 12px 20px;
    cursor: pointer;
    text-decoration: none;
    color: #333;
    transition: all 0.3s ease;
    position: relative;
    font-weight: 500;
}

.nav-item:hover {
    color: #409EFF;
    background-color: rgba(64, 158, 255, 0.05);
}

.nav-item.active {
    color: #409EFF;
    font-weight: 600;
}

.nav-item.active::before {
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    height: 100%;
    width: 4px;
    background-color: #409EFF;
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
    padding: 15px;
    border-top: 1px solid #e9ecef;
}

.storage-info {
    width: 100%;
}

.storage-bar {
    height: 6px;
    background-color: #e9ecef;
    border-radius: 3px;
    overflow: hidden;
    margin-bottom: 8px;
}

.storage-used {
    height: 100%;
    background: linear-gradient(90deg, #409EFF, #79bbff);
    border-radius: 3px;
    transition: width 0.3s ease;
}

.storage-text {
    font-size: 12px;
    color: #909399;
    text-align: center;
}

.user-content-container {
    flex: 1;
    padding: 25px;
    overflow-y: auto;
    background-color: #f5f7fa;
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

/* 添加卡片效果 */
.user-content-container > div {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    padding: 20px;
    min-height: 100%;
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
        padding: 15px;
    }
    
    /* 添加移动端卡片效果 */
    .user-content-container > div {
        padding: 15px;
        border-radius: 6px;
    }
}
</style>
