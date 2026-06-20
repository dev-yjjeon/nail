package com.yurinail.mapper;

import com.yurinail.domain.ShopOffday;
import org.apache.ibatis.annotations.Mapper;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OffdayMapper {
    List<ShopOffday> selectAllOffdays();
    List<ShopOffday> selectOffdaysByDate(LocalDate offDate);
    ShopOffday selectOffdayById(Long offdayId);
    void insertOffday(ShopOffday offday);
    void updateOffday(ShopOffday offday);
    void deleteOffday(Long offdayId);
}
