# reggie_take_out
---
title: 麦吉外卖笔记day01-03
tags: [麦吉项目]
index_img: https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030026538.jpg
date: 2022-8-3 0:53:00

---

# 瑞吉外卖开发笔记 一

### 软件开发流程

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727745.png)

### 角色分工

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727752.png)

### 软件环境

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727673.png)

## 2、瑞奇外卖项目介绍

### 项目介绍

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727798.png)

### 产品原型展示

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727757.png)

### 技术选型

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727766.png)

### 功能架构

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727353.png)

### 角色

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727275.png)

## 3、环境搭建

### 数据库环境搭建

- **创建数据库**
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727306.png)
- **执行SQL脚本**
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727298.png)
- **数据表**
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727336.png)

### maven项目搭建

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727314.png)

**解决IDEA加载maven工程缓慢**
VM Options:-DarchetypeCatalog=local

**添加依赖:**

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.5</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.ka</groupId>
    <artifactId>reggie</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <scope>compile</scope>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.2</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.20</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.76</version>
        </dependency>

        <dependency>
            <groupId>commons-lang</groupId>
            <artifactId>commons-lang</artifactId>
            <version>2.6</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.23</version>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.4.5</version>
            </plugin>
        </plugins>
    </build>
```

**application.yml**

```yaml
server:
  port: 8080
spring:
  application:
    #应用名称
    name: reggie_take_out
  datasource:
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/reggie?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true
      username: root
      password: 1234
mybatis-plus:
  configuration:
    #address_book->AddressBook
    #在映射实体或者属性时，将数据库中表名和字段名中的下划线去掉，按照驼峰命名法映射
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: ASSIGN_ID
```

**配置application启动类**

```less
@Slf4j // lombook提供的日志注解
@SpringBootApplication
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功！！！");
    }
}
```

把前端的静态资源放在resource目录
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727744.png)

设置静态资源映射

```scala
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始静态资源映射");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
}
```

启动项目，访问http://localhost:8080/backend/index.html
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727798.png)

## 4、后台登录功能开发

### 需求分析

- 登录页面展示(http://localhost:8080/backend/page/login/login.html)
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727104.png)
- 查看登录请求信息
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727913.png)
- 数据模型（employee表）
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727890.png)

### 代码开发

- 创建实体类Employee，和employee表进行映射

```typescript
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;//身份证号码

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
```

- 创建Controller，Service，Mapper

EmployeeMapper

```java
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
```

EmployeeService

```csharp
//MP提供了一个Service接口和实现类，分别是:IService和ServiceImpl,后者是对前者的一个具体实现。
//        MP已经帮我们把业务层的一些基础的增删改查都已经实现了，可以直接进行使用
public interface EmployeeService extends IService<Employee> {
}
```

EmployeeServiceImpl

```scala
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
```

EmployeeController

```less
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeServiceImpl employeeService;
}
```

- 导入返回结果类R
  - 此类是一个通用结果类，服务端响应的所有结果最终都会包装成此种类型返回给前端页面

```typescript
@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
```

- 在Controller中创建登录方法
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727926.png)
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727198.png)

```kotlin
@Slf4j
// 老师使用了RestController = Controller + ResponseBody
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeServiceImpl employeeService;

    @PostMapping("/login")
    //使用@RequestBody注解将外部传递的json数组数据映射到形参的employee对象中作为数据
    // 使用HttpServletRequest类的request对象，可以将获取到的employee中的id，存到session
    public R<Employee> logib(HttpServletRequest request, @RequestBody Employee employee) {


        // 1. 将页面提交的密码password进行m5加密处理
        String password = employee.getPassword();
        // getBytes()，可以将字符串类型的数据，转换为字节数组(相当于输入流，被内存读取）
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名来查数据库
        // 这个相当于是一个sql语句的条件对象，
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // eq()： 相当于 =,对应的sql语句为：
//        例： lqw.eq(User::getName, "Jerry").eq(User::getPassword, "jerry");
        // SELECT id,name,password,age,tel FROM user WHERE (name = ? AND password = ?)
        // queryWrapper的中文意思是查询包装器，这里相当于获取查询条件，将获取的username作为条件，放入查询包装器对象中
        // 第一个参数相当于是获取Employee对应的Username字段，第二个值是请求体中的username的值
        // 创建了一个条件 username = 请求体的username，然后根据这个条件查询
        queryWrapper.eq(Employee::getUsername, employee.getUsername());// 这里相当于获取实体类的username
        // 这里根据查询包装器的条件 来得到对象
        // 这里相当于用业务层调用数据层的方法，查询条件的sql是queryWrapper，
        Employee emp = employeeService.getOne(queryWrapper);// 使用业务层：根据实体类的username属性值，来获取Employee对象

        //3、如果没有查询到则返回失败结果
        if (emp == null) {
            return R.error("登录失败");
        }

        //4、比对密码，如果不一致则返回失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        //5、查看员工状态，如果已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        //6、登录成功，将用户id存入Session（使用setAttribute）并返回成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);// 这个参数是传入R对象，返回给前端
    }
}
```

## 5、后台退出功能开发

**需求分析**
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208021727207.png)

**代码实现**

在Controller中创建退出方法

```typescript
    //员工退出
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前员工登录的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
```





# 瑞吉外卖开发笔记 二

笔记内容为**黑马程序员**视频内容

## 1、完善登录功能

### 问题分析

前面我们已经完成了后台系统的员工登录功能开发，但是还存在一个问题:用户如果不登录，直接访问系统首页面，照样可以正常访问。

这种设计并不合理，我们希望看到的效果应该是，**只有登录成功后才可以访问系统中的页面**，如果没有登录则跳转到登录页面。

那么，具体应该怎么实现呢?

答案就是使用**过滤器或者拦截器**，在过滤器或者拦截器中**判断用户是否已经完成登录**，如果没有登录则跳转到登录页面

### 代码实现

实现步骤:

- **创建自定义过滤器LoginCheckFilter**

```java
										 // patterns是模式的意思
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 将类型转换成HttpServletRequest，得到请求的URL
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 将类型转换成HttpServletResponse，得到响应的数据然后放行
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 这个{} 是一个占位符，可以将逗号, 后面的值填充到占位符中去
        log.info("拦截到请求：{}",request.getRequestURI());
        // 放行请求的URL
        filterChain.doFilter(request,response);

    }
}
```

- **在启动类上加入注解@ServletComponentScan**

```less
@Slf4j
@SpringBootApplication
// 在启动类中配置扫描Servlet组件的注解
@ServletComponentScan // 扫描WebFilter注解，创建过滤器
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功！！！");
    }
}
```

- **完善过滤器的处理逻辑**

过滤器具体的处理逻辑如下:

1、获取本次请求的URI

2、判断本次请求是否需要处理

3、如果不需要处理，则直接放行

4、判断登录状态，如果已登录，则直接放行

5、如果未登录则返回未登录结果
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316071.png)

```typescript
/**
 *  检查用户是否已经完成登录
 */
@Slf4j                                     // patterns是模式的意思
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*") // 使用servlet过滤器
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();// 创建路径匹配器对象

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 将类型转换成HttpServletRequest，得到请求的URI
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 将类型转换成HttpServletResponse，得到响应的数据然后放行
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取本次请求的URI, URI可以理解为不是完整的URL，得到关键调用的url接口地址
        // URL：http://localhost:8081/test-context/laker/userInfo
        // URI：/test-context/laker/userInfo
        java.lang.String requestURI = request.getRequestURI(); // /backend/index.html

        log.info("拦截到请求：{}",requestURI);

        // 创建一个数组，用来存储放行的URI，相当于一个白名单
        java.lang.String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                // 将静态资源放行
                "/backend/**",
                // 这个是移动端的页面也放行
                "front/**"
                // 点击登录按钮会异步请求/employee/page，这个URI，不在白名单，所以拦截
        };

        // 2. 判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        // 3. 如果不需要处理，则直接放行(就是匹配上了，在白名单里面）
        if (check){
            log.info("本次请求{}不需要处理",requestURI);
            // filterChain放行的对象
            filterChain.doFilter(request,response);
            return; // 结束当前方法
        }

        // 4. 判断登录状态，如果已登录，则直接放行
        // 如果会话中有参数则 放行
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户已经登录,用户id为：{}",request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        // 5. 如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        // 输出流可以理解为：写数据（向外输出到硬盘），输入流可以理解为：读数据（向内读取到内存）
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));// 写的是一个JSON数据
        return;


    }

    /**
     *  路径匹配，检查本次请求是否需要放行
     * @param urls 白名单（需要放行的URI）
     * @param requestURI (请求的URI）
     * @return
     */
    public boolean check(String[] urls,String requestURI){

        // 使用foreach遍历String数组中的URI，和/backend/index.html这个URI进行匹配，
        for (String url : urls) {
            // 使用路径匹配器对象，匹配路径
            boolean match = PATH_MATCHER.match(url, requestURI);
            // 如果为true就返回true
            if (match){
                return true;
            }
        }
        // 整个foreach遍历完后还没有匹配上，就返回false
        return false;

    }



}

```

## 2、新增员工

### 需求分析

后台系统中可以管理员工信息，通过新增员工来添加后台系统用户。点击 **添加员工** 按钮跳转到新增页面，如下:
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316054.png)

### 数据模型

新增员工，其实就是将我们新增页面录入的员工数据插入到employee表。需要注意，employee表中对username字段加入了唯一约束，因为username是员工的登录账号，必须是**唯一**的

### 代码开发

在开发代码之前，需要梳理一下整个程序的执行过程:

- 页面发送ajax请求，将新增员工页面中输入的数据以json的形式提交到服务端
- 服务端Controller接收页面提交的数据并调用Service将数据进行保存
- Service调用Mapper操作数据库，保存数据
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316113.png)

```kotlin
    //新增员工
 @PostMapping// 这个路径不用写，因为在类上面写了 “/employee”
    // 由于前端传递过来的数据是json格式到Controller表现层，然后要获取这个参数要使用@RequestBody注解
//    由于只要返回一个成功的信息给前端，所以R泛型为String
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        // 使用实体类的toString方法，打印对象内容
        log.info("新增员工，员工信息：{}",employee.toString());

        // 设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes())); // 转成字节类型的值，可以给计算机读

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 获取当前登录用户的id，从session中获取，所以方法用要加形参HttpServletRequest
        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee); // 这个save方法继承了Iservice接口（使得业务层快速开发)，
                                        // 这个save方法会调用其数据层的方法，添加到数据库中（这一套都是mp提供的）

        return R.success("新增员工成功");
    }
```

前面的程序还存在一个问题，就是当我们在新增员工时输入的账号已经存在，由于employee表中对该字段加入了唯一约束，此时程序会抛出异常:
`java.sql.SQLIntegrityConstraintViolationException: Duplicate entry 'heniang' for key 'idx_username'`

此时需要我们的程序进行异常捕获，通常有两种处理方式:

1、在Controller方法中加入try.catch进行异常捕获

2、使用异常处理器进行全局异常捕获

```typescript
/**
 * 全局异常处理
 * annotations 是注释的意思
 */
                    // 只要类上面加了这两个注解，就会被这个类处理
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody // 将响应的数据以json数据返回  注解作用：设置当前控制器方法响应内容为当前返回值（将返回值转换为json），无需解析
@Slf4j
public class GlobalExceptionHandler {

    /**
     *  异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) // 拦截异常，拦截类型为sql异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //  日志输出异常打印信息
      log.info(ex.getMessage());

      if (ex.getMessage().contains("Duplicate entry")){
          // 如果异常信息包含了 Duplicate entry这个字符串信息，就以空格 来分割异常信息，
          String[] split = ex.getMessage().split(" ");
          // Duplicate entry 'zhangsan' for key 'employee.idx_username'
          // 异常信息中的索引为2的值，就是username账号，提示账号已经存在
          String mes = split[2] + "已存在";
          return R.error(mes);
      }
        
        return R.error("未知错误");
    }

}


```

**总结**

1、根据产品原型明确业务需求

2、重点分析数据的流转过程和数据格式

3、通过debug断点调试跟踪程序执行过程

## 3、员工信息分页查询

### 需求分析

系统中的员工很多的时候，如果在一个页面中全部展示出来会显得比较乱，不便于查看，所以一般的系统中都会以分页的方式来展示列表数据。

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316094.png)

### 代码开发

在开发代码之前，需要梳理一下整个程序的执行过程:

- 页面发送ajax请求，将分页查询参数(page.pageSize、name)提交到服务端
- 服务端Controller接收页面提交的数据并调用Service查询数据
- Service调用Mapper操作数据库，查询分页数据
- Controller将查询到的分页数据响应给页面
- 页面接收到分页数据并通过ElementUI的Table组件展示到页面上

配置MP分页插件

```java
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return  mybatisPlusInterceptor;
    }
}
```

员工信息分页查询

```java
/**
     *  员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){ // 这里返回对应的是R的data属性对象，因为data的类型是object
        // 这三个值是和占位符一一对应的
        log.info("page = {},pageSize = {}, name = {}",page,pageSize,name);

        // 构造分页查询器 // 第一个参数是：当前页码，第二个参数是每页显示的记录数
        Page pageInfo = new Page(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 添加过滤条件 这里.eq对应sql where 。。。= 。。。 等值查询
        // .like 相当于sql： where 。。。 like 。。。 模糊查询
        // 创建了一个条件 这个StringUtils.isNotEmpty(name)可以理解为不为null，就会执行like name = ？
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);// 如果请求的URL中包含name（模糊查询）
        // 添加排序条件
        // 这个条件是倒序排序，按照数据库中的UpdateTime字段倒叙
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 执行查询信息 用业务层调用数据层
        employeeService.page(pageInfo,queryWrapper);
        // 将page对象返回 -- 可以理解为将页面返回给前端
        return R.success(pageInfo);
    }
```

### 功能测试

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316066.png)

## 4、启用/禁用员工账号

### 需求分析

在员工管理列表页面，可以对某个员工账号进行启用或者禁用操作。账号禁用的员工不能登录系统，启用后的员工可以正常登录。

需要注意，只有管理员（admin用户）可以对其他普通用户进行启用、禁用操作，所以普通用户登录系统后启用、禁用按钮不显示。

### 代码开发

页面中是怎么做到只有管理员admin能够看到启用、禁用按钮的？

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316106.png)

在开发代码之前，需要梳理一下整个程序的执行过程:

1、页面发送ajax请求，将参数(id、 status)提交到服务端

2、服务端Controller接收页面提交的数据并调用Service更新数据

3、Service调用Mapper操作数据库

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316027.png)

页面中的ajax请求是如何发送的？
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316066.png)

根据id修改员工信息

```kotlin
@PutMapping
public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
    log.info(employee.toString());

    Long empId = (Long) request.getSession().getAttribute("employee");
    employee.setUpdateTime(LocalDateTime.now());
    employee.setUpdateUser(empId);
    employeeService.updateById(employee);

    return R.success("员工信息修改成功");
}
```

测试过程中没有报错，但是功能并没有实现，查看数据库中的数据也没有变化。观察控制台输出的SQL:
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316998.png)

SQL执行的结果是更新的数据行数为0，仔细观察id的值，和数据库中对应记录的id值并不相同
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316104.png)

### 代码修复

通过观察控制台输出的SQL发现页面传递过来的员工id的值和数据库中的id值不一致，这是怎么回事呢?

分页查询时服务端响应给页面的数据中id的值为19位数字，类型为long

页面中js处理long型数字只能精确到**前16位**，所以最终通过ajax请求提交给服务端的时候id就改变了

前面我们已经发现了问题的原因，即js对long型数据进行处理时**丢失精度**，导致提交的id和数据库中的id不一致。

如何解决这个问题?

我们可以在**服务端**给页面**响应json数据时进行处理**，**将long型数据统一转为String字符串**。

**具体实现步骤:**

**1)** 提供对象转换器JacksonobjectMapper，基于Jackson进行Java对象到json数据的转换（资料中已经提供，直接复制到项目中使用)

```scala
/**
 * 对象映射器:基于jackson将Java对象转为json，或者将json转为Java对象
 * 将JSON解析为Java对象的过程称为 [从JSON反序列化Java对象]
 * 从Java对象生成JSON的过程称为 [序列化Java对象到JSON]
 */
public class JacksonObjectMapper extends ObjectMapper {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public JacksonObjectMapper() {
        super();
        //收到未知属性时不报异常
        this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        //反序列化时，属性不存在的兼容处理
        this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


        SimpleModule simpleModule = new SimpleModule()
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))

                .addSerializer(BigInteger.class, ToStringSerializer.instance)
                // 将Long型转换成字符串
                .addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        //注册功能模块 例如，可以添加自定义序列化器和反序列化器
        this.registerModule(simpleModule);
    }
}

```

**2)** 在WebMvcConfig配置类中扩展Spring mvc的消息转换器，在此消息转换器中使用提供的对象转换器进行Java对象到json数据的转换

```java
    /**
     *  扩展MVC框架的消息转换器-- 重写 WebMvcConfigurationSupport的extendMessageConverters方法
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器。。。。");
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层使用了Jackson将java对象（这里是Long型的数据）转换为json（字符串）
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的消息转换器对象追加到mvc框架的转换器集合中
        // 执行完这条语句，消息转换器的对象converters的大小size会变成9，原来是8，代表扩展进去了
        converters.add(0,messageConverter);
    }
```

## 5、编辑员工信息

### 需求分析

在员工管理列表页面点击编辑按钮，跳转到编辑页面，在编辑页面回显员工信息并进行修改，最后点击保存按钮完成编辑操作

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208022316076.png)

### 代码开发

在开发代码之前需要梳理一下操作过程和对应的程序的执行流程:

1、点击编辑按钮时，页面跳转到add.html，并在url中携带参数[员工id]

2、在add.html页面获取url中的参数[员工id]

3、发送ajax请求，请求服务端，同时提交员工id参数

4、服务端接收请求，根据员工id查询员工信息，将员工信息以json形式响应给页面

```kotlin
    /**
     *  根据id查询员工信息
     * @param id
     * @return
     */

    @GetMapping("/{id}") // 这是接收前端的请求路径（少了id属性路径）
	// http://localhost:8080/employee/1554938060526084098的Id
     // @PathVariable作用：绑定路径参数与处理器方法形参间的关系，要求路径参数名与形参名一一对应（例如接收id）
    // @RequestBody用于接收json数据【application/json】
    // @RequestParam用于接收url地址传参，表单传参【application/x-www-form-urlencoded】
    public R<Employee> getById(@PathVariable Long id){ // 接收请求体中的基础数据类型的值id

        log.info("根据id查询员工 信息。。。");
        Employee employee = employeeService.getById(id);

        if (employee != null){
            return R.success(employee); // 将查询到的对象，返回给前端
        }
        return R.error("没有查询到对应的员工信息");
    }
