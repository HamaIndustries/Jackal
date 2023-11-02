package hama.industries.jackal.lib;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class LibCollections {
    public static <E> Set<E> makeWeakHashSet(){
        return Collections.newSetFromMap(new WeakHashMap<E, Boolean>());
    }
}
