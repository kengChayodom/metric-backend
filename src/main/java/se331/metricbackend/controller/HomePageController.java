package se331.metricbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import se331.metricbackend.dto.HomePageResponse;
import se331.metricbackend.service.HomePageService;

@RestController
@RequiredArgsConstructor
public class HomePageController {

    private final HomePageService homePageService;

    @GetMapping("/homePage")
    public HomePageResponse getHomePage() {
        return homePageService.getHomePage();
    }
}