```

5、页面接收服务端响应的json数据，通过VUE的数据绑定进行员工信息回显

6、点击保存按钮，发送ajax请求，将页面中的员工信息以json方式提交给服务端

7、服务端接收员工信息，并进行处理，完成后给页面响应

8、页面接收到服务端响应信息后进行相应处理

**注意**:add.html页面为公共页面，**新增员工和编辑员工都是在此页面操作**，所以该代码部分与之前添加员工代码对应，不需要重写。



# 瑞吉外卖开发笔记 三

笔记内容为**黑马程序员**视频内容

## 分类管理业务开发

### 公共字段自动填充

#### 问题分析

前面我们已经完成了后台系统的员工管理功能开发，在新增员工时需要设置创建时间、创建人、修改时间、修改人等字段，在编辑员工时需要设置修改时间和修改人等字段。这些字段属于公共字段，也就是很多表中都有这些字段，如下:
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030021864.png)
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030021906.png)

能不能对于这些公共字段在某个地方统一处理，来简化开发呢?答案就是使用Mybatis Plus提供的**公共字段自动填充**功能。

#### 代码实现

Mybatis Plus公共字段自动填充，也就是在插入或者更新的时候为指定字段赋予指定的值，使用它的好处就是可以统一对这些字段进行处理，避免了重复代码。

实现步骤:

1、在实体类的属性上加入@TableField注解，指定自动填充的策略，

```kotlin
// fill是填充，充满的意思 FieldFill是字段填充
@TableField(fill = FieldFill.INSERT)//插入时填充字段
private LocalDateTime createTime; // 在公共字段上面使用字段填充

@TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
private LocalDateTime updateTime;

@TableField(fill = FieldFill.INSERT)//插入时填充字段
private Long createUser;

@TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
private Long updateUser;
```

2、按照框架要求编写元数据对象处理器，在此类中统一为公共字段赋值，此类需要实现MetaObjectHandler接口

```typescript
/**
 *  自定义元数据对象处理器
 */

@Component // 被Spring容器管理
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {// MetaObjectHandler元对象处理程序的意思

    /**
     *  插入操作，自动填充
     * @param metaObject
     */
    // 重写填充公共字段填充的方法（插入时填充）
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】。。。");
        log.info(metaObject.toString()); // 使用日志打印该对象
        // 第一个参数是字段名-- 对应的实体类的属性名， 第二个参数是，值
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        // 暂时写死，这个数据库中的id类型是bigint，对应的java类型是Long
        metaObject.setValue("createUser",new Long(1));
        metaObject.setValue("updateUser",new Long(1));

    }

    /**
     *  更新操作，自动填充
     * @param metaObject
     */

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】。。。");
        log.info(metaObject.toString());
//      修改时填充：只要填修改时间和修改人就行
        // 第一个参数是字段名-- 对应的实体类的属性名， 第二个参数是，值
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",new Long(1));
    }
}
```

#### 功能完善

前面我们已经完成了公共字段自动填充功能的代码开发，但是还有一个问题没有解决，就是我们在自动填充createUser和updateUser时设置的用户id是固定值，现在我们需要改造成动态获取当前登录用户的id。

有的同学可能想到，用户登录成功后我们将用户id存入了HttpSession中，现在我从HttpSession中获取不就行了?

**注意**，我们在MyMetaObjectHandler类中是不能获得HttpSession对象的，所以我们需要通过其他方式来获取登录用户id。

可以使用**ThreadLocal**来解决此问题,它是JDK中提供的一个类。

在学习ThreadLocal之前，我们需要先确认一个事情，**就是客户端发送的每次http请求，对应的在服务端都会分配一个新的线程来处理**，在处理过程中涉及到下面类中的方法**都属于相同的一个线程**:

1、LoginCheckFilter的doFilter方法

2、EmployeeContraller的update方法

3、MyMetaObjectHandler的updateFill方法

可以在上面的三个方法中分别加入下面代码（获取当前线程id):

```objectivec
long id = Thread.currentThread().getId() ;
log.info("线程id:{}" ,id);
```

执行编辑员工功能进行验证，通过观察控制台输出可以发现，一次请求对应的线程id是相同的:

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030021894.png)



**注意**： ==前三个是同一个线程（因为是同一个http请求），但是第四个是第二次http请求（更新完数据后前端会再发一次查询页面的请求，所以会有两个线程），所以线程号不一样==

![image-20220805023919411](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208050239566.png)



**什么是ThreadLocal?**

ThreadLocal并不是一个Thread，而是Thread的局部变量。当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
ThreadLocal为每个线程提供单独一份存储空间，具有线程隔离的效果，只有在线程内才能获取到对应的值，线程外则不能访问。

ThreadLocal常用方法：

- public void set(T value) 设置当前线程局部变量的值
- public T get() 返回当前线程所对应的线程局部变量的值

我们可以在LoginCheckFilter的doFilter方法中获取当前登录用户id，并调用ThreadLocal的set方法来设置当前线程的线程局部变量的值（用户id)，然后在MyMetaObjectHandler的updateFill方法中调用ThreadLocal的get方法来获得当前线程所对应的线程局部变量的值(用户id)。

**实现步骤:**

1、编写BaseContext工具类，基于ThreadLocal封装的工具类

```csharp
/**
 *   基于ThreadLocal封装工具类，用户保存和获取当前登录用户id（不会混淆，也不会覆盖，这个ThreadLcal只对当前线程有效
 */
public class BaseContext {
    // 这里用静态的static，因为是一个工具类，容易不用创建对象，用类名调用即可
    // 这个线程id值是Long，所以泛型是Long
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) { // 这个方法是在获取到session中调用，将id传给这个方法
        threadLocal.set(id); // 将session中的id存入到线程中
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
```

2、在LogincheckFilter的doFilter方法中调用BaseContext来设置当前登录用户的id

```kotlin
        // 4. 判断登录状态，如果已登录，则直接放行
        // 如果会话中有参数则 放行
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户已经登录,用户id为：{}",request.getSession().getAttribute("employee"));

//            // 这个项目刚开始会经过这个类，获取当前线程的id
//            long id = Thread.currentThread().getId();
//            log.info("线程id为：{}",id);

            // 获取当前session，那session中的employee属性id值
            Long empId = (Long) request.getSession().getAttribute("employee");
            // 将id值存入到ThreadLocal的工具类BaseContext中
            BaseContext.setCurrentId(empId);


            filterChain.doFilter(request,response);
            return;
        }
```

3、在MyMeta0bjectHandler的方法中调用BaseContext获取登录用户的id

```typescript
/**
 *  自定义元数据对象处理器
 */

@Component // 被Spring容器管理
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {// MetaObjectHandler元对象处理程序的意思

    /**
     *  插入操作，自动填充
     * @param metaObject
     */
    // 重写填充公共字段填充的方法（插入时填充）
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】。。。");
        log.info(metaObject.toString()); // 使用日志打印该对象
        // 第一个参数是字段名-- 对应的实体类的属性名， 第二个参数是，值
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        // 暂时写死，这个数据库中的id类型是bigint，对应的java类型是Long
//        metaObject.setValue("createUser",new Long(1));
//        metaObject.setValue("updateUser",new Long(1));
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());

    }

    /**
     *  更新操作，自动填充
     * @param metaObject
     */

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】。。。");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);


//      修改时填充：只要填修改时间和修改人就行
        // 第一个参数是字段名-- 对应的实体类的属性名， 第二个参数是，值
        metaObject.setValue("updateTime",LocalDateTime.now());
        // 本来写死了，现在替换用ThreadLocal的工具类BaseContext
//        metaObject.setValue("updateUser",new Long(1));
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}

```

### 新增分类

#### 需求分析

后台系统中可以管理分类信息，分类包括两种类型，分别是菜品分类和套餐分类。当我们在后台系统中添加菜品时需要选择一个菜品分类，当我们在后台系统中添加一个套餐时需要选择一个套餐分类，在移动端也会按照菜品分类和套餐分类来展示对应的菜品和套餐。

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030021853.png)
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030021858.png)

#### 数据模型

新增分类，其实就是将我们新增窗口录入的分类数据插入到category表，表结构如下:

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030021881.png)

#### 代码开发

在开发业务功能前，先将需要用到的类和接口基本结构创建好:

- 实体类Category(直接从课程资料中导入即可)
- 每个实体类对应数据库中每个表，这个对应的是分类表，上一个实体类对应的是员工表

```java
/**
 * 分类，对应的是category菜品分类表的字段
 */
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //类型 1 菜品分类 2 套餐分类
    private Integer type;


    //分类名称
    private String name;


    //顺序
    private Integer sort;


    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    //创建人
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    //修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
    private Integer isDeleted;

}
```

- Mapper接口CategoryMapper，相对应的每个数据表的实体类对应一个接口（数据层的Mapper接口用来操作对应表）

```java
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
```

- 业务层接口CategoryService（对应表的业务层）

```csharp
public interface CategoryService extends IService<Category> {
}
```

- 业务层实现类CategoryServicelmpl

```scala
@Service
public class CategoryServicelmpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
```

- 控制层CategoryController（对应表的Controller）

```less
/**
 *  分类管理
 */
@RestController
@RequestMapping("/category") // 前端点击分类管理的按钮，会执行对应请求，后端接收这个url请求
@Slf4j
public class CategoryController {
    
    
    @Autowired
    private CategoryService categoryService; // 注入业务层接口对象，可以从表现层调用业务层
    
}

```

在开发代码之前，需要梳理一下整个程序的执行过程:

1、页面(backend/page/category/list.html)发送ajax请求，将新增分类窗口输入的数据以json形式提交到服务端

2、服务端Controller接收页面提交的数据并调用Service将数据进行保存

3、Service调用Mapper操作数据库，保存数据

可以看到新增菜品分类和新增套餐分类请求的服务端地址和提交的json数据结构相同，所以服务端只需要提供一个方法统一处理即可
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030022293.png)
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030022307.png)
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030022300.png)

```less
    /**
     *  新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category: {}",category);// 打印这个从前端接收过来的对象
        // 调用业务层，添加菜品分类（将从前端接收过来的对象传给业务层
        categoryService.save(category);

        return R.success("新增分类成功");
    }
```

### 分类信息分页查询

#### 需求分析

系统中的分类很多的时候，如果在一个页面中全部展示出来会显得比较乱，不便于查看，所以一般的系统中都会以分页的方式来展示列表数据。

#### 代码开发

在开发代码之前，需要梳理一下整个程序的执行过程:

1、点击页面分类管理按钮后：页面发送ajax请求，将分页查询参数(page.pageSize)提交到服务端

2、服务端Controller接收页面提交的数据并调用Service查询数据

3、Service调用Mapper操作数据库，查询分页数据

4、Controller将查询到的分页数据响应给页面

5、页面接收到分页数据并通过ElementUI的Table组件展示到页面上
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030022490.png)

```java
    /**
     *  分页查询
     * @param page 起始页码
     * @param pageSize 每页显示多少条记录
     * @return
     */

    @GetMapping("/page")
    // 返回R对象的Page类型对象的数据，所以泛型写Page
    public R<Page> page(int page,int pageSize){ // 请求体中的参数不是Json格式的，所以不用加@RequestBody注解
        // 创建分页构造器对象 这个分页构造器对象存储的是Category实体类对象，所以泛型也实体类
        Page<Category> pageInfo = new Page<>(page, pageSize);

        // 条件构造器, 为了给菜品排序
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件，根据sort进行升序排序
        // queryWrapper条件构造器相当于是生成一个sql语句，queryWrapper. 后面接的是一个方法（这里是排序方法），
        // 然后参数写实体类对应的方法 -- 根据什么来升序排序，这里根据实体类中的Sort属性
        queryWrapper.orderByAsc(Category::getSort); // 这里相当于生成了一个条件sql

        // 进行分页查询
        categoryService.page(pageInfo,queryWrapper); // 执行业务层分页查询的方法，页面条数信息和查询条件
        // 将分页的对象返回给R，然后返回给前端
        return R.success(pageInfo);

    }

```

**注意**：要把Category中的`private Integer isDeleted;`注释掉才能查询到数据

### 删除分类

#### 需求分析

在分类管理列表页面，可以对某个分类进行删除操作。需要注意的是当分类关联了菜品或者套餐时，此分类不允许删除。

#### 代码开发

在开发代码之前，需要梳理一下整个程序的执行过程:

1、页面发送ajax请求，将参数(id)提交到服务端
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208030022326.png)

2、服务端Controller接收页面提交的数据并调用Service删除数据

3、Service调用Mapper操作数据库

```typescript
    /**
     *  根据Id删除分类
     * @param ids,url中的参数ids
     * @return
     */

    @DeleteMapping// 不是restful风格（/users/{id}），所以不用加@PathVariable注解
    public R<String> delete(Long ids){ // 这里不是restful风格，因为是（调用了category方法后）URL携带的 ？后面接的参数
                    // http://localhost:8080/category?ids=1397844263642378242
        log.info("删除分类，id为：{}",ids);
        // 调用业务层接口，执行数据层，操作数据库
        categoryService.removeById(ids);
        return R.success("删除成功");
    }


```

#### 代码完善

前面我们已经实现了根据id删除分类的功能，但是并没有检查删除的分类是否关联了菜品或者套餐，所以我们需要进行功能完善。

要完善分类删除功能，需要先准备基础的类和接口:

1、实体类Dish和Setmeal （从课程资料中复制即可) -- ==创建完实体类后，就是创建数据层接口，在到业务层==

```kotlin
@Data
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //菜品名称
    private String name;


    //菜品分类id
    private Long categoryId;


    //菜品价格
    private BigDecimal price;


    //商品码
    private String code;


    //图片
    private String image;


    //描述信息
    private String description;


    //0 停售 1 起售
    private Integer status;


    //顺序
    private Integer sort;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
    private Integer isDeleted;

}
@Data
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //分类id
    private Long categoryId;


    //套餐名称
    private String name;


    //套餐价格
    private BigDecimal price;


    //状态 0:停用 1:启用
    private Integer status;


    //编码
    private String code;


    //描述信息
    private String description;


    //图片
    private String image;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
    private Integer isDeleted;
}
```

2、Mapper接口DishMapper和SetmealMapper -- ==这里的Setmeal可以理解为套餐的意思==

```java
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
}
```

3、Service接口DishService和SetmealService

```csharp
public interface DishService extends IService<Dish> {
}
public interface SetmealService extends IService<Setmeal> {
}
```

4、Service实现类DishServicelmpl和SetmealServicelmpl  -- 如果创建业务层接口对象要用日志功能，可以在实现类中添加@Slf4j

```scala
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService{
}
```

#### 关键代码

- 在CategoryService添加remove方法

```csharp
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
```

- 在CategoryServicelmpl实现remove方法

```scala
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService; // 注入菜品dish业务层接口对象

    @Autowired
    private SetmealService setmealService; // 创建Setmeal套餐的业务层接口对象

    /**
     *  根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
//        创建查询菜品实体类dish类条件的对象
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper= new LambdaQueryWrapper<>();
        // 添加查询条件，根据分类id进行查询 这里是等值查询所以用eq ：相当于sql：where ？ = ？
        // 在数据库相当于执行的语句是：select count(*) from dish where category_id = #{id}; -- 后面这个id是传入的参数id
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        // 符合条件查询的菜品数（行数），调用菜品dish的业务层接口的count方法，参数为queryWrapper条件
        int count1 = dishService.count(dishLambdaQueryWrapper);

        // 查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if (count1 > 0){
            // 已经关联了菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        // 查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        // 根据条件，查询符合套餐数（行数）
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            // 已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");

        }

        // 正常删除分类
        super.removeById(id); // super是父类方法
    }
}

```

定义异常类CustomException

```java
/**
 *  自定义业务异常类
 */
public class CustomException extends RuntimeException{ // 继承运行时异常类，要使用他的方法

    public CustomException(String message){ // 定义有参构造器，当创建对象时：给参数，就会调用父类构造器传进去
        super(message); // 父类构造器
    }

}
```

在全局异常处理器**GlobalExceptionHandler**添加

```typescript
    /**
     *  异常处理方法 -- 处理 自定义的CustomException
     * @return
     */
    @ExceptionHandler(CustomException.class) // 拦截异常，拦截类型为sql异常
    public R<String> exceptionHandler(CustomException ex){
        //  日志输出异常打印信息
        log.error(ex.getMessage()); // 这里是调用父类的方法


        return R.error(ex.getMessage());
    }

