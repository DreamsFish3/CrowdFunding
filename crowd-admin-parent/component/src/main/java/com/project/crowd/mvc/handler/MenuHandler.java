package com.project.crowd.mvc.handler;

import com.project.crowd.entity.Menu;
import com.project.crowd.service.api.MenuService;
import com.project.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 */
//@Controller
//@ResponseBody
@RestController
public class MenuHandler {

    @Autowired
    private MenuService menuService;

    /**
     * 因为使用了循环嵌套的方式，如果数据很多，就会造成循环次数成比增加，服务器负荷过大
     * @return
     */
    /*public ResultEntity<Menu> getWholeTreeOld() {

        // 调用service获取所有菜单对象集合
        List<Menu> menuList = menuService.getAll();

        // 声明一个变量用来存储找到的根节点
        Menu root = null;

        // 遍历对象集合
        for (Menu menu : menuList) {

            // 获取当前menu对象的pid属性值
            Integer pid = menu.getPid();

            // 检查pid是否为null，为null就表示当前对象为根节点
            if (pid == null) {

                // 把当前遍历到的menu对象赋值给root
                root = menu;

                // 停止本次循环，继续下次循环
                continue;
            }

            // 如果pid不为null，说明当前节点有父节点，找到父节点，建立父子关系组合
            for (Menu mFather : menuList) {

                // 获取当前mFather对象的id属性
                Integer id = mFather.getId();

                // 因为每遍历一个对象就又循环一次对象集合，
                // 所以一旦找到id值跟当前对象的pid一致，就表示循环到的对象是该对象的父节点
                if (Objects.equals(pid, id)) {

                    // 将该子节点对象存入到循环到的父节点对象的children集合中
                    mFather.getChildren().add(menu);

                    // 因为一个子节点只有一个父节点，所以可以停止这次循环,回到上一层的循环继续
                    break;
                }

            }

        }

        // 把组合好的树形结构的根节点对象返回
        return ResultEntity.successWithData(root);
    }*/

    /**
     * 获取菜单数据，并且根据父子关系的树形结构返回数据
     *
     * @return
     */
    //@ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTree() {

        // 获取所有的Menu对象
        List<Menu> menuList = menuService.getAll();

        // 声明一个变量用来存储找到的根节点
        Menu root = null;

        // 创建Map对象用来存储id和Menu对象的对应关系便于查找父节点
        Map<Integer, Menu> menuMap = new HashMap<>();

        // 遍历对象集合，把对象对应id和对象填充到map集合中
        for (Menu menu : menuList) {

            Integer id = menu.getId();

            menuMap.put(id, menu);
        }

        // 再次遍历对象集合
        for (Menu menu : menuList) {

            // 获取对象的pid属性
            Integer pid = menu.getPid();

            // 如果pid为null表示该对象为根节点
            if (pid == null) {
                // 赋值
                root = menu;

                // 因为该对象为根节点，没有父节点了，后续操作无需执行，退出本次循环，继续下次循环
                continue;
            }

            // 如果pid不为null，根据pid在map集合中找到该对象的父节点
            Menu father = menuMap.get(pid);

            // 把子节点存入到父节点的children集合中
            father.getChildren().add(menu);
        }

        // 把组合好的树形结构的根节点对象返回
        return ResultEntity.successWithData(root);
    }


    /**
     * 添加子节点
     * @param menu
     * @return
     */
    //@ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu) {

        menuService.saveMenu(menu);

        return ResultEntity.successWithoutData();
    }

    /**
     * 更新节点
     * @param menu
     * @return
     */
    //@ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu) {

        menuService.updateMenu(menu);

        return ResultEntity.successWithoutData();
    }

    /**
     * 删除节点
     * @param id
     * @return
     */
    //@ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id) {

        menuService.remove(id);

        return ResultEntity.successWithoutData();
    }
}
