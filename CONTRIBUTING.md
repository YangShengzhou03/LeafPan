# 贡献指南

<p align="center">
  <img src="https://raw.githubusercontent.com/YangShengzhou03/LeafPan/main/logo.png" alt="LeafPan Logo" width="150" height="150">
</p>

<h2 align="center">如何为 LeafPan 项目做出贡献</h2>

<p align="center">
  <a href="https://github.com/YangShengzhou03/LeafPan/issues">
    <img src="https://img.shields.io/github/issues/YangShengzhou03/LeafPan.svg" alt="Issues">
  </a>
  <a href="https://github.com/YangShengzhou03/LeafPan/pulls">
    <img src="https://img.shields.io/github/issues-pr/YangShengzhou03/LeafPan.svg" alt="Pull Requests">
  </a>
  <a href="https://github.com/YangShengzhou03/LeafPan/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/YangShengzhou03/LeafPan.svg" alt="License">
  </a>
</p>

感谢您对LeafPan项目的关注！本指南将帮助您了解如何为项目做出贡献，无论您是开发者、设计师还是文档撰写者。

## 🎯 开始之前

### 行为准则

LeafPan 项目采用开放友好的社区氛围，我们要求所有参与者遵守以下基本行为准则：

- **尊重他人**：尊重所有贡献者，无论经验水平、性别、性别认同和表达、性取向、残疾、外表、种族、年龄、宗教或国籍如何
- **包容接纳**：欢迎新贡献者，并帮助他们融入社区
- **建设性批评**：提供有建设性的反馈，避免人身攻击或贬低性言论
- **专注解决方案**：关注问题的解决方案，而不是问题本身

如果您遇到不符合上述准则的行为，请通过邮件联系项目维护者。

### 获取帮助

在开始贡献前，请先查阅以下资源获取帮助：