```

### 修改分类

#### 需求分析

在分类管理列表页面点击修改按钮，弹出修改窗口，在修改窗口回显分类信息并进行修改，最后点击确定按钮完成修改操作

#### 代码实现

```less
    /**
     *  根据id修改分类信息
     * @param category // 将传过来的json数据，封装成category对象
     * @return
     */

    @PutMapping // 请求的路径是http://localhost:8080/category

                                // 这里是将参数封装成category对象
    public R<String> update(@RequestBody Category category){ // 由于请求体中是json格式，所以用@RequestBody注解接收

        log.info("修改分类信息：{}", category);

        categoryService.updateById(category); // 调用业务层的方法，修改前端传过来的json参数

        return R.success("修改分类信息成功");
```





# 瑞吉外卖开发笔记 四

笔记内容为**黑马程序员**视频内容

## 菜品管理业务开发

### 文件上传下载

#### 文件上传介绍

文件上传，也称为upload，是指将本地图片、视频、音频等文件上传到服务器上，可以供其他用户浏览或下载的过程。文件上传在项目中应用非常广泛，我们经常发微博、发微信朋友圈都用到了文件上传功能。

文件上传时，对页面的form表单有如下要求:

- method="post" 　　　　　　　　　　　采用post方式提交数据
- enctype="multipart/form-data" 　　　　采用multipart格式上传文件
- type="file"　　　　　　　　　　　　 　使用input的file控件上传
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062318093.png)

目前一些前端组件库也提供了相应的上传组件，但是底层原理还是基于form表单的文件上传。例如ElementUI中提供的upload上传组件:
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062318046.png)

服务端要接收客户端页面上传的文件，通常都会使用Apache的两个组件:

- commons-fileupload
- commons-io

Spring框架在spring-web包中对文件上传进行了封装，大大简化了服务端代码，我们只需要在Controller的方法中声明一个MultipartFile类型的参数即可接收上传的文件。

#### 文件下载介绍

文件下载，也称为download，是指将文件从服务器传输到本地计算机的过程。
通过浏览器进行文件下载，通常有两种表现形式:

- 以附件形式下载，弹出保存对话框，将文件保存到指定磁盘目录
- 直接在浏览器中打开

通过浏览器进行文件下载，本质上就是服务端将文件以流的形式写回浏览器的过程。

#### 文件上传代码实现

文件上传，页面端可以使用ElementuI提供的上传组件。
可以直接使用资料中提供的上传页面，位置:资料/文件上传下载页面/upload.html

```xml
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>文件上传</title>
  <!-- 引入样式 -->
  <link rel="stylesheet" href="../../plugins/element-ui/index.css" />
  <link rel="stylesheet" href="../../styles/common.css" />
  <link rel="stylesheet" href="../../styles/page.css" />
    <link rel="shortcut icon" href="../../favicon.ico">
</head>
<body>
   <div class="addBrand-container" id="food-add-app">
    <div class="container">
        <el-upload class="avatar-uploader"
                action="/common/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeUpload"
                ref="upload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar"></img>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
    </div>
  </div>
    <!-- 开发环境版本，包含了有帮助的命令行警告 -->
    <script src="../../plugins/vue/vue.js"></script>
    <!-- 引入组件库 -->
    <script src="../../plugins/element-ui/index.js"></script>
    <!-- 引入axios -->
    <script src="../../plugins/axios/axios.min.js"></script>
    <script src="../../js/index.js"></script>
    <script>
      new Vue({
        el: '#food-add-app',
        data() {
          return {
            imageUrl: ''
          }
        },
        methods: {
          handleAvatarSuccess (response, file, fileList) {
              this.imageUrl = `/common/download?name=${response.data}`
          },
          beforeUpload (file) {
            if(file){
              const suffix = file.name.split('.')[1]
              const size = file.size / 1024 / 1024 < 2
              if(['png','jpeg','jpg'].indexOf(suffix) < 0){
                this.$message.error('上传图片只支持 png、jpeg、jpg 格式！')
                this.$refs.upload.clearFiles()
                return false
              }
              if(!size){
                this.$message.error('上传文件大小不能超过 2MB!')
                return false
              }
              return file
            }
          }
        }
      })
    </script>
</body>
</html>
```

添加CommonController,负责文件上传与下载

```less
/**
 *  文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {


    @PostMapping("/upload")
//    在Controller的方法中声明一个MultipartFile接口类型的参数即可接收上传的文件。
    public R<String> upload(MultipartFile file){ // 这里的参数名要和前端发送的文件名（name = file）一致
        // file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString()); // 打印前端上传过来的file对象
        return null;

    }

}
```

MultipartFile定义的file变量必须与name保持一致
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062318011.png)

完整代码

```typescript
/**
 *  文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

        /**
     *  文件上传
     */

    // 定义一个存储路径的遍历
    // 使用@Value这个注解会注入配置文件对应的值，赋值给basePath遍历
    @Value("${reggie.path}")
    private String basePath;


    @PostMapping("/upload")
//    在Controller的方法中声明一个MultipartFile接口类型的参数即可接收上传的文件。
    public R<String> upload(MultipartFile file){ // 这里的参数名要和前端发送的文件名（name = file）一致
        // file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString()); // 打印前端上传过来的file对象

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename(); // xxx.jpg
        // 获取后缀名：  xxx.jpg
        // String类中的substring方法，可以从传入的索引处截取，截取到末尾，得到新的字符串
        // lastIndexOf是返回指定字符在此字符串中最后一次出现处的索引
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); // 得到后缀

        // 使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        //UUID：通用唯一识别码，保证唯一性
        // 随机生成的UUID + 后缀即可
        String fileName = UUID.randomUUID().toString() + suffix;// dsfdss.jpg

        // 创建一个目录对象
        File dir = new File(basePath); // 这个路径写配置文件中的路径
        // 判断当前目录是否存在
        if (!dir.exists()){
            // 如果目录不存在，需要创建
            dir.mkdir();
        }


        try {
            // 将临时文件转存到指定位置
//            file.transferTo(new File("C:\\Users\\aufs\\UserFile\\hello.jpg"));// 这个相当于把这个图片名字写死了
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return R.success(fileName); // 将文件名返回给前端

    }

}

```

#### 文件下载代码实现

文件下载，页面端可以使用![img]()标签展示下载的图片
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062318049.png)

```java
    /**
     *  文件下载
     * @param name 文件名
     * @param response 响应对象，通过响应对象可以创建其对应的输出流，将文件写回浏览器，在浏览器展示图片
     */
    @GetMapping("/download") // 这个是前端的路径
    // 定义一个文件下载的方法，下载的路径和前端代码一致
    public void download(String name, HttpServletResponse response){

        try {
            // 创建一个输入流对象，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // 创建一个输出流对象，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            // 设置响应的内容类型  图片格式
            response.setContentType("image/jpeg");

            // 输入流去读，读到的数据去写
            int len = 0; // 定义一个初始的长度length
            // 创建一个字节数组对象长度为1024，通过这个长度，可以让输入流，每次读1MB
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){ // 这里不等于-1 的意思是没有读完，就执行下面的语句（读完了是-1）
                // 读到多少，就往浏览器写多少，从0开始写，写完len这个全部长度
                outputStream.write(bytes,0,len);
                outputStream.flush(); // 刷新流
            }

            // 关闭两个流
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
```

### 新增菜品

#### 需求分析

后台系统中可以管理菜品信息，通过新增功能来添加一个新的菜品，在添加菜品时需要选择当前菜品所属的菜品分类,并且需要上传菜品图片，在移动端会按照菜品分类来展示对应的菜品信息。
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062318043.png)

#### 数据模型

新增菜品，其实就是将新增页面录入的菜品信息插入到dish表，如果添加了口味做法，还需要向dish_flavor表插入数据。所以在新增菜品时，涉及到两个表:

- dish(菜品表)
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062318064.png)
- dish_flavor(菜品口味表)
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062318624.png)

#### 代码开发-准备工作

在开发业务功能前，先将需要用到的类和接口基本结构创建好:

- 实体类DishFlavor(直接从课程资料中导入即可，Dish实体前面课程中已经导入过了)

```kotlin
@Data
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //菜品id
    private Long dishId;


    //口味名称
    private String name;


    //口味数据list
    private String value;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
    private Integer isDeleted;
}
```

- Mapper接口DishFlavorMapper

```java
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
```

- 业务层接口DishFlavorService

```csharp
public interface DishFlavorService extends IService<DishFlavor> {
}
```

- 业务层实现类 DishFlavorServicelmpl

```scala
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>implements DishFlavorService {
}
```

- 控制层 DishController

```less
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
}
```

#### 代码开发-梳理交互过程

在开发代码之前，需要梳理一下新增菜品时前端页面和服务端的交互过程:

1、页面(backend/page/food/add.html)发送ajax请求，请求服务端获取菜品分类数据并展示到下拉框中

2、页面发送请求进行图片上传，请求服务端将图片保存到服务器

3、页面发送请求进行图片下载，将上传的图片进行回显

4、点击保存按钮，发送ajax请求，将菜品相关数据以json形式提交到服务端

开发新增菜品功能，其实就是在服务端编写代码去处理前端页面发送的这4次请求即可。

**菜品分类下拉框**：在CategoryController添加

```swift
    /**
     *  根据条件查询分类数据
     * @param category 将type参数封装称category实体类作为参数
     * @return
     */
    @GetMapping("/list")

                                // 将参数type，用category对象来封装
                                // 对应：http://localhost:8080/category/list?type=1
    public R<List<Category>> list(Category category){ // 返回的是一个list集合 -- 查询菜品分类

        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        // 从前端得到的参数中的type属性（对应数据库字段）不为null，执行后面语句
        // 条件生成： 实体类中的type = 参数中的type值（例如type = 1）
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        // 添加排序条件
        // 条件生成：根据sort字段升序排序，根据UpdateTime字段倒叙排序
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        // 使用业务层的list方法，查询数据库  得到符合条件type =1 为  菜品分类的集合
        List<Category> list = categoryService.list(queryWrapper);

        // 将查询到的list集合对象，返回给前端页面
        return R.success(list);
    }


```

导入DishDto（位置:资料/dto)，用于封装页面提交的数据

```scala
@Data
public class DishDto extends Dish { // 继承了Dish这个实体类（扩展了Dish实体类中的属性和方法)

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
```

**注意**：==DTO，全称为Data Transfer object，即数据传输对象，一般用于展示层与服务层之间的数据传输。==（DTO最大作用是用于多表查询，简化了操作）

新增菜品同时插入菜品对应的口味数据,需要操作两张表：dish、dishflavor

在DishService接口中添加方法saveWithFlavor,在DishServiceImpl实现

```scala
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到菜品表dish
        this.save(dishDto);

        Long dishid = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishid);
            return item;
        }).collect(Collectors.toList());
        //dishFlavorService.saveBatch(dishDto.getFlavors());
        //保存菜品口味到菜品数据表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }
}
```

由于以上代码涉及多表操作，**在启动类上开启事务支持**添加`@EnableTransactionManagement`注解，但是本人添加该注解会报错，项目启动会失败，并且springboot该注解应该是默认开启的，故没有添加

新增菜品

```less
@PostMapping
public R<String> save(@RequestBody DishDto dishDto){
    dishService.saveWithFlavor(dishDto);
    return R.success("新增菜品成功");
}
```

### 菜品信息分页查询

#### 需求分析

系统中的菜品数据很多的时候，如果在一个页面中全部展示出来会显得比较乱，不便于查看，所以一般的系统中都会以分页的方式来展示列表数据。
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062318669.png)

#### 代码开发-梳理交互过程

在开发代码之前，需要梳理一下菜品分页查询时前端页面和服务端的交互过程:

1、页面(backend/page/food/list.html)发送ajax请求，将分页查询参数(page、pageSize、name)提交到服务端，获取分页数据

2、页面发送请求，请求服务端进行图片下载，用于页面图片展示

开发菜品信息分页查询功能，其实就是在服务端编写代码去处理前端页面发送的这2次请求即可。

请求URL：

![image-20220807040431182](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208070404255.png)

Page类中的records（记录的意思）属性，是一个列表集合，这个参数是前端页面展现出来的数据，承载的集合，-- 后面将其遍历，将遍历后对象赋值给dishDto这个多表关联的实体类  

![image-20220808001742097](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080017255.png)

![image-20220808002022889](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080020977.png)



由于请求体中含有属性：categoryName（分类名称）    copies（副本的意思）

所以在对应的实体类DishDto中也添加了对应属性，用来接收前端传过来的数据

![image-20220807041050444](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208070410526.png)

```java
@GetMapping("/page")
public R<Page> page(int page, int pageSize, String name) {
  //构造分页构造器
  Page<Dish> pageInfo = new Page<>(page, pageSize);

  Page<DishDto> dishDtoPage = new Page<>();

  //构造条件构造器
  LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

  //添加过滤条件
  queryWrapper.like(!StringUtils.isEmpty(name), Dish::getName, name);

  //添加排序条件
  queryWrapper.orderByDesc(Dish::getUpdateTime);

  //进行分页查询
  dishService.page(pageInfo, queryWrapper);

  //对象拷贝 -- 将源对象pageInfo中的属性，拷贝一份到dishDtoPage对象中去，排除pageInfo中的records属性，单独处理这个属性
  BeanUtils.copyProperties(pageInfo,dishDtoPage,"records"); // 第三个参数是排除records属性

  List<Dish> records = pageInfo.getRecords();// 拿到records属性，进行处理（因为records是一个集合，
    // 遍历其属性records
  List<DishDto> list=records.stream().map((item)->{
    DishDto dishDto=new DishDto();
//  将遍历到的对象，拷贝一份到dishDto对象中去
    BeanUtils.copyProperties(item,dishDto);
      // 根据item对象，得到其对应的CategoryId
    Long categoryId = item.getCategoryId();
    //根据id查分类对象
    Category category = categoryService.getById(categoryId);
    if(category!=null){
      String categoryName = category.getName();
      dishDto.setCategoryName(categoryName);
    }
    return dishDto;
  }).collect(Collectors.toList());

  dishDtoPage.setRecords(list);

  return R.success(dishDtoPage);
}
```

### 修改菜品

#### 需求分析

在菜品管理列表页面点击修改按钮，跳转到修改菜品页面，在修改页面回显菜品相关信息并进行修改，最后点击确定按钮完成修改操作
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062318610.png)

#### 代码开发-梳理交互过程

在开发代码之前，需要梳理一下修改菜品时前端页面（ add.html)和服务端的交互过程:

1、页面发送ajax请求，请求服务端获取分类数据，用于菜品分类下拉框中数据展示

2、页面发送ajax请求，请求服务端，根据id查询当前菜品信息，用于菜品信息回显

- DishController处理Get请求

```kotlin
    /**
     *  根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
//根据Id查询菜品信息与对应的口味信息
    @GetMapping("/{id}") //  http://localhost:8080/dish/1555991783994073090
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
```

在DishService接口中创建getByIdWithFlavor

```java
public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish（菜品表）、dish_flavor（菜品口味表)
    public void saveWithFlavor(DishDto dishDto);

    // 根据id查询对应的菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);
}

```

在DishServiceImpl添加getByIdWithFlavor方法

```java
@Override
@Transactional
public DishDto getByIdWithFlavor(Long id) {
    //查询菜品基本信息
    Dish dish = this.getById(id);

    DishDto dishDto=new DishDto();
    BeanUtils.copyProperties(dish,dishDto);

    //查询菜品口味信息
    LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.eq(DishFlavor::getDishId,dish.getId());
    List<DishFlavor> list = dishFlavorService.list(queryWrapper);

    dishDto.setFlavors(list);

    return dishDto;
}
```

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062318617.png)

3、页面发送请求，请求服务端进行图片下载，用于页图片回显

4、点击保存按钮，页面发送ajax请求，将修改后的菜品相关数据以json形式提交到服务端

- 在DishController添加put方法

```less
//修改菜品
@PutMapping
public R<String> update(@RequestBody DishDto dishDto){
    dishService.updateWithFlavor(dishDto);
    return R.success("修改菜品成功");
}
```

- 在DishServiceImpl添加updateWithFlavor方法

```perl
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表中的基本信息
        // 调用业务层接口（本身就是业务层，所以用this）
        this.updateById(dishDto); // 当中updateById方法中要的是dish对象，我们可以传他的子类dishDto，同样可以修改dish表中的基础信息

        // 清理当前菜品对应口味数据 -- dish_flavor表中的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        // 最终要执行的sql为：delete *  from dish_flavor where id = ???
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());// 这个dishDto是前端传过来的对象，删除其原来的对象

        // 调用口味表相关的业务层接口，中的remove方法
        dishFlavorService.remove(queryWrapper);

        // 添加当前提交过来的口味数据  --dish_flavor表中的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());


        dishFlavorService.saveBatch(flavors); // 将其集合对象添加到数据库中去

    }
```

开发修改菜品功能，其实就是在服务端编写代码去处理前端页面发送的这4次请求即可。

### 停售/起售菜品，删除菜品

#### 需求分析

在商品买卖过程中，商品停售，起售可以更加方便的让用户知道店家还有什么类型的商品在卖。删除方法也更方便的管理菜品

#### 代码实现

在DishController添加sale方法与delete方法,通过数组保存ids，批量起售停售、删除都能生效

```typescript
 /**
     *    根据id，停售起售菜品
     *    这是批量停售和起售的url， 第一个参数0（状态码），第二个参数（id集合)
     *     http://localhost:8080/dish/status/0?ids=1555991783994073090,1413384757047271425,1413385247889891330
     * @param status，状态码
     * @param ids id集合
     * @return
     */
