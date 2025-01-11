package ru.minesweeper.mapper;

import org.mapstruct.Mapper;
import ru.minesweeper.model.Game;
import ru.minesweeper.model.GameDto;

@Mapper(componentModel = "spring")
public interface GameMapper extends Mappable<GameDto, Game> {
}
