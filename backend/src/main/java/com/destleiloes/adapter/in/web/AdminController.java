package com.destleiloes.adapter.in.web;

import com.destleiloes.adapter.in.web.dto.request.SetRoleRequest;
import com.destleiloes.adapter.in.web.dto.response.AdminAuctionResponse;
import com.destleiloes.adapter.in.web.dto.response.AdminUserResponse;
import com.destleiloes.adapter.in.web.security.AuthenticatedUser;
import com.destleiloes.application.port.in.admin.AdminCancelAuctionUseCase;
import com.destleiloes.application.port.in.admin.AdminDeleteAuctionUseCase;
import com.destleiloes.application.port.in.admin.AdminDeleteUserUseCase;
import com.destleiloes.application.port.in.admin.AdminListAuctionsUseCase;
import com.destleiloes.application.port.in.admin.AdminListUsersUseCase;
import com.destleiloes.application.port.in.admin.AdminSetUserRoleUseCase;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminListAuctionsUseCase adminListAuctionsUseCase;
    private final AdminCancelAuctionUseCase adminCancelAuctionUseCase;
    private final AdminDeleteAuctionUseCase adminDeleteAuctionUseCase;
    private final AdminListUsersUseCase adminListUsersUseCase;
    private final AdminSetUserRoleUseCase adminSetUserRoleUseCase;
    private final AdminDeleteUserUseCase adminDeleteUserUseCase;

    public AdminController(
            AdminListAuctionsUseCase adminListAuctionsUseCase,
            AdminCancelAuctionUseCase adminCancelAuctionUseCase,
            AdminDeleteAuctionUseCase adminDeleteAuctionUseCase,
            AdminListUsersUseCase adminListUsersUseCase,
            AdminSetUserRoleUseCase adminSetUserRoleUseCase,
            AdminDeleteUserUseCase adminDeleteUserUseCase) {
        this.adminListAuctionsUseCase = adminListAuctionsUseCase;
        this.adminCancelAuctionUseCase = adminCancelAuctionUseCase;
        this.adminDeleteAuctionUseCase = adminDeleteAuctionUseCase;
        this.adminListUsersUseCase = adminListUsersUseCase;
        this.adminSetUserRoleUseCase = adminSetUserRoleUseCase;
        this.adminDeleteUserUseCase = adminDeleteUserUseCase;
    }

    @GetMapping("/auctions")
    public List<AdminAuctionResponse> listAuctions() {
        return adminListAuctionsUseCase.listAuctions().stream().map(AdminAuctionResponse::from).toList();
    }

    @PostMapping("/auctions/{id}/cancel")
    public void cancelAuction(@PathVariable String id) {
        adminCancelAuctionUseCase.cancel(id);
    }

    @DeleteMapping("/auctions/{id}")
    public void deleteAuction(@PathVariable String id) {
        adminDeleteAuctionUseCase.delete(id);
    }

    @GetMapping("/users")
    public List<AdminUserResponse> listUsers() {
        return adminListUsersUseCase.listUsers().stream().map(AdminUserResponse::from).toList();
    }

    @PatchMapping("/users/{id}/role")
    public void setUserRole(
            @AuthenticationPrincipal AuthenticatedUser principal,
            @PathVariable String id,
            @Valid @RequestBody SetRoleRequest request) {
        adminSetUserRoleUseCase.setRole(principal.userId(), id, request.role());
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@AuthenticationPrincipal AuthenticatedUser principal, @PathVariable String id) {
        adminDeleteUserUseCase.deleteUser(principal.userId(), id);
    }
}
