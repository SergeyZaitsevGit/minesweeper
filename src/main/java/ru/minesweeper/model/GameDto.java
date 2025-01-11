package ru.minesweeper.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {
    @JsonProperty("game_id")
    private String gameId;
    private int width;
    private int height;
    @JsonProperty("mines_count")
    private int minesCount;
    private char[][] field;
    private boolean completed;

    @JsonGetter("field")
    public String[][] getFieldAsStringArray() {
        String[][] result = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result[i][j] = String.valueOf(field[i][j]);
            }
        }
        return result;
    }

    @JsonSetter("field")
    public void setFieldFromStringArray(String[][] fieldStrings) {
        this.field = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.field[i][j] = fieldStrings[i][j].charAt(0);
            }
        }
    }
}
