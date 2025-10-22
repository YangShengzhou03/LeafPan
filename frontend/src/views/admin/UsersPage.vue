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
                placeholder="搜索用户名或邮箱"
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
        <el-table :data="filteredUsers" style="width: 100%" v-loading="loading">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column prop="role" label="角色" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.role === 'admin' ? 'danger' : 'primary'">
                {{ scope.row.role === 'admin' ? '管理员' : '普通用户' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="storageUsed" label="已用存储" width="120">
            <template #default="scope">
              {{ formatStorage(scope.row.storageUsed) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'active' ? 'success' : 'danger'">
                {{ scope.row.status === 'active' ? '正常' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="注册时间" width="150" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
              <el-button 
                size="small" 
                :type="scope.row.status === 'active' ? 'warning' : 'success'"
                @click="toggleUserStatus(scope.row)"
              >
                {{ scope.row.status === 'active' ? '禁用' : '启用' }}
              </el-button>
              <el-button size="small" type="danger" @click="deleteUser(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        
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
      <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" :disabled="!!editingUser" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!editingUser">
          <el-input v-model="userForm.password" type="password" show-password />
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

// 用户表单
const userForm = reactive({
  username: '',
  email: '',
  password: '',
  role: 'user',
  status: 'active',
  storageQuota: 10
})

// 表单验证规则
const userRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
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
      user.username.toLowerCase().includes(query) || 
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
const formatStorage = (sizeInGB) => {
  if (sizeInGB < 1) {
    return `${(sizeInGB * 1024).toFixed(2)} MB`
  }
  return `${sizeInGB.toFixed(2)} GB`
}

// 加载用户数据
const loadUsers = async () => {
  loading.value = true
  try {
    // 这里应该调用后端API获取真实数据
    // 暂时使用模拟数据
    await new Promise(resolve => setTimeout(resolve, 500))
    
    users.value = [
      { id: 1, username: 'admin', email: 'admin@example.com', role: 'admin', storageUsed: 5.2, status: 'active', createdAt: '2023-01-01' },
      { id: 2, username: 'user001', email: 'user001@example.com', role: 'user', storageUsed: 2.8, status: 'active', createdAt: '2023-02-15' },
      { id: 3, username: 'user002', email: 'user002@example.com', role: 'user', storageUsed: 1.5, status: 'active', createdAt: '2023-03-20' },
      { id: 4, username: 'user003', email: 'user003@example.com', role: 'user', storageUsed: 3.7, status: 'disabled', createdAt: '2023-04-10' },
      { id: 5, username: 'user004', email: 'user004@example.com', role: 'user', storageUsed: 0.8, status: 'active', createdAt: '2023-05-05' },
      { id: 6, username: 'user005', email: 'user005@example.com', role: 'user', storageUsed: 4.2, status: 'active', createdAt: '2023-06-12' },
      { id: 7, username: 'user006', email: 'user006@example.com', role: 'user', storageUsed: 1.9, status: 'active', createdAt: '2023-07-18' },
      { id: 8, username: 'user007', email: 'user007@example.com', role: 'user', storageUsed: 2.3, status: 'active', createdAt: '2023-08-22' },
    ]
    
    totalUsers.value = users.value.length
  } catch (error) {
    console.error('加载用户数据失败:', error)
    ElMessage.error('加载用户数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
}

// 重置筛选条件
const resetFilters = () => {
  searchQuery.value = ''
  statusFilter.value = ''
  roleFilter.value = ''
  currentPage.value = 1
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
  Object.assign(userForm, {
    username: user.username,
    email: user.email,
    password: '',
    role: user.role,
    status: user.status,
    storageQuota: 10 // 假设默认配额，实际应该从用户数据中获取
  })
  showAddUserDialog.value = true
}

// 保存用户
const saveUser = async () => {
  if (!userFormRef.value) return
  
  try {
    await userFormRef.value.validate()
    
    // 这里应该调用后端API保存用户数据
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success(editingUser.value ? '用户更新成功' : '用户添加成功')
    showAddUserDialog.value = false
    editingUser.value = null
    resetUserForm()
    loadUsers()
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      console.error('保存用户失败:', error)
      ElMessage.error('保存用户失败')
    }
  }
}

// 切换用户状态
const toggleUserStatus = async (user) => {
  const action = user.status === 'active' ? '禁用' : '启用'
  try {
    await ElMessageBox.confirm(
      `确定要${action}用户 ${user.username} 吗？`,
      '确认操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 这里应该调用后端API更新用户状态
    await new Promise(resolve => setTimeout(resolve, 500))
    
    user.status = user.status === 'active' ? 'disabled' : 'active'
    ElMessage.success(`用户${action}成功`)
  } catch (error) {
    if (error !== 'cancel') {
      console.error(`${action}用户失败:`, error)
      ElMessage.error(`${action}用户失败`)
    }
  }
}

// 删除用户
const deleteUser = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 ${user.username} 吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 这里应该调用后端API删除用户
    await new Promise(resolve => setTimeout(resolve, 500))
    
    const index = users.value.findIndex(u => u.id === user.id)
    if (index !== -1) {
      users.value.splice(index, 1)
      totalUsers.value = users.value.length
    }
    
    ElMessage.success('用户删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除用户失败:', error)
      ElMessage.error('删除用户失败')
    }
  }
}

// 重置用户表单
const resetUserForm = () => {
  Object.assign(userForm, {
    username: '',
    email: '',
    password: '',
    role: 'user',
    status: 'active',
    storageQuota: 10
  })
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.admin-users {
  padding: 20px;
}

.users-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.search-bar {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>