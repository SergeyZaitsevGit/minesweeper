package ru.minesweeper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewGameRequest {
    @Min(value = 2, message = "Высота должна быть больше 1")
    @Max(value = 30, message = "Высота должны быть меньше 31")
    private int width;
    @Min(value = 2, message = "Ширина должна быть больше 1")
    @Max(value = 30, message = "Ширина должны быть меньше 31")
    private int height;
    @JsonProperty("mines_count")
    private int minesCount;

    @AssertTrue(message = "Количество мин не может превышать (высота * ширина) - 1")
    public boolean isMinesCountValid() {
        return minesCount <= (width * height - 1);
    }
}
