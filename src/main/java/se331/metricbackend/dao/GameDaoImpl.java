package se331.metricbackend.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import se331.metricbackend.entity.Game;
import se331.metricbackend.repository.GameRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GameDaoImpl implements GameDao {

    final GameRepository gameRepository;

    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Optional<Game> findById(String id) {
        return gameRepository.findById(id);
    }

    @Override
    public Page<Game> getGames(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public Page<Game> getGames(String title, Pageable pageable) {
        // ใช้เมธอดที่เราสร้างไว้ใน Repository
        return gameRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public void deleteById(String id) {
        gameRepository.deleteById(id);
    }
}