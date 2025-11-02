package se331.metricbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se331.metricbackend.dto.GameDTO;
import se331.metricbackend.entity.Game;
import se331.metricbackend.service.GameService;
import se331.metricbackend.util.LapMapper;

import java.util.List;

@RestController
@RequestMapping("/games") // ◀️ แก้ไข Path
@RequiredArgsConstructor
public class GameController {

    final GameService gameService;

    @GetMapping
    public ResponseEntity<?> getGames(
            @RequestParam(value = "_limit", required = false) Integer perPage,
            @RequestParam(value = "_page", required = false) Integer page,
            @RequestParam(value = "title", required = false) String title
    ) {
        perPage = (perPage == null) ? 10 : perPage;
        page = (page == null) ? 1 : page;

        Page<Game> gamePage;
        if (title != null && !title.isEmpty()) {
            gamePage = gameService.getGames(title, perPage, page);
        } else {
            gamePage = gameService.getGames(perPage, page);
        }

        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("x-total-count", String.valueOf(gamePage.getTotalElements()));

        // ◀️ (ถูกต้อง) แปลงเป็น DTO ก่อนส่ง
        List<GameDTO> gameDTOs = LapMapper.INSTANCE.toGameDTOs(gamePage.getContent());
        return new ResponseEntity<>(gameDTOs, responseHeader, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGameById(@PathVariable("id") String id) {
        Game game = gameService.getGameById(id);
        // ◀️ แปลง Game -> GameDTO
        return ResponseEntity.ok(LapMapper.INSTANCE.toGameDTO(game));
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody GameDTO gameDTO) {
        Game newGame = gameService.createGame(gameDTO);
        // ◀️ แปลง Game -> GameDTO
        return ResponseEntity.ok(LapMapper.INSTANCE.toGameDTO(newGame));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGame(
            @PathVariable("id") String id,
            @RequestBody GameDTO gameDTO
    ) {
        Game updatedGame = gameService.updateGame(id, gameDTO);
        // ◀️ แปลง Game -> GameDTO
        return ResponseEntity.ok(LapMapper.INSTANCE.toGameDTO(updatedGame));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable("id") String id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok("Game with id " + id + " has been deleted.");
    }
}