package com.destleiloes.application.service;

import com.destleiloes.application.port.in.admin.AdminAuctionView;
import com.destleiloes.application.port.in.admin.AdminCancelAuctionUseCase;
import com.destleiloes.application.port.in.admin.AdminDeleteAuctionUseCase;
import com.destleiloes.application.port.in.admin.AdminDeleteUserUseCase;
import com.destleiloes.application.port.in.admin.AdminListAuctionsUseCase;
import com.destleiloes.application.port.in.admin.AdminListUsersUseCase;
import com.destleiloes.application.port.in.admin.AdminSetUserRoleUseCase;
import com.destleiloes.application.port.in.admin.AdminUserView;
import com.destleiloes.application.port.out.AuctionEventPublisherPort;
import com.destleiloes.application.port.out.AuctionRepositoryPort;
import com.destleiloes.application.port.out.BidRepositoryPort;
import com.destleiloes.application.port.out.UserRepositoryPort;
import com.destleiloes.domain.exception.BadRequestException;
import com.destleiloes.domain.exception.NotFoundException;
import com.destleiloes.domain.model.Auction;
import com.destleiloes.domain.model.AuctionStatus;
import com.destleiloes.domain.model.Role;
import com.destleiloes.domain.model.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService
        implements AdminListAuctionsUseCase,
                AdminCancelAuctionUseCase,
                AdminDeleteAuctionUseCase,
                AdminListUsersUseCase,
                AdminSetUserRoleUseCase,
                AdminDeleteUserUseCase {

    private final AuctionRepositoryPort auctionRepository;
    private final BidRepositoryPort bidRepository;
    private final UserRepositoryPort userRepository;
    private final AuctionEventPublisherPort eventPublisher;

    public AdminService(
            AuctionRepositoryPort auctionRepository,
            BidRepositoryPort bidRepository,
            UserRepositoryPort userRepository,
            AuctionEventPublisherPort eventPublisher) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminAuctionView> listAuctions() {
        List<Auction> auctions = auctionRepository.findAllByOrderByCreatedAtDesc();
        if (auctions.isEmpty()) {
            return List.of();
        }
        List<String> auctionIds = auctions.stream().map(Auction::getId).toList();
        Map<String, Long> bidCounts = bidRepository.countByAuctionIds(auctionIds);
        Map<String, User> sellersById = usersById(auctions.stream().map(Auction::getSellerId).toList());
        return auctions.stream()
                .map(a -> new AdminAuctionView(
                        a.getId(),
                        a.getTitle(),
                        sellersById.get(a.getSellerId()).getName(),
                        a.getStatus(),
                        a.getCategory(),
                        a.getCurrentPrice(),
                        bidCounts.getOrDefault(a.getId(), 0L),
                        a.getEndsAt()))
                .toList();
    }

    @Override
    @Transactional
    public void cancel(String auctionId) {
        Auction auction = findAuctionOrThrow(auctionId);
        auction.setStatus(AuctionStatus.CANCELLED);
        auctionRepository.save(auction);
        eventPublisher.publishCancelled(auctionId);
    }

    @Override
    @Transactional
    public void delete(String auctionId) {
        findAuctionOrThrow(auctionId);
        auctionRepository.deleteById(auctionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserView> listUsers() {
        List<User> users = userRepository.findAllByOrderByCreatedAtDesc();
        if (users.isEmpty()) {
            return List.of();
        }
        List<String> ids = users.stream().map(User::getId).toList();
        Map<String, Long> auctionCounts = auctionRepository.countBySellerIds(ids);
        Map<String, Long> bidCounts = bidRepository.countByBidderIds(ids);
        return users.stream()
                .map(u -> new AdminUserView(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole(),
                        u.getCreatedAt(),
                        auctionCounts.getOrDefault(u.getId(), 0L),
                        bidCounts.getOrDefault(u.getId(), 0L)))
                .toList();
    }

    @Override
    @Transactional
    public void setRole(String adminId, String targetUserId, Role role) {
        if (adminId.equals(targetUserId) && role != Role.ADMIN) {
            throw new BadRequestException("Você não pode remover seu próprio acesso de administrador.");
        }
        User user = findUserOrThrow(targetUserId);
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String adminId, String targetUserId) {
        if (adminId.equals(targetUserId)) {
            throw new BadRequestException("Você não pode excluir a própria conta.");
        }
        findUserOrThrow(targetUserId);
        userRepository.deleteById(targetUserId);
    }

    private Auction findAuctionOrThrow(String id) {
        return auctionRepository.findById(id).orElseThrow(() -> new NotFoundException("Leilão não encontrado."));
    }

    private User findUserOrThrow(String id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    private Map<String, User> usersById(List<String> ids) {
        return userRepository.findAllById(ids.stream().distinct().toList()).stream()
                .collect(Collectors.toMap(User::getId, u -> u));
    }
}
