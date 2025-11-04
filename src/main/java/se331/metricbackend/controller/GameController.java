package se331.metricbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se331.metricbackend.dto.GameDTO;
import se331.metricbackend.dto.PositionRequest;
import se331.metricbackend.entity.Game;
import se331.metricbackend.service.GameService;
import se331.metricbackend.util.LapMapper;

import java.util.List;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    final GameService gameService;

    @GetMapping
    public ResponseEntity<?> getGames(
            @RequestParam(value = "_limit", required = false) Integer perPage,
            @RequestParam(value = "_page", required = false) Integer page,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "priceFilter", required = false) String priceFilter
    ) {
        perPage = (perPage == null) ? 16 : perPage; // ◀️ 2. Default 16
        page = (page == null) ? 1 : page;


        Page<Game> gamePage = gameService.getGames(
                title,
                categoryId,
                priceFilter,
                perPage,
                page
        );

        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("x-total-count", String.valueOf(gamePage.getTotalElements()));

        List<GameDTO> gameDTOs = LapMapper.INSTANCE.toGameDTOs(gamePage.getContent());
        return new ResponseEntity<>(gameDTOs, responseHeader, HttpStatus.OK);
    }

    // --- เมธอดที่เหลือ (getById, create, update, delete) เหมือนเดิม ---

    @GetMapping("/{id}")
    public ResponseEntity<?> getGameById(@PathVariable("id") String id) {
        Game game = gameService.getGameById(id);
        return ResponseEntity.ok(LapMapper.INSTANCE.toGameDTO(game));
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody GameDTO gameDTO) {
        Game newGame = gameService.createGame(gameDTO);
        return ResponseEntity.ok(LapMapper.INSTANCE.toGameDTO(newGame));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGame(
            @PathVariable("id") String id,
            @RequestBody GameDTO gameDTO
    ) {
        Game updatedGame = gameService.updateGame(id, gameDTO);
        return ResponseEntity.ok(LapMapper.INSTANCE.toGameDTO(updatedGame));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable("id") String id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok("Game with id " + id + " has been deleted.");
    }

    @PutMapping("/{id}/feature")
    public ResponseEntity<?> updateFeatured(
            @PathVariable String id,
            @RequestBody PositionRequest request
    ) {
        Game updated = gameService.setFeaturedPosition(id, request.getPosition());
        return ResponseEntity.ok(LapMapper.INSTANCE.toGameDTO(updated));
    }

    @PutMapping("/{id}/new-release")
    public ResponseEntity<?> updateNewRelease(
            @PathVariable String id,
            @RequestBody PositionRequest request
    ) {
        Game updated = gameService.setNewReleasePosition(id, request.getPosition());
        return ResponseEntity.ok(LapMapper.INSTANCE.toGameDTO(updated));
    }


    @DeleteMapping("/{id}/feature")
    public ResponseEntity<?> removeFeatured(@PathVariable String id) {
        Game updated = gameService.setFeaturedPosition(id, null);
        return ResponseEntity.ok(LapMapper.INSTANCE.toGameDTO(updated));
    }

    @DeleteMapping("/{id}/new-release")
    public ResponseEntity<?> removeNewRelease(@PathVariable String id) {
        Game updated = gameService.setNewReleasePosition(id, null);
        return ResponseEntity.ok(LapMapper.INSTANCE.toGameDTO(updated));
    }

}