package ru.minesweeper.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game implements Serializable {
    private String gameId;
    private int width;
    private int height;
    private int minesCount;
    private char[][] field;
    private boolean completed;
    private boolean[][] mines;
}
