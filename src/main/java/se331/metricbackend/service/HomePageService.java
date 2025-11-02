package se331.metricbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se331.metricbackend.dto.GameCardDto;
import se331.metricbackend.dto.HomePageResponse;
import se331.metricbackend.entity.Game;
import se331.metricbackend.repository.GameRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final GameRepository gameRepository;
    private final CartService cartService; // สมมติเรามี service นับ cart

    

    public HomePageResponse getHomePage() {
        List<GameCardDto> featured = gameRepository
                .findByHomepagePositionNotNullOrderByHomepagePositionAsc()
                .stream()
                .map(this::toCard)
                .toList();

        List<GameCardDto> newRelease = gameRepository
                .findByNewReleasePositionNotNullOrderByNewReleasePositionAsc()
                .stream()
                .map(this::toCard)
                .toList();

        return new HomePageResponse(featured, newRelease); // itemCount = 0, ไม่สน user
    }


    private GameCardDto toCard(Game g) {
        return new GameCardDto(
                g.getId(),
                g.getTitle(),
                g.getPrice(),
                g.getPromotionPrice(),
                g.getMainImageUrl(),
                g.getIcongameUrl(),
                g.getDescription()
        );
    }
}

