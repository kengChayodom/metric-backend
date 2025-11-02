package se331.metricbackend.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import se331.metricbackend.entity.Game;
import se331.metricbackend.repository.GameRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GameDaoImpl implements GameDao {

    final GameRepository gameRepository;
    final MongoTemplate mongoTemplate;

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
    @Override
    public Page<Game> getGamesByCategoryId(String categoryId, Pageable pageable) {
        return gameRepository.findByCategories_Id(categoryId, pageable);
    }

    @Override
    public Page<Game> getGames(String title, String categoryId, String priceFilter, Pageable pageable) {

        Query query = new Query().with(pageable);
        List<Criteria> criteriaList = new ArrayList<>();

        // 1. Title Filter (เหมือนเดิม)
        if (title != null && !title.isEmpty()) {
            criteriaList.add(Criteria.where("title").regex(title, "i"));
        }

        // 2. Category Filter (เหมือนเดิม)
        if (categoryId != null && !categoryId.isEmpty()) {
            criteriaList.add(Criteria.where("categories.id").is(categoryId));
        }

        // 3. 🔽 Price Filter (Logic ใหม่ที่ซับซ้อน) 🔽
        if (priceFilter != null && !priceFilter.isEmpty()) {
            Criteria priceCriteria = createPriceCriteria(priceFilter);
            if (priceCriteria != null) {
                criteriaList.add(priceCriteria);
            }
        }

        // 4. รวมทุกเงื่อนไขด้วย AND
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        // 5. ดึงข้อมูล (พร้อมแบ่งหน้า)
        List<Game> games = mongoTemplate.find(query, Game.class);

        // 6. นับจำนวนทั้งหมด (ไม่แบ่งหน้า)
        Query countQuery = new Query();
        if (!criteriaList.isEmpty()) {
            countQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }
        long total = mongoTemplate.count(countQuery, Game.class);

        // 7. คืนค่าเป็น Page
        return new PageImpl<>(games, pageable, total);
    }

    // 🔽🔽🔽 4. เพิ่ม Helper Method สำหรับสร้าง Price Criteria 🔽🔽🔽
    private Criteria createPriceCriteria(String priceFilter) {
        double min = 0;
        double max = Double.MAX_VALUE;

        // ตีความ String priceFilter เป็นช่วงราคา
        switch (priceFilter) {
            case "under-500":
                max = 500;
                break;
            case "500-1000":
                min = 500;
                max = 1000;
                break;
            case "1000-2000":
                min = 1000;
                max = 2000;
                break;
            case "over-2000":
                min = 2000;
                break;
            default:
                return null; // ถ้า filter ไม่ถูกต้อง
        }

        // Logic: (promotionPrice != null AND promotionPrice อยู่ในช่วง)
        Criteria promoPriceInRange = Criteria.where("promotionPrice").exists(true)
                .gte(min).lte(max);

        // Logic: (promotionPrice == null AND price อยู่ในช่วง)
        Criteria normalPriceInRange = Criteria.where("promotionPrice").exists(false)
                .and("price").gte(min).lte(max);

        // จัดการกรณี "over-2000" (เพราะ lte(MAX_VALUE) อาจไม่ทำงานตามคาด)
        if (max == Double.MAX_VALUE) {
            promoPriceInRange = Criteria.where("promotionPrice").exists(true).gte(min);
            normalPriceInRange = Criteria.where("promotionPrice").exists(false).and("price").gte(min);
        }

        // 5. รวม 2 เงื่อนไขด้วย $or (หรือ)
        // (เกมที่ตรงตามเงื่อนไขโปรโมชัน "หรือ" เกมที่ตรงตามเงื่อนไขราคาปกติ)
        return new Criteria().orOperator(promoPriceInRange, normalPriceInRange);
    }
}