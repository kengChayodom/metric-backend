package se331.metricbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se331.metricbackend.dto.UpdateMeRequest;
import se331.metricbackend.dto.UserReporter;
import se331.metricbackend.security.user.Role;
import se331.metricbackend.security.user.User;
import se331.metricbackend.security.user.UserService;
import se331.metricbackend.util.LapMapper;


import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;



    @GetMapping("users")
    public ResponseEntity<List<UserReporter>> listUsers(
            @RequestParam(value = "_limit", required = false) Integer perPage,
            @RequestParam(value = "_page", required = false) Integer page ) {

        perPage = perPage == null ? 10 : perPage;
        page = page == null ? 1 : page;
        Page<User> pageOutput;
        pageOutput = userService.getUsers(perPage , page);

        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("x-total-count", String.valueOf(pageOutput.getTotalElements()));

        return new ResponseEntity<>(LapMapper.INSTANCE.getUserReporters(pageOutput.getContent()), responseHeader , HttpStatus.OK);
    }

    @PutMapping("/users/{id}/roles")
    public ResponseEntity<UserReporter> updateUserRoles(
            @PathVariable String id,
            @RequestBody List<Role> roles,
            @RequestParam(required = false) Boolean enabled,
            @AuthenticationPrincipal User currentuser
    ){

        if (currentuser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin cannot modify own roles");
        }
        var saved = userService.updateRoles(id, roles , enabled);
        return ResponseEntity.ok(LapMapper.INSTANCE.getUserReporterDto(saved));


    }

    @GetMapping("/users/me")
    public ResponseEntity<UserReporter> getMe(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        // แนะนำให้โหลดจาก DB อีกครั้งเพื่อให้ข้อมูล (เช่น roles/enabled) เป็นปัจจุบันเสมอ
        User user = userService.findById(currentUser.getId());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return ResponseEntity.ok(LapMapper.INSTANCE.getUserReporterDto(user));
    }

    @PutMapping("/users/me")
    public ResponseEntity<UserReporter> updateMe(
            @AuthenticationPrincipal User currentUser,
            @RequestBody UpdateMeRequest req
    ) {
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        User user = userService.findById(currentUser.getId());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // อัปเดตเฉพาะฟิลด์ที่อนุญาต
        if (req.getFirstname() != null) user.setFirstname(req.getFirstname());
        if (req.getLastname() != null) user.setLastname(req.getLastname());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getProfileImage() != null) user.setProfileImage(req.getProfileImage());

        // ✅ ถ้ามีรหัสผ่านใหม่
        if (req.getNewPassword() != null && !req.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        }

        User updated = userService.save(user);
        return ResponseEntity.ok(LapMapper.INSTANCE.getUserReporterDto(updated));
    }



}
