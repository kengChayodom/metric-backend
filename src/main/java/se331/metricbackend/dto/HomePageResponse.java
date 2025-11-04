package se331.metricbackend.dto;

import java.util.List;

public record HomePageResponse(
//        int itemCount,
        List<GameCardDto> featuredGames,
        List<GameCardDto> newReleaseGames
) {}

