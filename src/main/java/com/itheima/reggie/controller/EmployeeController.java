package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    /**
     *  员工登录
     */

    // 自动装配业务层，实现表现层调用业务层
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    //使用@RequestBody注解将外部传递的json数组数据映射到形参的employee对象中作为数据
    // 使用HttpServletRequest类的request对象，可以将获取到的employee中的id，存到session
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        // 1. 将页面提交的密码password进行m5加密处理
        String password = employee.getPassword();
        // getBytes()，可以将字符串类型的数据，转换为字节数组(相当于输入流，被内存读取）
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名来查数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // eq()： 相当于 =,对应的sql语句为：
//        例： lqw.eq(User::getName, "Jerry").eq(User::getPassword, "jerry");
        // SELECT id,name,password,age,tel FROM user WHERE (name = ? AND password = ?)
        // queryWrapper的中文意思是查询包装器，这里相当于获取查询条件，将获取的username作为条件，放入查询包装器对象中
        // 第一个参数相当于是获取Employee对应的Username字段，第二个值是请求体中的username的值
        queryWrapper.eq(Employee::getUsername, employee.getUsername());// 这里相当于获取实体类的username
        // 这里根据查询包装器的条件 来得到对象
        // 这里相当于用业务层调用数据层的方法，查询条件的sql是queryWrapper，
        Employee emp = employeeService.getOne(queryWrapper);// 使用业务层：根据实体类的username属性值，来获取Employee对象


        // 3. 如果没有查询到则返回登录失败结果
        if (emp == null) {
            // 这个 error方法，会返回有个error对象
            return R.error("登录失败");
        }

        // 4. 密码比对，如果不一致则返回登录失败结果
        // 这个！是非的意思，比对不成功
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        // 5. 查看员工状态，如果已为禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){

            return R.error("账号已禁用");

        }

        // 6. 登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp); // 传给参数是传入R对象，返回给前端
    }

    /**
     * 员工退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 清理Session中保存的当前登录员工的Id ，参数写对应的属性名即可
        request.getSession().removeAttribute("employee");//
        return R.success("退出成功");
    }

    /**
     * 添加员工
     * @param request
     * @param employee
     * @return
     */

    @PostMapping// 这个路径不用写，因为在类上面写了 “/employee”
    // 由于前端传递过来的数据是json格式到Controller表现层，然后要获取这个参数要使用@RequestBody注解
//    由于只要返回一个成功的信息给前端，所以R泛型为String
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        // 使用实体类的toString方法，打印对象内容
        log.info("新增员工，员工信息：{}",employee.toString());

        // 设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("456789".getBytes())); // 转成字节类型的值，可以给计算机读

        // 这里的公共字段使用自动填充，在实体类中设置（插入时填充）
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        // 获取当前登录用户的id，从session中获取，所以方法用要加形参HttpServletRequest
//        Long empId = (Long) request.getSession().getAttribute("employee");

//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee); // 这个save方法继承了Iservice接口（使得业务层快速开发)，
                                        // 这个save方法会调用其数据层的方法，添加到数据库中（这一套都是mp提供的）

        return R.success("新增员工成功");
    }

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

    /**
     *  根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping // 这里的url就不用写了，因为在类上面写了employee这个请求路径了
            // @RequestBody接收请求体中的json数据，传递给形参
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString()); // log.info里面只能传入string类型的值，所以用toString转换

        // 如果执行修改操作，会走这个线程，获取线程id
        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);


        // 这里公共字段使用字段填充，在实体类中设置（修改时填充）
        // 手动设置修改日期和修改人（因为前端没有这个操作）
//        employee.setUpdateTime(LocalDateTime.now());
        // 修改人要从session中获取, 从Session中获取的是一个员工id（唯一标识）
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId);
        // 调用业务层修改数据库中的字段
        employeeService.updateById(employee);// 该employee对象里面中有属性id，可以修改


        return R.success("员工修改成功");
    }


    /**
     *  根据id查询员工信息
     * @param id
     * @return
     */

    @GetMapping("/{id}") // 这是接收前端的请求路径（少了id属性路径）http://localhost:8080/employee/1554938060526084098的Id
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



}
