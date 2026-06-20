package com.yurinail.controller;

import com.yurinail.domain.Reservation;
import com.yurinail.dto.SlotDto;
import com.yurinail.service.ReservationService;
import com.yurinail.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final ReservationService reservationService;
    private final MenuService menuService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("menus", menuService.selectActiveMenus());
        return "customer/index";
    }

    @GetMapping("/confirm")
    public String confirm() {
        return "customer/confirm";
    }

    @GetMapping("/api/reservations/slots")
    @ResponseBody
    public List<SlotDto> getSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("menuId") Long menuId) {
        return reservationService.getAvailableSlots(date, menuId);
    }

    @PostMapping("/api/reservations")
    @ResponseBody
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            reservationService.createReservation(reservation);
            return ResponseEntity.ok(Map.of("message", "예약이 신청되었습니다."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("예약 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/api/reservations/query")
    @ResponseBody
    public List<Reservation> queryReservations(@RequestBody Map<String, String> params) {
        String name = params.get("customerName");
        String phone = params.get("customerPhone");
        String password = params.get("password");
        return reservationService.getReservations(name, phone, password);
    }

    @PostMapping("/api/reservations/cancel/{id}")
    @ResponseBody
    public ResponseEntity<?> cancelReservation(@PathVariable("id") Long id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok(Map.of("message", "예약이 취소되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
