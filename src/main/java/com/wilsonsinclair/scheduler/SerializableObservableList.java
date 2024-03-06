package com.wilsonsinclair.scheduler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableListBase;

public class SerializableObservableList<E> extends ObservableListBase<E> implements Externalizable {
    private List<E> data;

    public SerializableObservableList() {
        data = new ArrayList<>();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public E get(int index) {
        return data.get(index);
    }

    public List<E> getData() { return data; }

    public SerializableObservableList(List<E> list) {
        data = new ArrayList<>(list);
    }

    @Override
    public void add(int index, E element) {
        data.add(index, element);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        data = (List<E>) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(data);
    }
}