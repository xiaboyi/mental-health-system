const fs = require("fs");
const {
  Document, Packer, Paragraph, TextRun, Table, TableRow, TableCell,
  Header, Footer, AlignmentType, LevelFormat,
  HeadingLevel, BorderStyle, WidthType, ShadingType,
  PageNumber, PageBreak
} = require("docx");

// === Numbering config ===
const numbering = {
  config: [
    {
      reference: "bullets",
      levels: [{ level: 0, format: LevelFormat.BULLET, text: "•", alignment: AlignmentType.LEFT,
        style: { paragraph: { indent: { left: 720, hanging: 360 } } } }]
    },
    {
      reference: "numbers",
      levels: [{ level: 0, format: LevelFormat.DECIMAL, text: "%1.", alignment: AlignmentType.LEFT,
        style: { paragraph: { indent: { left: 720, hanging: 360 } } } }]
    }
  ]
};

// === Helper: paragraph ===
function p(text, opts = {}) {
  return new Paragraph({
    spacing: { after: opts.after || 80, before: opts.before || 0 },
    alignment: opts.align,
    numbering: opts.num ? { reference: opts.num.ref, level: opts.num.level || 0 } : undefined,
    children: [new TextRun({ text, ...(opts.run || {}) })]
  });
}

function pRich(runs, opts = {}) {
  return new Paragraph({
    spacing: { after: opts.after || 80, before: opts.before || 0 },
    children: runs.map(r => new TextRun(r))
  });
}

function heading(level, text) {
  return new Paragraph({ heading: level, children: [new TextRun(text)] });
}

// === Table border ===
const border = { style: BorderStyle.SINGLE, size: 1, color: "BBBBBB" };
const borders = { top: border, bottom: border, left: border, right: border };
const cellMargins = { top: 60, bottom: 60, left: 100, right: 100 };

function headerCell(text, width) {
  return new TableCell({
    borders, width: { size: width, type: WidthType.DXA },
    shading: { fill: "1E2761", type: ShadingType.CLEAR },
    margins: cellMargins,
    children: [new Paragraph({
      alignment: AlignmentType.CENTER,
      children: [new TextRun({ text, color: "FFFFFF", bold: true, size: 20, font: "Arial" })]
    })]
  });
}

function cell(text, width, opts = {}) {
  return new TableCell({
    borders, width: { size: width, type: WidthType.DXA },
    shading: opts.fill ? { fill: opts.fill, type: ShadingType.CLEAR } : undefined,
    margins: cellMargins,
    children: [new Paragraph({
      alignment: opts.align || AlignmentType.LEFT,
      children: [new TextRun({ text, size: 18, font: "Arial", ...(opts.run || {}) })]
    })]
  });
}

function codeCell(text, width) {
  return new TableCell({
    borders, width: { size: width, type: WidthType.DXA },
    shading: { fill: "F5F7FA", type: ShadingType.CLEAR },
    margins: cellMargins,
    children: [new Paragraph({
      children: [new TextRun({ text, size: 16, font: "Consolas", color: "333333" })]
    })]
  });
}

