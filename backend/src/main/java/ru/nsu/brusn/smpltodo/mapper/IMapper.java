package ru.nsu.brusn.smpltodo.mapper;

public interface IMapper<N, M> {
    M getMapped(N object);
}
