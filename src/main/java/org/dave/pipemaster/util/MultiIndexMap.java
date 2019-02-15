package org.dave.pipemaster.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.dave.pipemaster.util.MultiIndexMap.EnumCategory.NUM;

public class MultiIndexMap<V> extends HashSet<V> {
    List<IndexerDefinition> indexers = new ArrayList<>();
    Map<IEnumCategory, Map<Object, Set<V>>> indices = new HashMap<>();

    int index = 0;

    public MultiIndexMap() {
        addIndex(NUM, int.class, v -> index++);
    }

    @Override
    public boolean add(V element) {
        boolean success = super.add(element);
        if(success) {
            for(IndexerDefinition indexer : indexers) {
                Object key = indexer.indexFunction.apply(element);
                if(key == null) {
                    continue;
                }

                if(!indices.containsKey(indexer.categoryName)) {
                    indices.put(indexer.categoryName, new HashMap<>());
                }

                if(!indices.get(indexer.categoryName).containsKey(key)) {
                    indices.get(indexer.categoryName).put(key, new HashSet<>());
                }

                indices.get(indexer.categoryName).get(key).add(element);
            }
        }

        return success;
    }

    public V getRandomElement(Random rand) {
        int index = rand.nextInt(this.size());
        return getFirstElement(NUM, index);
    }

    public Set<V> getElementsBy(IEnumCategory index, Object key) {
        if(!indices.containsKey(index)) {
            return Collections.emptySet();
        }

        if(!indices.get(index).containsKey(key)) {
            return Collections.emptySet();
        }

        return indices.get(index).get(key);
    }

    public V getFirstElement(IEnumCategory index, Object key) {
        return getElementsBy(index, key).stream().findFirst().orElse(null);
    }

    public <R> Set<R> keySet(Class<R> typeOfSet, IEnumCategory index) {
        if(!indices.containsKey(index)) {
            return Collections.emptySet();
        }

        return (Set<R>) indices.get(index).keySet();
    }

    public Collection<V> values() {
        if(!indices.containsKey(NUM)) {
            return Collections.emptySet();
        }

        Set<V> result = new HashSet<>();
        for(Set<V> vals : indices.get(NUM).values()) {
            result.addAll(vals);
        }

        return result;
    }

    public boolean contains(IEnumCategory index, Object key) {
        if(!indices.containsKey(index)) {
            return false;
        }

        return indices.get(index).containsKey(key);
    }

    public <T> void addIndex(IEnumCategory index, Class<T> type, Function<V, T> indexFunction) {
        indexers.add(new IndexerDefinition<>(index, indexFunction));
    }

    private static class IndexerDefinition<V, T> {
        public IEnumCategory categoryName;
        public Function<V, T> indexFunction;

        public IndexerDefinition(IEnumCategory categoryName, Function<V, T> indexFunction) {
            this.categoryName = categoryName;
            this.indexFunction = indexFunction;
        }
    }

    public static interface IEnumCategory {
    }

    public enum EnumCategory implements IEnumCategory {
        NUM
    }
}