@PostMapping("/status/{status}")  // 如果url中有{}占位符，就用@PathVariable
public R<String> sale(@PathVariable int status,
                      String[] ids){ // 第一个参数是接收状态码（停售0，起售1）
    for(String id: ids){
        // 根据id查询对象
        Dish dish = dishService.getById(id);
        //设置其状态
        dish.setStatus(status);
        // 更新其对象
        dishService.updateById(dish);
    }
    return R.success("修改成功");
}
//删除菜品
@DeleteMapping
public R<String> delete(String[] ids){
    for (String id:ids) {
        dishService.removeById(id);
    }
    return R.success("删除成功");
}
```





# 瑞吉外卖开发笔记 五

笔记内容为**黑马程序员**视频内容

## 套餐管理业务开发

### 新增套餐

#### 需求分析

套餐就是菜品的集合。

后台系统中可以管理套餐信息，通过新增套餐功能来添加一个新的套餐，在添加套餐时需要选择当前套餐所属的套餐分类和包含的菜品，并且需要上传套餐对应的图片，在移动端会按照套餐分类来展示对应的套餐。

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319429.png)
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319415.png)

#### 数据模型

新增套餐，其实就是将新增页面录入的套餐信息插入到setmeal表，还需要向setmeal_dish表插入套餐和菜品关联数据。所以在新增套餐时，涉及到两个表:

- setmeal　　　　　　　　　 套餐表
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319541.png)
- setmeal_dish　　　　　　　套餐菜品关系表
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319426.png)

#### 代码开发-准备工作

在开发业务功能前，先将需要用到的类和接口基本结构创建好:

- **实体类SetmealDish**(直接从课程资料中导入即可，Setmeal实体前面+ 课程中已经导入过了) -- setmeal可以理解为套餐的意思
- DTO **SetmealDto**(直接从课程资料中导入即可)
- Mapper接口SetmealDishMapper
- 业务层接口SetmealDishService
- 业务层实现类SetmealDishServicelmpl
- 控制层SetmealController

#### 代码开发-梳理交互过程

在开发代码之前，需要梳理一下新增套餐时前端页面和服务端的交互过程:

1、页面(backend/ page/comboladd.html)发送ajax请求，请求服务端获取套餐分类数据并展示到下拉框中

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319365.png)

2、页面发送ajax请求，请求服务端获取菜品分类数据并展示到添加菜品窗口中

3、页面发送ajax请求，请求服务端，根据菜品分类查询对应的菜品数据并展示到添加菜品窗口中

在DishController添加list方法

```perl
//根据条件查询对应菜品数据
@GetMapping("/list")
// URL：localhost:8080/dish/list?categoryId=1555297971076354050
// 使用参数(Long categoryId) 接收也可以，但是这里最好封装它，用Dish对象（里面有categoryId属性）
public R<List<Dish>> list(Dish dish){

    //构造查询条件
    LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
    //添加条件，查询状态为1的（起售状态）
    lambdaQueryWrapper.eq(Dish::getStatus,1);
    lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
    //条件排序条件
    lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

    List<Dish> list=dishService.list(lambdaQueryWrapper);

    return R.success(list);
}
```

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319412.png)

4、页面发送请求进行图片上传，请求服务端将图片保存到服务器

5、页面发送请求进行图片下载，将上传的图片进行回显

6、点击保存按钮，发送ajax请求，将套餐相关数据以json形式提交到服务端

在SetmealServiceImpl实现saveWithDish方法：新增套餐，同时要保持与菜品的关联关系

```scala
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService{

    @Autowired
    private SetmealDishService setmealDishService;

    //新增套餐，同时要保持与菜品的关联关系
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());// 这里得到的id是套餐setmeal_id，将setmeal_id赋值给菜品套餐表中去
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);

    }
}
```

在SetmealController添加save方法

```less
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("setmeal:{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }
}
```

开发新增套餐功能，其实就是在服务端编写代码去处理前端页面发送的这6次请求即可。

### 套餐分页查询

#### 需求分析

系统中的套餐数据很多的时候，如果在一个页面中全部展示出来会显得比较乱，不便于查看，所以一般的系统中都会以分页的方式来展示列表数据。

注意：有一个注意点：如果将以下代码运行不会报错，但是浏览器页面不会显示套餐分类这个字段内容，

![image-20220808011516986](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080115089.png)

在SetmealDto有这个属性字段，可以用这个参数来显示套餐分类字段 -- 使用对象拷贝到SetmealDto中去

![image-20220808012139340](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080121485.png)

![image-20220808011452128](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080114217.png)

#### 代码开发-梳理交互过程

在开发代码之前，需要梳理一下套餐分页查询时前端页面和服务端的交互过程:

1、页面(backend/page/combo/list.html)发送ajax请求，将分页查询参数(page、pageSize、name)提交到服务端，获取分页数据

2、页面发送请求，请求服务端进行图片下载，用于页面图片展示

开发套餐信息分页查询功能，其实就是在服务端编写代码去处理前端页面发送的这2次请求即可。

这个Page类中的records属性；就是页面存储的记录条数信息的对象

![image-20220808013816923](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080138018.png)

![image-20220808013650318](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080136419.png)

```java
@GetMapping("/page")
public R<Page> page(int page,int pageSize,String name){
    //构造分页构造器
    Page<Setmeal> pageInfo=new Page<>(page,pageSize);
// 创建这个SetmealDto泛型的对象可以让前端显示  套餐分类这个字段了
    Page<SetmealDto> pageDtoInfo=new Page<>();
    //构造条件构造器
    LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
    //根据name进行模糊查询
    queryWrapper.like(!StringUtils.isEmpty(name),Setmeal::getName,name);
    //添加排序条件，根据sort进行排序
    queryWrapper.orderByDesc(Setmeal::getUpdateTime);
    //进行分页查询
    setmealService.page(pageInfo,queryWrapper);

    //对象拷贝
    BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");

    List<Setmeal> records=pageInfo.getRecords();

    List<SetmealDto> list= records.stream().map((item)->{
        SetmealDto setmealDto=new SetmealDto();

        BeanUtils.copyProperties(item,setmealDto);
        Long categoryId = item.getCategoryId();
        //根据id查分类对象
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            // 拿到套餐分类对象的名称，返回给setmealDto，
            String categoryName = category.getName();
            setmealDto.setCategoryName(categoryName);
        }
        return setmealDto;
    }).collect(Collectors.toList());

    pageDtoInfo.setRecords(list); // 将新的list对象赋值给自己新定义pageDto对象

    return R.success(pageDtoInfo);
}
```

### 删除、起售、停售套餐

#### 需求分析

在套餐管理列表页面点击删除按钮，可以删除对应的套餐信息。也可以通过复选框选择多个套餐，点击批量删除按钮一次删除多个套餐。注意，对于状态为售卖中的套餐不能删除，需要先停售，然后才能删除。

#### 代码实现

开发删除套餐功能，其实就是在服务端编写代码去处理前端页面发送的这2次请求即可。
观察删除单个套餐和批量删除套餐的请求信息可以发现，两种请求的地址和请求方式都是相同的，不同的则是传递的id个数，所以在服务端可以提供一个方法来统一处理。

```typescript
    /**
     *  删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
//    集合保存普通参数：请求参数名与形参集合对象名相同且请求参数为多个，@RequestParam绑定参数关系
                            // http://localhost:8080/setmeal?ids=1556324903628898306,1415580119015145474
                            // 由于请求的是一个参数中是一个集合对象的参数，这个时候形参要用@RequestParam注解
                            // 当然如果用数组接收的话，就不用@RequestParam注解了
    public R<String> delete(@RequestParam List<Long> ids) { // 将参数封装到list集合中，也可以封装到Long数组中去
        log.info("ids:{}",ids);

        setmealService.removeWithDish(ids); // 这个removeWithDish是mp的方法，删除对应的集合
        return R.success("套餐数据删除成功");
    }

    /**
     *  套餐停售功能
     * @param status // 接收前端传过来的状态码 点击停售，传过来 0
     * @param ids // 要删除的套餐id集合
     * @return
     */
    @PostMapping("/status/{status}") //http://localhost:8080/setmeal/status/0?ids=1415580119015145474
    public R<String> sale(@PathVariable int status,String[] ids){
        for (String id:ids){
            // 根据id拿到对应的对象
            Setmeal setmeal = setmealService.getById(id);
            // 设置状态码，值为前端传过来的值
            setmeal.setStatus(status);
            // 执行sql，更新操作
            setmealService.updateById(setmeal);
        }
        return R.success("修改成功");
    }

```

定义业务层删除套餐对应的SetmealServiceImpl实现类的removeWithDish方法：

```java
    /**
     *  删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    // 由于删除了两张表，所以加一个事务注解，保证数据的一致性
    @Transactional
    public void removeWithDish(List<Long> ids) {

        // 执行条件的sql为： select count(*) from setmeal where id in (1,2,3) and status = 1
        // 查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1); // 查询起售的商品

        int count = this.count(queryWrapper); // 这个count是mq框架的方法

        if (count > 0){
            // 如果不能删除，抛出一个业务异常，自己定义了
            throw  new CustomException("套餐正在售卖中，不能删除");
        }
        // 如果可以删除，先删除套餐表中的数据-- setmeal表
        this.removeByIds(ids); // 这个removeByIds是mq提供的方法，参数提供一个集合，把集合中符合条件的都删除

        // 删除关系表中的数据---setmeal_dish表
        // delete from setmeal_dish where setmeal_id in (1,2,3) 这里的123是集合中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 相当于sqldelete from setmeal_dish where setmeal_id in (1,2,3)
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        // 删除关系表中的数据--setmeal_dish 删除哪张表，就调用对应的业务层
        setmealDishService.remove(lambdaQueryWrapper);


    }

```

### 修改套餐

#### 需求分析

在套餐管理列表页面点击修改按钮，跳转到修改套餐页面，在修改页面回显套餐相关信息并进行修改，最后点击确定按钮完成修改操作

#### 代码开发-梳理交互过程

在开发代码之前，需要梳理一下修改套餐时前端页面（ add.html)和服务端的交互过程:

1、页面发送ajax请求，请求服务端获取分类数据，用于套餐分类下拉框中数据展示

2、页面发送ajax请求，请求服务端，根据id查询当前套餐信息，用于套餐信息回显

- SetmealController处理Get请求

```kotlin
//根据Id查询套餐信息
@GetMapping("/{id}")
public R<SetmealDto> getById(@PathVariable Long id){
    SetmealDto setmealDto=setmealService.getByIdWithDish(id);

    return R.success(setmealDto);
}
```

- SetmealServiceImpl添加getByIdWithDish方法

```java
@Override
public SetmealDto getByIdWithDish(Long id) {
    //查询套餐基本信息
    Setmeal setmeal = this.getById(id);
    SetmealDto setmealDto = new SetmealDto();
    BeanUtils.copyProperties(setmeal, setmealDto);

    //查询套餐菜品信息
    LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
    List<SetmealDish> list = setmealDishService.list(queryWrapper);

    setmealDto.setSetmealDishes(list);
    return setmealDto;
}
```

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319723.png)

3、页面发送请求，请求服务端进行图片下载，用于页图片回显

4、点击保存按钮，页面发送ajax请求，将修改后的菜品相关数据以json形式提交到服务端

- 在SetmealServiceImpl添加updateWithDish方法

```perl
@Override
public void updateWithDish(SetmealDto setmealDto) {
    //更新setmeal表基本信息
    this.updateById(setmealDto);

    //更新setmeal_dish表信息delete操作
    LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
    setmealDishService.remove(queryWrapper);

    //更新setmeal_dish表信息insert操作
    List<SetmealDish> SetmealDishes = setmealDto.getSetmealDishes();

    SetmealDishes = SetmealDishes.stream().map((item) -> {
        item.setSetmealId(setmealDto.getId());
        return item;
    }).collect(Collectors.toList());

    setmealDishService.saveBatch(SetmealDishes);
}
```

- 在SetmealController处理put请求

```less
//修改套餐
@PutMapping
public R<String> update(@RequestBody SetmealDto setmealDto){
    setmealService.updateWithDish(setmealDto);
    return R.success("修改成功");
}
```

**注意：**开发修改套餐功能，其实就是在服务端编写代码去处理前端页面发送的这4次请求即可。

## 手机验证码登录

### 短信发送

#### 短信服务介绍

目前市面上有很多第三方提供的短信服务，这些第三方短信服务会和各个运营商（移动、联通、电信)对接，我们只需要注册成为会员并且按照提供的开发文档进行调用就可以发送短信。需要说明的是，这些短信服务一般都是收费服务。

常用短信服务:

- 阿里云
- 华为云
- 腾讯云
- 京东
- 梦网
- 乐信

#### 阿里云短信服务-介绍

阿里云短信服务（Short Message Service)是广大企业客户快速触达手机用户所优选使用的通信能力。调用API或用群发助手，即可发送验证码、通知类和营销类短信;国内验证短信秒级触达，到达率最高可达99%;国际/港澳台短信覆盖200多个国家和地区，安全稳定，广受出海企业选用。

应用场景:

- 验证码
- 短信通知
- 推广短信

#### 阿里云短信服务-注册账号

阿里云官网: https://www.aliyun.com/

点击官网首页注册按钮。

#### 阿里云短信服务-设置短信签名

注册成功后，点击登录按钮进行登录。登录后进入短信服务管理页面，选择国内消息菜单:

<img src="https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319840.png" alt="image"  />

短信签名是短信发送者的署名，表示发送方的身份。

#### 阿里云短信服务-设置短信模板

切换到【模板管理】标签页:
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319842.png)

短信模板包含短信发送内容、场景、变量信息。

#### 阿里云短信服务-设置AccessKey

光标移动到用户头像上，在弹出的窗口中点击【AccessKey管理】∶
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319866.png)

#### 注意：腾讯云使用的方法不一样：

参考文档：https://blog.csdn.net/qq_56233219/article/details/118015291

​					https://blog.csdn.net/weixin_38007185/article/details/108245766?spm=1001.2014.3001.5506

​				https://blog.csdn.net/weixin_38007185/article/details/108245766

申请公众号的方式：https://icode.best/i/05667645150915

#### 代码开发

使用阿里云短信服务发送短信，可以参照官方提供的文档即可。
具体开发步骤:

1、导入maven坐标

```xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.5.16</version>
</dependency>
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-dysmsapi</artifactId>
    <version>1.1.0</version>
</dependency>
```

2、调用API

```typescript
public class SMSUtils {

	/**
	 * 发送短信
	 * @param signName 签名
	 * @param templateCode 模板
	 * @param phoneNumbers 手机号
	 * @param param 参数
	 */
	public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param){
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "", "");
		IAcsClient client = new DefaultAcsClient(profile);

		SendSmsRequest request = new SendSmsRequest();
		request.setSysRegionId("cn-hangzhou");
		request.setPhoneNumbers(phoneNumbers);
		request.setSignName(signName);
		request.setTemplateCode(templateCode);
		request.setTemplateParam("{\"code\":\""+param+"\"}");
		try {
			SendSmsResponse response = client.getAcsResponse(request);
			System.out.println("短信发送成功");
		}catch (ClientException e) {
			e.printStackTrace();
		}
	}
}

```

==如果是模拟的话，可以定义一个随机验证码的工具类：==

```java
/**
 * 随机生成验证码工具类
 */
public class ValidateCodeUtils {
    /**
     * 随机生成验证码
     * @param length 长度为4位或者6位
     * @return
     */
    public static Integer generateValidateCode(int length){
        Integer code =null;
        if(length == 4){
            code = new Random().nextInt(9999);//生成随机数，最大为9999
            if(code < 1000){
                code = code + 1000;//保证随机数为4位数字
            }
        }else if(length == 6){
            code = new Random().nextInt(999999);//生成随机数，最大为999999
            if(code < 100000){
                code = code + 100000;//保证随机数为6位数字
            }
        }else{
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     * @param length 长度
     * @return
     */
    public static String generateValidateCode4String(int length){
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String capstr = hash1.substring(0, length);
        return capstr;
    }
}

```

### 手机验证码登录

#### 需求分析

为了方便用户登录，移动端通常都会提供通过手机验证码登录的功能。

手机验证码登录的优点:

- 方便快捷，无需注册，直接登录
- 使用短信验证码作为登录凭证，无需记忆密码
- 安全

登录流程:
输入手机号>获取验证码>输入验证码>点击登录>登录成功

注意:通过手机验证码登录，手机号是区分不同用户的标识。

#### 数据模型

通过手机验证码登录时，涉及的表为user表，即用户表。结构如下:
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319888.png)

#### 代码开发

在开发代码之前，需要梳理一下登录时前端页面和服务端的交互过程:

1、在登录页面(front/page/login.html)输入手机号，点击【获取验证码】按钮，页面发送ajax请求，在服务端调用短信服务API给指定手机号发送验证码短信

2、在登录页面输入验证码，点击【登录】按钮，发送ajax请求，在服务端处理登录请求

开发手机验证码登录功能，其实就是在服务端编写代码去处理前端页面发送的这2次请求即可。

在开发业务功能前，先将需要用到的类和接口基本结构创建好:

- 实体类User(直接从课程资料中导入即可)
- Mapper接口UserMapper
- 业务层接口UserService
- 业务层实现类UserServicelmpl
- 控制层UserController
- 工具类SMSutils、 ValidateCodeutils（直接从课程资料中导入即可)

前面我们已经完成了LogincheckFilter过滤器的开发，此过滤器用于检查用户的登录状态。我们在进行手机验证码登录时，发送的请求需要在此过滤器处理时直接放行。

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319966.png)

LoginCheckFilter过滤器添加

```kotlin
//        4-2、判断登录状态，如果已登录，则直接放行
if (request.getSession().getAttribute("user") != null) {
    log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("user"));

    Long userId= (Long) request.getSession().getAttribute("user");

    BaseContext.setCurrentId(userId);

    filterChain.doFilter(request, response);
    return;
}

```

由于资料中代码不全login.js自行添加

```kotlin
function sendMsgApi(data) {
    return $axios({
        'url':'/user/sendMsg',
        'method':'post',
        data
    })
}
```

login.html

```kotlin
// this.form.code = (Math.random()*1000000).toFixed(0)
sendMsgApi({phone:this.form.phone})
```

UserController处理post请求（发送验证码的请求）

```typescript
@PostMapping("/sendMsg")
public R<String> sendMsg(@RequestBody User user, HttpSession session){
    //获取手机号
    String phone=user.getPhone();
    if(!StringUtils.isEmpty(phone)) {
        //生成随机的4位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        log.info("code={}",code);
        //调用阿里云提供的短信服务API完成发送短信
        //SMSUtils.sendMessage("瑞吉外卖","",phone,code);

        //需要将生成的验证码保存到Session
        session.setAttribute(phone,code); // 键值对的类型，属性名位phone，值为code
        return R.success("手机验证码短信发送成功");
    }
    return R.error("手机短信发送失败");
}

```

由于前端页面有部分代码缺失，建议拷贝资料中day05的front代码

在UserController编写login处理post请求

```typescript
    /**
     *  移动端用户登录
     * @param map
     * @param session
     * @return
     */

    @PostMapping("/login")
                                // 使用Map类来封装，键值对，phone：code
    public R<User> login(@RequestBody Map map, HttpSession session) {

        log.info(map.toString());

        // 获取手机号
        String phone = map.get("phone").toString(); // 将得到的value转换成字符串类型

        // 获取验证码
        String code = map.get("code").toString();
        // 从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);

        // 进行验证码比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeInSession !=null && codeInSession.equals(code)){

            // 如果能够比对成功，说明登录成功
            // 判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone); // 如果手机号码和数据库中的号码相等，说明已经注册过

            User user = userService.getOne(queryWrapper);

            if (user == null){ // 说明是新用户
                // 不为空则是新用户，自动完成注册
                user = new User();// 重新创建一个新的对象，覆盖原来的，重置设置其属性值
                user.setPhone(phone);
                user.setStatus(1); // 状态：1表示正常
                userService.save(user); // 添加到数据库中
            }

            // 登录成功后，将登录后的id，传给session
            // 当http请求中session有id时：就不会闪退页面，
            session.setAttribute("user",user.getId()); // key为“user”，value为：id

            return R.success(user);
        }
        return R.error("短信发送失败");
    }
