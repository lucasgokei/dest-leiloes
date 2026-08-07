package com.destleiloes.adapter.out.bootstrap;

import com.destleiloes.application.port.out.AuctionRepositoryPort;
import com.destleiloes.application.port.out.PasswordHasherPort;
import com.destleiloes.application.port.out.UserRepositoryPort;
import com.destleiloes.domain.model.Auction;
import com.destleiloes.domain.model.AuctionCategory;
import com.destleiloes.domain.model.Role;
import com.destleiloes.domain.model.User;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Recria os mesmos dados de demonstração do antigo prisma/seed.ts. */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepositoryPort userRepository;
    private final AuctionRepositoryPort auctionRepository;
    private final PasswordHasherPort passwordHasher;
    private final boolean enabled;

    public DataSeeder(
            UserRepositoryPort userRepository,
            AuctionRepositoryPort auctionRepository,
            PasswordHasherPort passwordHasher,
            @Value("${app.seed.enabled}") boolean enabled) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.passwordHasher = passwordHasher;
        this.enabled = enabled;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled) {
            return;
        }

        userRepository
                .findByEmail("admin@destleiloes.app")
                .orElseGet(
                        () ->
                                userRepository.save(
                                        newUser("Administrador", "admin@destleiloes.app", "admin1234", Role.ADMIN)));

        User seller =
                userRepository
                        .findByEmail("vendedor@destleiloes.app")
                        .orElseGet(
                                () ->
                                        userRepository.save(
                                                newUser("Loja Demo", "vendedor@destleiloes.app", "vendedor1234", Role.USER)));

        if (auctionRepository.findBySellerIdOrderByCreatedAtDesc(seller.getId()).isEmpty()) {
            createDemoAuction(
                    seller,
                    "Relógio de pulso vintage",
                    "Relógio mecânico dos anos 70, revisado e funcionando perfeitamente.",
                    new BigDecimal("250"),
                    Duration.ofMinutes(30),
                    AuctionCategory.OTHERS);
            createDemoAuction(
                    seller,
                    "Guitarra elétrica Stratocaster",
                    "Guitarra usada, poucos riscos, ótimo estado de conservação.",
                    new BigDecimal("1800"),
                    Duration.ofHours(2),
                    AuctionCategory.OTHERS);
            createDemoAuction(
                    seller,
                    "Bicicleta speed usada",
                    "Quadro de alumínio, 21 marchas, revisada recentemente.",
                    new BigDecimal("900"),
                    Duration.ofMinutes(5),
                    AuctionCategory.VEHICLES);
            createDemoAuction(
                    seller,
                    "Sedan Toyota Corolla 2018",
                    "Único dono, revisões em dia, pneus novos, sem sinistro.",
                    new BigDecimal("68000"),
                    Duration.ofHours(6),
                    AuctionCategory.VEHICLES);
            createDemoAuction(
                    seller,
                    "Apartamento 2 quartos - Centro",
                    "60m², vaga de garagem, próximo ao metrô, pronto para morar.",
                    new BigDecimal("180000"),
                    Duration.ofDays(3),
                    AuctionCategory.PROPERTIES);
            createDemoAuction(
                    seller,
                    "Notebook gamer RTX",
                    "16GB RAM, SSD 1TB, placa de vídeo dedicada, pouco uso.",
                    new BigDecimal("4200"),
                    Duration.ofHours(12),
                    AuctionCategory.ELECTRONICS);
        }

        log.info(
                "Seed concluído. Admin: admin@destleiloes.app / admin1234 — Vendedor: vendedor@destleiloes.app / vendedor1234");
    }

    private User newUser(String name, String email, String rawPassword, Role role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordHasher.encode(rawPassword));
        user.setRole(role);
        return user;
    }

    private void createDemoAuction(
            User seller,
            String title,
            String description,
            BigDecimal price,
            Duration duration,
            AuctionCategory category) {
        Instant now = Instant.now();
        Auction auction = new Auction();
        auction.setTitle(title);
        auction.setDescription(description);
        auction.setStartingPrice(price);
        auction.setCurrentPrice(price);
        auction.setStartsAt(now);
        auction.setEndsAt(now.plus(duration));
        auction.setSellerId(seller.getId());
        auction.setCategory(category);
        auctionRepository.save(auction);
    }
}
