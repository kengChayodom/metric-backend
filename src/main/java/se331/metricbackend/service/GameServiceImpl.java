package se331.metricbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se331.metricbackend.dao.GameDao;
import se331.metricbackend.dto.GameDTO;
import se331.metricbackend.entity.Category;
import se331.metricbackend.entity.Game;
import se331.metricbackend.repository.CategoryRepository;
// import se331.metricbackend.util.GameMapper; // <-- 1. ลบ Import นี้
import se331.metricbackend.util.LapMapper;     // <-- คุณมีตัวนี้อยู่แล้ว

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    final GameDao gameDao;
    final CategoryRepository categoryRepository;

    // ... (เมธอด getGameById, getGames ไม่ต้องแก้) ...
    @Override
    public Game getGameById(String id) {
        return gameDao.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));
    }

    @Override
    public Page<Game> getGames(Integer pageSize, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return gameDao.getGames(pageable);
    }

    @Override
    public Page<Game> getGames(String title, Integer pageSize, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return gameDao.getGames(title, pageable);
    }

    @Override
    public Game createGame(GameDTO gameDTO) {
        // 1. แปลง DTO (Simple fields)
        // 2. เปลี่ยนจาก GameMapper เป็น LapMapper
        Game game = LapMapper.INSTANCE.toGame(gameDTO);

        // 2. ดึง Categories จาก DB โดยใช้ ID ที่ส่งมา
        List<Category> categories = categoryRepository.findAllById(gameDTO.getCategoryIds());

        // 3. ตั้งค่า Categories (ที่เป็น @DBRef)
        game.setCategories(categories);

        return gameDao.save(game);
    }

    @Override
    public Game updateGame(String id, GameDTO gameDTO) {
        // 1. ดึงเกมเดิม
        Game existingGame = getGameById(id);

        // 2. อัปเดต Simple fields (อันนี้คุณใช้ LapMapper อยู่แล้ว ถูกต้องครับ)
        LapMapper.INSTANCE.updateGameFromDto(gameDTO, existingGame);

        // 3. อัปเดต Categories (เหมือนตอน Create)
        List<Category> categories = categoryRepository.findAllById(gameDTO.getCategoryIds());
        existingGame.setCategories(categories);

        return gameDao.save(existingGame);
    }

    // ... (เมธอด deleteGame ไม่ต้องแก้) ...
    @Override
    public void deleteGame(String id) {
        gameDao.deleteById(id);
    }
    @Override
    public Page<Game> getGamesByCategoryId(String categoryId, Integer pageSize, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return gameDao.getGamesByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Game> getGames(String title, String categoryId, String priceFilter, Integer pageSize, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        // ส่งต่อทั้งหมดไปยัง DAO
        return gameDao.getGames(title, categoryId, priceFilter, pageable);
    }

    public Game setFeaturedPosition(String gameId, Integer position) {
        Game game = gameDao.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        game.setHomepagePosition(position); // อัปเดตแค่ featured slot
        return gameDao.save(game);
    }

    public Game setNewReleasePosition(String gameId, Integer position) {
        Game game = gameDao.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        game.setNewReleasePosition(position); // อัปเดตแค่ new release slot
        return gameDao.save(game);
    }
}