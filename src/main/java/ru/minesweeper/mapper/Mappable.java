package ru.minesweeper.mapper;

public interface Mappable<D, E> {
    D toDto(E entity);
}
