<template>
    <div class="user-layout">
        <div class="user-header">
            <div class="header-content">
                <div class="logo-container">
                    <i class="icon-leaf"></i>
                    <h1>枫叶网盘</h1>
                </div>
                <p class="subtitle">安全、简单的个人网盘服务</p>
            </div>
            <div class="header-actions">
                <div class="user-info">
                    <i class="icon-user"></i>
                    <span>用户名</span>
                </div>
                <button class="logout-btn">
                    <i class="icon-logout"></i>
                    <span>退出</span>
                </button>
            </div>
        </div>
        <div class="user-content">
            <div class="user-content-nav" :class="{ 'collapsed': isCollapsed }">
                <div class="nav-toggle" @click="isCollapsed = !isCollapsed">
                    <i class="icon-menu" :class="{ 'rotated': isCollapsed }"></i>
                </div>
                <div class="nav-items">
                    <router-link to="/dashboard" class="nav-item" active-class="active">
                        <i class="icon-dashboard"></i>
                        <span v-show="!isCollapsed">仪表盘</span>
                    </router-link>
                    <router-link to="/files" class="nav-item" active-class="active">
                        <i class="icon-files"></i>
                        <span v-show="!isCollapsed">我的文件</span>
                    </router-link>
                    <router-link to="/shared" class="nav-item" active-class="active">
                        <i class="icon-share"></i>
                        <span v-show="!isCollapsed">共享文件</span>
                    </router-link>
                    <router-link to="/recycle" class="nav-item" active-class="active">
                        <i class="icon-recycle"></i>
                        <span v-show="!isCollapsed">回收站</span>
                    </router-link>
                    <router-link to="/settings" class="nav-item" active-class="active">
                        <i class="icon-settings"></i>
                        <span v-show="!isCollapsed">设置</span>
                    </router-link>
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
import { ref } from 'vue'

const isCollapsed = ref(false)
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

.user-info i {
    margin-right: 8px;
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
    transition: width 0.3s ease;
    overflow: hidden;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
}

.user-content-nav.collapsed {
    width: 60px;
}

.nav-toggle {
    padding: 15px;
    cursor: pointer;
    display: flex;
    justify-content: center;
    border-bottom: 1px solid #e9ecef;
    transition: background-color 0.3s ease;
}

.nav-toggle:hover {
    background-color: #f5f7fa;
}

.nav-toggle i {
    font-size: 18px;
    color: #606266;
    transition: transform 0.3s ease, color 0.3s ease;
}

.nav-toggle:hover i {
    color: #409EFF;
}

.nav-toggle i.rotated {
    transform: rotate(90deg);
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
        width: 60px;
    }
    
    .nav-item span {
        display: none;
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
