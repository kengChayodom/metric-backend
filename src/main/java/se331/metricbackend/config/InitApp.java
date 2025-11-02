package se331.metricbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se331.metricbackend.entity.Category;
import se331.metricbackend.entity.Game;
import se331.metricbackend.repository.CategoryRepository;
import se331.metricbackend.repository.GameRepository;

import se331.metricbackend.security.user.Role;
import se331.metricbackend.security.user.User;
import se331.metricbackend.security.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitApp implements CommandLineRunner {

    final CategoryRepository categoryRepository;
    final GameRepository gameRepository;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder; // ใช้ Bean จาก Spring Security

    // เก็บ Category ที่บันทึกแล้วไว้ใช้เชื่อมกับ Game
    Category catAction, catRpg, catSports, catSim, catHorror;

    @Override
    public void run(String... args) throws Exception {

        // รันเฉพาะเมื่อฐานข้อมูลว่างเปล่า
        if (categoryRepository.count() == 0 && gameRepository.count() == 0 && userRepository.count() == 0) {

            // 1. สร้าง Categories
            addCategories();

            // 2. สร้าง Users
            addUsers();

            // 3. สร้าง Games (และเชื่อมกับ Categories)
            addGames();
        }
    }

    private void addCategories() {
        catAction = categoryRepository.save(Category.builder()
                .name("Action")
                .categoryImage("https://cdn.mos.cms.futurecdn.net/3dpYx5chpvupQDVPbVxAZ5-970-80.jpg.webp")
                .build());

        catRpg = categoryRepository.save(Category.builder()
                .name("RPG")
                .categoryImage("https://cdn.mos.cms.futurecdn.net/w844TWsyvV7jNksWB6ma9J.jpg")
                .build());

        catSports = categoryRepository.save(Category.builder()
                .name("SPORTS")
                .categoryImage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/2195250/ss_5320cc6b6c7fd39b41defe0570c3182df7065fd5.1920x1080.jpg?t=1730826798")
                .build());

        catSim = categoryRepository.save(Category.builder()
                .name("SIMULATION")
                .categoryImage("https://image.api.playstation.com/vulcan/ap/rnd/202407/1008/f29fea6f3087ea29ccc5639dba9ec9b742a7eac49c293674.jpg")
                .build());

        catHorror = categoryRepository.save(Category.builder()
                .name("HORROR")
                .categoryImage("https://cdn2.unrealengine.com/blogAssets%2F2016%2FOctober+2016%2FOctober28_DeadByDaylight%2FFEATURE_DeadByDaylight-1920x960-ad1fdfa6feb360fde03d2336e76cf84118b028bc.jpg")
                .build());
    }

    private void addUsers() {
        // ใช้ passwordEncoder ที่ฉีดเข้ามา
        User user1 = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .firstname("admin")
                .lastname("admin")
                .email("admin@admin.com")
                .profileImage("https://xvizzlwlipdsxerekvvk.supabase.co/storage/v1/object/public/BamminionsBucket/Ice.jpg") // แก้ไข Field
                .enabled(true)
                .roles(new ArrayList<>(List.of(Role.ROLE_ADMIN))) // กำหนด Role
                .build();

        User user2 = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .firstname("user")
                .lastname("user")
                .email("enabled@user.com")
                .profileImage("https://xvizzlwlipdsxerekvvk.supabase.co/storage/v1/object/public/BamminionsBucket/Cat.jpg") // แก้ไข Field
                .enabled(true)
                .roles(new ArrayList<>(List.of(Role.ROLE_MEMBER)))
                .build();

        User user3 = User.builder()
                .username("disableUser")
                .password(passwordEncoder.encode("disableUser"))
                .firstname("disableUser")
                .lastname("disableUser")
                .email("disableUser@user.com")
                .profileImage("https://xvizzlwlipdsxerekvvk.supabase.co/storage/v1/object/public/BamminionsBucket/202510304236819-IMG_0119.jpeg") // แก้ไข Field
                .enabled(false) // User นี้ถูกปิด
                .roles(new ArrayList<>(List.of(Role.ROLE_MEMBER)))
                .build();

        User user4 = User.builder()
                .username("keng")
                .password(passwordEncoder.encode("keng"))
                .firstname("keng")
                .lastname("user")
                .email("keng@user.com")
                .profileImage("https://xvizzlwlipdsxerekvvk.supabase.co/storage/v1/object/public/BamminionsBucket/Kevin.png") // แก้ไข Field
                .enabled(true)
                .roles(new ArrayList<>(List.of(Role.ROLE_MEMBER)))
                .build();

        User user5 = User.builder()
                .username("mean")
                .password(passwordEncoder.encode("mean"))
                .firstname("mean")
                .lastname("user")
                .email("mean@user.com")
                .profileImage("https://xvizzlwlipdsxerekvvk.supabase.co/storage/v1/object/public/BamminionsBucket/Bob.png") // แก้ไข Field
                .enabled(true)
                .roles(new ArrayList<>(List.of(Role.ROLE_MEMBER)))
                .build();

        User user6 = User.builder()
                .username("guide")
                .password(passwordEncoder.encode("guide"))
                .firstname("guide")
                .lastname("user")
                .email("guide@user.com")
                .profileImage("https://xvizzlwlipdsxerekvvk.supabase.co/storage/v1/object/public/BamminionsBucket/Stuart.png") // แก้ไข Field
                .enabled(true)
                .roles(new ArrayList<>(List.of(Role.ROLE_MEMBER)))
                .build();

        userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6));
    }

    private void addGames() {
        List<Game> gameList = new ArrayList<>();

        // 1. Elden Ring
        gameList.add(Game.builder()
                .title("Elden Ring").price(1790.0).promotionPrice(1074.0)
                .description("An epic RPG adventure by FromSoftware.")
                .mainImageUrl("https://static.bandainamcoent.eu/high/elden-ring/elden-ring/00-page-setup/elden-ring-new-header-mobile.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202108/0410/np2Eb60bDep9fDWtqNNSzqZI.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=E3Huy2cdih0")
                .categories(List.of(catRpg, catAction))
                .build());

        // 2. Cyberpunk 2077
        gameList.add(Game.builder()
                .title("Cyberpunk 2077").price(1799.0).promotionPrice(716.90)
                .description("A futuristic open-world RPG with deep storytelling.")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202111/3013/UjQ1pWQiHwymgQQ6q4pWQkMC.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202111/3013/cKZ4tKNFj9C00giTzYtH8PF1.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=8X2kIfS6fb8")
                .categories(List.of(catRpg, catAction))
                .build());

        // 3. God of War
        gameList.add(Game.builder()
                .title("God of War").price(1290.0).promotionPrice(516.0)
                .description("Kratos returns in a new Norse mythology adventure.")
                .mainImageUrl("https://image.api.playstation.com/vulcan/img/rnd/202010/2217/LsaRVLF2IU2L1FNtu9d3MKLq.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/img/rnd/202011/1021/X3WIAh63yKhRRiMohLoJMeQu.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=K0u_kAWLJOA")
                .categories(List.of(catAction, catRpg))
                .build());

        // 4. Hogwarts Legacy
        gameList.add(Game.builder()
                .title("Hogwarts Legacy").price(1890.0).promotionPrice(472.50)
                .description("An open-world RPG set in the Harry Potter universe.")
                .mainImageUrl("https://static0.gamerantimages.com/wordpress/wp-content/uploads/wm/2024/12/hogwarts-legacy-2-bite-bullet-choice-system-1.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202208/0921/dR9KJAKDW2izPbptHQbh3rnj.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=BsC-Rl9GYy0")
                .categories(List.of(catRpg, catAction))
                .build());

        // 5. Red Dead Redemption 2
        gameList.add(Game.builder()
                .title("Red Dead Redemption 2").price(1899.0).promotionPrice(474.75)
                .description("An immersive Wild West adventure from Rockstar.")
                .mainImageUrl("https://gaming-cdn.com/images/products/16201/orig/red-dead-redemption-2-playstation-4-game-playstation-store-cover.jpg?v=1730298570")
                .icongameUrl("https://image.api.playstation.com/cdn/UP1004/CUSA03041_00/Hpl5MtwQgOVF9vJqlfui6SDB5Jl4oBSq.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=gmA6MrX81z4")
                .categories(List.of(catAction, catRpg))
                .build());

        // 6. Resident Evil 4 Remake
        gameList.add(Game.builder()
                .title("Resident Evil 4 Remake").price(1399.0).promotionPrice(699.50)
                .description("A modern remake of the classic horror game.")
                .mainImageUrl("https://static.thairath.co.th/media/dFQROr7oWzulq5Fa5BWCLXBQIF2H3QgdHi4xXBeCop7swfrSs3w7Mqu26ZOsZaXqA2v.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202210/0706/EVWyZD63pahuh95eKloFaJuC.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=E69tKrfEQag")
                .categories(List.of(catHorror, catAction))
                .build());

        // 7. The Witcher 3: Wild Hunt
        gameList.add(Game.builder()
                .title("The Witcher 3: Wild Hunt").price(1039.0).promotionPrice(207.99)
                .description("An epic RPG about monster hunting and choice-driven storytelling.")
                .mainImageUrl("https://wallpapers.com/images/hd/the-witcher-3-wild-hunt-geralt-mpnbxmg15qm3bnh2.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202211/0711/kh4MUIuMmHlktOHar3lVl6rY.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=c0i88t0Kacs")
                .categories(List.of(catRpg, catAction))
                .build());

        // 8. Grand Theft Auto V
        gameList.add(Game.builder()
                .title("Grand Theft Auto V").price(762.0).promotionPrice(381.0)
                .description("A legendary open-world crime adventure.")
                .mainImageUrl("https://wallpapers.com/images/featured/gta-5-qpjtjdxwbwrk4gyj.jpg")
                .icongameUrl("https://image.api.playstation.com/cdn/UP1004/CUSA00419_00/fd8W1NzjmglT8LxNrKdBE6C4m3PfuHqOcn6E5FRTRhWbfjBfme4WqHqud1iG6PRF.png")
                .trailerUrl("https://www.youtube.com/watch?v=QkkoHAzjnUs")
                .categories(List.of(catAction))
                .build());

        // 9. Final Fantasy XVI
        gameList.add(Game.builder()
                .title("Final Fantasy XVI").price(1690.0).promotionPrice(1098.50)
                .description("Epic RPG with deep storytelling.")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202211/3007/JnzRCl2Yj208yuJoSfoGXMGt.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202211/1404/RMELUDlp2uzJ3EmsVhzr00A6.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=bXWTtjwc7Ng")
                .categories(List.of(catRpg, catAction))
                .build());

        // 10. God Of War Ragnarök
        gameList.add(Game.builder()
                .title("God Of War Ragnarök").price(1690.0).promotionPrice(1352.0)
                .description("An epic action-adventure game following Kratos and Atreus...")
                .mainImageUrl("https://r.testifier.nl/Acbs8526SDKI/resizing_type:fill/plain/https%3A%2F%2Fs3-newsifier.ams3.digitaloceanspaces.com%2Fxgn.nl%2Fimages%2F2024-03%2Fgod-of-war-ragnarok-headerf1671704240-6601aa6bdef43.jpg@webp")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202207/1210/4xJ8XB3bi888QTLZYdl7Oi0s.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=Ut7FkcpYL74")
                .categories(List.of(catAction, catRpg))
                .build());

        // 11. Marvel's Spider-Man 2
        gameList.add(Game.builder()
                .title("Marvel's Spider-Man 2").price(1690.0).promotionPrice(1380.0)
                .description("Marvel superhero action adventure.")
                .mainImageUrl("https://4kwallpapers.com/images/wallpapers/marvels-spider-man-3440x1440-12968.jpeg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202306/1219/1c7b75d8ed9271516546560d219ad0b22ee0a263b4537bd8.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=nq1M_Wc4FIc")
                .categories(List.of(catAction))
                .build());

        // 12. Baldur's Gate 3
        gameList.add(Game.builder()
                .title("Baldur's Gate 3").price(1799.0).promotionPrice(1439.20)
                .description("D&D-based RPG with deep choices.")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202308/1519/95cce955dc59d04e2ea5ab624a823ace14e9c5f7e24dfb8f.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202302/2321/ba706e54d68d10a0eb6ab7c36cdad9178c58b7fb7bb03d28.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=1T22wNvoNiU")
                .categories(List.of(catRpg))
                .build());

        // 13. Lies of P
        gameList.add(Game.builder()
                .title("Lies of P").price(1982.0).promotionPrice(1189.20)
                .description("Dark souls-like action RPG.")
                .mainImageUrl("https://i0.wp.com/gatecrashers.fan/wp-content/uploads/2024/04/lies-of-p-sequel.jpg?fit=1920%2C1080&ssl=1")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202305/2308/06b354c8015b3c71e54f43aa883aab4641285d4a91734681.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=kXZoKdr-xeo")
                .categories(List.of(catRpg, catAction))
                .build());

        // 14. Alan Wake 2
        gameList.add(Game.builder()
                .title("Alan Wake 2").price(1250.0).promotionPrice(990.0)
                .description("Psychological horror thriller.")
                .mainImageUrl("https://cdn1.epicgames.com/offer/c4763f236d08423eb47b4c3008779c84/EGS_AlanWake2_RemedyEntertainment_S1_2560x1440-ec44404c0b41bc457cb94cd72cf85872")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202305/2420/c3daf3037feb797d9e71b81618e3b5ff3ff1f9609db5a4a2.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=dlQ3FeNu5Yw")
                .categories(List.of(catHorror, catAction))
                .build());

        // 15. Dead Space Remake
        gameList.add(Game.builder()
                .title("Dead Space Remake").price(1599.0).promotionPrice(399.75)
                .description("A sci-fi horror survival game.")
                .mainImageUrl("https://img.4gamers.com.tw/ckfinder-th/image2/auto/2023-01/Dead%20Space%20Remake%203-230114-232743.jpg?versionId=LK19gUfhtLN2pjZ5Lks.sgAEaCy7Sgpa")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202209/1918/MP0I6Folca9wOgs9A39wwvj1.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=C1yiuM7blIw")
                .categories(List.of(catHorror, catAction))
                .build());

        // 16. The First Berserker: Khazan
        gameList.add(Game.builder()
                .title("The First Berserker: Khazan").price(2022.0).promotionPrice(1718.70)
                .description("A hardcore action RPG where players become Khazan...")
                .mainImageUrl("https://asia.sega.com/khazan/assets/images/common/main-visual.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202406/2604/78cf326a8cceaed3f4a807fe9e3f811abdfb5df96fe0cd06.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=-9uMGePq3zw")
                .categories(List.of(catAction, catRpg))
                .build());

        // 17. HELLDIVERS™ 2
        gameList.add(Game.builder()
                .title("HELLDIVERS™ 2").price(1800.0).promotionPrice(1620.0)
                .description("A cooperative twin-stick shooter where players defend Super Earth...")
                .mainImageUrl("https://sm.pcmag.com/t/pcmag_au/review/h/helldivers/helldivers-2_emzp.1920.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202412/1814/a694d615aa55cda06d7f4d9299689c488d4b1d5b981fa34c.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=UC5EpJR0GBQ")
                .categories(List.of(catAction))
                .build());

        // 18. Monster Hunter Wilds
        gameList.add(Game.builder()
                .title("Monster Hunter Wilds").price(1990.0).promotionPrice(null)
                .description("An open-world action RPG where players hunt massive creatures...")
                .mainImageUrl("https://preview.redd.it/full-monster-hunter-wilds-box-art-textless-v0-nyzly6nl8uqd1.png?auto=webp&s=b97f1a148aceaf2a7a7e221d49f201ec4122c60d")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202409/0506/aa5c40ba185302dfcc88edc276a876fdc6c516c4db07ec9d.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=2u6VmVmhh-k")
                .categories(List.of(catAction, catRpg))
                .build());

        // 19. Split Fiction
        gameList.add(Game.builder()
                .title("Split Fiction").price(1788.0).promotionPrice(1609.20)
                .description("A narrative-driven adventure game exploring parallel universes...")
                .mainImageUrl("https://www.gematsu.com/wp-content/uploads/2024/12/Game-Page-Featured_Split-Fiction.jpg?w=640")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202502/2109/0c582584b280e3bf8872316fd4626ec084efaeb9ba436c65.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=fcwngWPXQtg")
                .categories(List.of(catAction))
                .build());

        // 20. Assassin’s Creed Shadows
        gameList.add(Game.builder()
                .title("Assassin’s Creed Shadows").price(2390.0).promotionPrice(2151.0)
                .description("An action-adventure game set in a vast open world...")
                .mainImageUrl("https://static0.gamerantimages.com/wordpress/wp-content/uploads/2024/06/assassins-creed-shadows-social-stealth.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202412/2018/6d3fddf363563ef352c729c8f5077056ab342fd0a9daae9b.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=zxtJFdnMo7k")
                .categories(List.of(catAction, catRpg))
                .build());

        // 21. Path of Exile 2
        gameList.add(Game.builder()
                .title("Path of Exile 2").price(790.0).promotionPrice(null)
                .description("A free-to-play action RPG offering deep character customization...")
                .mainImageUrl("https://static0.gamerantimages.com/wordpress/wp-content/uploads/2024/12/lumerius-key-art-path-of-exile-2.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202405/2622/14995e5303b1573ae19f6d8b8702cc5428c1e1ccbb2c3bf3.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=KhhNhpOln64")
                .categories(List.of(catRpg, catAction))
                .build());

        // 22. Black Myth: Wukong
        gameList.add(Game.builder()
                .title("Black Myth: Wukong").price(2022.0).promotionPrice(1819.80)
                .description("An action RPG inspired by Chinese mythology...")
                .mainImageUrl("https://preview.redd.it/black-myth-wukong-8k-resolution-wallpaper-v0-kyuamtohf8gd1.jpg?width=3840&format=pjpg&auto=webp&s=77ea2ce3c89e94f49ab717e763f378137bec7dd7")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202405/2117/bd406f42e9352fdb398efcf21a4ffe575b2306ac40089d21.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=pnSsgRJmsCc")
                .categories(List.of(catAction, catRpg))
                .build());

        // 23. The Last of Us™ Part II Remastered
        gameList.add(Game.builder()
                .title("The Last of Us™ Part II Remastered").price(2290.0).promotionPrice(null)
                .description("A remastered version of the acclaimed action-adventure game...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202312/0117/0b68ebf352db559cb45a75bd32fed3381f34dc52c5480192.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202312/0117/315718bce7eed62e3cf3fb02d61b81ff1782d6b6cf850fa4.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=-llaUBqovHw")
                .categories(List.of(catAction, catHorror))
                .build());

        // 24. FINAL FANTASY VII REBIRTH
        gameList.add(Game.builder()
                .title("FINAL FANTASY VII REBIRTH").price(2390.0).promotionPrice(2151.0)
                .description("The next chapter in the FFVII remake series...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202308/3005/537b5208a8ee42935286a44b3b981da86d976bf54899bf98.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202308/3005/6de6b1c4b0c475b5c28fd2011655ab8206ed846926fa1671.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=KOhs9ZLImgE")
                .categories(List.of(catRpg, catAction))
                .build());

        // 25. DYNASTY WARRIORS: ORIGINS
        gameList.add(Game.builder()
                .title("DYNASTY WARRIORS: ORIGINS").price(2350.0).promotionPrice(null)
                .description("A hack-and-slash game set in ancient China...")
                .mainImageUrl("https://cdn.wccftech.com/wp-content/uploads/2024/11/Dynasty-Warriors-Origins-Preview-01-Header.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202410/3101/0f411dcdec81501788dd9a7dd05efee496db057c3d346e74.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=AyRjVdaabqs")
                .categories(List.of(catAction, catRpg))
                .build());

        // 26. Marvel's Spider-Man Remastered
        gameList.add(Game.builder()
                .title("Marvel's Spider-Man Remastered").price(1500.0).promotionPrice(1020.0)
                .description("Play as Spider-Man in this remastered action-adventure game...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202009/3021/5ayReKkz8RaBVuTvrxgA3rvh.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202009/3021/B2aUYFC0qUAkNnjbTHRyhrg3.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=1E051WtpyWg")
                .categories(List.of(catAction))
                .build());

        // 27. The Last of Us™ Part I
        gameList.add(Game.builder()
                .title("The Last of Us™ Part I").price(2290.0).promotionPrice(null)
                .description("Experience the original story of Joel and Ellie...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202206/0720/0kRqUeSBIbQzz7cen3c989c6.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202206/0720/eEczyEMDd2BLa3dtkGJVE9Id.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=R2Ebc_OFeug")
                .categories(List.of(catAction, catHorror))
                .build());

        // 28. STAR WARS Jedi: Survivor™
        gameList.add(Game.builder()
                .title("STAR WARS Jedi: Survivor™").price(2353.0).promotionPrice(588.0)
                .description("An action-adventure game set in the Star Wars universe...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202304/1016/66eb1ad90edf540651444d04f0476a463a80a81ba0b46ea0.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202304/1016/47becbe467e18575f71429abbaec9af707865744b825f34d.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=VRaobDJjiec")
                .categories(List.of(catAction, catRpg))
                .build());

        // 29. Tom Clancy's Rainbow Six Siege
        gameList.add(Game.builder()
                .title("Tom Clancy's Rainbow Six Siege").price(1800.0).promotionPrice(420.0)
                .description("A tactical shooter focusing on team-based combat...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202209/2121/laGYFrQE8GOrCyKr7wdXftli.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202209/0617/eiXonwxcJH00bkGo3G6lUm9t.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=w03L5ZOKmPE")
                .categories(List.of(catAction))
                .build());

        // 30. Far Cry 6
        gameList.add(Game.builder()
                .title("Far Cry 6").price(1915.0).promotionPrice(478.0)
                .description("An open-world first-person shooter...")
                .mainImageUrl("https://www.theaureview.com/wp-content/uploads/2021/10/far-cry-6-benchmark-cpu-bottleneck.jpeg")
                .icongameUrl("https://image.api.playstation.com/vulcan/img/rnd/202106/0722/ZD3S3PLtJd6dratIs7lc09mS.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=-IJuKT1mHO8")
                .categories(List.of(catAction))
                .build());

        // 31. SILENT HILL 2
        gameList.add(Game.builder()
                .title("SILENT HILL 2").price(2297.0).promotionPrice(1607.0)
                .description("A survival horror game where players explore a mysterious town...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202210/2000/7cb6eP7FKb4PMq6ZnDDBAlzw.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202210/2000/IgwsFz9BiBrFvyV7pIWpoVgd.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=pyC_qiW_4ZY")
                .categories(List.of(catHorror))
                .build());

        // 32. Horizon Forbidden West™
        gameList.add(Game.builder()
                .title("Horizon Forbidden West™").price(1690.0).promotionPrice(null)
                .description("An action RPG where players explore a vast open world...")
                .mainImageUrl("https://cdn1.epicgames.com/offer/24cc2629b0594bf29340f6acf9816af8/EGS_HorizonForbiddenWestCompleteEdition_GuerrillaGamesNixxesSoftware_S1_2560x1440-90cc343f3fcef2f750f13d8a2d84893b")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202107/3100/HO8vkO9pfXhwbHi5WHECQJdN.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=Lq594XmpPBg")
                .categories(List.of(catAction, catRpg))
                .build());

        // 33. It Takes Two
        gameList.add(Game.builder()
                .title("It Takes Two").price(1390.0).promotionPrice(null)
                .description("A co-op adventure game where two players work together...")
                .mainImageUrl("https://store-images.s-microsoft.com/image/apps.40253.14488339386131194.84ca8b8a-582e-4d34-904e-8f1e60f71000.c3aaab37-0ce8-464b-85c1-4f42a74d9972")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202012/0815/7CRynuLSAb0vysSC4TmZy5e4.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=ohClxMmNLQQ")
                .categories(List.of(catAction, catSim))
                .build());

        // 34. Stellar Blade™
        gameList.add(Game.builder()
                .title("Stellar Blade™").price(2090.0).promotionPrice(null)
                .description("An action-adventure game set in a futuristic world...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202401/2211/2937cd4563d2dc50f0632a1bcfbac1f1f7dee3d2984ea02c.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202401/2211/40e7cfd126a11fe5118310ebce6d9b3a23e7cabaca717217.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=DSznLWimMlU")
                .categories(List.of(catAction, catRpg))
                .build());

        // 35. Dragon's Dogma 2
        gameList.add(Game.builder()
                .title("Dragon's Dogma 2").price(2460.0).promotionPrice(1402.0)
                .description("An open-world action RPG with dynamic combat...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202311/1309/746f848d0be7107e301df1142745b334b64fbc87c218e3b1.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202311/0203/37bd2f7293cecbc4045d33578d487127ccaa1840eb15eeb5.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=cT0rIgaiPWA")
                .categories(List.of(catRpg, catAction))
                .build());

        // 36. Ghost of Tsushima
        gameList.add(Game.builder()
                .title("Ghost of Tsushima").price(2090.0).promotionPrice(null)
                .description("An open-world action-adventure game set in feudal Japan.")
                .mainImageUrl("https://cdn.shopify.com/s/files/1/0556/5795/5430/articles/ghost-of-tsushima.jpg?v=1712922641")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202106/2322/c16gs6a7lbAYzPf7ZTikbH1c.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=A5gVt028Hww")
                .categories(List.of(catAction, catRpg))
                .build());

        // 37. Little Nightmares II
        gameList.add(Game.builder()
                .title("Little Nightmares II").price(427.0).promotionPrice(null)
                .description("A suspense-adventure game where players navigate a dark world.")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202203/3115/n4pxf1tnfOt6JUp3zvn9La1h.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202204/0416/e4pNnBSfhs0LIr8MHBfEcS6b.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=AI9zBBTyX-E")
                .categories(List.of(catHorror))
                .build());

        // 38. Party Animals
        gameList.add(Game.builder()
                .title("Party Animals").price(738.0).promotionPrice(442.0)
                .description("A multiplayer party game where players control cute animals...")
                .mainImageUrl("https://playulti.com/storage/images/62efb9c94234b.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202405/0904/5a7cc905a82c67bca58a6fde83e19d22fe01865d78add83b.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=Cunvu8b7rj0")
                .categories(List.of(catSim, catAction))
                .build());

        // 39. ELDEN RING NIGHTREIGN
        gameList.add(Game.builder()
                .title("ELDEN RING NIGHTREIGN").price(1390.0).promotionPrice(null)
                .description("An action RPG set in a dark fantasy world...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202501/2901/b21fc6bafea0353ad8578dec61cd2690020b615f161c416e.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202501/3109/6a019ef01f397711db919b2f73785c0321579f6c0d62ddc7.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=Djtsw5k_DNc")
                .categories(List.of(catRpg, catAction))
                .build());

        // 40. Dying Light 2 Stay Human
        gameList.add(Game.builder()
                .title("Dying Light 2 Stay Human").price(2022.0).promotionPrice(null)
                .description("An open-world survival horror game with parkour mechanics.")
                .mainImageUrl("https://www.godisageek.com/wp-content/uploads/Dying-Light-2-Review-6.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202501/3115/3c17bf3bb4c9b5b572f69217554f0afdebade3a37959bac3.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=68bZ1LKKh7Q")
                .categories(List.of(catHorror, catAction))
                .build());

        // 41. Coral Island
        gameList.add(Game.builder()
                .title("Coral Island").price(1200.0).promotionPrice(1080.0)
                .description("A farming simulation game set on a tropical island.")
                .mainImageUrl("https://www.humblegames.com/wp-content/uploads/2022/03/Coral-Island-Key-Art-1920x1080-1.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202310/3018/578c25e83d7fe754a625ae6910d84e4ce12d420f9771beb1.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=iyRUBNF7-k4")
                .categories(List.of(catSim, catRpg))
                .build());

        // 42. Palworld
        gameList.add(Game.builder()
                .title("Palworld").price(845.0).promotionPrice(633.0)
                .description("An open-world survival game where players befriend and battle creatures.")
                .mainImageUrl("https://media.wired.com/photos/65ab0f66f3c358e7835caef5/master/pass/Palworld-Pokemon-With-Guns-Culture-20230608_Palworld_Screenshot_02.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202405/2108/a9c0fb99b704bfd04e85512a280acaf9a19901c8548b6792.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=D9w97KSEAOo")
                .categories(List.of(catSim, catAction, catRpg))
                .build());

        // 43. SEKIRO: SHADOWS DIE TWICE
        gameList.add(Game.builder()
                .title("SEKIRO: SHADOWS DIE TWICE").price(2099.0).promotionPrice(1049.50)
                .description("An action-adventure game set in a reimagined late 1500s Japan.")
                .mainImageUrl("https://image.api.playstation.com/vulcan/img/rnd/202010/2818/ofQOsetcmEILO4MjvRudoUOC.png")
                .icongameUrl("https://image.api.playstation.com/cdn/HP0506/CUSA13910_00/QKJRzanGk86ezpx2pk5QqQaduoXGJV3u8vHIejSav4MYiHA3F7zNgxSOF9bJmt2T.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=rXMX4YJ7Lks")
                .categories(List.of(catAction, catRpg))
                .build());

        // 44. Rise of the Ronin
        gameList.add(Game.builder()
                .title("Rise of the Ronin").price(2200.0).promotionPrice(null)
                .description("An open-world action RPG set in late 19th-century Japan...")
                .mainImageUrl("https://www.appdisqus.com/wp-content/uploads/2024/03/a01abaf7aa72544302f22a4f927bcb3461ca8cc9-scaled-1.webp")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202312/0610/080873a4b459d2b1c602a81f5be18a85600be2c5ddda32a7.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=zS8EvlTGCiE")
                .categories(List.of(catAction, catRpg))
                .build());

        // 45. Persona 3 Reload
        gameList.add(Game.builder()
                .title("Persona 3 Reload").price(1890.0).promotionPrice(null)
                .description("A modern reimagining of the classic JRPG...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202307/2605/19353f11f85964dd2be8eae4564a0e78b011a81824d9b4a9.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202307/2601/85487475426de191c3c100b47ddc61f77c515b513948272e.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=VsMvSZeiKmw")
                .categories(List.of(catRpg, catSim))
                .build());

        // 46. Marvel's Spider-Man: Miles Morales
        gameList.add(Game.builder()
                .title("Marvel's Spider-Man: Miles Morales").price(1890.0).promotionPrice(null)
                .description("An action-adventure game where players control Miles Morales...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/img/rnd/202010/2417/tnCutdREPv6Pa7atqb8MTxGW.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202008/1020/T45iRN1bhiWcJUzST6UFGBvO.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=CMRBuagwRb4")
                .categories(List.of(catAction))
                .build());

        // 47. Phasmophobia
        gameList.add(Game.builder()
                .title("Phasmophobia").price(738.0).promotionPrice(450.0)
                .description("A co-op psychological horror game...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202306/1214/bc6f8e8124fb74288042b46cf2358ad4527dbcd810cea594.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202306/1214/9ec3be8acd540e2f84e7362cb492e8d53b93e8a7bea4cd5f.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=sRa9oeo5KiY")
                .categories(List.of(catHorror))
                .build());

        // 48. PAYDAY 3
        gameList.add(Game.builder()
                .title("PAYDAY 3").price(1059.0).promotionPrice(null)
                .description("A cooperative first-person shooter where players plan and execute heists...")
                .mainImageUrl("https://www.paydaythegame.com/ovk-media/redux/pd3-pl-hiu732gfuwah/pd3pl-ss1.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202305/2616/74efe3cba65d7ac5f08389b840602fa8349b075c4753901e.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=dc5gw3ctb0o")
                .categories(List.of(catAction))
                .build());

        // 49. Poppy Playtime: Chapter 3
        gameList.add(Game.builder()
                .title("Poppy Playtime: Chapter 3").price(524.0).promotionPrice(null)
                .description("The third installment in the horror-puzzle series...")
                .mainImageUrl("https://cdn.gameleap.com/images/articles/art_OQPquJhhE2/art-img_tp0pGAnta/1x.webp")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202408/2820/3ac645931328e4ca5a7775db1164c09fef421589e6a50b88.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=s3_k03RC668")
                .categories(List.of(catHorror))
                .build());

        // 50. Call of Duty®: Black Ops 6
        gameList.add(Game.builder()
                .title("Call of Duty®: Black Ops 6").price(2449.0).promotionPrice(null)
                .description("The latest installment in the Black Ops series...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202405/2921/05a08ddb076656e71324f6d578bdbaa16c8925ab01dc3046.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202405/2921/4b45cf4b319a65e05f6e4f87a22c7b91d2e7e8aeb247b61f.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=oyZY8LNB7e58y8Zu")
                .categories(List.of(catAction))
                .build());

        // 51. EA SPORTS FC™ 25
        gameList.add(Game.builder()
                .title("EA SPORTS FC™ 25").price(1999.0).promotionPrice(599.70)
                .description("The newest football simulation game from EA SPORTS...")
                .mainImageUrl("https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/2669320/ss_009991924bc40dd9d9793bbef7b5783470e3030d.1920x1080.jpg?t=1740132694")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202502/1900/2505e0d616d90755ea6441f4c3fad4a9619e89cab9c3db26.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=pBM2xyco_Kg")
                .categories(List.of(catSports, catSim))
                .build());

        // 52. Minecraft
        gameList.add(Game.builder()
                .title("Minecraft").price(900.0).promotionPrice(810.0)
                .description("A sandbox game that allows players to build and explore...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202407/1020/91fe046f742042e3b31e57f7731dbe2226e1fd1e02a36223.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202407/0401/670c294ded3baf4fa11068db2ec6758c63f7daeb266a35a1.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=MmB9b5njVbA")
                .categories(List.of(catSim, catAction))
                .build());

        // 53. RESIDENT EVIL 3
        gameList.add(Game.builder()
                .title("RESIDENT EVIL 3").price(1189.0).promotionPrice(297.0)
                .description("A survival horror game where players control Jill Valentine...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202206/0307/wIVJREWyBg8SyXvXYuBPG6je.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202206/0206/WmriZBRlSeXWEEDLJOWW7MdW.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=BBky2uCGqtM")
                .categories(List.of(catHorror, catAction))
                .build());

        // 54. Ratchet & Clank: Rift Apart
        gameList.add(Game.builder()
                .title("Ratchet & Clank: Rift Apart").price(2290.0).promotionPrice(null)
                .description("An action-adventure game featuring dynamic duo Ratchet and Clank...")
                .mainImageUrl("https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/1895880/ss_5d2c999ecdccbc200121318b479bf0bdeb16b9b9.1920x1080.jpg?t=1717621710")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202101/2921/DwVjpbKOsFOyPdNzmSTSWuxG.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=55PRv_e00wc")
                .categories(List.of(catAction))
                .build());

        // 55. Resident Evil Village
        gameList.add(Game.builder()
                .title("Resident Evil Village").price(1288.0).promotionPrice(515.0)
                .description("A survival horror game where players control Ethan Winters...")
                .mainImageUrl("https://preview.redd.it/75bn91ijvc451.png?auto=webp&s=23c6928bbbaac979b888aef509c3584fe42bad18")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202101/0812/FkzwjnJknkrFlozkTdeQBMub.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=btFclZUXpzA")
                .categories(List.of(catHorror, catAction))
                .build());

        // 56. Control
        gameList.add(Game.builder()
                .title("Control").price(1380.0).promotionPrice(null)
                .description("An action-adventure game where players control Jesse Faden...")
                .mainImageUrl("https://cdn.prod.website-files.com/64630b03551142e3347ae3da/64e62e09480e98b970cf0698_news_ControlUE_4K.webp")
                .icongameUrl("https://image.api.playstation.com/vulcan/img/rnd/202009/1409/LOjeGQPXg7C1yoMmhsKYOwfa.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=PT5yMfC9LQM")
                .categories(List.of(catAction))
                .build());

        // 57. DAVE THE DIVER
        gameList.add(Game.builder()
                .title("DAVE THE DIVER").price(364.0).promotionPrice(null)
                .description("An adventure game where players explore the ocean's depths...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202401/2508/cef621908426dde90821d6f6e960e9272a696f7f973466de.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202401/2508/f194dc023adef481689d90db93f343b73f5b4c5741925556.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=p85VHMpE0to")
                .categories(List.of(catSim, catRpg, catAction))
                .build());

        // 58. ASTRO BOT
        gameList.add(Game.builder()
                .title("ASTRO BOT").price(1990.0).promotionPrice(null)
                .description("A platformer game where players guide Astro through various levels...")
                .mainImageUrl("https://images5.alphacoders.com/137/1370580.jpeg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202406/0500/8f15268257b878597757fcc5f2c9545840867bc71fc863b1.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=wHMNx5chpvup")
                .categories(List.of(catAction))
                .build());

        // 59. FINAL FANTASY VII REMAKE
        gameList.add(Game.builder()
                .title("FINAL FANTASY VII REMAKE").price(1391.0).promotionPrice(null)
                .description("A reimagining of the classic RPG...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/img/rnd/202010/0723/vDLeyNzrJdGwabFlEo44GkEZ.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/img/cfn/11307dEuqrDATOr86ceCwmb7NMtMOIauT9NukLGh5w5eM08MNxjdR1jJVINZkvGwDnbkkOreMCAwiXNCB5XT-YJkLrkj5xSc.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=eHdaWMP7D2s")
                .categories(List.of(catRpg, catAction))
                .build());

        // 60. Sea of Thieves
        gameList.add(Game.builder()
                .title("Sea of Thieves").price(1380.0).promotionPrice(null)
                .description("A pirate-themed adventure game where players explore an open world...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202402/2215/5e2dc182e7dfd5af31e01723d21af419dfdcf428458de200.jpg")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202402/2811/d0338c0adc01bdaf7407068a90b4bf8a865939ce44ae22ac.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=r5JIBaasuE8")
                .categories(List.of(catAction, catSim))
                .build());

        // 61. Warhammer 40,000: Space Marine 2
        gameList.add(Game.builder()
                .title("Warhammer 40,000: Space Marine 2").price(2343.0).promotionPrice(null)
                .description("A third-person action game set in the Warhammer 40,000 universe...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202212/0712/TIazq3vCJyMmP9RSo5Wr4NK0.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202212/0711/MKweDX2LQpXiPcU6Jitg9KAg.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=PMqvC507g1M")
                .categories(List.of(catAction))
                .build());

        // 62. DARK SOULS™ III
        gameList.add(Game.builder()
                .title("DARK SOULS™ III").price(2033.0).promotionPrice(1620.0)
                .description("An action RPG known for its challenging combat...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/img/rnd/202010/0707/JXnNHVb2IYgh31GtnujFqlfL.jpg")
                .icongameUrl("https://image.api.playstation.com/cdn/HP0700/CUSA03434_00/8h7cWRisC1eoKS3dNjsSt7bMM59QDsJhh2VpJ5v0j18T57tUOabxc3QaG8A5HAJw.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=cWBwFhUv1-8")
                .categories(List.of(catRpg, catAction))
                .build());

        // 63. Demon's Souls
        gameList.add(Game.builder()
                .title("Demon's Souls").price(2290.0).promotionPrice(1589.0)
                .description("A remake of the classic action RPG...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/img/rnd/202011/1716/9jRQWmnsX9SVmdGYoVzjcg4T.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/img/rnd/202011/1717/GemRaOZaCMhGxQ9dRhnQQyT5.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=2TMs2E6cms4")
                .categories(List.of(catRpg, catAction))
                .build());

        // 64. Balatro
        gameList.add(Game.builder()
                .title("Balatro").price(364.0).promotionPrice(327.0)
                .description("A deck-building roguelike game...")
                .mainImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202402/1416/081d7231762c748a7c25becc18d90ec8fa18360311e1f453.png")
                .icongameUrl("https://image.api.playstation.com/vulcan/ap/rnd/202401/2218/d8c5d5861249cd80a300efb723450f56d0347e4345e2eb80.png?w=440&thumb=false")
                .trailerUrl("https://www.youtube.com/watch?v=VUyP21iQ_-g")
                .categories(List.of(catSim, catRpg))
                .build());


        // บันทึกเกมทั้งหมดลง DB
        gameRepository.saveAll(gameList);
    }
}