package com.yurinail.controller;

import com.yurinail.domain.NailMenu;
import com.yurinail.domain.Reservation;
import com.yurinail.domain.ShopOffday;
import com.yurinail.service.MenuService;
import com.yurinail.service.OffdayService;
import com.yurinail.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ReservationService reservationService;
    private final MenuService menuService;
    private final OffdayService offdayService;

    // 세션 검증 헬퍼
    private boolean isAdmin(HttpSession session) {
        return session.getAttribute("admin") != null;
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        if (isAdmin(session)) {
            return "redirect:/admin/dashboard";
        }
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        if ("admin".equals(username) && "admin1234".equals(password)) {
            session.setAttribute("admin", "admin");
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        return "admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:/admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpSession session,
            Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        List<Reservation> reservations = reservationService.getReservationsByDate(targetDate);
        List<NailMenu> menus = menuService.selectAllMenus();

        model.addAttribute("reservations", reservations);
        model.addAttribute("menus", menus);
        model.addAttribute("currentDate", targetDate);
        return "admin/dashboard";
    }

    @PostMapping("/reservations/approve/{id}")
    public String approveReservation(
            @PathVariable("id") Long id,
            @RequestParam("date") String dateStr,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        reservationService.approveReservation(id);
        return "redirect:/admin/dashboard?date=" + dateStr;
    }

    @PostMapping("/reservations/cancel/{id}")
    public String cancelReservation(
            @PathVariable("id") Long id,
            @RequestParam("date") String dateStr,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        reservationService.cancelReservation(id);
        return "redirect:/admin/dashboard?date=" + dateStr;
    }

    // 메뉴 관리
    @GetMapping("/menu")
    public String menuList(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("menus", menuService.selectAllMenus());
        return "admin/menu";
    }

    @PostMapping("/menu/add")
    public String addMenu(NailMenu menu, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        if (menu.getUseYn() == null || menu.getUseYn().isEmpty()) {
            menu.setUseYn("Y");
        }
        menuService.insertMenu(menu);
        return "redirect:/admin/menu";
    }

    @PostMapping("/menu/update")
    public String updateMenu(NailMenu menu, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        menuService.updateMenu(menu);
        return "redirect:/admin/menu";
    }

    @PostMapping("/menu/delete/{id}")
    public String deleteMenu(@PathVariable("id") Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        menuService.deleteMenu(id);
        return "redirect:/admin/menu";
    }

    // 휴무일 관리
    @GetMapping("/offday")
    public String offdayList(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("offdays", offdayService.selectAllOffdays());
        return "admin/offday";
    }

    @PostMapping("/offday/add")
    public String addOffday(
            @RequestParam("offDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate offDate,
            @RequestParam(value = "startTime", required = false) String startTimeStr,
            @RequestParam(value = "endTime", required = false) String endTimeStr,
            @RequestParam("offReason") String offReason,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        ShopOffday offday = new ShopOffday();
        offday.setOffDate(offDate);
        offday.setOffReason(offReason);
        if (startTimeStr != null && !startTimeStr.isEmpty()) {
            offday.setStartTime(java.time.LocalTime.parse(startTimeStr));
        }
        if (endTimeStr != null && !endTimeStr.isEmpty()) {
            offday.setEndTime(java.time.LocalTime.parse(endTimeStr));
        }

        offdayService.insertOffday(offday);
        return "redirect:/admin/offday";
    }

    @PostMapping("/offday/update")
    public String updateOffday(
            @RequestParam("offdayId") Long offdayId,
            @RequestParam("offDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate offDate,
            @RequestParam(value = "startTime", required = false) String startTimeStr,
            @RequestParam(value = "endTime", required = false) String endTimeStr,
            @RequestParam("offReason") String offReason,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        ShopOffday offday = offdayService.selectOffdayById(offdayId);
        if (offday != null) {
            offday.setOffDate(offDate);
            offday.setOffReason(offReason);
            if (startTimeStr != null && !startTimeStr.isEmpty()) {
                offday.setStartTime(java.time.LocalTime.parse(startTimeStr));
            } else {
                offday.setStartTime(null);
            }
            if (endTimeStr != null && !endTimeStr.isEmpty()) {
                offday.setEndTime(java.time.LocalTime.parse(endTimeStr));
            } else {
                offday.setEndTime(null);
            }
            offdayService.updateOffday(offday);
        }
        return "redirect:/admin/offday";
    }

    @PostMapping("/offday/delete/{id}")
    public String deleteOffday(@PathVariable("id") Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        offdayService.deleteOffday(id);
        return "redirect:/admin/offday";
    }
}
