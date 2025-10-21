# 贡献指南

感谢您对LeafPan项目的关注！本指南将帮助您了解如何为项目做出贡献。

## 🎯 开始之前

### 行为准则

请阅读并遵守我们的[行为准则](CODE_OF_CONDUCT.md)。我们致力于为所有贡献者创造一个友好、尊重的环境。

### 获取帮助

- 查看[文档](https://github.com/YangShengzhou03/LeafPan/wiki)
- 在[讨论区](https://github.com/YangShengzhou03/LeafPan/discussions)提问
- 查看[现有问题](https://github.com/YangShengzhou03/LeafPan/issues)

## 🚀 开发流程

### 1. 设置开发环境

#### 后端开发
```bash
# 克隆项目
git clone https://github.com/YangShengzhou03/LeafPan.git
cd LeafPan/backend

# 安装依赖
./mvnw clean install

# 运行开发服务器
./mvnw spring-boot:run
```

#### 前端开发
```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

### 2. 运行测试

```bash
# 后端测试
cd backend
./mvnw test

# 前端测试
cd frontend
npm run test

# 端到端测试
npm run test:e2e
```

## 📝 贡献类型

### 报告Bug

1. 在[Issues](https://github.com/YangShengzhou03/LeafPan/issues)中搜索是否已有相关报告
2. 如果没有，创建新的Issue
3. 提供详细的问题描述、复现步骤、期望行为等

### 功能请求

1. 在[Discussions](https://github.com/YangShengzhou03/LeafPan/discussions)中讨论想法
2. 创建功能请求Issue
3. 描述功能需求、使用场景、实现建议

### 代码贡献

#### 小修复
- 直接提交Pull Request
- 包含详细的修改说明

#### 新功能
1. 先在Discussions中讨论
2. 创建功能分支
3. 实现功能并添加测试
4. 提交Pull Request

## 🔧 代码规范

### 提交信息

遵循[Conventional Commits](https://www.conventionalcommits.org/)规范：

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

类型说明：
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 重构代码
- `test`: 测试相关
- `chore`: 构建过程或辅助工具变动

### 代码风格

#### Java代码
- 遵循[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 使用4个空格缩进
- 类名使用大驼峰命名法
- 方法名和变量名使用小驼峰命名法

#### Vue/JavaScript代码
- 遵循[Vue Style Guide](https://v3.vuejs.org/style-guide/)
- 使用2个空格缩进
- 组件名使用大驼峰命名法
- 变量名使用小驼峰命名法

### 测试要求

- 新功能必须包含单元测试
- 重要功能需要集成测试
- 确保所有测试通过
- 测试覆盖率不低于80%

## 🔄 Pull Request流程

1. **Fork项目**
   - 点击GitHub上的Fork按钮
   - 克隆你的fork到本地

2. **创建分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **提交更改**
   ```bash
   git add .
   git commit -m "feat: add your feature"
   git push origin feature/your-feature-name
   ```

4. **创建Pull Request**
   - 在GitHub上创建Pull Request
   - 填写详细的描述
   - 关联相关Issue

5. **代码审查**
   - 等待项目维护者审查
   - 根据反馈进行修改
   - 可能需要多次迭代

## 🏷️ 分支策略

- `main`: 主分支，稳定版本
- `develop`: 开发分支，集成新功能
- `feature/*`: 功能开发分支
- `bugfix/*`: 问题修复分支
- `release/*`: 发布分支
- `hotfix/*`: 紧急修复分支

## 📋 审查标准

### 代码质量
- 代码清晰易读
- 遵循项目代码规范
- 包含必要的注释
- 没有安全漏洞

### 功能完整性
- 功能按需求实现
- 包含相关测试
- 文档已更新
- 向后兼容

### 性能考虑
- 没有明显的性能问题
- 数据库查询优化
- 内存使用合理

## 🎖️ 成为维护者

长期贡献者有机会成为项目维护者。维护者职责包括：

- 审查Pull Request
- 管理Issues和Discussions
- 发布新版本
- 指导新贡献者

## 🙏 致谢

感谢所有为LeafPan项目做出贡献的开发者！您的贡献让项目变得更好。

---

如有任何问题，请随时联系我们！