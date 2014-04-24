package co.lemonlabs.mortar.example.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public final class Lists {

    private Lists() {}

    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
        //noinspection unchecked
        return (elements instanceof Collection)
            ? new ArrayList<>((Collection<E>)(elements))
            : newArrayList(elements.iterator());
    }

    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
        ArrayList<E> list = new ArrayList<>();
        while (elements.hasNext()) {
            list.add(elements.next());
        }
        return list;
    }
}