```

#### 效果展示

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319079.png)

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208062319287.png)





# 瑞吉外卖开发笔记 六

笔记内容为**黑马程序员**视频内容

## 导入用户地址簿相关功能代码

### 需求分析

地址簿，指的是移动端消费者用户的地址信息，用户登录成功后可以维护自己的地址信息。同一个用户可以有多个地址信息，但是只能有一个**默认地址**。

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354154.png)

### 数据模型

用户的地址信息会存储在address_book表，即地址簿表中。具体表结构如下:

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354166.png)

### 导入功能代码

功能代码清单:

- 实体类AddressBook(直接从课程资料中导入即可)
- Mapper接口AddressBookMapper
- 业务层接口AddressBookService
- 业务层实现类AddressBookServicelmpl
- 控制层AddressBookController(直接从课程资料中导入即可)

```kotlin
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookService.list(queryWrapper));
    }
}
```

### 功能测试

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354046.png)

## 菜品展示

### 需求分析

用户登录成功后跳转到系统首页，在首页需要根据分类来展示菜品和套餐。如果菜品设置了口味信息需要展示 [选择规格] 按钮，否则显示 [+] 按钮。

### 代码开发

#### 代码开发-梳理交互过程

在开发代码之前，需要梳理一下前端页面和服务端的交互过程:

1、页面(front/index.html)发送ajax请求，获取分类数据（菜品分类和套餐分类)

2、页面发送ajax请求，获取第一个分类下的菜品或者套餐

开发菜品展示功能，其实就是在服务端编写代码去处理前端页面发送的这2次请求即可。

注意:首页加载完成后，还发送了一次ajax请求用于加载购物车数据，此处可以将这次请求的地址暂时修改一下，从静态json文件获取数据，等后续开发购物车功能时再修改回来，如下:

```kotlin
//获取购物车内商品的集合
function cartListApi(data) {
    return $axios({
        // 'url': '/shoppingCart/list',
        'url':'/front/cartData.json',
        'method': 'get',
        params:{...data}
    })
}
```

cartData.json:

```ruby
{"code":1,"msg":null,"data":[],"map":{}}
```

改造DishController中的list方法

```java
    /**
     *  根据条件查询对应的菜品数据（改造后的，用于移动端显示）
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
```

在SetmealController里添加list方法显示套餐信息

```perl
    /**
     *  显示套餐信息
     * @param setmeal，传过来的long型categoryId参数封装成setmeal对象
     * @return
     *  // 请求的URL http://localhost:8080/setmeal/list?categoryId=1413342269393674242&status=1
     */

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal !=null, Setmeal::getCategoryId,setmeal.getCategoryId());
        // 查询套餐售卖状态，
        queryWrapper.eq(setmeal!=null, Setmeal::getStatus,setmeal.getStatus());
        // 设置条件：排序展示
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);

    }

```

### 功能测试

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354160.png)

## 购物车

### 需求分析

移动端用户可以将菜品或者套餐添加到购物车。对于菜品来说，如果设置了口味信息，则需要选择规格后才能加入购物车;对于套餐来说，可以直接点击 **[+]** 将当前套餐加入购物车。在购物车中可以修改菜品和套餐的数量,也可以清空购物车。

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354360.png)

### 数据模型

购物车对应的数据表为shopping_cart表，具体表结构如下:
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354139.png)

### 代码开发

#### 代码开发-梳理交互过程

在开发代码之前，需要梳理一下购物车操作时前端页面和服务端的交互过程:

1、点击 **[加入购物车]** 或者 **[+]** 按钮，页面发送ajax请求，请求服务端，将菜品或者套餐添加到购物车

2、点击购物车图标，页面发送ajax请求，请求服务端查询购物车中的菜品和套餐

3、点击清空购物车按钮，页面发送ajax请求，请求服务端来执行清空购物车操作

开发购物车功能，其实就是在服务端编写代码去处理前端页面发送的这3次请求即可。

#### 代码开发-准备工作

在开发业务功能前，先将需要用到的类和接口基本结构创建好:

- 实体类ShoppingCart(直接从课程资料中导入即可)
- Mapper接口ShoppingCartMapper
- 业务层接口ShoppingcartService
- 业务层实现类ShoppingCartServicelmpl
- 控制层ShoppingCartController

#### 代码开发-添加购物车

```perl
    /**
     *  添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据：{}", shoppingCart);

        // 设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();// 获取当前线程的用户id（对应的是user表），用来当作用户id
        shoppingCart.setUserId(currentId);

        // 查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();// 从请求的参数中获取菜品id

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId); // 查询线程id的值

        if (dishId !=null){
            // 前端代表传过来的是菜品id，添加到购物车的是菜品dish
            queryWrapper.eq(ShoppingCart::getDishId,dishId);


        }else {
            // 添加到购物车的是套餐
            // 定义查询条件
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        // 查询当前菜品或者套餐是否在购物车中
        // SQL: select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);// 根据条件调用业务层方法查询

        if (cartServiceOne !=null){
            // 不为空，说明查到了，这个菜品/套餐已存在，，在原来的基础上加1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne); // 更新数据库对象

        }else {
            // 为空，就代表没查到，菜品/套餐不存在，则添加到购物车，数据默认就是1
            shoppingCart.setNumber(1);
             // 设置入库的create_time
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);// 将对象添加进去
            cartServiceOne = shoppingCart; // cartServiceOne是根据queryWrapper条件查询到的对象，将新的参数对象赋值
        }


        return R.success(cartServiceOne);
    }

```

#### 代码开发-查看购物车

把前端假数据改回来

```java
function cartListApi(data) {
    return $axios({
        'url': '/shoppingCart/list',
        // 'url':'/front/cartData.json',
        'method': 'get',
        params:{...data}
    })
}
```

查看购物车  -- 不用user账号中的购物车信息都不一样，因为购物车数据表和user数据表中的userid关联了，会根据不同的userid，不查询购物车表对应的数据

```swift
 /**
     *  清空购物车
     * @return
     */
@GetMapping("/list")
public R<List<ShoppingCart>> list(){
    log.info("查看购物车");
    LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
    queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
    List<ShoppingCart> list = shoppingcartService.list(queryWrapper);
    return R.success(list);
}
```

#### 代码开发-清空购物车

```perl
@DeleteMapping("/clean")
public R<String> clean(){

    LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
    shoppingcartService.remove(queryWrapper);
    return R.success("清空购物车成功");
}
```

#### 代码开发-减少菜品

```perl
 /**
     *  删除购物车菜品
     * @param shoppingCart
     * @return
     */
@PostMapping("/sub")
public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
    Long setmealId = shoppingCart.getSetmealId();
    Long dishId = shoppingCart.getDishId();
    LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

    if (setmealId!=null){
        queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
    }else {
        queryWrapper.eq(ShoppingCart::getDishId,dishId);
    }
    ShoppingCart one = shoppingcartService.getOne(queryWrapper);
    Integer number = one.getNumber();
    if(number==1){
        shoppingcartService.remove(queryWrapper);
    }else {
        one.setNumber(number-1);
        shoppingcartService.updateById(one);
    }

    return R.success(one);
}
```

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354695.png)

## 下单

### 需求分析

移动端用户将菜品或者套餐加入购物车后，可以点击购物车中的 **【去结算】** 按钮，页面跳转到订单确认页面，点击 **【去支付】** 按钮则完成下单操作。

### 数据模型

用户下单业务对应的数据表为orders表和order_detail表:

- orders:订单表
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354660.png)
- order_detail:订单明细表
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354672.png)

### 代码开发

#### 代码开发-梳理交互过程

在开发代码之前，需要梳理一下用户下单操作时前端页面和服务端的交互过程:

1、在购物车中点击 **【去结算】** 按钮，页面跳转到订单确认页面

2、在订单确认页面，发送ajax请求，请求服务端获取当前登录用户的默认地址

3、在订单确认页面，发送ajax请求，请求服务端获取当前登录用户的购物车数据

4、在订单确认页面点击 **【去支付】** 按钮，发送ajax请求，请求服务端完成下单操作

开发用户下单功能，其实就是在服务端编写代码去处理前端页面发送的请求即可。

#### 代码开发-准备工作

在开发业务功能前，先将需要用到的类和接口基本结构创建好:

- 实体类Orders、OrderDetail（直接从课程资料中导入即可)
- Mapper接口OrderMapper、OrderDetailMapper
- 业务层接口OrderService、OrderDetailService
- 业务层实现类OrderServicelmpl、OrderDetailServicelmpl
- 控制层OrderController、OrderDetailController

#### 代码开发

在OrderService添加submit方法用于用户下单

```java
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;
    // 注入订单明细的对象
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     *  用户下单
     * @param orders
     */
    @Transactional // 由于要操作多次数据库，和多张表，要保证事务的一致性，
    public void submit(Orders orders) {

        // 获得当前用户id
        Long userId = BaseContext.getCurrentId();

        // 查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        // 根据条件userId查询购物车的数据（所以调用购物车的业务层对象）
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);// 查到的是一个集合

        if (shoppingCarts == null || shoppingCarts.size() == 0) { // 如果购物车对象为null，或者购物车对象的数量为0
            // 就抛异常
            throw new CustomException("购物车为空，不能下单");

        }
        // 查询用户数据
        User user = userService.getById(userId);

        // 查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("用户地址信息有误，不能下单");
        }
        //IdWorker 是mp提供的类，使用其getId方法，生成唯一id，当作我们的订单号
        long orderId = IdWorker.getId(); // 订单号

        /**
         * AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减。
         * AtomicInteger 是对int 类型的一个封装，提供原子性的访问和更新操作
         * 其原子性的操作实现是基于CAS (compare-and-swap)技术
         * 如果当前数值不变，代码没有其他线程进行并发修改，则成功更新。
         */
        // 这里简单理解就是创建了一个更加安全的Integer类，起始值为0，这个类在多线程保证安全
        AtomicInteger amount = new AtomicInteger(0); // amout是金额对象


        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        // 向订单表插入数据，一条数据
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
//        订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//计算总金额
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee()); // 获取收货人属性对象
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        // 向订单表插入数据，一条数据
        this.save(orders);

        // 向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);// saveBatch是mp添加一个集合对象，list()是查询一个集合

        // 清空购物车数据
        shoppingCartService.remove(wrapper);

    }
}
```

在OrderController的submit方法处理post请求实现上面的方法

```less
//用户下单
@PostMapping("/submit")
public R<String> submit(@RequestBody Orders orders){
    log.info("订单数据:{}",orders);
    orderService.submit(orders);
    return R.success("下单成功");
}
```

#### 功能测试

下单界面：
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354608.png)

下单成功界面：
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354606.png)

## 功能补充

补充教程视频中未实现的方法（纯菜鸡手打，若读者发现bug或者更好的方法，欢迎评论补充TVT,虽然肯定没人看）

### 用户登出

在UserController添加loginout方法

```typescript
//用户登出
@PostMapping("/loginout")
public R<String> loginout(HttpServletRequest request){
    //清理Session中保存的当前用户登录的id
    request.getSession().removeAttribute("user");
    return R.success("退出成功");
}
```

### 订单管理

导入OrderDto需手动添加`private int sumNum;`（前端会计算数量）

在OrderController添加userPage方法

```java
//订单管理
@Transactional
@GetMapping("/userPage")
public R<Page> userPage(int page,int pageSize){
    //构造分页构造器
    Page<Orders> pageInfo = new Page<>(page, pageSize);

    Page<OrdersDto> ordersDtoPage = new Page<>();

    //构造条件构造器
    LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

    //添加排序条件
    queryWrapper.orderByDesc(Orders::getOrderTime);

    //进行分页查询
    orderService.page(pageInfo,queryWrapper);

    //对象拷贝
    BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

    List<Orders> records=pageInfo.getRecords();

    List<OrdersDto> list = records.stream().map((item) -> {
        OrdersDto ordersDto = new OrdersDto();

        BeanUtils.copyProperties(item, ordersDto);
        Long Id = item.getId();
        //根据id查分类对象
        Orders orders = orderService.getById(Id);
        String number = orders.getNumber();
        LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderDetail::getOrderId,number);
        List<OrderDetail> orderDetailList = orderDetailService.list(lambdaQueryWrapper);
        int num=0;

        for(OrderDetail l:orderDetailList){
            num+=l.getNumber().intValue();
        }

        ordersDto.setSumNum(num);
        return ordersDto;
    }).collect(Collectors.toList());

    ordersDtoPage.setRecords(list);
    
    return R.success(ordersDtoPage);
}
```

### 再来一单

用户可以通过该方法快速再下一单

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354849.png)

在OrderController中添加again方法处理post请求

```typescript
//再来一单
@Transactional
@PostMapping("/again")
public R<String> again(@RequestBody Orders order1){
    //取得orderId
    Long id = order1.getId();
    Orders orders = orderService.getById(id);
    //设置订单号码
    long orderId = IdWorker.getId();
    orders.setId(orderId);
    //设置订单号码
    String number = String.valueOf(IdWorker.getId());
    orders.setNumber(number);
    //设置下单时间
    orders.setOrderTime(LocalDateTime.now());
    orders.setCheckoutTime(LocalDateTime.now());
    orders.setStatus(2);
    //向订单表中插入一条数据
    orderService.save(orders);
    //修改订单明细表
    LambdaQueryWrapper<OrderDetail> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.eq(OrderDetail::getOrderId,id);
    List<OrderDetail> list = orderDetailService.list(queryWrapper);
    list.stream().map((item)->{
        //订单明细表id
        long detailId = IdWorker.getId();
        //设置订单号码
        item.setOrderId(orderId);
        item.setId(detailId);
        return item;
    }).collect(Collectors.toList());

    //向订单明细表中插入多条数据
    orderDetailService.saveBatch(list);
    return R.success("再来一单");
}
```

### 管理端订单明细

在OrderController添加page方法处理get请求

```typescript
@GetMapping("/page")
public R<Page> page(int page, int pageSize, String number,String beginTime,String endTime){
    //构造分页构造器
    Page<Orders> pageInfo = new Page<>(page, pageSize);

    Page<OrdersDto> ordersDtoPage=new Page<>();
    //构造条件构造器
    LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
    //根据number进行模糊查询
    queryWrapper.like(!StringUtils.isEmpty(number),Orders::getNumber,number);
    //根据Datetime进行时间范围查询

//        log.info("开始时间：{}",beginTime);
//        log.info("结束时间：{}",endTime);
    if(beginTime!=null&&endTime!=null){
        queryWrapper.ge(Orders::getOrderTime,beginTime);
        queryWrapper.le(Orders::getOrderTime,endTime);
    }
    //添加排序条件
    queryWrapper.orderByDesc(Orders::getOrderTime);

    //进行分页查询
    orderService.page(pageInfo,queryWrapper);

    //对象拷贝
    BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

    List<Orders> records=pageInfo.getRecords();

    List<OrdersDto> list=records.stream().map((item)->{
        OrdersDto ordersDto=new OrdersDto();

        BeanUtils.copyProperties(item,ordersDto);
        String name="用户"+item.getUserId();
        ordersDto.setUserName(name);
        return ordersDto;
    }).collect(Collectors.toList());

    ordersDtoPage.setRecords(list);
    return R.success(ordersDtoPage);
}
```

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354161.png)

### 外卖订单派送

在OrderController处理post请求修改status

```java
 @PutMapping
