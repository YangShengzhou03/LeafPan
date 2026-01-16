<template>
  <div class="admin-users">
    <el-card class="users-card">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="showAddUserDialog = true">
            <el-icon><Plus /></el-icon>
            添加用户
          </el-button>
        </div>
      </template>
      
      <div class="users-content">
        <!-- 搜索和筛选 -->
        <div class="search-bar">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-input
                v-model="searchQuery"
                placeholder="搜索邮箱"
                clearable
                @clear="handleSearch"
                @keyup.enter="handleSearch"
              >
                <template #append>
                  <el-button @click="handleSearch">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-select v-model="statusFilter" placeholder="用户状态" clearable @change="handleSearch">
                <el-option label="全部" value="" />
                <el-option label="正常" value="active" />
                <el-option label="禁用" value="disabled" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="roleFilter" placeholder="用户角色" clearable @change="handleSearch">
                <el-option label="全部" value="" />
                <el-option label="普通用户" value="user" />
                <el-option label="管理员" value="admin" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button @click="resetFilters">重置</el-button>
            </el-col>
          </el-row>
        </div>
        
        <!-- 用户表格 -->
        <div class="table-container">
          <el-table :data="filteredUsers" style="width: 100%" v-loading="loading" :scroll="{ x: 1200 }">
            <el-table-column prop="id" label="ID" width="180" :show-overflow-tooltip="true">
              <template #default="scope">
                <span class="truncate-id">{{ scope.row.id }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="email" label="邮箱" width="180" :show-overflow-tooltip="true" />
            <el-table-column prop="gender" label="性别" width="80">
              <template #default="scope">
                {{ scope.row.gender === 'MALE' ? '男' : scope.row.gender === 'FEMALE' ? '女' : '未设置' }}
              </template>
            </el-table-column>
            <el-table-column prop="phone" label="手机号" width="120" :show-overflow-tooltip="true" />
            <el-table-column prop="role" label="角色" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.role === 'admin' ? 'danger' : 'primary'">
                  {{ scope.row.role === 'admin' ? '管理员' : '普通用户' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="storageQuota" label="总配额" width="100">
              <template #default="scope">
                {{ formatStorage(scope.row.storageQuota) }}
              </template>
            </el-table-column>
            <el-table-column prop="storageUsed" label="存储使用" width="140">
          <template #default="{ row }">
            {{ formatStorage(row.storageUsed) }} / {{ formatStorage(row.storageQuota) }}
          </template>
        </el-table-column>
        <el-table-column prop="usagePercentage" label="使用率" width="80">
          <template #default="{ row }">
            <span :class="getUsageClass(row.storageUsed, row.storageQuota)">
              {{ calculateUsagePercentage(row.storageUsed, row.storageQuota) }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.status === 'active' ? 'success' : 'danger'">
                  {{ scope.row.status === 'active' ? '正常' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="lastLoginTime" label="最后上线时间" width="200" :show-overflow-tooltip="true" />
            <el-table-column label="操作" width="280" fixed="right">
              <template #default="scope">
                <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
                <el-button 
                  size="small" 
                  :type="scope.row.status === 'active' ? 'warning' : 'success'"
                  @click="toggleUserStatus(scope.row)"
                >
                  {{ scope.row.status === 'active' ? '禁用' : '启用' }}
                </el-button>
                <el-button size="small" type="info" @click="resetPassword(scope.row)">重置密码</el-button>
                <el-button size="small" type="danger" @click="deleteUser(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="totalUsers"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>
    
    <!-- 添加/编辑用户对话框 -->
    <el-dialog
      v-model="showAddUserDialog"
      :title="editingUser ? '编辑用户' : '添加用户'"
      width="500px"
    >
      <!-- 用户头像显示区域 -->
      <div v-if="editingUser" class="avatar-section">
        <div class="avatar-display">
          <el-avatar 
            v-if="editingUser.avatar" 
            :size="80" 
            :src="editingUser.avatar" 
            fit="cover"
          />
          <el-avatar 
            v-else 
            :size="80" 
            :style="{ backgroundColor: '#409EFF' }"
          >
            {{ '默认头像' }}
          </el-avatar>
        </div>
      </div>
      
      <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="80px">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!editingUser">
          <el-input v-model="userForm.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="userForm.gender" style="width: 100%">
            <el-option label="男" value="MALE" />
            <el-option label="女" value="FEMALE" />
            <el-option label="未设置" value="NOT_SET" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" style="width: 100%">
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio label="active">正常</el-radio>
            <el-radio label="disabled">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="存储配额" prop="storageQuota">
          <el-input-number v-model="userForm.storageQuota" :min="1" :max="1000" />
          <span style="margin-left: 10px">GB</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddUserDialog = false">取消</el-button>
          <el-button type="primary" @click="saveUser">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import Server from '@/utils/Server.js'

// 数据状态
const loading = ref(false)
const users = ref([])
const searchQuery = ref('')
const statusFilter = ref('')
const roleFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const totalUsers = ref(0)
const showAddUserDialog = ref(false)
const editingUser = ref(null)
const userFormRef = ref(null)

// 用户表单数据
const userForm = reactive({
  email: '',
  password: '',
  gender: 'NOT_SET',
  phone: '',
  role: 'user',
  status: 'active',
  storageQuota: 1 // 默认1GB，单位GB
})

// 表单验证规则
const userRules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  phone: [
    { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择用户角色', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择用户状态', trigger: 'change' }
  ],
  storageQuota: [
    { required: true, message: '请设置存储配额', trigger: 'blur' }
  ]
}

// 过滤后的用户列表
const filteredUsers = computed(() => {
  let result = users.value
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(user => 
      user.email.toLowerCase().includes(query)
    )
  }
  
  if (statusFilter.value) {
    result = result.filter(user => user.status === statusFilter.value)
  }
  
  if (roleFilter.value) {
    result = result.filter(user => user.role === roleFilter.value)
  }
  
  return result
})

// 格式化存储大小
const formatStorage = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 计算存储使用率
const calculateUsagePercentage = (used, quota) => {
  if (!quota || quota === 0) return 0
  return Math.round((used / quota) * 100)
}

// 加载用户数据
const loadUsers = async () => {
  loading.value = true
  try {
    // 调用后端API获取真实数据
    const response = await Server.get('/admin/user/list', {
      params: {
        page: currentPage.value - 1,
        size: pageSize.value
      }
    })
    
    if (response.code === 200) {
      const userData = response.data
      users.value = userData.content.map(user => ({
        id: user.id,
        email: user.email,
        avatar: user.avatar ? `${process.env.VUE_APP_API_URL}/avatar/view/${user.id}` : null,
        gender: user.gender === 1 ? 'MALE' : user.gender === 2 ? 'FEMALE' : 'NOT_SET',
        phone: user.phone || '',
        role: user.role === 1 ? 'admin' : 'user',
        storageQuota: user.storageQuota || 1073741824,
        storageUsed: user.usedStorage || 0,
        status: user.status === 1 ? 'active' : 'disabled',
        lastLoginTime: user.lastLoginTime ? new Date(user.lastLoginTime).toLocaleString('zh-CN') : '从未登录',
        createdAt: user.createdTime ? new Date(user.createdTime).toLocaleString('zh-CN') : '未知'
      }))
      totalUsers.value = userData.totalElements
    } else {
      throw new Error(response.message)
    }
  } catch (error) {
    ElMessage.error('加载用户数据失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  loadUsers()
}

// 重置筛选条件
const resetFilters = () => {
  searchQuery.value = ''
  statusFilter.value = ''
  roleFilter.value = ''
  currentPage.value = 1
  loadUsers()
}

// 重置密码
const resetPassword = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要重置用户 "${user.email}" 的密码为"123456"吗？`,
      '确认重置密码',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await Server.put(`/admin/user/${user.id}/password?newPassword=123456`)
    ElMessage.success(`密码重置成功，新密码为：123456`)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重置密码失败: ' + (error.response?.data?.message || error.message))
    }
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  loadUsers()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadUsers()
}

// 编辑用户
const editUser = (user) => {
  editingUser.value = user
  userForm.email = user.email
  userForm.gender = user.gender
  userForm.phone = user.phone
  userForm.role = user.role
  userForm.status = user.status
  // 将字节转换为GB显示
  userForm.storageQuota = Math.round(user.storageQuota / (1024 * 1024 * 1024))
  userForm.password = '' // 编辑时不显示密码
  showAddUserDialog.value = true
}

// 保存用户
const saveUser = async () => {
  if (!userFormRef.value) return
  
  try {
    await userFormRef.value.validate()
    
    // 将GB转换为字节
    const storageQuotaInBytes = userForm.storageQuota * 1024 * 1024 * 1024
    
    // 调用后端API保存用户数据
    const userData = {
      email: userForm.email,
      password: userForm.password,
      gender: userForm.gender === 'MALE' ? 1 : userForm.gender === 'FEMALE' ? 2 : 0,
      phone: userForm.phone,
      role: userForm.role === 'admin' ? 1 : 0,
      status: userForm.status === 'active' ? 1 : 0,
      storageQuota: storageQuotaInBytes
    }
    
    if (editingUser.value) {
      // 更新用户
      await Server.put(`/admin/user/${editingUser.value.id}`, userData)
    } else {
      // 添加用户 - 使用管理员创建用户接口
      const newUser = {
        email: userForm.email,
        password: userForm.password,
        nickname: userForm.email.split('@')[0], // 默认昵称
        gender: userForm.gender === 'MALE' ? 1 : userForm.gender === 'FEMALE' ? 2 : 0,
        phone: userForm.phone,
        role: userForm.role === 'admin' ? 1 : 0,
        status: userForm.status === 'active' ? 1 : 0,
        storageQuota: storageQuotaInBytes
      }
      // 使用管理员创建用户接口
      await Server.post('/admin/user', newUser)
    }
    
    ElMessage.success(editingUser.value ? '用户更新成功' : '用户添加成功')
    showAddUserDialog.value = false
    editingUser.value = null
    resetUserForm()
    loadUsers()
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      ElMessage.error('保存用户失败: ' + (error.response?.data?.message || error.message))
    }
  }
}

// 切换用户状态
const toggleUserStatus = async (user) => {
  try {
    const newStatus = user.status === 'active' ? 'disabled' : 'active'
    const enabled = newStatus === 'active'
    await Server.put(`/admin/user/${user.id}/status?enabled=${enabled}`)
    ElMessage.success(`用户已${newStatus === 'active' ? '启用' : '禁用'}`)
    loadUsers()
  } catch (error) {
    ElMessage.error('切换用户状态失败: ' + (error.response?.data?.message || error.message))
  }
}

// 删除用户
const deleteUser = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${user.email}" 吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await Server.delete(`/admin/user/${user.id}`)
    ElMessage.success('用户删除成功')
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除用户失败: ' + (error.response?.data?.message || error.message))
    }
  }
}

// 获取使用率颜色类
const getUsageClass = (used, quota) => {
  const percentage = calculateUsagePercentage(used, quota)
  if (percentage >= 90) return 'usage-high'
  if (percentage >= 70) return 'usage-medium'
  return 'usage-low'
}

// 重置用户表单
const resetUserForm = () => {
  Object.assign(userForm, {
    email: '',
    password: '',
    gender: 'NOT_SET',
    phone: '',
    role: 'user',
    status: 'active',
    storageQuota: 1 // 默认1GB，单位GB
  })
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.admin-users {
  padding: 0px;
}

.users-card {
  margin-bottom: 16px;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  background-color: #ffffff;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  padding: 16px 20px;
  border-bottom: 1px solid #e0e0e0;
}

.search-bar {
  margin-bottom: 16px;
  padding: 0 20px;
}

.table-container {
  width: 100%;
  overflow-x: auto;
}

.truncate-id {
  display: inline-block;
  max-width: 160px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pagination-container {
  margin-top: 16px;
  display: flex;
  justify-content: center;
  padding: 16px 20px;
  border-top: 1px solid #e0e0e0;
}

/* 表格样式优化 */
:deep(.el-table) {
  border-radius: 0;
  min-width: 1200px;
}

:deep(.el-table__header) {
  background-color: #fafbfc;
}

:deep(.el-table th) {
  background-color: #fafbfc;
  color: #5f6368;
  font-weight: 600;
  font-size: 14px;
  border-bottom: 2px solid #e0e0e0;
}

:deep(.el-table td) {
  border-bottom: 1px solid #e0e0e0;
  color: #202124;
  font-size: 14px;
}

:deep(.el-table__body-wrapper) {
  overflow-x: auto;
}

:deep(.el-table .cell) {
  white-space: nowrap;
}

:deep(.el-table__row:hover > td) {
  background-color: #f8f9fa;
}

/* 使用率颜色样式 */
.usage-high {
  color: #d93025;
  font-weight: 700;
}

.usage-medium {
  color: #f57c00;
  font-weight: 700;
}

.usage-low {
  color: #188038;
  font-weight: 700;
}

/* 按钮样式优化 */
:deep(.el-button) {
  border-radius: 4px;
  font-weight: 500;
  padding: 8px 16px;
  font-size: 14px;
}

:deep(.el-button--primary) {
  background-color: #1a73e8;
  border-color: #1a73e8;
}

:deep(.el-button--primary:hover) {
  background-color: #1557b0;
  border-color: #1557b0;
}

:deep(.el-button--success) {
  background-color: #188038;
  border-color: #188038;
}

:deep(.el-button--success:hover) {
  background-color: #0d6529;
  border-color: #0d6529;
}

:deep(.el-button--warning) {
  background-color: #f57c00;
  border-color: #f57c00;
}

:deep(.el-button--warning:hover) {
  background-color: #d86900;
  border-color: #d86900;
}

:deep(.el-button--danger) {
  background-color: #d93025;
  border-color: #d93025;
}

:deep(.el-button--danger:hover) {
  background-color: #b92618;
  border-color: #b92618;
}

:deep(.el-button--info) {
  background-color: #5f6368;
  border-color: #5f6368;
}

:deep(.el-button--info:hover) {
  background-color: #3c4043;
  border-color: #3c4043;
}

/* 头像显示区域样式 */
.avatar-section {
  text-align: center;
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
}

.avatar-label {
  font-weight: 600;
  color: #202124;
  margin-bottom: 12px;
  font-size: 14px;
}

.avatar-display {
  margin-bottom: 8px;
}

.avatar-info {
  font-size: 12px;
  color: #5f6368;
}

/* 对话框样式调整 */
:deep(.el-dialog__body) {
  padding-top: 20px;
}

/* 标签样式优化 */
:deep(.el-tag) {
  border-radius: 4px;
  font-weight: 500;
  font-size: 13px;
  padding: 4px 10px;
}

:deep(.el-tag--primary) {
  background-color: #e8f0fe;
  color: #1a73e8;
  border-color: #d2e3fc;
}

:deep(.el-tag--success) {
  background-color: #e6f4ea;
  color: #188038;
  border-color: #ceead6;
}

:deep(.el-tag--warning) {
  background-color: #fef7e0;
  color: #f57c00;
  border-color: #fce8b2;
}

:deep(.el-tag--danger) {
  background-color: #fce8e6;
  color: #d93025;
  border-color: #fad2cf;
}

/* 输入框样式优化 */
:deep(.el-input__wrapper) {
  border-radius: 4px;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 4px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .users-card {
    margin-bottom: 12px;
  }

  .card-header {
    padding: 14px 16px;
  }

  .search-bar {
    padding: 0 16px;
    margin-bottom: 12px;
  }

  .pagination-container {
    padding: 14px 16px;
  }

  :deep(.el-button) {
    padding: 6px 12px;
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .users-card {
    margin-bottom: 10px;
  }

  .card-header {
    padding: 12px 14px;
  }

  .search-bar {
    padding: 0 14px;
    margin-bottom: 10px;
  }

  .pagination-container {
    padding: 12px 14px;
  }

  :deep(.el-button) {
    padding: 5px 10px;
    font-size: 12px;
  }
}
</style>