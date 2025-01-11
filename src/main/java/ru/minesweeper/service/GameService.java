package ru.minesweeper.service;

import ru.minesweeper.model.Game;
import ru.minesweeper.model.NewGameRequest;
import ru.minesweeper.model.TurnRequest;

public interface GameService {
    Game createGame(NewGameRequest newGameRequest);

    Game turn(TurnRequest turnRequest);
}