public R<String> send(@RequestBody Orders orders){
    Long id = orders.getId();
    Integer status = orders.getStatus();
    LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.eq(Orders::getId,id);
    Orders one = orderService.getOne(queryWrapper);
    one.setStatus(status);
    orderService.updateById(one);
    return R.success("派送成功");
}
```

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/202208080354171.png)





# Linux

**为什么要学Linux ?**

- 企业用人要求
- 个人发展要求

**学习后能干什么？**
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164500769-412957756.png)

## Linux简介

### 不同应用领域的主流操作系统

- 桌面操作系统
  - Windows （用户数量最多)
  - Mac OS（操作体验好，办公人士首选)
  - Linux（用户数量少)
- 服务器操作系统
  - UNIX（安全、稳定、付费)
  - Linux（安全、稳定、免费、占有率高)
  - Windows Server (付费、占有率低)
- 移动设备操作系统
  - Android (基于Linux 、开源，主要用于智能手机、平板电脑和智能电视)
  - ios（苹果公司开发、不开源，用于苹果公司的产品，例如: iPhone、iPad )
- 嵌入式操作系统
  - Linux（机顶盒、路由器、交换机)

### Linux发展历史

Linux系统历史

- 时间:1991年
- 地点:芬兰赫尔辛基大学
- 人物:Linus Torvalds ( 21岁)
- 语言:C语言、汇编语言
- logo:企鹅
- 特点:免费、开源、多用户、多任务

### Linux系统版本

Linux系统分为内核版和发行版

- 内核版
  - 由Linus Torvalds及其团队开发、维护
  - 免费、开源
  - 负责控制硬件
- 发行版
  - 基于Linux内核版进行扩展
  - 由各个Linux厂商开发、维护
  - 有收费版本和免费版本

**Linux系统发行版**:

- ubuntu:以桌面应用为主·
- RedHat:应用最广泛、收费
- CentOs: RedHat的社区版、免费·
- opensuSE:对个人完全免费、图形界面华丽
- Fedora:功能完备、快速更新、免费
- 红旗Linux:北京中科红旗软件技术有限公司开发
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164510201-1893673796.png)

## Linux安装

### 安装方式介绍

Linux系统的安装方式

- 物理机安装:直接将操作系统安装到服务器硬件上
- 虚拟机安装:通过虚拟机软件安装

**虚拟机**( Virtual Machine)指通过**软件**模拟的具有完整硬件系统功能、运行在完全隔离环境中的完整计算机系统。

常用虚拟机软件

- VMware
- VirtualBox
- VMLite workStation
- Qemu
- HopeddotVos

### 安装Linux

**安装方式-安装VMWare**

直接双击运行资料中的VMware安装程序，根据提示完成安装即可

**安装CentOS镜像**

使用资料中提供的CentOS镜像文件来完成Linux系统的安装

### 网卡设置

由于启动服务器时未加载网卡，导致IP地址初始化失败

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164517145-1413567992.png)

修改网络初始化配置，设定网卡在系统启动时初始化
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164525095-1179631425.png)

### 安装SSH连接工具

SSH ( Secure Shell)，建立在应用层基础上的安全协议

常用的SSH连接工具

- putty
- secureCRT
- xshell
- finalshell

通过SSH连接工具就可以实现从本地连接到远程的Linux服务器

### Linux和windows目录结构对比

Linux系统中的目录

- /是所有目录的顶点
- 目录结构像一颗倒挂的树
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164532669-2029309901.png)

### Linux目录结构

- bin存放二进制可执行文件
- boot存放系统引导时使用的各种文件
- dev存放设备文件
- etc存放系统配置文件
- home存放系统用户的文件
- lib存放程序运行所需的共享库和内核模块
- opt额外安装的可选应用程序包所放置的位置
- root超级用户目录
- sbin存放二进制可执行文件，只有root用户才能访问
- tmp存放临时文件
- usr存放系统应用程序
- var存放运行时需要改变数据的文件，例如日志文件
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164538098-689217035.png)

## Linux常用命令

### Linux命令初体验

| 序号 | 命令           | 对应英文             | 作用                     |
| ---- | -------------- | -------------------- | ------------------------ |
| 1    | ls             | list                 | 查看当前目录下的内容     |
| 2    | pwd            | print work directory | 查看当前所在目录         |
| 3    | cd [目录名]    | change directory     | 切换目录                 |
| 4    | touch [文件名] | touch                | 如果文件不存在，新建文件 |
| 5    | mkdir [目录名] | make directory       | 创建目录                 |
| 6    | rm [文件名]    | remove               | 删除指定文件             |

**使用技巧**

- Tab键自动补全
- 连续两次Tab键，给出操作提示
- 使用上下箭头快速调出曾经使用过的命令
- 使用clear命令或者Ctrl+l快捷键实现清屏

**命令格式**

command 【-options】【parameter】

说明:

- command:命令名
- 【-options】:选项，可用来对命令进行控制，也可以省略
- 【parameter】:传给命令的参数，可以是零个、一个或者多个

注意:

[]代表可选

命令名、选项、参数之间有空格进行分隔

### 文件目录操作命令

#### 文件目录操作命令**ls**

作用:显示指定目录下的内容

语法:ls [-al] [dir]

说明:

- -a显示所有文件及目录(.开头的隐藏文件也会列出)
- -l除文件名称外，同时将文件型态(d表示目录，-表示文件)、权限、拥有者、文件大小等信息详细列出

注意:
由于我们使用ls命令时经常需要加入-l选项，所以Linux为ls -l命令提供了一种简写方式，即ll

#### 文件目录操作命令**cd**

作用:用于切换当前工作目录，即进入指定目录

语法:cd [dirName]

特殊说明:

- ~表示用户的home目录
- .表示目前所在的目录
- ..表示目前目录位置的上级目录

举例:

- cd ..
  切换到当前目录的上级目录
- cd ~
  切换到用户的home目录
- cd /usr/local 切换到/usr/local目录

#### 文件目录操作命令**cat**

作用:用于显示文件内容

语法: cat [-n] fileName

说明:

- -n :由1开始对所有输出的行数编号

举例:

- cat /etc/profile
  查看/etc目录下的profile文件内容

#### 文件目录操作命令**more**

作用:以分页的形式显示文件内容

语法: more fileName

操作说明:

- 回车键
  向下滚动一行
- 空格键
  向下滚动一屏
- b
  返回上一屏
- q或者Ctrl+C
  退出more

举例:

more /etc/profile
以分页方式显示/etc目录下的profile文件内容

#### 文件目录操作命令**tail**

作用:查看文件末尾的内容

语法:tail [-f] fileName

说明:

- -f :动态读取文件末尾内容并显示，通常用于日志文件的内容输出

举例:

- tail /etc/profile
  显示/etc目录下的profile文件末尾10行的内容
- tail -20 /etc/profile
  显示/etc目录下的profile文件末尾20行的内容
- tail -f /itcast/my.log
  动态读取/itcast目录下的my.log文件末尾内容并显示

#### 文件目录操作命令**mkdir**

作用:创建目录

语法:mkdir [-p] dirName

说明:

- -p︰确保目录名称存在，不存在的就创建一个。通过此选项，可以实现多层目录同时创建

举例:

- mkdir itcast　在当前目录下，建立一个名为itcast的子目录
- mkdir-p itcast/test　在工作目录下的itcast目录中建立一个名为test的子目录，若itcast目录不存在，则建立一个

#### 文件目录操作命令rmdir

作用:删除空目录

语法:rmdir [-p] dirName

说明:

- -p:当子目录被删除后使父目录为空目录的话，则一并删除

举例:

- rmdir itcast　删除名为itcast的空目录
- rmdir-p itcast/test　删除itcast目录中名为test的子目录，若test目录删除后itcast目录变为空目录，则也被删除
- rmdir itcast*　删除名称以itcast开始的空目录

#### 文件目录操作命令rm

作用:删除文件或者目录

语法:rm[-rf] name

说明:

- -r:将目录及目录中所有文件（目录）逐一删除，即递归删除
- -f:无需确认，直接删除

举例:

- rm -r itcast/删除名为itcast的目录和目录中所有文件，删除前需确认
- rm -rf itcast/无需确认，直接删除名为itcast的目录和目录中所有文件
- rm -f hello.txt无需确认，直接删除hello.txt文件

### 拷贝移动命令

#### 拷贝移动命令cp

作用:用于复制文件或目录语法:cp [-r] source dest

说明:

- -r:如果复制的是目录需要使用此选项，此时将复制该目录下所有的子目录和文件

举例:

- cp hello.txt itcast/
  将hello.txt复制到itcast目录中
- cp hello.txt . / hi.txt
  将hello.txt复制到当前目录，并改名为hi.txt
- cp -r itcast/ ./itheimal将itcast目录和目录下所有文件复制到itheima目录下
- cp -r itcast/* ./ itheima/将itcast目录下所有文件复制到itheima目录下

#### 拷贝移动命令mv

作用:为文件或目录改名、或将文件或目录移动到其它位置

语法: mv source dest

举例:

- mv hello.txt hi.txt
  将hello.txt改名为hi.txt
- mv hi.txt itheima/
  将文件hi.txt移动到itheima目录中
- mv hi.txt itheima/ hello.txt将hi.txt移动到itheima目录中，并改名为hello.txt
- mv itcast/ itheima/如果itheima目录不存在，将itcast目录改名为itheima
- mv itcast/ itheima/
  如果itheima目录存在，将itcast目录移动到itheima目录中

### 打包压缩命令tar

作用:对文件进行打包、解包、压缩、解压

语法: tar [-zcxvf] fileName [files]

包文件后缀为.tar表示只是完成了打包，并没有压缩

包文件后缀为.tar.gz表示打包的同时还进行了压缩

说明:

- -z∶ z代表的是gzip，通过gzip命令处理文件，gzip可以对文件压缩或者解压
- -c: c代表的是create，即创建新的包文件
- -x: x代表的是extract，实现从包文件中还原文件  (可以理解为将C打包好的文件，解包)
- -v: v代表的是verbose，显示命令的执行过程
- -f: f代表的是file，用于指定包文件的名称    -- 一般vf都是连着一起的

### 案例：当前root的家目录(~)有test目录，我们要将其打包

![image-20220812020938148](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812020938148.png)

#### 第一组常用命令组合：tar -cvf 对文件或目录进行打包（没有压缩），打包文件后缀为：*.tar

使用tar命令：tar -cvf （-c是打包，-v是显示执行命令过程，-f用于指定包文件名的名称） tar.tar(是打包的名称)    test(是要被打包的文件目录)

![image-20220812021027736](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812021027736.png)

#### 使用tar -zcvf  命令可以对文件进行打包且压缩--（压缩就是将文件变小，）

所以后缀名为.tar.gz : 代表这个文件是被打包且压缩了的，要用 -g命令进行解压

![image-20220812021615382](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812021615382.png)

可以查看其压缩和不压缩的文件大小区别：

![image-20220812021940958](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812021940958.png)



使用tar命令：tar -xvf + 要解包的文件名   进行解包，和C命令的打包相反，，默认解压到当前目录(也可以指定 -C)，

![image-20220812022855452](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812022855452.png)

#### （最常用组合）对文件进行解压和解包：tar -zxvf + 要解压的文件名

![image-20220812023338952](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812023338952.png)

其他例子：

第四个命令  -C 参数，可以指定解压的目录

![image-20220812023427822](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812023427822.png)

使用-C参数，解压到指定目录（/usr/local）这个目录一般是我们常用的本地目录，。 test目录是解压后的目录

![image-20220812023651012](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812023651012.png)



### 文本编辑命令vi/vim

作用: vi命令是Linux系统提供的一个文本编辑工具，可以对文件内容进行编辑，类似于Windows中的记事本

语法: vi fileName

说明:

1、 vim是从vi发展来的一个功能更加强大的文本编辑工具，在编辑文件时可以对文本内容进行着色，方便我们对文件进行编辑处理，所以实际工作中vim更加常用。

2、要使用vim命令，需要我们自己完成安装。可以使用该命令来完成安装:yum install vim

#### 文本编辑命令vim

作用:对文件内容进行编辑，vim其实就是一个文本编辑器

语法:vim fileName

说明:

1、在使用vim命令编辑文件时，如果指定的文件存在则直接打开此文件。如果指定的文件不存在则新建文件。

2、vim在进行文本编辑时共分为三种模式，分别是命令模式(Command mode)，插入模式（Insert mode)和底行模式(Last line mode)。这三种模式之间可以相互切换。我们在使用vim时一定要注意我们当前所处的是哪种模式。

针对vim中的三种模式说明如下:

1、命令模式

- 命令模式下可以查看文件内容、移动光标（上下左右箭头、gg、G)
- 通过vim命令打开文件后，默认进入命令模式
- 另外两种模式需要首先进入命令模式，才能进入彼此

2、插入模式

- 插入模式下可以对文件内容进行编辑
- 在命令模式下按下[i,a,o]任意一个，可以进入插入模式。进入插入模式后，下方会出现【insert】字样
- 在插入模式下按下ESC键，回到命令模式

3、底行模式

- 底行模式下可以通过命令对文件内容进行查找、显示行号、退出等操作
- 在命令模式下按下[:,/]任意一个，可以进入底行模式
- 通过/方式进入底行模式后，可以对文件内容进行查找
- 通过:方式进入底行模式后，可以输入wq（保存并退出）、q!(不保存退出） 、 set nu(显示行号)

### 查找命令

#### 查找命令find

作用:在指定目录下查找文件

语法:find dirName -option fileName

举例:

- find . -name "*.java"
  在当前目录及其子目录下查找.java结尾文件
- find /itcast -name "*.java"
  在/itcast目录及其子目录下查找.java结尾的文件

#### 查找命令grep

作用:从指定文件中查找指定的文本内容

语法: grep word fileName

举例:

- grep Hello Helloworld.java
  查找Helloworld.java文件中出现的Hello字符串的位置
- grep hello *.java
  查找当前目录中所有.java结尾的文件中包含hello字符串的位置

## Linux软件安装

### 软件安装方式

- 二进制发布包安装
  - 软件已经针对具体平台编译打包发布，只要解压，修改配置即可
- rpm安装
  - 软件已经按照redhat的包管理规范进行打包，==使用rpm命令进行安装，不能自行解决库依赖问题==
- yum安装
  - ==一种在线软件安装方式==，本质上还是rpm安装，自动下载安装包并安装，安装过程中自动解决库依赖问题
- 源码编译安装
  - 软件以源码工程的形式发布，需要自己编译打包

### 安装jdk

操作步骤:

1、使用FinalShell自带的上传工具将jdk的二进制发布包上传到Linux jdk-8u171-linux-×64.tar.gz

2、解压安装包，命令为tar -zxvf jdk-8u171-linux-x64.tar.gz -C/usr/local

![image-20220812040610974](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812040610974.png)

3、配置环境变量，==使用vim命令修改/etc/profile文件, 这个目录是配置环境变量的==，在文件末尾加入如下配置 *（按shift + G到文档末尾，插入）

- JAVA_HOME=/usr/local/jdk1.8.0_171
- PATH=$JAVA_HOME/bin:$PATH

4、重新加载profile文件，使更改的配置立即生效，命令为source /etc/profile

![image-20220812041207989](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812041207989.png)

5、检查安装是否成功，命令为java -version

![image-20220812041244602](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812041244602.png)

### 安装Tomcat

#### 操作步骤:

1、使用FinalShell自带的上传工具将Tomcat的二进制发布包上传到Linuxapache-tomcat-7.0.57.tar.gz

2、解压安装包，命令为tar -zxvf apache-tomcat-7.0.57.tar.gz -C/usr/local

3、==进入Tomcat的bin目录启动服务，==命令为sh startup.sh或者./startup.sh

![image-20220812042009689](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812042009948.png)

#### 验证Tomcat启动是否成功，有多种方式:

- 查看启动日志

  more /usr/local/apache-tomcat-7.0.57/logs/catalina.out（在apch-tomcat文件目录下的log目录下执行）

  tail -50 /usr/local/apache-tomcat-7.0.57/logs/catalina.out（在apch-tomcat文件目录下的log目录下执行）

- 查看进程 ps -ef | grep tomcat （ps是进程查看命令，ps  -ef查看所有进程），然后用管道符查找 tomcat关键字

- 进程id：2307

- ![image-20220812042616110](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812042616110.png)

- 在windows电脑访问linux的tomcat服务 linux_ip:8080  即可访问：（要关闭防风墙）

- 

**注意:**

- ps命令是linux下非常强大的进程查看命令，通过ps -ef可以查看当前运行的所有进程的详细信息
- “|”在Linux中称为管道符，可以将前一个命令的结果输出给后一个命令作为输入
- 使用ps命令查看进程时，经常配合管道符和查找命令grep一起使用，来查看特定进程

#### 防火墙操作:

- 查看防火墙状态(systemctl status firewalld、firewall-cmd --state)   第二个命令查看防风墙简洁一些
- 暂时关闭防火墙(systemctl stop firewalld)
- 永久关闭防火墙(systemctl disable firewalld) -- 一般开启防火，开启指定端口就行，安全
- 开启防火墙(systemctl start firewalld)
- 开放指定端口(firewall-cmd --zone=public --add-port=8080/tcp --permanent)
- 关闭指定端口(firewall-cmd --zone=public --remove-port=8080/tcp --permanent)
- 立即生效(firewall-cmd --reload) ==-- 开放端口后一定要生效==
- 查看开放的端口(firewall-cmd --zone=public --list-ports)

**注意:**

1、systemctl是管理Linux中服务的命令，可以对服务进行启动、停止、重启、查看状态等操作

2、firewall-cmd是Linux中专门用于控制防火墙的命令

3、为了保证系统安全，服务器的防火墙不建议关闭

#### 停止Tomcat服务的方式:

- 运行Tomcat的bin目录中提供的停止服务的脚本文件`shutdown.sh`

  sh shutdown.sh

  ./shutdown.sh

  ![image-20220812044331386](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812044331386.png)

- 结束Tomcat进程
  查看Tomcat进程，获得进程id
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164615590-1183764675.png)
  ==执行命令结束进程**kill -9 7742==**   -9表示强制关闭，这个进程id不是固定的

**注意:**
kill命令是Linux提供的用于结束进程的命令，-9表示强制结束

### 安装MySQL

1、检测当前系统中是否安装MySQL数据库

- rpm -qa
  查询当前系统中安装的所有软件
- rpm -qa l grep mysql
  查询当前系统中安装的名称带mysql的软件
- ![image-20220812044708566](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812044708566.png)
- rpm -qa l grep mariadb
  查询当前系统中安装的名称带mariadb的软件
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164632289-1107734166.png)

RPM（ Red-Hat Package Manager ）RPM软件包管理器，是红帽Linux用于管理和安装软件的工具

**注意**：如果当前系统中已经安装有MySQL数据库，安装将失败。**CentoS7自带mariadb，与My5QL数据库冲突**

**2、卸载已经安装的冲突软件**

- rpm -e --nodeps + 软件名称 　　　　卸载软件
- rpm -e --nodeps mariadb-libs-5.5.60-1.el7_5.x86_64

3、将资料中提供的MySQL安装包上传到Linux并解压

- mkdir /usr/local/mysql

- tar -zxvf mysql-5.7.25-1.el7.x86_64.rpm-bundle.tar.gz -C /usr/local/mysql

  ![image-20220812045153632](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812045153632.png)

**4、按照顺序安装rpm软件包（解压到的6个安装包，用rpm安装--特定的安装包）**

- rpm -ivh mysql-community-common-5.7.25-1.el7.x86_64.rpm

- rpm -ivh mysql-community-libs-5.7.25-1.el7.x86_64.rpm

- rpm -ivh mysql-community-devel-5.7.25-1.el7.x86_64.rpm

- rpm -ivh mysql-community-libs-compat-5.7.25-1.el7.x86_64.rpm

- rpm -ivh mysql-community-client-5.7.25-1.el7.x86_64.rpm（如果按照失败提示：密钥 ID 5072e1f5: NOKEY，后面加参数 --force --nodeps）

- yum install net-tools （如果提示缺少libaio依赖就执行yum install libaio命令）

- rpm -ivh mysql-community-server-5.7.25-1.el7.x86_64.rpm

  说明1:安装过程中提示缺少net-tools依赖，使用yum安装

  说明2:可以通过指令升级现有软件及系统内核

- yum update

### 例如：提示缺少镜像

 Cannot prepare internal mirrorlist: No URLs in mirrorlist，centos8可执行：

```shell
# sudo sed -i -e "s|mirrorlist=|#mirrorlist=|g" /etc/yum.repos.d/CentOS-*

