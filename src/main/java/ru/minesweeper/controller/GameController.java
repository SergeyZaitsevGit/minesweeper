package ru.minesweeper.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.minesweeper.mapper.GameMapper;
import ru.minesweeper.model.GameDto;
import ru.minesweeper.model.NewGameRequest;
import ru.minesweeper.model.TurnRequest;
import ru.minesweeper.service.GameService;

@RestController
@RequestMapping("/minesweeper/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;
    private final GameMapper gameMapper;

    @PostMapping("/new")
    public GameDto newGame(@RequestBody @Valid NewGameRequest newGameRequest) {
        return gameMapper.toDto(gameService.createGame(newGameRequest));
    }

    @PostMapping("/turn")
    public GameDto turn(@RequestBody TurnRequest turnRequest) {
        return gameMapper.toDto(gameService.turn(turnRequest));
    }
}
