package com.yurinail.service;

import com.yurinail.domain.ShopOffday;
import com.yurinail.mapper.OffdayMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OffdayService {

    private final OffdayMapper offdayMapper;

    public List<ShopOffday> selectAllOffdays() {
        return offdayMapper.selectAllOffdays();
    }

    public List<ShopOffday> selectOffdaysByDate(LocalDate offDate) {
        return offdayMapper.selectOffdaysByDate(offDate);
    }

    public ShopOffday selectOffdayById(Long offdayId) {
        return offdayMapper.selectOffdayById(offdayId);
    }

    public void insertOffday(ShopOffday offday) {
        offdayMapper.insertOffday(offday);
    }

    public void updateOffday(ShopOffday offday) {
        offdayMapper.updateOffday(offday);
    }

    public void deleteOffday(Long offdayId) {
        offdayMapper.deleteOffday(offdayId);
    }
}