- **官方文档**：查看项目的[README.md](README.md)和其他技术文档
- **GitHub讨论区**：在[讨论区](https://github.com/YangShengzhou03/LeafPan/discussions)提问
- **GitHub Issues**：查看[现有问题](https://github.com/YangShengzhou03/LeafPan/issues)是否已有相关讨论
- **技术栈文档**：查阅相关技术栈的官方文档

### 贡献前的准备

1. **安装Git**：确保您的系统已安装Git版本控制工具
2. **GitHub账号**：注册一个GitHub账号
3. **开发环境**：准备适合前端和后端开发的环境（详见开发环境设置部分）

## 🚀 开发流程

### 1. 设置开发环境

#### 环境要求

- **后端**：
  - JDK 17+
  - Maven 3.8+
  - MySQL 8.0+
  - Redis 6.0+
  - MinIO

- **前端**：
  - Node.js 16+
  - npm 8+
  - Vue CLI

#### 后端开发设置
```bash
# 克隆项目
git clone https://github.com/YangShengzhou03/LeafPan.git
cd LeafPan/backend

# 复制环境配置文件
cp src/main/resources/application-dev.example.yml src/main/resources/application-dev.yml

# 编辑配置文件，设置数据库连接等信息
# 数据库配置示例：
# spring:
#   datasource:
#     url: jdbc:mysql://localhost:3306/leafpan?useSSL=false&serverTimezone=UTC
#     username: root
#     password: yourpassword

# 安装依赖
./mvnw clean install

# 运行数据库迁移（如果使用Flyway）
./mvnw flyway:migrate

# 运行开发服务器
./mvnw spring-boot:run
```

#### 前端开发设置
```bash
cd LeafPan/frontend

# 复制环境配置文件
cp .env.example .env.development

# 编辑配置文件，设置API地址等信息
# VITE_API_BASE_URL=http://localhost:8080/api

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

# 生成测试覆盖率报告
./mvnw jacoco:report

# 前端单元测试
cd frontend
npm run test:unit

# 前端组件测试
npm run test:component

# 端到端测试
npm run test:e2e
```

### 3. 代码质量检查

```bash
# 后端代码检查
cd backend
./mvnw checkstyle:check

# 前端代码检查
cd frontend
npm run lint
```

## 📝 贡献类型

我们欢迎多种形式的贡献，无论您的技能水平如何：

### 报告Bug

1. **搜索现有Issue**：在[Issues](https://github.com/YangShengzhou03/LeafPan/issues)中搜索是否已有相关报告
2. **创建新Issue**：如果没有找到相关报告，创建新的Issue
3. **提供详细信息**：
   - 清晰的问题标题
   - 详细的问题描述
   - 具体的复现步骤
   - 预期行为和实际行为的对比
   - 系统环境信息（操作系统、浏览器版本等）
   - 相关截图或日志（如适用）

#### Bug报告模板

```markdown
## 描述问题
请清晰简洁地描述您遇到的问题。

## 复现步骤
1. 打开...
2. 点击...
3. 观察...

## 预期行为
应该发生什么？

## 实际行为
实际发生了什么？

## 环境信息
- 操作系统：
- 浏览器：
- 版本：

## 截图/日志
如有可能，请提供相关截图或错误日志。
```

### 功能请求

1. **在讨论区交流**：在[Discussions](https://github.com/YangShengzhou03/LeafPan/discussions)中讨论您的想法
2. **创建功能请求Issue**：描述功能需求和使用场景
3. **提供详细信息**：
   - 功能的详细描述
   - 使用场景和用户故事
   - 可能的实现方案
   - 相关参考资料（如适用）

#### 功能请求模板

```markdown
## 功能描述
请详细描述您希望添加的功能。

## 使用场景
这个功能将如何被使用？请提供具体的用户故事。

## 预期行为
这个功能应该如何工作？

## 实现建议
如果您有关于实现的想法，请分享。

## 参考资料
如有相关的参考资料，请提供链接。
```

### 代码贡献

#### 小修复
对于简单的修复（如拼写错误、小bug修复）：
- 直接创建分支并实现修复
- 提交Pull Request，包含详细的修改说明
- 参考[Pull Request流程](#-pull-request流程)部分

#### 新功能或大型修复
对于新功能或较大规模的修复：
1. 先在Discussions中讨论您的想法
2. 创建功能分支
3. 实现功能并添加适当的测试
4. 确保所有测试通过
5. 提交Pull Request

### 文档贡献

我们非常欢迎文档改进：
- 修正文档中的错误
- 完善现有文档
- 添加新的使用指南或教程
- 翻译文档

### 其他贡献

- **设计贡献**：UI/UX改进、图标设计等
- **测试贡献**：编写测试用例、进行回归测试
- **安全贡献**：安全漏洞报告和修复
- **社区支持**：回答其他用户的问题、分享使用经验

## 🔧 代码规范

### 提交信息

我们严格遵循[Conventional Commits](https://www.conventionalcommits.org/)规范，确保提交历史清晰易懂：

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

#### 类型说明

| 类型 | 描述 | 示例 |
|------|------|------|
| `feat` | 添加新功能 | `feat: add file sharing feature` |
| `fix` | 修复bug | `fix: resolve file upload error` |
| `docs` | 文档更新 | `docs: update installation guide` |
| `style` | 代码格式调整 | `style: format Java code` |
| `refactor` | 重构代码 | `refactor: optimize file processing` |
| `test` | 测试相关 | `test: add unit tests for user service` |
| `chore` | 构建过程或辅助工具变动 | `chore: update dependencies` |
| `perf` | 性能优化 | `perf: improve file upload speed` |
| `ci` | CI配置更改 | `ci: update GitHub Actions workflow` |
| `revert` | 回滚更改 | `revert: revert previous commit` |

#### 提交信息示例

```
feat(auth): implement JWT authentication

Add JSON Web Token authentication mechanism with refresh token support.

- Implement token generation and validation
- Add token refresh endpoint
- Update security configuration

Closes #42
```

### 代码风格

#### Java代码
- 遵循[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 使用4个空格缩进
- 类名使用大驼峰命名法
- 方法名和变量名使用小驼峰命名法
- 每行不超过100个字符
- 方法不超过50行代码
- 类不超过1000行代码
- 使用Javadoc注释公共API

#### Vue/JavaScript代码
- 遵循[Vue Style Guide](https://v3.vuejs.org/style-guide/)
- 使用2个空格缩进
- 组件名使用大驼峰命名法
- 变量名使用小驼峰命名法
- 每行不超过80个字符
- 使用ES6+语法
- 为组件提供详细的props和emits定义
- 使用TypeScript类型注解

#### CSS/SCSS代码
- 使用BEM命名约定（Block, Element, Modifier）
- 组件样式使用scoped
- 避免使用!important
- 优先使用CSS变量管理主题

### 测试要求

- **单元测试**：新功能必须包含单元测试，覆盖所有关键路径
- **集成测试**：重要功能或涉及多个模块的功能需要集成测试
- **端到端测试**：用户交互相关功能需要端到端测试
- **测试覆盖率**：
  - 后端代码：单元测试覆盖率不低于80%
  - 前端代码：组件测试覆盖率不低于75%
  - 关键功能：测试覆盖率不低于90%
- **测试质量**：测试应关注业务逻辑而非简单的代码覆盖率

### 静态代码分析

项目使用多种静态代码分析工具确保代码质量：

- **后端**：
  - Checkstyle：检查代码风格
  - SonarQube：检测代码质量和安全问题
  - PMD：识别潜在问题

- **前端**：
  - ESLint：JavaScript/TypeScript代码检查
  - Stylelint：CSS/SCSS样式检查
  - Vue ESLint Plugin：Vue组件检查

提交前请确保通过所有静态代码分析检查。

## 🔄 Pull Request流程

### 1. Fork和克隆项目

```bash
# Fork项目（通过GitHub界面）

# 克隆你的Fork到本地
git clone https://github.com/YOUR_USERNAME/LeafPan.git
cd LeafPan

# 添加上游仓库
git remote add upstream https://github.com/YangShengzhou03/LeafPan.git
```

### 2. 同步上游代码

在开始工作前，确保你的本地仓库与上游同步：

```bash
# 获取上游更改
git fetch upstream

# 切换到main分支
git checkout main

# 合并上游更改
git merge upstream/main

# 推送更新到你的fork
git push origin main
```

### 3. 创建分支

为你的工作创建一个新分支：

```bash
# 从main分支创建新分支
git checkout -b <branch-type>/<description>

# 分支类型示例：feature, bugfix, docs, chore, etc.
# 例如：git checkout -b feature/file-upload-optimization
```

### 4. 实现更改

在你的分支上实现更改：

- 编写代码实现功能或修复问题
- 添加必要的测试
- 更新相关文档
- 确保所有测试通过
- 运行代码质量检查

### 5. 提交更改

提交你的更改，遵循提交信息规范：

```bash
# 添加更改
git add .

# 提交更改（使用Conventional Commits格式）
git commit -m "feat: add new feature description"

# 如果需要更新提交信息
git commit --amend
```

### 6. 推送到你的Fork

```bash
git push origin <your-branch-name>
```

### 7. 创建Pull Request

通过GitHub界面创建Pull Request：

1. 导航到你的Fork仓库
2. 点击"Pull request"按钮
3. 选择正确的分支
4. 填写详细的Pull Request描述，包括：
   - 更改的目的
   - 实现的功能或修复的问题
   - 任何相关的Issue引用
   - 测试方法

#### Pull Request模板

```markdown
## 更改类型
- [ ] Bug修复
- [ ] 新功能
- [ ] 文档更新
- [ ] 代码重构
- [ ] 性能优化

## 相关Issue
<!-- 如有相关Issue，请引用 -->
- #

## 更改描述
请详细描述你所做的更改以及原因。

## 实现细节
请描述你的实现方法和关键设计决策。

## 测试方法
请描述你如何测试这些更改。

## 截图/演示
如有可能，请提供截图或演示。

## 检查清单
- [ ] 代码遵循项目规范
- [ ] 所有测试通过
- [ ] 代码质量检查通过
- [ ] 相关文档已更新
- [ ] 更改向后兼容
```

### 8. 代码审查

- 项目维护者将审查你的Pull Request
- 审查者可能会提出修改建议
- 请及时回应审查意见并进行必要的修改
- 修改后再次推送更新到你的分支，Pull Request会自动更新

### 9. 合并

一旦你的Pull Request获得批准：
- 项目维护者将合并你的代码
- 你的分支将被删除（可选）
- 你的贡献将出现在项目的提交历史中

### 10. 同步更新

在你的Pull Request合并后，更新你的本地仓库：

```bash
# 切换到main分支
git checkout main

# 同步上游更改
git fetch upstream
git merge upstream/main

# 推送更新到你的fork
git push origin main
```

## 🏷️ 分支策略

LeafPan项目采用Git Flow分支管理策略，清晰定义不同分支的用途：

| 分支类型 | 命名格式 | 用途 | 合并目标 |
|---------|---------|------|----------|
| `main` | `main` | 主分支，包含稳定生产代码 | - |
| `develop` | `develop` | 开发主分支，集成新功能 | `main` (发布时) |
| `feature` | `feature/<name>` | 功能开发分支 | `develop` |
| `bugfix` | `bugfix/<issue-number>` | 问题修复分支 | `develop` 和 `main` |
| `release` | `release/<version>` | 发布准备分支 | `main` 和 `develop` |
| `hotfix` | `hotfix/<version>` | 紧急生产修复分支 | `main` 和 `develop` |

### 分支生命周期

- **feature分支**：从`develop`分支创建，完成后合并回`develop`分支
- **bugfix分支**：从`develop`分支创建，完成后合并回`develop`分支
- **release分支**：从`develop`分支创建，完成后合并到`main`和`develop`分支
- **hotfix分支**：从`main`分支创建，完成后合并到`main`和`develop`分支

### 分支命名规范

- 分支名使用小写
- 使用连字符(-)分隔单词
- 功能分支应清晰描述实现的功能
- Bug修复分支应包含相关Issue编号
- 发布和热修复分支应包含版本号

```bash
# 好的分支名示例
feature/file-preview-enhancement
bugfix/123-user-login-error
release/v1.2.0
hotfix/v1.1.1

# 不好的分支名示例
mybranch
fix-stuff
dev
```

## 📋 审查标准

所有提交的代码都将经过严格的审查流程，确保代码质量和项目健康发展。以下是审查的主要标准：

### 代码质量
- **可读性**：代码清晰易读，逻辑结构合理
- **遵循规范**：符合项目代码规范和风格指南
- **注释完善**：包含必要的注释，特别是公共API和复杂逻辑
- **代码组织**：良好的模块化和关注点分离
- **避免重复**：没有重复代码，遵循DRY原则
- **安全性**：没有安全漏洞，如SQL注入、XSS等风险

### 功能完整性
- **需求实现**：功能按需求完整实现
- **测试覆盖**：包含充分的单元测试和集成测试
- **文档更新**：相关文档已更新
- **向后兼容**：更改不破坏现有功能和API
- **错误处理**：包含适当的错误处理和日志记录

### 性能考虑
- **算法效率**：选择适当的算法和数据结构
- **资源使用**：内存和CPU使用合理
- **数据库优化**：数据库查询已优化，避免N+1查询等问题
- **前端性能**：前端代码考虑渲染性能和资源加载优化

### 审查流程

1. **自动化检查**：
   - 持续集成检查（CI）
   - 测试通过率
   - 代码覆盖率
   - 静态代码分析

2. **人工审查**：
   - 至少一名项目维护者进行代码审查
   - 可能会提出修改建议或问题
   - 开发者需要回应所有审查意见

3. **批准标准**：
   - 所有自动化检查通过
   - 审查者批准
   - 没有未解决的冲突
   - 更改符合项目目标和范围

4. **反馈处理**：
   - 收到审查反馈后，及时进行修改
   - 如有疑问，与审查者进行讨论
   - 修改后再次提交代码供审查

## 🎖️ 贡献者激励与认可

### 贡献者认可

我们非常重视每一位贡献者，并通过以下方式表示感谢：

- **贡献者名单**：在项目README中列出重要贡献者
- **GitHub认可**：通过GitHub的Contributors列表展示所有贡献者
- **项目徽章**：为活跃贡献者提供专属徽章
- **社区认可**：在项目博客和社交媒体中致谢

### 成为维护者

长期活跃且高质量的贡献者有机会成为项目维护者。维护者具有以下职责和权限：

- **代码审查**：审查和批准Pull Request
- **项目管理**：管理Issues和Discussions
- **版本发布**：负责新版本的发布
- **社区管理**：回答问题并帮助新贡献者
- **项目决策**：参与项目发展方向和技术决策

### 维护者选择标准

- 持续高质量的代码贡献（至少3个月）
- 积极参与代码审查和问题讨论
- 对项目有深入理解
- 展示良好的技术能力和沟通能力
- 愿意为项目长期投入

### 维护者权责

**权利：**
- 推送代码到主仓库
- 批准和合并Pull Request
- 管理项目设置和CI/CD配置
- 参与项目路线图规划

**责任：**
- 维护代码质量和项目标准
- 及时响应社区反馈
- 保持项目的可持续发展
- 遵循项目的行为准则
- 帮助新贡献者融入社区

## 💬 社区与支持

### 社区渠道

- **GitHub Discussions**：讨论项目想法、功能建议和使用问题
- **GitHub Issues**：报告bug和追踪问题
- **开发团队邮件**：重要事项可通过邮件联系开发团队

### 常见问题解答

#### 如何开始贡献？

1. 从一个简单的任务开始，如修复文档错误或简单的bug
2. 阅读项目文档，了解项目结构和开发流程
3. 加入社区讨论，介绍自己并表达贡献意向
4. 寻找带有"good first issue"标签的任务

#### 贡献被拒绝了怎么办？

不要气馁！贡献被拒绝通常是为了保持项目质量。请：
- 仔细阅读拒绝理由
- 与审查者进行讨论，了解改进方向
- 根据反馈修改后再次提交

#### 贡献有时间限制吗？

没有强制的时间要求。您可以根据自己的时间安排灵活参与项目贡献。

#### 我需要什么技能才能贡献？

不同类型的贡献需要不同的技能：
- 代码贡献：Java、Vue.js、TypeScript等
- 文档贡献：Markdown、技术写作能力
- 设计贡献：UI/UX设计、前端样式
- 测试贡献：测试编写和自动化测试经验

## 🙏 致谢

感谢所有为LeafPan项目做出贡献的开发者、设计师、文档撰写者和用户！您的贡献让项目变得更好。

特别感谢：

- 所有代码贡献者
- 报告bug和提供反馈的用户
- 改进文档的贡献者
- 帮助新贡献者的社区成员

---

我们期待您的贡献！如有任何问题，请随时联系我们。

*让我们一起让LeafPan变得更好！*