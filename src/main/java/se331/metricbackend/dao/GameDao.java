package se331.metricbackend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.metricbackend.entity.Game;

import java.util.Optional;

public interface GameDao {
    Game save(Game game);
    Optional<Game> findById(String id);
    Page<Game> getGames(Pageable pageable);
    Page<Game> getGames(String title, Pageable pageable);
    void deleteById(String id);
}