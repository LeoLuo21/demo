package map;

import org.openjdk.jol.info.ClassLayout;

import java.util.*;

/**
 * @author leo
 * @date 20220713 16:01:55
 */
public class SMap<K,V> implements Map<K,V> {

    private static class Node<K,V> {
        private K k;
        private V v;
        private Node<K,V> next;

        public Node(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

    private static final int defaultCapacity = 17;
    private int capacity;
    private int N = 0;
    private Node<K,V>[] buckets;

    public SMap() {
        this(defaultCapacity);
    }

    public SMap(int capacity) {
        this.capacity = capacity;
        this.buckets = new Node[this.capacity];
    }

    int hash(Object key) {
        return (key.hashCode() & 0x7fffffff) % this.capacity;
    }

    public void resize() {
        if (this.capacity >= 1073741824) {
            return;
        }
        SMap<K, V> temp = new SMap<>(this.capacity * 2);
        Node<K,V>[] oldTable = this.buckets;
        this.buckets = new Node[this.capacity];
        for (int i = 0; i < oldTable.length; i++) {
            for(Node<K,V> e = oldTable[i]; e != null; e = e.next) {
                temp.put(e.k,e.v);
            }
        }
        this.capacity = temp.capacity;
        this.buckets = temp.buckets;
        this.N = temp.N;
        System.out.println("resize done");
    }

    @Override
    public int size() {
        return this.N;
    }

    @Override
    public boolean isEmpty() {
        return this.N == 0;
    }

    @Override
    public boolean containsKey(Object o) {
        return this.getNode(o) != null;
    }

    @Override
    public boolean containsValue(Object o) {
        throw new UnsupportedOperationException();
    }

    private Node<K,V> getNode(Object o) {
        Objects.requireNonNull(o);
        int index = hash(o);
        Node<K,V> node = this.buckets[index];
        while (node != null) {
            if (node.k.equals(o)) {
                return node;
            }else {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public V get(Object o) {
        Objects.requireNonNull(o);
        Node<K,V> node = getNode(o);
        return node == null ? null :node.v;
    }

    @Override
    public V put(K k, V v) {
        Objects.requireNonNull(k);
        Node<K, V> node = getNode(k);
        if (node != null) {
            V oldVal = node.v;
            node.v = v;
            return oldVal;
        }
        Node<K,V> newNode = new Node<>(k,v);
        int index = hash(k);
        if (this.buckets[index] == null) {
            this.buckets[index] = newNode;
        }else {
            newNode.next = this.buckets[index];
            this.buckets[index] = newNode;
        }
        this.N++;
        if (this.N > this.capacity) {
            this.resize();
        }
        return v;
    }

    @Override
    public V remove(Object o) {
        Objects.requireNonNull(o);
        int index = hash(o);
        Node<K, V> p = this.buckets[index];
        if ( p != null ) {
            if (p.k.equals(o)) {
                V result = p.v;
                this.buckets[index] = p.next;
                this.N--;
                return result;
            }
            while (p.next != null) {
                if (p.next.k.equals(o)) {
                    V result = p.next.v;
                    p.next = p.next.next;
                    this.N--;
                    return result;
                }
                p = p.next;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException("This method not implemented yet.");
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            this.buckets[i] = null;
        }
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        int cap = 100000;
        Test.testMap(new SMap(cap),cap);
        Test.testMap(new HashMap(cap),cap);
    }

}
