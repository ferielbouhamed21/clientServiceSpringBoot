package com.example.demo.mappers;

import java.util.List;

public interface AbstractMapper<D,C,E> {

    D toDto (E e);
    E toNewEntity(C c);
    E toEntity(D d);
    List<D> toDtos (List<E> e);
    List<E> toEntities(List<D> d);

}
