<template>
    <div class="user-layout">
        <div class="user-header">
            <div class="header-content">
                <div class="logo-container">
                    <h1>轻羽云盘</h1>
                </div>
            </div>
            <div class="header-actions">
                <div class="user-dropdown" :class="{ 'active': isDropdownOpen }">
                    <div class="user-info" @click="toggleDropdown">
                        <div class="user-avatar">
                            <img :src="avatarUrl" alt="用户头像" class="avatar-image" @error="handleAvatarError">
                        </div>
                        <div class="user-details">
                            <span class="username">{{ formatLongText(currentUser?.nickname || currentUser?.username || '用户') }}</span>
                        </div>
                        <i class="dropdown-arrow" :class="{ 'rotate': isDropdownOpen }"></i>
                    </div>
                    <transition name="dropdown">
                        <div class="dropdown-menu" v-show="isDropdownOpen">
                            <div class="dropdown-item" @click="handleLogout">
                                <span>退出登录</span>
                            </div>
                        </div>
                    </transition>
                </div>
            </div>
        </div>
        <div class="user-content">
            <nav class="user-content-nav">
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
                    <router-link to="/user/favorites" class="nav-item" active-class="active">
                        <i class="icon-favorite"></i>
                        <span>我的收藏</span>
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
            </nav>
            <main class="user-content-container">
                <router-view v-slot="{ Component }">
                    <transition name="fade" mode="out-in">
                        <component :is="Component" />
                    </transition>
                </router-view>
            </main>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import store from '@/utils/store.js'

const router = useRouter()
const isDropdownOpen = ref(false)

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const closeDropdown = (event) => {
  if (!event.target.closest('.user-dropdown')) {
    isDropdownOpen.value = false
  }
}

const currentUser = computed(() => {
  return store.state.user
})

const avatarUrl = computed(() => {
  const user = currentUser.value
  if (!user) {
    return 'https://picsum.photos/id/1005/200/200'
  }
  if (user.avatar) {
    return `${process.env.VUE_APP_API_URL}/avatar/view/${user.id}`
  }
  return 'https://picsum.photos/id/1005/200/200'
})

const handleAvatarError = (event) => {
  event.target.src = 'https://picsum.photos/id/1005/200/200'
}

const formatLongText = (text, maxLength = 15) => {
  if (!text) return ''
  if (text.length <= maxLength) return text
  
  const startLength = Math.floor(maxLength / 2)
  const endLength = maxLength - startLength - 4
  return text.substring(0, startLength) + '****' + text.substring(text.length - endLength)
}

watch(currentUser, async (newUser) => {
  if (newUser) {
    await refreshStorageInfo()
  }
}, { immediate: false })

const handleLogout = async () => {
  try {
    isDropdownOpen.value = false
    await store.logout()
    router.push('/')
  } catch (error) {
    store.clearUser()
    router.push('/')
  }
}

const refreshStorageInfo = async () => {
  try {
    await store.fetchStorageInfo()
  } catch (error) {
  }
}

onMounted(async () => {
  try {
    if (!store.state.user) {
      await store.fetchCurrentUser()
    }
    await refreshStorageInfo()
  } catch (error) {
    await store.init()
  }
  document.addEventListener('click', closeDropdown)
})

onUnmounted(() => {
  document.removeEventListener('click', closeDropdown)
})
</script>

<style scoped>
.user-layout {
    height: 100vh;
    display: flex;
    flex-direction: column;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
    color: #1a1a1a;
    background-color: #f5f7fa;
}

.user-header {
    padding: 0 24px;
    height: 56px;
    background-color: #fff;
    border-bottom: 1px solid #e8eaed;
    display: flex;
    justify-content: space-between;
    align-items: center;
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
}

.logo-container h1 {
    font-size: 18px;
    font-weight: 600;
    margin: 0;
    color: #1a73e8;
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
    cursor: pointer;
    padding: 6px 12px;
    border-radius: 6px;
    transition: background-color 0.2s;
}

.user-info:hover {
    background-color: #f1f3f4;
}

.user-avatar {
    width: 32px;
    height: 32px;
    margin-right: 10px;
    overflow: hidden;
    border-radius: 50%;
}

.avatar-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.user-details {
    display: flex;
    flex-direction: column;
}

.username {
    color: #202124;
    font-size: 14px;
    font-weight: 500;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 150px;
}

