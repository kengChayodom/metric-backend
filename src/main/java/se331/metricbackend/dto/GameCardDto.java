package se331.metricbackend.dto;

public record GameCardDto(
        String id,
        String title,
        Double price,
        Double promotionPrice,
        String mainImageUrl,
        String iconGameUrl,
        String description
) {}

