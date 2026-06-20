package com.yurinail.service;

import com.yurinail.domain.NailMenu;
import com.yurinail.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuMapper menuMapper;

    public List<NailMenu> selectAllMenus() {
        return menuMapper.selectAllMenus();
    }

    public List<NailMenu> selectActiveMenus() {
        return menuMapper.selectActiveMenus();
    }

    public NailMenu selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    public void insertMenu(NailMenu menu) {
        menuMapper.insertMenu(menu);
    }

    public void updateMenu(NailMenu menu) {
        menuMapper.updateMenu(menu);
    }

    public void deleteMenu(Long menuId) {
        menuMapper.deleteMenu(menuId);
    }
}
