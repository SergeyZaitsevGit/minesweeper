package ru.minesweeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.minesweeper.model.Game;
import ru.minesweeper.model.NewGameRequest;
import ru.minesweeper.model.TurnRequest;
import ru.minesweeper.repository.GameRepository;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    /***
     * Логика создания новой игры
     * @param newGameRequest запрос на создание новой игры
     * @return сохраненная игра
     */
    @Override
    public Game createGame(NewGameRequest newGameRequest) {
        Game game = initializeGame(newGameRequest);
        gameRepository.saveGame(game);
        return game;
    }

    @Override
    public Game turn(TurnRequest turnRequest) {
        int row = turnRequest.getRow();
        int col = turnRequest.getCol();
        Game game = gameRepository.findGame(turnRequest.getGameId());

        if (game.isCompleted()) {
            throw new RuntimeException("Нельзя ходить после завершения игры");
        }

        if (game.getField()[row][col] != ' ') {
            game.setCompleted(true);
            gameRepository.saveGame(game);
            throw new RuntimeException("Нельзя открывать уже открытые ячейки");
        }

        boolean compile = openCellRecursive(row, col, game);
        game.setCompleted(compile);
        gameRepository.saveGame(game);
        return game;
    }

    /***
     * Метод инициализирует объект игры:
     * рандомно расставляет мины и
     * помечает ячейки как неоткрытые
     * @param newGameRequest объект запроса создания игры
     * @return объект игры
     */
    private Game initializeGame(NewGameRequest newGameRequest) {
        int height = newGameRequest.getHeight();
        int width = newGameRequest.getWidth();
        int minesCount = newGameRequest.getMinesCount();
        char[][] field = new char[height][width];
        boolean[][] mines = new boolean[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = ' ';
            }
        }

        Random random = new Random();
        int placedMines = 0;

        while (placedMines < minesCount) {
            int row = random.nextInt(height);
            int col = random.nextInt(width);
            if (!mines[row][col]) {
                mines[row][col] = true;
                placedMines++;
            }
        }
        return Game.builder()
                .minesCount(newGameRequest.getMinesCount())
                .width(newGameRequest.getWidth())
                .height(newGameRequest.getHeight())
                .field(field)
                .mines(mines)
                .build();
    }

    /***
     * Метод считает количество мин вокруг ячейки
     * @param mines поле мин
     * @param row номер строки
     * @param col номер колонки
     * @param height высота поля
     * @param width ширина поля
     * @return количество мин вокруг ячейки
     */
    private int countAdjacentMines(boolean[][] mines, int row, int col, int height, int width) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newRow = row + i;
                int newCol = col + j;
                if (isInBounds(newRow, newCol, height, width) && mines[newRow][newCol]) {
                    count++;
                }
            }
        }
        return count;
    }


    private boolean isInBounds(int row, int col, int height, int width) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    /***
     * Логика открытия ячеек в зависимости от хода
     * @param row номер строки хода
     * @param col номер колонки хода
     * @param game объект текущей игры
     * @return флаг, показывающий была ли завершена игра данным ходом
     *  ***/
    private boolean openCellRecursive(int row, int col, Game game) {
        int height = game.getHeight();
        int width = game.getWidth();
        char[][] field = game.getField();
        boolean[][] mines = game.getMines();

        if (!isInBounds(row, col, height, width) || field[row][col] != ' ') {
            return false;
        }

        if (mines[row][col]) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (mines[i][j]) {
                        field[i][j] = 'X';
                    } else if (field[i][j] == ' ') {
                        field[i][j] = Character.forDigit(countAdjacentMines(mines, row, col, height, width), 10);
                    }
                }
            }
            field[row][col] = 'X';
            return true;
        }

        int countClose = 0;

        field[row][col] = Character.forDigit(countAdjacentMines(mines, row, col, height, width), 10);

        if (field[row][col] == '0') {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue;
                    openCellRecursive(row + i, col + j, game);
                }
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (field[i][j] == ' ') {
                    countClose++;
                }
            }
        }

        if (countClose == game.getMinesCount()) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (mines[i][j]) {
                        field[i][j] = 'M';
                    }
                }
            }
            return true;
        }
        return false;
    }

}
