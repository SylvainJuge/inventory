package sylvain.juge.inventory.util;

import java.util.*;

public final class ReadOnlyList<T> extends AbstractList<T> {

    private final List<T> list;

    @SafeVarargs // safe because we don't try to alter varargs param array
    public ReadOnlyList(T... values){
        this.list = Arrays.asList(values);
    }

    public ReadOnlyList(List<T> list){
        this.list = new ArrayList<>(list);
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }
}
