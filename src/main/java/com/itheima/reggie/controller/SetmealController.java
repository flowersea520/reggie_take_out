package com.itheima.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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





}
