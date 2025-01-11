package ru.minesweeper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.minesweeper.model.Game;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class GameRepository {

    @Value("${game.time.active}:30")
    private int gameTimeActive;

    private final RedisTemplate<String, Game> redisTemplate;

    public void saveGame(Game game) {
        if (game.getGameId() == null) {
            game.setGameId(UUID.randomUUID().toString());
        }
        redisTemplate.opsForValue().set(game.getGameId(), game, gameTimeActive, TimeUnit.MINUTES);
    }

    public Game findGame(String gameId) {
        return redisTemplate.opsForValue().get(gameId);
    }
}
