package com.project.crowd.service.api;

import com.project.crowd.entity.Menu;

import java.util.List;

/**
 * @description:
 */
public interface MenuService {

    List<Menu> getAll();

    void saveMenu(Menu menu);

    void updateMenu(Menu menu);

    void remove(Integer id);
}