# sudo sed -i -e "s|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g" /etc/yum.repos.d/CentOS-*
```

5、启动mysql

- systemctl status mysqld
  查看mysql服务状态
- systemctl start mysqld
  启动mysql服务
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164641114-1683153309.png)

说明:可以设置开机时启动mysql服务，避免每次开机启动mysql

- systemctl enable mysqld
  开机启动mysql服务
- netstat -tunlp
  查看已经启动的服务
- netstat -tunlp | grep mysql
- ps -ef | grep mysql
  查看mysql进程

6、登录MySQL数据库，查阅临时密码

- cat /var/log/mysqld.log
  查看文件内容
- cat /var/log/mysqld.log l grep password
  查看文件内容中包含password的行信息
- ![image-20220812053419527](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220812053419527.png)

7、登录MySQL，修改密码，开放访问权限

- mysql -uroot -p
  登录mysql（使用临时密码登录)

修改密码

- set global validate_password_length=4;
  设置密码长度最低位数
- set global validate_password_policy=LOW;
  设置密码安全等级低，便于密码可以修改成root
- set password = password('root');
  设置密码为root

开启访问权限

- grant all on *.* to 'root'@'%' identified by 'root';

  flush privileges;

8、测试Mysql数据库是否正常工作

- showdatabase;

### ps:  mysql初始化完成登录报错

mysql: error while loading shared libraries: libncurses.so.5: cannot open shared object file: No such file or directory

执行如下安装

sudo yum install libncurses*

### 安装lrzsz

**操作步骤:**

1、搜索lrzsz安装包，命令为 **yum list lrzsz**

2、使用yum命令在线安装，命令为**yum install lrzsz.x86_64**

**注意：** Yum(全称为Yellow dog Updater, Modified)是一个在Fedora和RedHat以及CentoS中的Shell前端软件包管理器。基于RPM包管理，能够从指定的服务器自动下载RPM包并且安装，可以自动处理依赖关系，并且一次安装所有依赖的软件包，无须繁琐地一次次下载、安装。

## 项目部署

### 手工部署项目

一、在IDEA中开发SpringBoot项目并打成jar包

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

pom文件必须加入上面代码

二、将jar包上传到Linux服务器

- mkdir /usr/local/app创建目录，将项目jar包放到此目录

三、启动Spring程序
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164657090-508266715.png)

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164651066-1125429480.png)

四、检查防火墙，确保8080端口对外开放，访问SpringBoot项目

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164701736-1278145242.png)

五、改为后台运行SpringBoot程序，并将日志输出到日志文件目前程序运行的问题

- 线上程序不会采用控制台霸屏的形式运行程序，而是将程序在后台运行（退出终端不会停止运行）
- 线上程序不会将日志输出到控制台，而是输出到日志文件，方便运维查阅信息

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164708361-1366414462.png)

六、停止SpringBoot程序
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164714373-1729045522.png)

### 通过Shell脚本自动部署项目

操作步骤:

- 1、在Linux中安装Git
- 2、使用Git克隆代码
- 3、在Linux中安装maven
- 4、编写Shell脚本（拉取代码、编译、打包、启动）
- 5、为用户授予执行Shell脚本的权限
- 6、执行Shell脚本

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164722000-1572884584.png)

#### 1、在Linux中安装Git

- yum list git
  列出git安装包
- yum install git
  在线安装git

#### 2、使用Git克隆代码

- cd /usr/local/
- git clone https://github.com/SummerW1nd/helloworld.git

#### 3、在Linux中安装maven

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164728977-1234910859.png)

![image-20220813102417228](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220813102417228.png)

#### 4、将资料中提供的Shell脚本文件复制到Linux (bootStart.sh)

- Shell脚本(shell script)，是一种Linux系统中的脚本程序。
- 使用Shell脚本编程跟JavaScript、Java编程一样，只要有一个能编写代码的文本编辑器和一个能解释执行的脚本解释器就可以了.
- 对于Shell脚本编写不作为本课程重点内容，直接使用课程资料中提供的脚本文件bootStart.sh即可。

#### 五、为用户授权

chmod（英文全拼: change mode）命令是控制用户对文件的权限的命令

Linux中的权限分为:读(r)、写(w)、执行(x)三种权限

Linux的文件调用权限分为三级︰文件所有者（Owner)，就是当前用户、用户组（Group)、其它用户(Other Users)

只有文件的所有者和超级用户可以修改文件或目录的权限

要执行Shell脚本需要有对此脚本文件的执行权限，如果没有则不能执行

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164739518-66571149.png)

chmod命令可以使用八进制数来指定权限

**举例**:

- chmod 777 bootStart.sh为所有用户授予读、写、执行权限
- chmod 755 bootStart.sh为文件拥有者授予读、写、执行权限，同组用户和其他用户授予读、执行权限
- chmod 210 bootstart.sh为文件拥有者授予写权限，同组用户授予执行权限，其他用户没有任何权限

**注意**:三位数字分别代表不同用户的权限

- 第1位表示文件拥有者的权限
- 第2位表示同组用户的权限
- 第3位表示其他用户的权限

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164747415-1856910097.png)

#### 设置静态ip

修改文件/etc/sysconfig/network-scripts/ifcfg-ens33，内容如下:

```ini
TYPE="Ethernet"
PROXY_METHOD="none"
BROWSER_ONLY="no"
BOOTPROTO="static"#使用静态IP地址，默认为dhcp
IPADDR="192.168.138.100"#设置的静态IP地址
NETMASK="255.255.255.0"#子网掩码
GATEWAY="192.168.138.2"#网关地址
DNS1="192.168.138. 2"#DNS服务器
DEFROUTE="yes"
IPV4_FAILURE_FATAL="no"
IPV6INIT="yes "
IPV6_AUTOCONF="yes"
IPV6_DEFROUTE="yes"
IPV6_FAILURE_FATAL="no"
IPV6_ADDR_GEN_MODE="stable-privacy"
NAME="ens33"
UUID="95b614cd-79b0-4755-be8d-99f1cca7271b"
DEVICE="ens33"
ONBOOT="yes"#是否开机启用
```

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220522164757410-1051230850.png)





# 瑞吉外卖开发笔记 七.1

## 缓存优化

**问题说明**

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220523173028609-449458851.png)

**用户数量多，系统访问量大频繁访问数据库，系统性能下降，用户体验差**

### 环境搭建

#### maven坐标

在项目的pom.xm1文件中导入spring data redis的maven坐标:

```xml
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### 配置文件

在项目的application.yml中加入redis相关配置:

```yaml
spring
    redis:
        host:172.17.2.94
        port: 6379
        password: root@123456
        database: 0
```

#### 配置类

在项目中加入配置类RedisConfig:

```scala
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();
        //默认的Key序列化器为: JdkSerializationRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory( connectionFactory) ;
        return redisTemplate;
    }
}
```

可以用StringRedisTemplate就不用配置类

### 缓存短信验证码

#### 实现思路

前面我们已经实现了移动端手机验证码登录，随机生成的验证码我们是保存在HttpSession中的。现在需要改造为将验证码缓存在Redis中，具体的实现思路如下:

1、在服务端UserController中注入RedisTemplate对象，用于操作Redis

```java
@Autowired
private RedisTemplate redisTemplate;
```

2、在服务端UserController的sendMsg方法中，将随机生成的验证码缓存到Redis中，并设置有效期为5分钟

```scss
redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
```

3、在服务端UserController的login方法中，从Redis中获取缓存的验证码，如果登录成功则删除Redis中的验证码

```dart
//从redis中获取保存的验证码
Object codeInSession =redisTemplate.opsForValue().get(phone);
//如果用户登录成功则删除Redis中缓存的验证码
redisTemplate.delete(phone);
```

![image-20220815015807353](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220815015807353.png)

Redis获取验证码成功，登录后，会自动删除，这个key（就是phone手机号，显示清晰，是因为在RedisTemplate类中定义了序列化器

![image-20220815020124670](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220815020124670.png)

![image-20220815015816702](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220815015816702.png)

#### redis在idea中对不同类型的Value进行操作的API：

![image-20220815022855647](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220815022855647.png)

### 缓存菜品数据

#### 实现思路

前面我们已经实现了移动端菜品查看功能，对应的服务端方法为DishController的list方法，此方法会根据前端提交的查询条件进行数据库查询操作。在高并发的情况下，频繁查询数据库会导致系统性能下降，服务端响应时间增长。现在需要对此方法进行缓存优化，提高系统的性能。

具体的实现思路如下:

1、改造DishController的list方法，先从Redis中获取菜品数据，如果有则直接返回，无需查询数据库;如果没有则查询数据库，并将查询到的菜品数据放入Redis。

```kotlin
List<DishDto> dishDtoList=null;
//动态构造Key
String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
//先从redis中获取缓存数据
dishDtoList= (List<DishDto>) redisTemplate.opsForValue().get(key);
if(dishDtoList!=null){
    //如果存在，则直接返回，无需查询数据库
    return R.success(dishDtoList);
}
...
...
...
//如果不存在，则查询数据库，并且将查询到的菜品数据添加到缓存中，设置该key，在缓存中的存活时间
redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
```

2、改造DishController的save和update方法，加入清理缓存的逻辑

```go
//清理所有菜品缓存数据
//Set keys = redisTemplate.keys("dish_*");
//redisTemplate.delete(keys);

//清理某个分类下面的菜品缓存数据
String key="dish_"+dishDto.getCategoryId()+"_"+dishDto.getStatus();
redisTemplate.delete(key);
```

**注意**：在使用缓存过程中，要注意保证数据库中的数据和缓存中的数据一致，如果数据库中的数据发生变化，需要及时清理缓存数据。

### Spring Cache

#### Spring Cache介绍

Spring cache是一个框架，实现了基于注解的缓存功能，只需要简单地加一个注解，就能实现缓存功能。

Spring Cache提供了一层抽象，底层可以切换不同的cache实现。具体就是通过CacheManager接口来统一不同的缓存技术。

CacheManager是Spring提供的各种缓存技术抽象接口。

针对不同的缓存技术需要实现不同的CacheManager:
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220523173040674-859737708.png)

#### Spring Cache常用注解

![image](https://img2022.cnblogs.com/blog/2592691/202205/2592691-20220523173043578-1055601938.png)

在spring boot项目中，使用缓存技术只需在项目中导入相关缓存技术的依赖包，并在启动类上使用@EnableCaching开启缓存支持即可。

例如，使用Redis作为缓存技术，只需要导入Spring data Redis的maven坐标即可。

#### Spring Cache使用方式

在Spring Boot项目中使用Spring Cache的操作步骤(使用redis缓存技术);

1、导入maven坐标

- spring-boot-starter-data-redis、spring-boot-starter-cache

2、配置application.yml

```yaml
spring:
    cache:
        redis:
            time-to-live: 1800000#设置缓存有效期
```

3、在启动类上加入@EnableCaching注解，开启缓存注解功能

4、在Controller的方法上加入@Cacheable、@CacheEvict等注解，进行缓存操作

### 缓存套餐数据

#### 实现思路

前面我们已经实现了移动端套餐查看功能，对应的服务端方法为SetmealController的list方法，此方法会根据前端提交的查询条件进行数据库查询操作。在高并发的情况下，频繁查询数据库会导致系统性能下降，服务端响应时间增长。现在需要对此方法进行缓存优化，提高系统的性能。

具体的实现思路如下:

1、导入Spring Cache和Redis相关maven坐标

2、在application.yml中配置缓存数据的过期时间

3、在启动类上加入@EnableCaching注解，开启缓存注解功能

4、在SetmealController的list方法上加入@Cacheable注解

5、在SetmealController的save和delete方法上加入CacheEvict注解

#### 代码改造

在pom.xml文件中导入maven坐标:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

在application.yml中配置缓存数据过期时间:

```yaml
cache:
    redis:
        time-to-live: 1800000 #设置缓存数据过期时间
```

在启动类@EnableCaching注解

在list方法上添加注解，实现在redis里添加缓存：

```kotlin
@Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
```

在update，add，delete方法上添加注解，清除缓存：

```kotlin
@CacheEvict(value = "setmealCache",allEntries = true)
```

**注意**：要让R实现Serializable接口（序列化），注解才能生效

```java
/**
 *  套餐管理
 */

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService; // 注入套餐setmealService业务层接口对象

    @Autowired
    private SetmealDishService setmealDishService; // 注入套餐setmeal和Dish菜品关系的业务层接口

    @Autowired
    private CategoryService categoryService;


    /**
     *  保存套餐和菜品数据
     * @param setmealDto
     * @return
     */
    @PostMapping
    // 新增套餐，修改套餐，删除套餐都要将缓存删除，因为数据更新了，要重新查询数据库
    @CacheEvict(value = "setmealCache",allEntries = true) // 删除这个setmealCache分类下的缓存数据
    public R<String> save(@RequestBody SetmealDto setmealDto){ // 用SetmealDto（多表合并的实体类）

        log.info("套餐信息：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");

    }



    /**
     *  套餐分页查询
     * @param page 起始页
     * @param pageSize 每页的记录条数
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
// 创建这个SetmealDto泛型的对象可以让前端显示  套餐分类这个字段了
        Page<SetmealDto> pageDtoInfo=new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        //根据name进行模糊查询
        queryWrapper.like(!StringUtils.isEmpty(name),Setmeal::getName,name);
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //进行分页查询
        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");

        List<Setmeal> records=pageInfo.getRecords(); // 得到页面记录条数属性对象records

        List<SetmealDto> list= records.stream().map((item)->{
            SetmealDto setmealDto=new SetmealDto();

            BeanUtils.copyProperties(item,setmealDto); // 将遍历出来的每条记录数赋值给setmealDto对象
            Long categoryId = item.getCategoryId();
            //根据id查分类对象
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                // 拿到套餐分类对象的名称，返回给setmealDto，
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        pageDtoInfo.setRecords(list);

        return R.success(pageDtoInfo);
    }

    /**
     *  删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
//    集合保存普通参数：请求参数名与形参集合对象名相同且请求参数为多个，@RequestParam绑定参数关系
                            // http://localhost:8080/setmeal?ids=1556324903628898306,1415580119015145474
                            // 由于请求的是一个参数中是一个集合对象的参数，这个时候形参要用@RequestParam注解
                            // 当然如果用数组接收的话，就不用@RequestParam注解了
    @CacheEvict(value = "setmealCache",allEntries = true) // 删除这个setmealCache分类下的缓存数据
    public R<String> delete(@RequestParam List<Long> ids) { // 将参数封装到list集合中，也可以封装到Long数组中去
        log.info("ids:{}",ids);

        setmealService.removeWithDish(ids); // 这个removeWithDish是mp的方法，删除对应的集合
        return R.success("套餐数据删除成功");
    }

    /**
     *  套餐停售功能
     * @param status // 接收前端传过来的状态码 点击停售，传过来 0
     * @param ids // 要删除的套餐id集合
     * @return
     */
    @PostMapping("/status/{status}") //http://localhost:8080/setmeal/status/0?ids=1415580119015145474
    public R<String> sale(@PathVariable int status,String[] ids){
        for (String id:ids){
            // 根据id拿到对应的对象
            Setmeal setmeal = setmealService.getById(id);
            // 设置状态码，值为前端传过来的值
            setmeal.setStatus(status);
            // 执行sql，更新操作
            setmealService.updateById(setmeal);
        }
        return R.success("修改成功");
    }

    /**
     * 根据Id查询套餐信息（用于套餐管理的修改功能）
     * @param id
     * @return
     */
    @GetMapping("/{id}") // http://localhost:8080/setmeal/1556324903628898306
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto setmealDto=setmealService.getByIdWithDish(id);

        return R.success(setmealDto);
    }

    /**
     *  套餐修改功能
     * @param setmealDto
     * @return
     */

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){

        setmealService.updateWithDish(setmealDto);
        return R.success("修改成功");
    }

    /**
     *  显示套餐信息（根据条件查询套餐数据
     * @param setmeal，传过来的long型categoryId参数封装成setmeal对象
     * @return
     *  // 请求的URL http://localhost:8080/setmeal/list?categoryId=1413342269393674242&status=1
     */

    @GetMapping("/list")
    // 拼成一个key
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal !=null, Setmeal::getCategoryId,setmeal.getCategoryId());
        // 查询套餐售卖状态，
        queryWrapper.eq(setmeal!=null, Setmeal::getStatus,setmeal.getStatus());
        // 设置条件：排序展示
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);

    }





}
```





# 瑞吉外卖开发笔记 七.2

笔记内容为**黑马程序员**视频内容

## 读写分离

**问题分析**
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213336962-361872906.png)
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213341745-2032654848.png)

### Mysql主从复制

#### 介绍

MysSQL主从复制是一个异步的复制过程，底层是基于Mysql数据库自带的**二进制日志**功能。就是一台或多台AysQL数据库(slave，即**从库**）从另一台MysQL数据库(master，即**主库**）进行日志的复制然后再解析日志并应用到自身，最终实现**从库**的数据和**主库**的数据保持一致。MySQL主从复制是MysQL数据库自带功能，无需借助第三方工具。

MysQL复制过程分成三步:

- master将改变记录到二进制日志（ binary log)
- slave将master的binary log拷贝到它的中继日志（relay log）
- slave重做中继日志中的事件，将改变应用到自己的数据库中(主库做一遍，从库也做一遍)

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213352219-1805015334.png)

#### 配置-前置条件

提前准备好两台服务器，分别安装Mysql并启动服务成功

- 主库Master 192.168.188.100
- 从库slave 192.168.188.101 -- 从库可以有多个

**注意**：克隆的虚拟机需要修改数据库的uuid（推荐博客）

https://blog.csdn.net/pratise/article/details/80413198

#### 配置-主库Master

第一步:修改Mysq1数据库的配置文件/etc/my.cnf

```ini
[mysqld]
log-bin=mysql-bin #[必须]启用二进制日志
server-id=100 #[必须]服务器唯一ID
```

第二步:重启Mysql服务
`systemctl restart mysqld`

第三步:登录Mysql数据库，执行下面SQL

```
GRANT REPLICATION SLAVE ON *.* to 'xiaoming'@'%' identified by 'Root@123456';
```

注:上面SQL的作用是创建一个用户**xiaoming**，密码为**Root@123456**，并且给xiaoming用户授予**REPLICATION SLAVE**权限。常用于建立复制时所需要用到的用户权限，也就是slave必须被master授权具有该权限的用户，才能通过该用户复制。

第四步:登录Mysql数据库，执行下面SQL，记录下结果中File和Position的值

```
show master status;
```

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213401569-1057043333.png)

注:上面SQL的作用是查看Master的状态，执行完此SQL后不要再执行任何操作

#### 配置-从库Slave

第一步:修改Mysq1数据库的配置文件/etc/my.cnf

```ini
[mysqld]
server-id=101 #[必须]服务器唯一ID
```

第二步:重启Mysql服务
`systemctl restart mysqld`

第三步:登录Mysq1（从库）数据库，执行下面SQL：根据具体主库的状态去修改：我的状态是这个（记得ip，改成主库的ip）

![image-20220816093756761](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816093756761.png)

```sql
change master to
master_host='192.168.67.130',master_user='xiaoming',master_password='Root@123456',master_log_file='mysql-bin.000002',master_log_pos=441;

start slave;
```

第四步:登录Mysql数据库，执行下面SQL，查看从数据库的状态`show slave status\G;` -- 100是主库，101是从库（状态是yes就行了） 
![image-20220816103710523](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816103710523.png)

主库创建itcast数据库后，从库也会跟着创建itcast

![image-20220816105959607](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816105959607.png)

#### 主从复制的作用：（查询查从库，增删改执行主库)

​	**==主库进行什么操作，从库也会跟着进行什么操作：（主库创建了一个user表，从库也会创建一个user表 -- 向表插入数据，从库也会跟着进行)==* *

![image-20220816110328914](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816110328914.png)

### 读写分离案例

#### 背景

面对日益增加的系统访问量，数据库的吞吐量面临着巨大瓶颈。对于同一时刻有大量并发读操作和较少写操作类型的应用系统来说，将数据库拆分为**主库和从库**，主库负责处理事务性的增删改操作，从库负责处理查询操作，能够有效的避免由数据更新导致的行锁，使得整个系统的查询性能得到极大的改善。
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213418223-286629127.png)

#### Sharding-JDBC介绍

Sharding-JDBC定位为轻量级Java框架，在Java的JDBC层提供的额外服务。它使用客户端直连数据库,以jar包形式提供服务，无需额外部署和依赖，可理解为增强版的JDBC驱动，完全兼容JDBC和各种ORM框架。

使用Sharding-JDBC可以在程序中轻松的实现数据库读写分离。

- 适用于任何基于JDBC的ORM框架，如: JPA, Hibernate,Mybatis, Spring JDBC Template或直接使用JDBC。
- 支持任何第三方的数据库连接池，如:DBCP，C3PO,BoneCP, Druid, HikariCP等。
- 支持任意实现JDBC规范的数据库。目前支持MySQL，Oracle,SQLServer，PostgreSQL以及任何遵循SQL92标准的数据库。

