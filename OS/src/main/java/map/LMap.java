package map;

import org.openjdk.jol.info.ClassLayout;

import java.util.*;

/**
 * @author leo
 * @date 20220714 20:46:55
 */
public class LMap<K,V> implements Map<K,V> {
    int hash (Object o) {
        return (o.hashCode() & 0x7fffffff) % capacity;
    }
    private static final int defaultCapacity = 17;
    private int capacity;
    private int size;
    private K[] keys;
    private V[] values;

    public LMap() {
        this(defaultCapacity);
    }

    public LMap(int capacity) {
        this.capacity = capacity;
        this.keys = (K[]) new Object[this.capacity];
        this.values = (V[]) new Object[this.capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public V get(Object o) {
        int index = hash(o);
        K key;
        while ((key = keys[index]) != null) {
            if (key.equals(o)) {
                return values[index];
            }
            index = (index + 1) % this.capacity;
        }
        return null;
    }

    @Override
    public V put(K k, V v) {
        int index = hash(k);
        while (true) {
            if (keys[index] == null) {
                keys[index] = k;
                values[index] = v;
                if (++this.size > this.capacity / 2) {
                    resize(2 * this.capacity);
                }
                return null;
            }else {
                if (keys[index].equals(k)) {
                    V oldVal = values[index];
                    values[index] = v;
                    return oldVal;
                }else {
                    index = (index+1) % this.capacity;
                }
            }
        }
    }

    public void resize(int newCap) {
        LMap<K, V> tmp = new LMap<K, V>(newCap);
        for (int i = 0; i < this.capacity; i++) {
            if (keys[i] != null) {
                tmp.put(keys[i], values[i]);
                keys[i] = null;
                values[i] = null;
            }
        }
        this.size = tmp.size;
        this.capacity = tmp.capacity;
        this.keys = tmp.keys;
        this.values = tmp.values;
        System.out.println("resize done");
    }

    @Override
    public V remove(Object o) {
        int index = hash(o);
        while (keys[index] != null) {
            if (keys[index].equals(o)) {
                V oldVal = values[index];
                keys[index] = null;
                values[index] = null;

                int i = (index + 1) % this.capacity;
                while (keys[i] != null) {
                    put(keys[i],values[i] );
                    i++;
                }
                this.size--;
                return oldVal;
            }else {
                index = (index + 1) % this.capacity;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {

    }

    @Override
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            keys[i] = null;
            values[i] = null;
        }
        this.size = 0;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    public static void main(String[] args) {
        int cap = 100000;
        Test.testMap(new LMap<>(cap),cap / 2);
        Test.testMap(new HashMap(cap / 2),cap / 2);
    }
}
