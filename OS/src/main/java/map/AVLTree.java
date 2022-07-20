package map;

import org.openjdk.jol.info.ClassLayout;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author leo
 * @date 20220719 13:06:39
 */
public class AVLTree<K,V> implements Map<K,V> {
    private Node root;
    private int size;
    int leftRotate;
    int rightRotate;
    
    int put;
    int delete;

    int compare(Object key, Object another) {
        return ((Comparable)key).compareTo(another);
    }

    int height(Node x) {
        if (x == null) return -1;
        return x.height;
    }

    int computeHeight(Node x) {
        return Math.max(height(x.left),height(x.right)) + 1;
    }

    int balanceFactor(Node x) {
        return height(x.left) - height(x.right);
    }

    void balance(Node x) {
        while ( x != null) {
            x.height = computeHeight(x);
            if (balanceFactor(x) < -1) {
                if (balanceFactor(x.right) > 0) {
                    rotateRight(x.right);
                }
                rotateLeft(x);
            } else if (balanceFactor(x) > 1) {
                if (balanceFactor(x.left) < 0) {
                    rotateLeft(x.left);
                }
                rotateRight(x);
            }else {
                x = x.parent;
            }
        }
    }

    @Override
    public V get(Object key) {
        Node<K,V> x = getNode(key);
        return x == null ? null: x.value;
    }

    public Node<K,V> getNode(Object key) {
        Node<K,V> x = root;
        while (x != null) {
            int cmp = compare(key,x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0 ) {
                x = x.right;
            }else {
                return x;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (root == null) {
            root = new Node(key,value);
            return null;
        }
        Node<K,V> x = root;
        while (true) {
            int cmp = compare(key,x.key);
            if (cmp < 0) {
                if (x.left == null) {
                    x.left = new Node<>(key,value);
                    x.left.parent = x;
                    x.height = Math.max(height(x.left),height(x.right)) + 1;
                    break;
                }
                x = x.left;
            } else if (cmp >0) {
                if (x.right == null) {
                    x.right = new Node<>(key,value);
                    x.right.parent = x;
                    x.height = Math.max(height(x.left),height(x.right)) + 1;
                    break;
                }
                x = x.right;
            }else {
                return x.value;
            }
        }
        balance(x);
        put++;
        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        Node<K,V> x = getNode(key);
        if (x == null) return null;
        fixAfterRemove(x);
        delete++;
        size--;
        return x.value;
    }

    void fixAfterRemove(Node x) {
        Node p = x.parent;
        if (x.left == null || x.right == null) {
            if (p.left == x) {
                if (x.left == null) {
                    p.left = x.right;
                    if (x.right != null) {
                        x.right.parent = p;
                    }
                }else  {
                    p.left = x.left;
                    if (x.left != null) {
                        x.left.parent = p;
                    }
                }
            }else {
                if (x.left == null) {
                    p.right = x.right;
                    if (x.right != null) {
                        x.right.parent = p;
                    }
                }else  {
                    p.right = x.left;
                    if (x.left != null) {
                        x.left.parent = p;
                    }
                }
            }
        } else {
            Node y = x;
            x = min(y.right);
            y.key = x.key;
            y.value = x.value;
            if (x == y.right) {
                y.right = x.right;
            }else {
                x.parent.left = x.right;
            }
            p = x.parent;
            if (x.right != null) {
                x.right.parent = p;
            }
        }
        balance(p);
    }

    Node min(Node x) {
        while (x.left != null) {
            x = x.left;
        }
        return x;
    }

    void rotateLeft(Node x) {
        leftRotate++;
        Node oldX = x;
        x = oldX.right;
        if (oldX.parent != null) {
            if (oldX.parent.left == oldX) {
                oldX.parent.left = x;
            }
            else {
                oldX.parent.right = x;
            }
        }
        x.parent = oldX.parent;
        oldX.right = x.left;
        if (x.left != null) {
            x.left.parent = oldX;
        }
        x.left = oldX;
        oldX.parent = x;
        oldX.height = computeHeight(oldX);
        x.height = computeHeight(x);
        if (oldX == root) {
            root = x;
        }
    }

    void rotateRight(Node x) {
        rightRotate++;
        Node oldX = x;
        x = oldX.left;
        if (oldX.parent != null) {
            if (oldX.parent.left == oldX) {
                oldX.parent.left = x;
            }
            else {
                oldX.parent.right = x;
            }
        }
        x.parent = oldX.parent;
        oldX.left = x.right;
        if (x.right != null) {
            x.right.parent = oldX;
        }
        x.right = oldX;
        oldX.parent = x;
        oldX.height = computeHeight(oldX);
        x.height = computeHeight(x);
        if (oldX == root) {
            root = x;
        }
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
        return getNode(o) != null;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {

    }

    @Override
    public void clear() {

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

    private static class Node<K,V> {
        K key;
        V value;
        Node<K,V> left;
        Node<K,V> right;

        Node<K,V> parent;
        int height;
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private boolean isAVL() {
        return isAVL(root);
    }

    /**
     * Checks if AVL property is consistent in the subtree.
     *
     * @param x the subtree
     * @return {@code true} if AVL property is consistent in the subtree
     */
    private boolean isAVL(Node x) {
        if (x == null) return true;
        int bf = balanceFactor(x);
        if (bf > 1 || bf < -1) {
            return false;
        }
        return isAVL(x.left) && isAVL(x.right);
    }

    public static void main(String[] args) {
        int cycle = 1000000;
        AVLTree<Object, Object> avlTree = new AVLTree<>();
        Test.testMap(avlTree,cycle);
        System.out.println("avlTree.isAVL() = " + avlTree.isAVL());
        System.out.println("avlTree.put = " + avlTree.put);
        System.out.println("avlTree.delete = " + avlTree.delete);
        System.out.println("avlTree.leftRotate = " + avlTree.leftRotate);
        System.out.println("avlTree.rightRotate = " + avlTree.rightRotate);
        System.out.println("avlTree.isAVL() = " + avlTree.isAVL());
        while (true) {

        }
    }
}
