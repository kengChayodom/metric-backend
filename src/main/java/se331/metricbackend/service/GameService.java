package se331.metricbackend.service;

import org.springframework.data.domain.Page;
import se331.metricbackend.dto.GameDTO;
import se331.metricbackend.entity.Game;

public interface GameService {
    Game getGameById(String id);
    Page<Game> getGames(Integer pageSize, Integer page);
    Page<Game> getGames(String title, Integer pageSize, Integer page);
    Game createGame(GameDTO gameDTO);
    Game updateGame(String id, GameDTO gameDTO);
    void deleteGame(String id);
}