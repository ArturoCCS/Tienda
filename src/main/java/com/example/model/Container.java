package com.example.model;

import com.example.interfaces.Keyable;
import com.example.interfaces.Operable;

import java.util.ArrayList;
import java.util.List;

public class Container<T extends Keyable> implements Operable<T> {
// Catalogo tiene parametro de tipo <T>
// Mientras que Operable tiene argumento de tipo <T>

    List<T> contender;

    public Container() {
        super();
        contender = new ArrayList<>();
    }

    @Override
    public boolean add(T item) {
        return contender.add(item);
    }
// Metodo addAll

    @Override
    public boolean remove(T item) {
        return contender.remove(item);
    }

    public boolean remove(String key) {
        return contender.remove(key);
    }

    @Override
    public boolean contains(T item) {
        return contender.contains(item);
    }

    @Override
    public T get(int index) {
        return contender.get(index);
    }

    @Override
    public int getPosition(T item) {
        return contender.indexOf(item);
    }

    public int getPosition(String key) {
        for (int i = 0; i < contender.size(); i++)
            if (contender.get(i).getKey().equals(key))
                return i;
        return -1;
    }

    @Override
    public void sort() {
        contender.sort(null);
    }

    @Override
    public List<T> getAll() {
        return contender;
    }

    @Override
    public boolean isEmpty() {
        return contender.isEmpty();
    }

    @Override
    public void addAll(List<? extends T> items) {
        contender.addAll(items);
    }


}
