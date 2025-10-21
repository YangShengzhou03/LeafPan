<template>
    <div class="home-page">
        <div class="user-header">
            <h1>枫叶网盘 - 首页</h1>
            <p class="subtitle">欢迎来到枫叶网盘</p>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'

const isCollapsed = ref(false)
</script>

<style scoped>
.home-page {
    height: 100vh;
    display: flex;
    flex-direction: column;
}
.user-header {
    padding: 20px;
    background-color: #f5f5f5;
    text-align: center;
}
</style>
