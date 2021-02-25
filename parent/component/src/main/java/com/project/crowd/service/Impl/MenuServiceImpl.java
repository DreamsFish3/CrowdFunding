package com.project.crowd.service.Impl;

import com.project.crowd.entity.Menu;
import com.project.crowd.entity.MenuExample;
import com.project.crowd.mapper.MenuMapper;
import com.project.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAll() {
        return menuMapper.selectByExample(new MenuExample());
    }

    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void updateMenu(Menu menu) {

        // 由于pid属性没有修改，所以选择有选择的更新函数，保证pid不被置空
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    public void remove(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }
}