// ===== BUILD DOCUMENT =====
const doc = new Document({
  styles: {
    default: { document: { run: { font: "Arial", size: 22 } } },
    paragraphStyles: [
      { id: "Heading1", name: "Heading 1", basedOn: "Normal", next: "Normal", quickFormat: true,
        run: { size: 32, bold: true, font: "Arial", color: "1E2761" },
        paragraph: { spacing: { before: 360, after: 120 }, outlineLevel: 0 } },
      { id: "Heading2", name: "Heading 2", basedOn: "Normal", next: "Normal", quickFormat: true,
        run: { size: 26, bold: true, font: "Arial", color: "2E4A8E" },
        paragraph: { spacing: { before: 240, after: 100 }, outlineLevel: 1 } },
      { id: "Heading3", name: "Heading 3", basedOn: "Normal", next: "Normal", quickFormat: true,
        run: { size: 22, bold: true, font: "Arial", color: "333333" },
        paragraph: { spacing: { before: 160, after: 80 }, outlineLevel: 2 } },
    ]
  },
  numbering,
  sections: [{
    properties: {
      page: {
        size: { width: 11906, height: 16838 }, // A4
        margin: { top: 1134, right: 1134, bottom: 1134, left: 1134 }
      }
    },
    headers: {
      default: new Header({
        children: [new Paragraph({
          alignment: AlignmentType.RIGHT,
          children: [new TextRun({ text: "大学生心理健康咨询系统 · 模块一答辩材料", size: 16, color: "999999", italics: true })]
        })]
      })
    },
    footers: {
      default: new Footer({
        children: [new Paragraph({
          alignment: AlignmentType.CENTER,
          children: [new TextRun({ text: "Page ", size: 16, color: "999999" }),
            new TextRun({ children: [PageNumber.CURRENT], size: 16, color: "999999" })]
        })]
      })
    },
    children: [

      // ===== COVER =====
      new Paragraph({ spacing: { before: 2400 } }),
      new Paragraph({
        alignment: AlignmentType.CENTER,
        children: [new TextRun({ text: "模块一：用户管理模块", size: 52, bold: true, font: "Arial", color: "1E2761" })]
      }),
      new Paragraph({
        alignment: AlignmentType.CENTER, spacing: { after: 200 },
        children: [new TextRun({ text: "答辩准备材料", size: 36, font: "Arial", color: "667EEA" })]
      }),
      new Paragraph({ spacing: { before: 600 } }),
      new Paragraph({
        alignment: AlignmentType.CENTER, spacing: { after: 80 },
        children: [new TextRun({ text: "大学生心理健康咨询系统", size: 24, font: "Arial", color: "666666" })]
      }),
      new Paragraph({
        alignment: AlignmentType.CENTER, spacing: { after: 80 },
        children: [new TextRun({ text: "技术栈：Spring Boot 3.4.7 + MyBatis-Plus 3.5 + Vue 3 + Element Plus + MySQL 8.0", size: 20, font: "Arial", color: "888888" })]
      }),
      new Paragraph({
        alignment: AlignmentType.CENTER,
        children: [new TextRun({ text: "答辩人：成员1 ｜ 负责模块：用户注册/登录/个人信息管理/管理员用户管理/JWT鉴权", size: 20, font: "Arial", color: "888888" })]
      }),

      new Paragraph({ children: [new PageBreak()] }),

      // ===== 1. MODULE OVERVIEW =====
      heading(HeadingLevel.HEADING_1, "一、模块概述"),
      p("本模块是大学生心理健康咨询系统的核心基础模块，负责整个系统的用户身份认证与权限管理。所有其他模块（心理测评、咨询预约、在线咨询、知识库、数据看板）都依赖本模块提供的用户身份识别和JWT Token鉴权机制。"),
      p("模块核心职责：", { run: { bold: true, size: 22 }, after: 40 }),
      p("用户注册与登录 —— 提供系统入口，支持学生/咨询师/管理员三种角色注册", { num: { ref: "bullets" }, after: 40 }),
      p("JWT无状态认证 —— 登录后签发Token，后续请求通过拦截器自动验证身份", { num: { ref: "bullets" }, after: 40 }),
      p("个人信息管理 —— 支持查看/修改个人信息、头像上传、密码修改", { num: { ref: "bullets" }, after: 40 }),
      p("管理员用户管理 —— 管理员可查看所有用户列表、按角色筛选、启用/禁用用户", { num: { ref: "bullets" }, after: 40 }),
      p("前后端路由权限控制 —— 前端路由守卫 + 后端JWT拦截器双重校验", { num: { ref: "bullets" }, after: 80 }),

      // ===== 2. TECH STACK =====
      heading(HeadingLevel.HEADING_1, "二、技术栈明细"),
      new Table({
        width: { size: 9638, type: WidthType.DXA },
        columnWidths: [2200, 3500, 3938],
        rows: [
          new TableRow({ children: [headerCell("层级", 2200), headerCell("技术", 3500), headerCell("说明", 3938)] }),
          new TableRow({ children: [cell("后端框架", 2200), cell("Spring Boot 3.4.7", 3500, { run: { bold: true } }), cell("Java 25运行，内嵌Tomcat 10.1，自动配置", 3938)] }),
          new TableRow({ children: [cell("ORM框架", 2200, { fill: "F5F7FA" }), cell("MyBatis-Plus 3.5.7", 3500, { fill: "F5F7FA", run: { bold: true } }), cell("BaseMapper CRUD、LambdaQueryWrapper条件查询、分页插件", 3938, { fill: "F5F7FA" })] }),
          new TableRow({ children: [cell("密码加密", 2200), cell("Spring Security Crypto (BCrypt)", 3500, { run: { bold: true } }), cell("BCryptPasswordEncoder，自动加盐，60位密文", 3938)] }),
          new TableRow({ children: [cell("JWT库", 2200, { fill: "F5F7FA" }), cell("io.jsonwebtoken jjwt 0.12.6", 3500, { fill: "F5F7FA", run: { bold: true } }), cell("HMAC-SHA256签名，24h过期，Claims携带userId/role", 3938, { fill: "F5F7FA" })] }),
          new TableRow({ children: [cell("参数校验", 2200), cell("Jakarta Validation", 3500, { run: { bold: true } }), cell("@NotBlank注解校验DTO字段，MethodArgumentNotValidException全局处理", 3938)] }),
          new TableRow({ children: [cell("前端框架", 2200, { fill: "F5F7FA" }), cell("Vue 3 + Vite 5 + Element Plus", 3500, { fill: "F5F7FA", run: { bold: true } }), cell("Composition API + <script setup> + Vue Router 4", 3938, { fill: "F5F7FA" })] }),
          new TableRow({ children: [cell("HTTP通信", 2200), cell("Axios 1.6", 3500, { run: { bold: true } }), cell("请求拦截器自动注入Bearer Token，响应拦截器统一错误处理401跳转", 3938)] }),
          new TableRow({ children: [cell("数据库", 2200, { fill: "F5F7FA" }), cell("MySQL 8.0", 3500, { fill: "F5F7FA", run: { bold: true } }), cell("utf8mb4编码，逻辑删除@TableLogic，自动填充create_time/update_time", 3938, { fill: "F5F7FA" })] }),
        ]
      }),

      new Paragraph({ spacing: { before: 160 } }),
      // ===== 3. DATABASE =====
      heading(HeadingLevel.HEADING_1, "三、数据库设计 — user表"),
      p("我负责设计并创建了 user 表，是整个系统最核心的基础数据表，被其他所有模块的6张表通过外键引用："),
      new Table({
        width: { size: 9638, type: WidthType.DXA },
        columnWidths: [1800, 1400, 1000, 5438],
        rows: [
          new TableRow({ children: [headerCell("字段名", 1800), headerCell("类型", 1400), headerCell("约束", 1000), headerCell("说明", 5438)] }),
          ...[
            ["id", "BIGINT", "PK AUTO", "用户唯一标识，MyBatis-Plus @TableId(type=IdType.AUTO)自增"],
            ["username", "VARCHAR(50)", "UNIQUE NOT NULL", "登录用户名，唯一索引uk_username，LambdaQueryWrapper.eq查询"],
            ["password", "VARCHAR(255)", "NOT NULL", "BCrypt加密后的密文，60字符$2a$10$...格式"],
            ["real_name", "VARCHAR(50)", "DEFAULT NULL", "真实姓名，注册时默认为username，后续可修改"],
            ["phone", "VARCHAR(20)", "DEFAULT NULL", "手机号，注册时可填，前端正则校验/^1[3-9]\\d{9}$/"],
            ["email", "VARCHAR(100)", "DEFAULT NULL", "邮箱地址"],
            ["avatar", "VARCHAR(255)", "DEFAULT NULL", "头像URL，配合FileController上传接口获取"],
            ["gender", "TINYINT", "DEFAULT 0", "0-未知 1-男 2-女，前端el-radio-group展示"],
            ["age", "INT", "DEFAULT NULL", "年龄，前端el-input-number组件，范围1-120"],
            ["role", "VARCHAR(20)", "NOT NULL DEFAULT STUDENT", "角色：STUDENT/COUNSELOR/ADMIN，决定了前端动态菜单和权限"],
            ["status", "TINYINT", "NOT NULL DEFAULT 1", "0-禁用(无法登录) 1-正常，管理员可切换"],
            ["create_time", "DATETIME", "自动填充", "@TableField(fill=FieldFill.INSERT)，MyMetaObjectHandler自动生成"],
            ["update_time", "DATETIME", "自动填充", "@TableField(fill=FieldFill.INSERT_UPDATE)，修改时自动更新"],
            ["deleted", "TINYINT", "DEFAULT 0", "@TableLogic逻辑删除，MyBatis-Plus自动过滤deleted=1的记录"],
          ].map(([f, t, c, d], i) => new TableRow({
            children: [
              cell(f, 1800, i % 2 === 1 ? { fill: "F5F7FA" } : {}),
              cell(t, 1400, i % 2 === 1 ? { fill: "F5F7FA" } : {}),
              cell(c, 1000, i % 2 === 1 ? { fill: "F5F7FA" } : {}),
              cell(d, 5438, i % 2 === 1 ? { fill: "F5F7FA" } : {}),
            ]
          }))
        ]
      }),

      new Paragraph({ children: [new PageBreak()] }),
      // ===== 4. BACKEND =====
      heading(HeadingLevel.HEADING_1, "四、后端实现详解"),
      heading(HeadingLevel.HEADING_2, "4.1 项目包结构"),
      p("我负责编写了以下所有后端类（共15个文件），遵循Controller → Service → Mapper 三层分层架构："),
      new Table({
        width: { size: 9638, type: WidthType.DXA },
        columnWidths: [2400, 7238],
        rows: [
          new TableRow({ children: [headerCell("包/类", 2400), headerCell("我负责的文件", 7238)] }),
          ...[
            ["entity/", "User.java — 用户实体类，@TableName(\"user\")映射数据表"],
            ["dto/", "LoginDTO.java, RegisterDTO.java, LoginResultDTO.java, Result.java, PageResult.java"],
            ["mapper/", "UserMapper.java — 继承BaseMapper<User>，自定义countByRole查询"],
            ["service/", "UserService.java (接口) + impl/UserServiceImpl.java (实现)"],
            ["controller/", "AuthController.java — 登录注册接口\nUserController.java — 用户信息/列表/状态管理"],
            ["utils/", "JwtUtils.java — JWT Token生成与解析工具类"],
            ["interceptor/", "JwtInterceptor.java — JWT拦截器，验证Token并注入request属性"],
            ["config/", "WebConfig.java — CORS跨域 + 拦截器注册 + 文件资源映射",
             "MyBatisPlusConfig.java — 分页插件 + @MapperScan",
             "MyMetaObjectHandler.java — create_time/update_time自动填充",
             "GlobalExceptionHandler.java — 全局异常统一处理"],
          ].map(([pkg, files], i) => new TableRow({
            children: [
              cell(pkg, 2400, i % 2 === 1 ? { fill: "F5F7FA" } : {}, { run: { bold: true, size: 18 } }),
              cell(files, 7238, i % 2 === 1 ? { fill: "F5F7FA" } : {}, { run: { size: 18 } }),
            ]
          }))
        ]
      }),

      heading(HeadingLevel.HEADING_2, "4.2 实体类 — User.java"),
      p("核心注解说明（这些注解是答辩时必须讲清楚的技术点）："),
      p("@TableName(\"user\") — MyBatis-Plus注解，指定映射的数据库表名", { num: { ref: "bullets" }, after: 30 }),
      p("@TableId(type = IdType.AUTO) — 主键自增策略，数据库AUTO_INCREMENT，插入后自动回填ID", { num: { ref: "bullets" }, after: 30 }),
      p("@TableField(fill = FieldFill.INSERT) — createTime字段在插入时由MyMetaObjectHandler自动填充LocalDateTime.now()", { num: { ref: "bullets" }, after: 30 }),
      p("@TableField(fill = FieldFill.INSERT_UPDATE) — updateTime字段在插入和更新时自动填充", { num: { ref: "bullets" }, after: 30 }),
      p("@TableLogic — 逻辑删除注解。调用baseMapper.deleteById()时，MyBatis-Plus自动将deleted字段设置为1，而不会物理删除。查询时自动追加 WHERE deleted=0", { num: { ref: "bullets" }, after: 80 }),

      heading(HeadingLevel.HEADING_2, "4.3 DTO数据传输对象"),
      heading(HeadingLevel.HEADING_3, "LoginDTO.java — 登录请求体"),
      p("@NotBlank(message = \"用户名不能为空\") — Jakarta Validation注解，如果前端传入空字符串，Spring自动抛出MethodArgumentNotValidException，被GlobalExceptionHandler捕获并返回400错误", { num: { ref: "bullets" }, after: 80 }),
      heading(HeadingLevel.HEADING_3, "RegisterDTO.java — 注册请求体"),
      p("包含username/password/realName/phone/email/gender/age/role八个字段，其中username和password使用@NotBlank强制非空", { num: { ref: "bullets" }, after: 80 }),
      heading(HeadingLevel.HEADING_3, "LoginResultDTO.java — 登录响应体"),
      p("包含token/userId/username/realName/role/avatar六个字段。前端收到后存入localStorage，用于后续所有请求的身份标识", { num: { ref: "bullets" }, after: 80 }),
      heading(HeadingLevel.HEADING_3, "Result<T> — 统一响应封装"),
      p("所有API返回统一的JSON结构：{\"code\": 200, \"message\": \"success\", \"data\": {...}}。成功时code=200，失败时code=400/401/500。这是RESTful API的最佳实践", { num: { ref: "bullets" }, after: 80 }),

      new Paragraph({ children: [new PageBreak()] }),
      heading(HeadingLevel.HEADING_2, "4.4 Mapper数据访问层 — UserMapper.java"),
      p("核心代码："),
      p("public interface UserMapper extends BaseMapper<User>", { run: { font: "Consolas", size: 18, bold: true }, after: 40 }),
      p("继承MyBatis-Plus的BaseMapper<User>后，自动拥有以下方法，无需写任何SQL：", { after: 40 }),
      p("baseMapper.insert(user) — 插入用户", { num: { ref: "bullets" }, after: 30 }),
      p("baseMapper.selectById(id) — 根据ID查询", { num: { ref: "bullets" }, after: 30 }),
      p("baseMapper.selectOne(wrapper) — 条件查询单条", { num: { ref: "bullets" }, after: 30 }),
      p("baseMapper.selectCount(wrapper) — 条件统计数量", { num: { ref: "bullets" }, after: 30 }),
      p("baseMapper.updateById(user) — 根据ID更新", { num: { ref: "bullets" }, after: 30 }),
      p("baseMapper.selectPage(page, wrapper) — 分页查询（需配置分页插件）", { num: { ref: "bullets" }, after: 40 }),
      p("自定义SQL（应对答辩提问：有没有写自定义SQL？）：", { run: { bold: true }, after: 40 }),
      p("@Select(\"SELECT COUNT(*) FROM user WHERE role = #{role} AND deleted = 0 AND status = 1\")", { run: { font: "Consolas", size: 17, color: "333333" }, after: 30 }),
      p("Long countByRole(String role);", { run: { font: "Consolas", size: 17, color: "333333" }, after: 40 }),
      p("这个自定义SQL用于统计各角色在线用户数（管理员仪表盘使用），使用@Select注解直接写SQL，同时自动过滤逻辑删除和禁用用户。", { after: 80 }),

      heading(HeadingLevel.HEADING_2, "4.5 Service业务层 — UserServiceImpl.java"),
      p("这是模块一最核心的业务逻辑类，继承 ServiceImpl<UserMapper, User> 并实现 UserService 接口。每一个方法在答辩时都要能讲清楚：", { after: 80 }),

      heading(HeadingLevel.HEADING_3, "① 登录方法 login(LoginDTO) → LoginResultDTO"),
      p("第1步：LambdaQueryWrapper<User>条件查询 —— 使用Lambda表达式 eq(User::getUsername, loginDTO.getUsername())，类型安全，避免字符串硬编码", { num: { ref: "numbers" }, after: 30 }),
      p("第2步：用户存在性检查 —— 若不存在，throw RuntimeException(\"用户名或密码错误\")（故意不区分用户名错误和密码错误，防止恶意枚举用户名）", { num: { ref: "numbers" }, after: 30 }),
      p("第3步：账号状态检查 —— 若status==0，throw RuntimeException(\"账号已被禁用\")，被拦截的账号即使在数据库中存在也无法登录", { num: { ref: "numbers" }, after: 30 }),
      p("第4步：BCrypt密码验证 —— encoder.matches(明文密码, 数据库密文)，BCrypt算法内部从密文中提取盐值，对明文重新计算后比对。注意：绝对不能对密文再次加密后比较，必须用matches()方法", { num: { ref: "numbers" }, after: 30 }),
      p("第5步：JWT Token生成 —— jwtUtils.generateToken(userId, username, role)，内部将userId/username/role写入Claims，设置24小时过期时间，使用HMAC-SHA256签名", { num: { ref: "numbers" }, after: 30 }),
      p("第6步：组装返回 —— 将token/userId/username/realName/role/avatar封装进LoginResultDTO返回", { num: { ref: "numbers" }, after: 80 }),

      heading(HeadingLevel.HEADING_3, "② 注册方法 register(RegisterDTO)"),
      p("第1步：用户名唯一性校验 —— baseMapper.selectCount(wrapper.eq(User::getUsername, dto.getUsername()))，count>0则抛出异常", { num: { ref: "numbers" }, after: 30 }),
      p("第2步：BCrypt加密 —— encoder.encode(dto.getPassword())，每次加密生成不同的随机盐，即使两个用户的密码相同，密文也不同（防彩虹表）", { num: { ref: "numbers" }, after: 30 }),
      p("第3步：默认值设置 —— realName为空则默认用username，role为空则默认\"STUDENT\"，gender为空则默认0", { num: { ref: "numbers" }, after: 30 }),
      p("第4步：插入数据库 —— baseMapper.insert(user)，MyBatis-Plus自动回填自增ID", { num: { ref: "numbers" }, after: 80 }),

      new Paragraph({ children: [new PageBreak()] }),
      heading(HeadingLevel.HEADING_3, "③ 获取当前用户 getCurrentUser(token)"),
      p("解析Token中的userId → baseMapper.selectById(userId) → 返回User实体。这是一个简单的查询，但它是所有需要[知道当前用户是谁]的接口的基础。调用方（UserController）拿到User后会将password置为null再返回前端，确保密码密文不会泄露。", { after: 80 }),

      heading(HeadingLevel.HEADING_2, "4.6 Controller控制层"),
      heading(HeadingLevel.HEADING_3, "AuthController — 认证接口（无需JWT拦截）"),
      p("POST /api/auth/login — @Valid @RequestBody LoginDTO，自动触发参数校验", { num: { ref: "bullets" }, after: 30 }),
      p("POST /api/auth/register — @Valid @RequestBody RegisterDTO，注册成功后返回\"注册成功\"", { num: { ref: "bullets" }, after: 30 }),
      p("异常处理：try-catch捕获RuntimeException，返回Result.error(401, e.getMessage())。同时GlobalExceptionHandler会兜底处理校验异常。", { num: { ref: "bullets" }, after: 80 }),

      heading(HeadingLevel.HEADING_3, "UserController — 用户管理接口（需JWT拦截）"),
      p("GET /api/user/info — 从Header提取Token，调用getCurrentUser()，返回用户信息（密码置null）", { num: { ref: "bullets" }, after: 30 }),
      p("PUT /api/user/info — 从request.getAttribute(\"userId\")获取当前用户ID（由JwtInterceptor注入），更新个人信息。使用setter只更新允许修改的字段，将password/username/role/status设为null防止用户通过此接口提权", { num: { ref: "bullets" }, after: 30 }),
      p("PUT /api/user/password — 先验证旧密码(encoder.matches)，再BCrypt加密新密码更新", { num: { ref: "bullets" }, after: 30 }),
      p("GET /api/user/list — 管理员接口。LambdaQueryWrapper条件筛选，按role过滤(可选)，按create_time倒序，MyBatis-Plus分页 → PageResult封装返回。所有记录password置null。", { num: { ref: "bullets" }, after: 30 }),
      p("PUT /api/user/status/{id} — 管理员启用/禁用用户。先getById(id)检查存在性，再设置status并updateById", { num: { ref: "bullets" }, after: 80 }),

      heading(HeadingLevel.HEADING_2, "4.7 安全机制（答辩重点加分项）"),
      heading(HeadingLevel.HEADING_3, "JwtUtils.java — JWT令牌工具"),
      p("这是一个Spring @Component组件，从application.yml读取jwt.secret和jwt.expiration配置："),
      p("generateToken(userId, username, role)：Jwts.builder().claims(claims).subject(username).issuedAt(new Date()).expiration(24小时).signWith(HMAC-SHA256密钥).compact()", { num: { ref: "bullets" }, after: 30 }),
      p("parseToken(token)：Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload()，解析失败返回null", { num: { ref: "bullets" }, after: 30 }),
      p("validateToken(token)：判断Claims是否为null，简单但有效的Token验证方法", { num: { ref: "bullets" }, after: 30 }),
      p("密钥管理：使用io.jsonwebtoken.security.Keys.hmacShaKeyFor()从配置的字符串生成SecretKey对象，符合jjwt 0.12新API规范", { num: { ref: "bullets" }, after: 80 }),

      heading(HeadingLevel.HEADING_3, "JwtInterceptor.java — JWT拦截器"),
      p("这是整个系统安全机制的核心，每个/api/**请求（除login/register外）都会经过此拦截器："),
      p("OPTIONS预检请求直接放行(CORS需要)", { num: { ref: "numbers" }, after: 30 }),
      p("从Header提取Authorization，截取\"Bearer \"后的Token字符串", { num: { ref: "numbers" }, after: 30 }),
      p("调用jwtUtils.validateToken()验证Token有效性和过期时间", { num: { ref: "numbers" }, after: 30 }),
      p("验证通过：将userId/username/role注入request.setAttribute()，后续Controller通过request.getAttribute(\"userId\")获取当前用户", { num: { ref: "numbers" }, after: 30 }),
      p("验证失败：返回HTTP 401状态码 + JSON错误体{\"code\":401,\"message\":\"请先登录\"}", { num: { ref: "numbers" }, after: 80 }),

      heading(HeadingLevel.HEADING_3, "WebConfig.java — 拦截器与CORS配置"),
      p("addInterceptors：注册JwtInterceptor，拦截/api/**路径，排除/api/auth/login和/api/auth/register", { num: { ref: "bullets" }, after: 30 }),
      p("addCorsMappings：全局CORS配置，allowedOriginPatterns(*)允许所有来源，allowedMethods(GET/POST/PUT/DELETE/OPTIONS)，allowCredentials(true)支持携带Cookie和Authorization头", { num: { ref: "bullets" }, after: 30 }),
      p("addResourceHandlers：映射/uploads/**到本地./uploads/目录，使上传的头像文件可通过URL直接访问", { num: { ref: "bullets" }, after: 80 }),

      heading(HeadingLevel.HEADING_3, "BCrypt加密原理（答辩常见追问）"),
      p("BCrypt是一种基于Blowfish加密算法的密码哈希算法，特点：", { after: 40 }),
      p("自动加盐：不需要手动管理盐值，每次调用encode()自动生成16字节随机盐，内嵌到密文中（密文格式：$2a$10$[22字符salt][31字符hash]）", { num: { ref: "bullets" }, after: 30 }),
      p("工作因子cost=10：表示迭代2^10=1024次，耗时约0.1秒。这个值越大越安全但越慢，10是工程上的最佳平衡点", { num: { ref: "bullets" }, after: 30 }),
      p("单向不可逆：无法从密文反推明文，只能通过matches()方法验证", { num: { ref: "bullets" }, after: 30 }),
      p("抗彩虹表：不同用户相同密码产生不同密文，无法通过预计算的哈希表批量破解", { num: { ref: "bullets" }, after: 80 }),

      new Paragraph({ children: [new PageBreak()] }),
      heading(HeadingLevel.HEADING_2, "4.8 配置类"),
      heading(HeadingLevel.HEADING_3, "MyBatisPlusConfig.java"),
      p("@MapperScan(\"com.mentalhealth.mapper\") — 自动扫描Mapper接口", { num: { ref: "bullets" }, after: 30 }),
      p("MybatisPlusInterceptor + PaginationInnerInterceptor(DbType.MYSQL) — 分页插件，自动拦截SQL追加LIMIT子句，同时计算total总数", { num: { ref: "bullets" }, after: 80 }),

      heading(HeadingLevel.HEADING_3, "MyMetaObjectHandler.java"),
      p("实现MetaObjectHandler接口，重写insertFill和updateFill方法：", { num: { ref: "bullets" }, after: 30 }),
      p("插入时：自动填充createTime和updateTime为LocalDateTime.now()", { num: { ref: "bullets" }, after: 30 }),
      p("更新时：自动填充updateTime为LocalDateTime.now()", { num: { ref: "bullets" }, after: 40 }),
      p("这样开发人员不需要在每个插入/更新操作中手动设置时间字段，减少代码冗余", { after: 80 }),

      heading(HeadingLevel.HEADING_3, "GlobalExceptionHandler.java"),
      p("@RestControllerAdvice + @ExceptionHandler实现全局异常拦截：", { num: { ref: "bullets" }, after: 30 }),
      p("RuntimeException → 400 + 异常消息（处理业务异常如用户名已存在、密码错误）", { num: { ref: "bullets" }, after: 30 }),
      p("MethodArgumentNotValidException → 400 + 第一条校验失败消息（处理@Valid @NotBlank）", { num: { ref: "bullets" }, after: 30 }),
      p("Exception → 500 + 详细错误信息（兜底处理未知异常）", { num: { ref: "bullets" }, after: 80 }),

      new Paragraph({ children: [new PageBreak()] }),
      // ===== 5. FRONTEND =====
      heading(HeadingLevel.HEADING_1, "五、前端实现详解"),
      heading(HeadingLevel.HEADING_2, "5.1 项目入口 — main.js"),
      p("createApp(App).use(router).use(ElementPlus).mount('#app')", { run: { font: "Consolas", size: 18 }, after: 40 }),
      p("注册Vue Router（路由管理）、Element Plus（UI组件库）、Element Plus Icons（图标库全量注册）。使用Composition API + <script setup>语法糖编写所有组件。", { after: 80 }),

      heading(HeadingLevel.HEADING_2, "5.2 Axios请求封装 — utils/request.js"),
      p("这是前后端通信的核心封装，两个拦截器是答辩要点："),
      p("请求拦截器(request interceptor)：从localStorage读取token，自动注入到config.headers.Authorization = 'Bearer ' + token。这样所有Vue组件调用API时不需要手动传Token，完全自动化。", { num: { ref: "numbers" }, after: 40 }),
      p("响应拦截器(response interceptor)：检查response.data.code，非200时自动ElMessage.error弹出错误提示；code===401时自动清除token并router.push('/login')跳转登录页。这确保了Token过期后用户不会卡在空页面。", { num: { ref: "numbers" }, after: 40 }),
      p("baseURL: '/api' — 配合Vite的proxy配置，开发环境下所有/api请求被代理到localhost:8080后端", { num: { ref: "numbers" }, after: 80 }),

      heading(HeadingLevel.HEADING_2, "5.3 API接口封装 — api/index.js"),
      p("我负责的API封装（authApi + userApi）："),
      p("authApi.login(data) → POST /api/auth/login", { run: { font: "Consolas", size: 18 }, after: 30 }),
      p("authApi.register(data) → POST /api/auth/register", { run: { font: "Consolas", size: 18 }, after: 30 }),
      p("userApi.getInfo() → GET /api/user/info", { run: { font: "Consolas", size: 18 }, after: 30 }),
      p("userApi.updateInfo(data) → PUT /api/user/info", { run: { font: "Consolas", size: 18 }, after: 30 }),
      p("userApi.updatePassword(data) → PUT /api/user/password", { run: { font: "Consolas", size: 18 }, after: 30 }),
      p("userApi.list(params) → GET /api/user/list?page=&size=&role=", { run: { font: "Consolas", size: 18 }, after: 30 }),
      p("userApi.updateStatus(id, status) → PUT /api/user/status/{id}", { run: { font: "Consolas", size: 18 }, after: 80 }),

      heading(HeadingLevel.HEADING_2, "5.4 路由配置与权限守卫 — router/index.js"),
      p("路由守卫 beforeEach 是前端权限控制的核心，我实现了三层检查：", { after: 40 }),
      p("Token检查：to.path不是login/register 且 无token → next('/login')强制跳转登录", { num: { ref: "numbers" }, after: 30 }),
      p("已登录反向拦截：to.path是login/register 且 已有token → next('/')跳转首页", { num: { ref: "numbers" }, after: 30 }),
      p("角色权限检查：to.meta.role存在 且 当前用户角色不在允许列表中 → next('/dashboard')跳转看板", { num: { ref: "numbers" }, after: 40 }),
      p("例如AdminUsers页面的meta: { role: 'ADMIN' }，只有role=ADMIN的用户才能访问，STUDENT和COUNSELOR会被重定向到首页。这是前端层面的权限控制，与后端JWT拦截器形成双重保护。", { after: 80 }),

      heading(HeadingLevel.HEADING_2, "5.5 页面组件"),
      heading(HeadingLevel.HEADING_3, "Login.vue — 登录页"),
      p("技术要点：", { run: { bold: true }, after: 30 }),
      p("Element Plus表单校验：el-form :rules绑定校验规则，username和password均为必填，trigger: 'blur'在失去焦点时触发", { num: { ref: "bullets" }, after: 30 }),
      p("登录流程：formRef.value.validate() → 异步调用authApi.login() → 成功后localStorage.setItem存token和userInfo → router.push('/')跳转首页", { num: { ref: "bullets" }, after: 30 }),
      p("loading状态：防止重复提交，登录按钮在请求期间显示loading动画并禁用点击", { num: { ref: "bullets" }, after: 30 }),
      p("CSS渐变背景：background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)，卡片居中flex布局", { num: { ref: "bullets" }, after: 80 }),

      new Paragraph({ children: [new PageBreak()] }),
      heading(HeadingLevel.HEADING_3, "Register.vue — 注册页"),
      p("技术要点：", { run: { bold: true }, after: 30 }),
      p("自定义校验规则：确认密码字段使用validator函数，value === form.password 否则 callback(new Error('两次密码不一致'))", { num: { ref: "bullets" }, after: 30 }),
      p("手机号正则校验：pattern: /^1[3-9]\\d{9}$/，只允许合法的中国大陆手机号格式", { num: { ref: "bullets" }, after: 30 }),
      p("性别选择：el-radio-group绑定gender，:label=\"1\"为男，:label=\"2\"为女", { num: { ref: "bullets" }, after: 80 }),

      heading(HeadingLevel.HEADING_3, "Profile.vue — 个人信息页"),
      p("技术要点：", { run: { bold: true }, after: 30 }),
      p("头像上传：el-upload组件，action=\"/api/file/upload?businessType=AVATAR\"，headers动态注入Authorization Token(computed计算属性)，on-success回调更新avatar URL并自动保存", { num: { ref: "bullets" }, after: 30 }),
      p("修改密码：el-dialog弹出对话框，验证旧密码(BCrypt匹配) → 加密新密码 → 更新数据库", { num: { ref: "bullets" }, after: 30 }),
      p("数据加载：onMounted生命周期钩子中调用userApi.getInfo()获取当前用户信息 → Object.assign(form, res.data)填充表单", { num: { ref: "bullets" }, after: 80 }),

      heading(HeadingLevel.HEADING_3, "AdminUsers.vue — 管理员用户管理页"),
      p("技术要点：", { run: { bold: true }, after: 30 }),
      p("分页列表：el-table + el-pagination，v-model:current-page + @current-change重新加载", { num: { ref: "bullets" }, after: 30 }),
      p("角色标签：el-tag根据role显示不同颜色，ADMIN=danger红色，COUNSELOR=warning橙色，STUDENT=success绿色", { num: { ref: "bullets" }, after: 30 }),
      p("状态切换：toggleStatus()方法，调用userApi.updateStatus(id, 新状态)，成功后在本地直接修改row.status实现即时UI反馈", { num: { ref: "bullets" }, after: 80 }),

      heading(HeadingLevel.HEADING_3, "Layout.vue — 布局页（动态菜单）"),
      p("技术要点：", { run: { bold: true }, after: 30 }),
      p("动态菜单路由：根据userInfo.role从menusByRole对象中选择对应的菜单配置数组，STUDENT有8个菜单项，COUNSELOR有6个，ADMIN有3个（含子菜单）", { num: { ref: "bullets" }, after: 30 }),
      p("折叠侧边栏：isCollapse响应式变量控制el-aside的width('64px'/'220px')和el-menu的collapse属性", { num: { ref: "bullets" }, after: 30 }),
      p("退出登录：handleLogout()清除localStorage中的token和userInfo → router.push('/login')", { num: { ref: "bullets" }, after: 80 }),

      new Paragraph({ children: [new PageBreak()] }),
      // ===== 6. API LIST =====
      heading(HeadingLevel.HEADING_1, "六、模块一完整API接口清单"),
      p("以下是我负责开发的全部8个API接口，所有接口都遵循RESTful规范，使用统一Result响应格式："),
      new Table({
        width: { size: 9638, type: WidthType.DXA },
        columnWidths: [1000, 2200, 2300, 4138],
        rows: [
          new TableRow({ children: [headerCell("方法", 1000), headerCell("路径", 2200), headerCell("鉴权", 2300), headerCell("功能与实现说明", 4138)] }),
          ...[
            ["POST", "/api/auth/register", "无需JWT", "用户注册。@Valid校验参数，BCrypt加密密码，用户名唯一性检查，默认角色STUDENT"],
            ["POST", "/api/auth/login", "无需JWT", "用户登录。用户名查询→status检查→BCrypt验证→JWT签发→返回token+用户信息"],
            ["GET", "/api/user/info", "需JWT", "获取当前用户信息。从Token解析userId→查数据库→密码字段置null后返回"],
            ["PUT", "/api/user/info", "需JWT", "更新个人信息。从拦截器注入的request属性获取userId，只允许修改realName/phone/email/gender/age"],
            ["PUT", "/api/user/password", "需JWT", "修改密码。先验证旧密码(encoder.matches)→BCrypt加密新密码→updateById保存"],
            ["GET", "/api/user/list", "需JWT", "管理员查看用户列表。支持分页(page/size)+角色筛选(role)，LambdaQueryWrapper条件查询，按create_time倒序"],
            ["PUT", "/api/user/status/{id}", "需JWT", "管理员启用/禁用用户。根据ID查找→设置status→保存。status=0时用户无法登录"],
            ["POST", "/api/file/upload", "需JWT", "头像上传(FileController)。接收multipart文件→保存到本地/uploads/→返回文件URL，业务类型参数businessType=AVATAR"],
          ].map(([method, path, auth, desc], i) => new TableRow({
            children: [
              cell(method, 1000, i % 2 === 1 ? { fill: "F5F7FA" } : {}, { run: { bold: true } }),
              cell(path, 2200, i % 2 === 1 ? { fill: "F5F7FA" } : {}, { run: { font: "Consolas", size: 17 } }),
              cell(auth, 2300, i % 2 === 1 ? { fill: "F5F7FA" } : {}),
              cell(desc, 4138, i % 2 === 1 ? { fill: "F5F7FA" } : {}, { run: { size: 17 } }),
            ]
          }))
        ]
      }),

      new Paragraph({ spacing: { before: 160 } }),
      // ===== 7. FLOW =====
      heading(HeadingLevel.HEADING_1, "七、核心业务流程详解"),
      heading(HeadingLevel.HEADING_2, "7.1 用户注册完整流程"),
      p("① 前端Register.vue：用户填写表单 → Element Plus表单校验(username必填/password≥6位/确认密码一致/手机号格式) → 通过后调用authApi.register(form)", { num: { ref: "numbers" }, after: 30 }),
      p("② 前端request.js：请求拦截器检查localStorage无token，不加Authorization头 → 发送POST /api/auth/register", { num: { ref: "numbers" }, after: 30 }),
      p("③ 后端WebConfig：/api/auth/register在JWT拦截器的excludePathPatterns中，直接放行到AuthController", { num: { ref: "numbers" }, after: 30 }),
      p("④ AuthController.register()：@Valid触发RegisterDTO校验(用户名非空/密码非空) → 调用userService.register(dto)", { num: { ref: "numbers" }, after: 30 }),
      p("⑤ UserServiceImpl.register()：用户名唯一性检查(baseMapper.selectCount) → encoder.encode(password)生成BCrypt密文 → 设置默认值(role=STUDENT, status=1) → baseMapper.insert(user)入库 → MyMetaObjectHandler自动填充create_time/update_time", { num: { ref: "numbers" }, after: 30 }),
      p("⑥ 返回 Result.success(\"注册成功\", null) → 前端收到code=200 → ElMessage.success → router.push('/login')跳转登录页", { num: { ref: "numbers" }, after: 80 }),

      heading(HeadingLevel.HEADING_2, "7.2 用户登录与JWT签发完整流程"),
      p("① Login.vue表单校验 → authApi.login(form) → POST /api/auth/login", { num: { ref: "numbers" }, after: 30 }),
      p("② UserServiceImpl.login()：LambdaQueryWrapper查询用户 → status!=0检查 → encoder.matches()密码验证 → jwtUtils.generateToken()签发Token", { num: { ref: "numbers" }, after: 30 }),
      p("③ JwtUtils.generateToken()内部：Jwts.builder().claims({userId,username,role}).subject(username).issuedAt(now).expiration(now+24h).signWith(HMAC-SHA256密钥).compact()", { num: { ref: "numbers" }, after: 30 }),
      p("④ 返回LoginResultDTO{token, userId, username, realName, role, avatar} → 前端localStorage.setItem存储token和userInfo → router.push('/')进入系统", { num: { ref: "numbers" }, after: 30 }),
      p("⑤ 后续所有请求：request.js请求拦截器自动从localStorage取token注入Authorization: Bearer xxx → JwtInterceptor拦截验证 → 解析userId注入request → Controller读取", { num: { ref: "numbers" }, after: 80 }),

      heading(HeadingLevel.HEADING_2, "7.3 Token过期处理流程"),
      p("① JwtInterceptor：parseToken()返回null(isTokenExpired) → 返回HTTP 401和JSON错误", { num: { ref: "numbers" }, after: 30 }),
      p("② 前端request.js响应拦截器：检测到res.code===401 → localStorage.clear()清除所有存储 → router.push('/login') → ElMessage提示'请先登录'", { num: { ref: "numbers" }, after: 80 }),

      new Paragraph({ children: [new PageBreak()] }),
      // ===== 8. DEFENSE KEY POINTS =====
      heading(HeadingLevel.HEADING_1, "八、答辩核心要点 —— 评委最爱问的问题"),
      heading(HeadingLevel.HEADING_2, "8.1 技术亮点（主动展示）"),
      p("BCrypt密码加密：使用Spring Security Crypto的BCryptPasswordEncoder，自动加盐、工作因子10、60位密文。相同密码每次加密结果不同，有效防止彩虹表攻击。（这是+5分的加分项）", { num: { ref: "numbers" }, after: 40 }),
      p("JWT无状态认证：使用jjwt 0.12.6库，HMAC-SHA256签名，24小时过期。自定义JwtInterceptor拦截器实现请求级身份验证，相比传统Session方案，无状态架构更适合前后端分离、便于水平扩展。（这是+10分的加分项）", { num: { ref: "numbers" }, after: 40 }),
      p("三层分层架构：Controller接收请求+参数校验 → Service处理业务逻辑 → Mapper数据持久化。每层职责清晰，代码耦合度低，符合企业级开发规范。（良好档位80-90分）", { num: { ref: "numbers" }, after: 40 }),
      p("MyBatis-Plus零SQL开发：继承BaseMapper后自动拥有CRUD方法，LambdaQueryWrapper实现类型安全的条件查询，分页插件自动拦截SQL。同时保留了@Select注解写自定义SQL的能力(如countByRole)。", { num: { ref: "numbers" }, after: 40 }),
      p("前后端双重权限控制：前端Vue Router的beforeEach路由守卫检查token和role → 后端JwtInterceptor验证Token并注入身份 → Controller层再次从request获取userId校验。三层防护确保安全性。", { num: { ref: "numbers" }, after: 40 }),
      p("全局异常处理：@RestControllerAdvice + @ExceptionHandler，统一处理业务异常(RuntimeException→400)、参数校验异常(MethodArgumentNotValidException→400)、未知异常(Exception→500)，避免异常信息泄露到前端。", { num: { ref: "numbers" }, after: 40 }),
      p("Token安全设计：密码错误时不区分[用户名不存在]和[密码错误]，统一返回[用户名或密码错误]，防止恶意用户通过错误信息枚举有效账号。", { num: { ref: "numbers" }, after: 80 }),

      heading(HeadingLevel.HEADING_2, "8.2 常见答辩追问与应对"),
      p("Q1：为什么用BCrypt而不是MD5/SHA256？", { run: { bold: true, size: 22 }, after: 30 }),
      p("A：MD5和SHA256是通用哈希算法，计算速度快（每秒数十亿次），容易被暴力破解和彩虹表攻击。BCrypt专为密码存储设计，内置随机盐值、可调节的计算成本(cost factor=10)、单向不可逆。每次加密结果不同，即使两个用户密码相同，存储的密文也不同。", { after: 80 }),

      p("Q2：JWT Token过期后用户怎么办？", { run: { bold: true, size: 22 }, after: 30 }),
      p("A：前端Axios响应拦截器检测到401状态码后，自动清除localStorage中的token和userInfo，并重定向到登录页。用户重新登录后获得新Token。24小时的过期时间是人性的设计——一天内无需重复登录，过期后自动提醒。", { after: 80 }),

      p("Q3：如果用户修改了localStorage中的role字段会不会提权？", { run: { bold: true, size: 22 }, after: 30 }),
      p("A：不会。前端的role仅用于UI展示（如动态菜单）。真正的权限验证在后端JWT Token中，Token由服务器用HMAC-SHA256签名，用户无法伪造或篡改。即使前端改了localStorage，JwtInterceptor解析Token时读取的是服务器签发的真实role。前端菜单看不到不代表API调不通，真正安全的是后端拦截器。", { after: 80 }),

      p("Q4：为什么用MyBatis-Plus而不是JPA？", { run: { bold: true, size: 22 }, after: 30 }),
      p("A：MyBatis-Plus在MyBatis基础上增强了CRUD自动化能力，同时保留了原生SQL的灵活性。相比JPA的自动建表和复杂的关联映射，MyBatis-Plus更轻量、性能更好、SQL可控性强。LambdaQueryWrapper解决了字符串硬编码的魔法值问题，代码重构时IDE可以自动修改字段引用。", { after: 80 }),

      p("Q5：@TableLogic逻辑删除有什么好处？", { run: { bold: true, size: 22 }, after: 30 }),
      p("A：逻辑删除不会真正从数据库删除数据，而是将deleted字段设为1。好处：①数据可恢复，防止误删；②保留数据用于审计和分析；③MyBatis-Plus自动在所有查询中追加WHERE deleted=0，开发者无需手动处理。", { after: 80 }),

      // ===== 9. TALKING SCRIPT =====
      heading(HeadingLevel.HEADING_1, "九、5分钟答辩口述稿"),
      p("各位老师好，我是小组成员1，负责的是系统的用户管理模块。下面我从数据库设计、后端实现、前端实现和安全机制四个方面进行汇报。", { after: 80 }),
      p("首先，数据库层面，我设计了user表，包含14个字段。其中username是唯一索引，password采用BCrypt加密存储，role支持STUDENT/COUNSELOR/ADMIN三种角色，status控制账号的启用和禁用，deleted字段配合MyBatis-Plus的@TableLogic实现逻辑删除。create_time和update_time通过自定义的MetaObjectHandler自动填充，不用手动维护。", { after: 80 }),
      p("后端实现上，我严格遵守Controller-Service-Mapper三层分层架构。Service层继承MyBatis-Plus的ServiceImpl，重写了login、register和getCurrentUser三个核心方法。login方法有6个步骤：Lambda条件查询、用户存在性检查、状态检查、BCrypt密码验证、JWT签发、结果封装。register方法会先检查用户名唯一性，然后用BCrypt加密密码，设置默认角色和状态后入库。", { after: 80 }),
      p("安全机制是我模块最大的亮点，实现了JWT无状态认证加BCrypt密码加密两个加分项。JwtUtils工具类基于jjwt 0.12库，使用HMAC-SHA256算法签名，Token中携带userId、username和role，有效期24小时。自定义的JwtInterceptor拦截所有/api请求，验证Token有效性后将用户信息注入request供Controller使用。同时前端Vue Router的beforeEach路由守卫实现了前端层面的权限控制，不同角色看到不同的菜单。", { after: 80 }),
      p("前端方面，我使用Vue 3 Composition API编写了Login、Register、Profile和AdminUsers四个页面。Axios封装了请求和响应两个拦截器，自动注入Token和处理401异常。所有表单都使用了Element Plus的校验规则，确认密码通过自定义validator函数比对，手机号用正则表达式验证。", { after: 80 }),
      p("以上是我负责的用户管理模块的汇报，请各位老师提问。谢谢。", { after: 120 }),

    ]
  }]
});

// ===== OUTPUT =====
Packer.toBuffer(doc).then(buf => {
  fs.writeFileSync("D:\\DevTools\\mental-health-system\\模块一_用户管理_答辩材料.docx", buf);
  console.log("✅ 答辩材料已生成");
});
