package com.example.interfaces;

import java.util.List;

public interface Operable<T> {
    boolean add(T item);
    void addAll(List<? extends T> items);
    boolean remove(T item);
    boolean contains(T item);
    T get(int index);
    int getPosition(T item);
    void sort();
    List<T> getAll();
    boolean isEmpty();
}