```xml
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>4.0.0-RC1</version>
</dependency>
```

#### 入门案例

使用Sharding-JDBC实现读写分离步骤:

1、导入maven坐标

2、在配置文件中配置读写分离规则

```yaml
spring:
  shardingsphere:
    datasource:
      names:
        master,slave #定义两个数据源，记得换ip
      # 主数据源
      master:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://192.168.188.100:3306/rw?characterEncoding=utf-8
        username: root
        password: 123456
      # 从数据源
      slave: #slave是奴隶的意思，这里是从库的意思，也可以定义多个从库，名字记得要换
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://192.168.188.101:3306/rw?characterEncoding=utf-8
        username: root
        password: 123456
    masterslave:
      # 读写分离配置
      load-balance-algorithm-type: round_robin #轮询
      # 最终的数据源名称
      name: dataSource
      # 主库数据源名称
      master-data-source-name: master
      # 从库数据源名称列表，多个逗号分隔
      slave-data-source-names: slave
    props:
      sql:
        show: true #开启SQL显示，默认false
```

3、在配置文件中配置**允许bean定义覆盖**配置项，原来是不允许覆盖数据源的

```yaml
spring:
    main:
        allow-bean-definition-overriding: true
```

创建了两个数据源

![image-20220816115121334](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816115121334.png)

查询走的是从库，增删改走的是主库，减轻数据库的压力

![image-20220816115546653](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816115546653.png)

这个192.168.67.130是主库的ip，进行增加操作，走主库

![image-20220816115945058](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816115945058.png)

### 项目实现读写分离

#### 环境准备(主从复制)

直接使用我们前面在虚拟机中搭建的主从复制的数据库环境即可。

在主库中创建瑞吉外卖项目的业务数据库reggie并导入相关表结构和数据。

#### 代码构造

在项目中加入Sharding-JDBC实现读写分离步骤:

1、导入maven坐标

2、在配置文件中配置读写分离规则

3、在配置文件中配置**允许bean定义覆盖**配置项

```java
server:
  port: 8080
spring:
  application:
    # 应用的名称，可选
    name: reggie_take_out
  shardingsphere:
    datasource:
      names:
        master,slave #定义两个数据源
      # 主数据源
      master:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://192.168.67.130:3306/reggie?characterEncoding=utf-8
        username: root
        password: root
      # 从数据源，也可以定义多个从库，名字记得要换
      slave:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://192.168.67.131:3306/reggie?characterEncoding=utf-8
        username: root
        password: root
    masterslave:
      # 读写分离配置
      load-balance-algorithm-type: round_robin
      # 最终的数据源名称
      name: dataSource
      # 主库数据源名称
      master-data-source-name: master
      # 从库数据源名称列表，多个逗号分隔
      slave-data-source-names: slave
    props:
      sql:
        show: true #开启SQL显示，默认false
  main:
    allow-bean-definition-overriding: true


  redis:
    host: 192.168.67.130
    port: 6379
    password: 123456
    database: 0
  cache:
    redis:
      time-to-live: 1800000 #设置缓存数据的过期时间


mybatis-plus:
  configuration:
    #在映射实体或者属性时，将数据库中表名和字段名中的下划线去掉，按照驼峰命名法映射
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: ASSIGN_ID

# 定义文件上传目录
reggie:
  path: C:\Users\aufs\UserFile\img\
```

测试成功：查询走的是从库slave：

![image-20220816125529125](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816125529125.png)

增删改走的是主库：

![image-20220816125815321](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816125815321.png)

PS：一般是创建一个新的分支，执行新的操作，然后将新的操作推送到push到远程分支，然后回到主分支main，合并刚刚操作的分支，即可

案例：刚刚我操作主从库的操作时：创建了一个新的分支v1.1，然后测试完代码后，push到远程的v1.1分支，最后切换回main分支，使用merge合并v1.1分支即可

## Nginx

### Nginx概述

Nginx是一款轻量级的web服务器/反向代理服务器及电子邮件（IMAP/POP3）代理服务器。其特点是占有内存少，并发能力强，事实上nginx的并发能力在同类型的网页服务器中表现较好，中国大陆使用nginx的网站有:百度、京东、新浪、网易、腾讯、淘宝等。

Nginx是由伊戈尔·赛索耶夫为俄罗斯访问量第二的Rambler .ru站点（俄文: Paw6nep)开发的，第一个公开版本0.1.e发布于2004年10月4日。

官网: https://nginx.org/

### Nginx下载与安装

可以到Nginx官方网站下载Nginx的安装包，地址为: https://nginx.org/en/download.html

安装过程:
1、安装依赖包yum -y install gcc pcre-devel zlib-devel openssl openssl-devel

2、下载Nginx安装包wget https://nginx.org/download/nginx-1.16.1.tar.gz(需要先yum install wget)

3、解压tar -zxvf nginx-1.16.1.tar.gz

4、cd nginx-1.16.1

5、./ configure --prefix=/usr/local/nginx

6、make && make install

### Nginx目录结构

安装完Nginx后，我们先来熟悉一下Nginx的目录结构，如下图:

tree命令：

![image-20220816161003600](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816161003600.png)

### 重点目录/文件:

- conf/nginx.conf nginx配置文件
- html
  存放静态文件(html、css、Js等)
- logs
  日志目录，存放日志文件
- sbin/nginx
  二进制文件，用于启动、停止Nginx服务

### Nginx命令

#### 查看版本

在sbin目录下输入`./nginx -v`
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213439288-1059195245.png)

#### 检查配置文件正确性

在启动Nginx服务之前，可以先检查一下conf/nginx.conf文件配置的是否有错误，命令如下:

```
./nginx -t
```

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213446774-1869088051.png)

#### 启动和停止

在sbin目录下。

启动Nginx服务使用如下命令:`./nginx`

停止Nginx服务使用如下命令:`./nginx -s stop`

启动完成后可以查看Nginx进程:`ps -ef | grep nginx`

启动后，访问的是首页(index.html)，端口号为80；

![image-20220816161526204](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816161526204.png)

访问的是index.html

![image-20220816161658290](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816161658290.png)

修改运行的进程数目：
`vim usr/local/nginx/conf/nginx.conf`

```undefined
worker_processes  2;
```

修改后记得重新加载进程：/usr/local/nginx/sbin/nginx -s reload, 可以看到两个worker进程

![image-20220816163133646](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220816163133646.png)

#### 重新加载配置文件

可以通过修改profile文件配置环境变量（配置了环境变量后，无论在哪个目录都可以使用nginx命令），在`/`目录下可以直接使用nginx命令

vim etc/profile

```bash
PATH=/usr/local/nginx/sbin:$JAVA_HOME/bin:$PATH
```

使配置文件生效：`source /etc/profile`

重启Nginx：`nginx -s reload`

停止Nginx：`nginx -s stop`

启动Nginx：`nginx`

### Nginx配置文件结构

**整体结构介绍**

Nginx配置文件(conf/nginx.conf)整体分为三部分:

- 全局块
  和Nginx运行相关的全局配置

- events块
  和网络连接相关的配置

- http块

  代理、缓存、日志记录、虚拟主机配置

  - http全局块
  - Server块
    - Server全局块
    - location块

**注意**:http块中可以配置多个Server块，每个Server块中可以配置多个location块。

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213459886-1202400981.png)

### Nginx具体应用：主要部署静态资源

#### 部署静态资源

Nginx可以作为静态web服务器来部署静态资源。静态资源指在服务端真实存在并且能够直接展示的一些文件，比如常见的html页面、css文件、js文件、图片、视频等资源。

相对于Tomcat，Nginx处理静态资源的能力更加高效，所以在生产环境下，一般都会将静态资源部署到Nginx中。

将静态资源部署到Nginx非常简单，只需要将文件复制到Nginx安装目录下的html目录中即可。，这个文件在nginx/conf/nginx.conf

```perl
server {
  listen 80;                #监听端口
  server_name localhost;    #服务器名称(域名)
  location/{                #匹配客户端请求url
    root html;              #指定静态资源根目录
    index index.html;       #指定默认首页
}
```

#### 反向代理

- 正向代理

  是一个位于客户端和原始服务器(origin server)之间的服务器，为了从原始服务器取得内容，客户端向代理发送一个请求并指定目标(原始服务器)，然后代理向原始服务器转交请求并将获得的内容返回给客户端。

  正向代理的典型用途是为在防火墙内的局域网客户端提供访问Internet的途径。

  正向代理一般是**在客户端设置代理服务器**，通过代理服务器转发请求，最终访问到目标服务器。
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213510103-1625991610.png)

- 反向代理（访问反向代理服务器，客户端什么都不用配，即可到达目标服务器）

  反向代理服务器位于用户与目标服务器之间，但是对于用户而言，反向代理服务器就相当于目标服务器，即用户直接访问反向代理服务器就可以获得目标服务器的资源，反向代理服务器负责将请求转发给目标服务器。

  用户**不需要知道目标服务器的地址**，也无须在用户端作任何设定。(正向代理用户知道配置了代理（在客户端配置），==反向代理不需要配置，直接访问反向代理服务器即可达到目标服务器 -- 在服务端配置)==

  ==**(正向代理:代理的是客服端,对客服端负责,帮助客服端访问服务器);**==

  ==**(反向代理:代理的是服务端,对服务端负责,帮助服务器提供服务)**==

  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213519321-202537398.png)

- 配置反向代理

```perl
server {
  listen       82;
  server_name  localhost;

  location / {
          proxy_pass http://192.168.188.101:8080; #反向代理配置
  } 
}
```

#### 负载均衡（反向代理原来是一台，现在面对多台）

早期的网站流量和业务功能都比较简单，单台服务器就可以满足基本需求，但是随着互联网的发展，业务流量越来越大并且业务逻辑也越来越复杂，单台服务器的性能及单点故障问题就凸显出来了，因此需要多台服务器组成应用集群，进行性能的水平扩展以及避免单点故障出现。

- 应用集群:将同一应用部署到多台机器上，组成应用集群，接收负载均衡器分发的请求，进行业务处理并返回响应数据
- 负载均衡器:将用户请求根据对应的负载均衡算法分发到应用集群中的一台服务器进行处理
  ![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213528386-1881671315.png)

**配置负载均衡**:
修改ngnix.conf

```css
upstream targetserver{    #upstream指令可以定义一组服务器
  server 192.168.188.101:8080;
  server 192.168.188.101:8081;
}

server {
  listen  8080;
  server_name     localhost;
  location / {
          proxy_pass http://targetserver;
  }
}
```

**负载均衡策略**
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213534999-1519097614.png)

## 前后端分离开发

### 问题分析

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213540228-485930618.png)

- 开发人员同时负责前端和后端代码开发，分工不明确
- 开发效率低
- 前后端代码混合在一个工程中，不便于管理
- 对开发人员要求高，人员招聘困难

### 前后端分离开发

#### 介绍

**前后端分离开发**，就是在项目开发过程中，对于前端代码的开发由专门的**前端开发人员**负责，后端代码则由**后端开发人员**负责，这样可以做到分工明确、各司其职，提高开发效率，前后端代码并行开发，可以加快项目开发进度。目前，前后端分离开发方式已经被越来越多的公司所采用，成为当前项目开发的主流开发方式。

前后端分离开发后，从工程结构上也会发生变化，即前后端代码不再混合在同一个maven工程中，而是分为**前端工程和后端工程**。
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213546687-1510083457.png)

#### 开发流程

前后端分离开发后，面临一个问题，就是前端开发人员和后端开发人员如何进行配合来共同开发一个项目?可以按照如下流程进行:
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213552773-1876530849.png)
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213555675-202531881.png)

**接口(API接口)** 就是一个http的请求地址，主要就是去定义:请求路径、请求方式、请求参数、响应数据等内容

#### 前端技术栈

开发工具

- Visual Studio Code
- hbuilder

技术框架

- nodejs
- VUE
- ElementUI
- mock
- webpack

### Yapi

#### 介绍

YApi是高效、易用、功能强大的api管理平台，旨在为开发、产品、测试人员提供更优雅的接口管理服务。可以帮助开发者轻松创建、发布、维护 API，YApi还为用户提供了优秀的交互体验，开发人员只需利用平台提供的接口数据写入工具以及简单的点击操作就可以实现接口的管理。

YApi让接口开发更简单高效，让接口的管理更具可读性、可维护性，让团队协作更合理。

源码地址: https://github.com/YMFE/yapi

要使用YApi，需要自己进行部署。

#### 使用

使用YApi可以执行下面操作

- 添加项目
- 添加分类
- 添加接口
- 编辑接口
- 查看接口
- **按照这个接口文档的标准来开发**
- ![image-20220817092046659](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220817092046659.png)

### Swagger

#### 介绍：就是让更容易看懂接口的规范，像说明书一样

**==使用Swagger你只需要按照它的规范去定义接口及接口相关的信息，再通过Swagger衍生出来的一系列项目和工具，就可以做到生成各种格式的接口文档，以及在线接口调试页面等等。==**

官网:https://swagger.io/

knife4j是为**Java MVC框架集成Swagger**生成Api文档的增强解决方案。

#### 使用方式

操作步骤:

1、导入knife4j的maven坐标

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <version>3.0.2</version>
</dependency>
```

2、导入knife4j相关配置类

WebMvcConfig

```less
@Slf4j
@Configuration
@EnableSwagger2
@EnableKnife4j
public class WebMvcConfig extends WebMvcConfigurationSupport {
  @Bean
  public Docket createRestApi() {
      //文档类型
      return new Docket(DocumentationType.SWAGGER_2)
              .apiInfo(apiInfo())
              .select()
      // 扫描controller包下的所有类，每一个方法生成一个接口
              .apis(RequestHandlerSelectors.basePackage("com.ka.reggie.controller"))
              .paths(PathSelectors.any())
              .build();
  }
  private ApiInfo apiInfo() {
      return new ApiInfoBuilder()
              .title("瑞吉外卖")
              .version("1.0")
              .description("瑞吉外卖接口文档")
              .build();
  }
}
```

![image-20220817094422164](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220817094422164.png)

3、设置静态资源，否则接口文档页面无法访问(addResourceHandlers方法)

```bash
registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
```

4、在LoginCheckFilter中设置不需要处理的请求路径

```javascript
String[] urls = new String[]{
        "/employee/login",
        "/employee/logout",
        "/backend/**",
        "/front/**",
        "/common/**",
        "/user/sendMsg",
        "/user/login",
// 不登陆也能访问Swagger
        "/doc.html", // 接口文档
        "/webjars/**",
        "/swagger-resources",
        "/v2/api-docs"
};
```

![image-20220817094625012](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220817094625012.png)

#### 常用注解

| 注解               | 说明                                                     |
| ------------------ | -------------------------------------------------------- |
| @Api               | 用在请求的类上，例如Controller，表示对类的说明           |
| @ApiModel          | 用在类上，通常是实体类，表示一个返回响应数据的信息       |
| @ApiModelProperty  | 用在属性上，描述响应类的属性                             |
| @ApiOperation      | 用在请求的方法上，说明方法的用途、作用                   |
| @ApilmplicitParams | 用在请求的方法上，表示一组参数说明                       |
| ApilmplicitParam   | 用在@ApilmplicitParams注解中，指定一个请求参数的各个方面 |

#### 具体使用：

操作代码，及相关后台文档接口展示：

```java
package com.itheima.reggie.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *  通用返回结果，服务端响应的数据最终都会封装成此对象
 * @param <T>
 */

@Data
@ApiModel("返回结果") // 实体类上用apimodel
public class R<T> implements Serializable { // 实现序列化接口，将java对象转换成字节数据

    @ApiModelProperty("编码")
    private Integer code; //编码：1成功，0和其它数字为失败

    @ApiModelProperty("错误信息")
    private String msg; //错误信息

    @ApiModelProperty("数据")
    private T data; //数据

    @ApiModelProperty("动态数据")
    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
```

![image-20220817102000963](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220817102000963.png)



```java
/**
 * 套餐管理
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")// 在controller层用Api注解
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    @ApiOperation(value = "新增套餐接口")
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto);

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "套餐分页查询接口")
    @ApiImplicitParams({ // 对一组参数进行说明
            @ApiImplicitParam(name = "page",value = "页码",required = true), // required表示参数是否为必须的
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "name",value = "套餐名称",required = false)
    })
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    @ApiOperation(value = "套餐删除接口")
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);

        setmealService.removeWithDish(ids);

        return R.success("套餐数据删除成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    @ApiOperation(value = "套餐条件查询接口")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }
}
```

![image-20220817101931222](https://typora011.oss-cn-guangzhou.aliyuncs.com/image-20220817101931222.png)



### 项目部署

#### 部署架构

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213605379-604398106.png)

#### 部署环境说明

服务器:

- 192.168.138.100（服务器A)

  Nginx:部署前端项目、配置反向代理

  Mysql:主从复制结构中的主库

  Redis:缓存中间件

- 192.168.138.101（服务器B)

  jdk:运行Java项目

  git:版本控制工具

  maven:项目构建工具

  jar: Spring Boot项目打成jar包基于内置Tomcat运行

  Mysql:主从复制结构中的从库

#### 部署前端项目

第一步:在服务器A中安装Nginx，将课程资料中的dist目录上传到Nginx的html目录下

![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213611550-883091753.png)

**第二步:修改Nginx配置文件nginx.conf ， 相当于网关服务器，反向代理到目标服务器-- 后端服务器**

```perl
server{
  listen 80;
  server_name localhost;

  location /{
    root html/dist;
    index index.html;
  }

  location ^~ /api/{
          rewrite ^/api/(.*)$ /$1 break;
          proxy_pass http://192.168.188.101:8080; // 反向代理到后端的tomacat服务器（那里部署了后端服务器）
  }

  error_page 500 502 503 504 /50x.html;
  location = /50x.html{
      root html;
  }
}
```

#### 部署后端项目

第一步∶在服务器B中安装jdk、git、maven、MySQL，使用git clone命令将git远程仓库的代码克隆下来
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213617523-2096030105.png)

第二步:将资料中提供的reggieStart.sh文件上传到服务器B，通过chmod命令设置执行权限
![image](https://typora011.oss-cn-guangzhou.aliyuncs.com/2592691-20220525213622760-1659033867.png)

第三步:执行reggieStart.sh脚本文件，自动部署项目

**注意**： 本人用该脚本从git拉取的文件不完整，运行不了，建议用idea手动打包部署

