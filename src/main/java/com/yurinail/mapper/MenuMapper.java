package com.yurinail.mapper;

import com.yurinail.domain.NailMenu;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MenuMapper {
    List<NailMenu> selectAllMenus();
    List<NailMenu> selectActiveMenus();
    NailMenu selectMenuById(Long menuId);
    void insertMenu(NailMenu menu);
    void updateMenu(NailMenu menu);
    void deleteMenu(Long menuId);
}