.dropdown-arrow {
    width: 0;
    height: 0;
    border-left: 4px solid transparent;
    border-right: 4px solid transparent;
    border-top: 4px solid #5f6368;
    margin-left: 8px;
    transition: transform 0.2s;
}

.dropdown-arrow.rotate {
    transform: rotate(180deg);
}

.dropdown-menu {
    position: absolute;
    top: 100%;
    right: 0;
    margin-top: 6px;
    background-color: #fff;
    border: 1px solid #e8eaed;
    border-radius: 6px;
    min-width: 160px;
    z-index: 1000;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.dropdown-item {
    display: flex;
    align-items: center;
    padding: 10px 16px;
    cursor: pointer;
    transition: background-color 0.2s;
    color: #3c4043;
    font-size: 14px;
}

.dropdown-item:hover {
    background-color: #f1f3f4;
    color: #d93025;
}

.user-content {
    flex: 1;
    display: flex;
    overflow: hidden;
}

.user-content-nav {
    width: 220px;
    background-color: #fff;
    border-right: 1px solid #e8eaed;
    display: flex;
    flex-direction: column;
    overflow: hidden;
}

.nav-items {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 12px 8px;
}

.nav-item {
    display: flex;
    align-items: center;
    padding: 10px 14px;
    cursor: pointer;
    text-decoration: none;
    color: #5f6368;
    transition: all 0.2s;
    position: relative;
    font-weight: 500;
    font-size: 14px;
    margin: 2px 4px;
    border-radius: 6px;
}

.nav-item:hover {
    color: #1a73e8;
    background-color: #f1f3f4;
}

.nav-item.active {
    color: #1a73e8;
    background-color: #e8f0fe;
}

.nav-item i {
    font-size: 18px;
    width: 20px;
    margin-right: 12px;
    text-align: center;
    color: #5f6368;
}

.nav-item:hover i,
.nav-item.active i {
    color: #1a73e8;
}

.nav-divider {
    height: 1px;
    background-color: #e8eaed;
    margin: 8px 12px;
}

.user-content-container {
    flex: 1;
    padding: 16px;
    overflow-y: auto;
    background-color: #f5f7fa;
}

.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.2s;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}

.dropdown-enter-active,
.dropdown-leave-active {
    transition: all 0.2s;
    transform-origin: top right;
}

.dropdown-enter-from,
.dropdown-leave-to {
    opacity: 0;
    transform: scale(0.95) translateY(-6px);
}

.user-content-container > div {
    background-color: #fff;
    border: 1px solid #e8eaed;
    border-radius: 8px;
    padding: 20px;
    min-height: 92%;
}

@media (max-width: 768px) {
    .user-header {
        padding: 12px 16px;
    }
    
    .logo-container h1 {
        font-size: 18px;
    }
    
    .user-info span {
        display: none;
    }
    
    .user-content-nav {
        width: 64px;
    }
    
    .user-content-nav .nav-item span {
        display: none;
    }
    
    .user-content-nav .nav-item {
        justify-content: center;
        padding: 12px 0;
        margin: 0 8px;
        border-radius: 4px;
    }
    
    .user-content-nav .nav-item i {
        margin-right: 0;
        font-size: 20px;
    }
    
    .user-content-nav .nav-item.active::before {
        width: 100%;
        height: 3px;
        top: 0;
        left: 0;
    }
    
    .user-content-container {
        padding: 12px;
    }
    
    .user-content-container > div {
        padding: 16px;
    }
}

@media (max-width: 640px) {
    .user-header {
        padding: 10px 12px;
    }
    
    .logo-container h1 {
        font-size: 16px;
    }
    
    .user-avatar {
        width: 32px;
        height: 32px;
    }
    
    .user-content-nav {
        width: 56px;
    }
    
    .user-content-nav .nav-item i {
        font-size: 18px;
    }
    
    .user-content-container {
        padding: 10px;
    }
    
    .user-content-container > div {
        padding: 14px;
    }
}

@media (max-width: 480px) {
    .user-header {
        padding: 8px 12px;
    }
    
    .logo-container h1 {
        font-size: 15px;
    }
    
    .user-content-nav {
        width: 52px;
    }
    
    .user-content-nav .nav-item {
        padding: 10px 0;
    }
    
    .user-content-container {
        padding: 8px;
    }
    
    .user-content-container > div {
        padding: 12px;
    }
}
</style>
