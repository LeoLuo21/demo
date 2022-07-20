package map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author leo
 * @date 20220717 22:15:40
 */
public class LLRBTree<K,V> implements Map<K,V> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private static class TreeNode<K,V> {
        K key;
        V value;
        TreeNode<K,V> parent;
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        boolean color;
        public TreeNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    private static boolean isRed(TreeNode x) {
        return x == null ? false : x.color == RED;
    }
    private TreeNode root;
    private int size;
    
    int moveRotate;
    int leftRotate;
    int rightRotate;
    int put;
    int delete;
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
    public void putAll(Map map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    private TreeNode<K,V> getNode(Object key) {
        TreeNode x = root;
        while (x != null) {
            int cmp = compare(key,x.key);
            if (cmp < 0) x = x.left;
            else if(cmp > 0) x = x.right;
            else return x;
        }
        return null;
    }

    @Override
    public V get(Object key) {
        TreeNode<K, V> node = getNode(key);
        return node == null ? null: node.value;
    }

    @Override
    public V put(K key, V value) {
        TreeNode<K,V> x;
        if(root == null) {
            x = new TreeNode<>(key,value);
            compare(key,x.key);
            root = x;
        }else {
            x = root;
            while (true) {
                int cmp = compare(key,x.key);
                if (cmp < 0) {
                    if (x.left == null) {
                        x.left = new TreeNode<>(key,value);
                        x.left.parent = x;
                        x = x.left;
                        break;
                    }
                    x= x.left;
                }else if(cmp > 0 ) {
                    if (x.right == null) {
                        x.right = new TreeNode<>(key,value);
                        x.right.parent = x;
                        x = x.right;
                        break;
                    }
                    x = x.right;
                }else {
                    V oldValue = x.value;
                    x.value = value;
                    return oldValue;
                }
            }
        }
        put++;
        size++;
        fixAfterInsertion(x);
        return null;
    }

    private void fixAfterInsertion(TreeNode x) {
        TreeNode p = x.parent;
        while (p != null) {
            p = balance(p);
            p = p.parent;
        }
        root.color = BLACK;
    }

    private void flipColors(TreeNode x) {
        x.color = !x.color;
        x.left.color = !x.left.color;
        x.right.color = !x.right.color;
    }

    @Override
    public V remove(Object key) {
        TreeNode<K, V> x = getNode(key);
        if (x == null) return null;
        V oldVal = x.value;
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        delete++;
        size--;
        fixAfterRemove(x);
        return oldVal;
    }

    private void fixAfterRemove(TreeNode x) {
        TreeNode t = root;
        TreeNode p;
        while (t != x) {
            int cmp = compare(x.key,t.key);
            if (cmp < 0) {
                if (!isRed(t.left) && !isRed(t.left.left)) {
                    t = moveRedLeft(t);
                }
                t = t.left;
            }else {
                if (isRed(t.left)) {
                    t.left.color = t.color;
                    t.color = RED;
                    rotateRight(t);
                    t = t.parent;
                }
                if (!isRed(t.right) && !isRed(t.right.left)) {
                    t = moveRedRight(t);
                }
                t = t.right;
            }
        }
        if (isRed(t.left)) {
            t.left.color = t.color;
            t.color = RED;
            rotateRight(t);
        }
        if (t.right == null) {
            p = t;
            if(!isRed(t)) {
                System.out.println("delete a black node");
            }
            delete(t);
        } else {
            if (!isRed(t.right) && !isRed(t.right.left)) {
                moveRedRight(t);
            }
            TreeNode r = successor(t);
            t.key = r.key;
            t.value = r.value;
            t = t.right;
            while (t != r) {
                if (!isRed(t.left) && !isRed(t.left.left)) {
                    t = moveRedLeft(t);
                }
                t = t.left;
            }
            p = t.parent;
            if(!isRed(t)) {
                System.out.println("delete a black node");
            }
            delete(t);
        }
        while (true) {
            if (p == root) {
                if (root != null) {
                    root = balance(p);
                }
                break;
            }
            p = balance(p);
            p = p.parent;
        }
        if (root != null) {
            root.color = BLACK;
        }
    }

    public void delete(TreeNode x) {
        if( x == root) {
            root = null;
            return;
        }
        if(x.parent.left == x) {
            x.parent.left = null;
        }else {
            x.parent.right = null;
        }
    }

    private TreeNode<K,V> successor (TreeNode x) {
        TreeNode r = x.right;
        if (r == null) return null;
        while (r.left != null) {
            r = r.left;
        }
        return r;
    }

    private TreeNode moveRedLeft( TreeNode p) {
        flipColors(p);
        if (isRed(p.right.left)) {
            moveRotate+=2;
            p.right.left.color = p.right.color;
            p.right.color = RED;
            rotateRight(p.right);
            p.right.color = p.color;
            p.color = RED;
            rotateLeft(p);
            p = p.parent;
            flipColors(p);
        }
        return p;
    }

    private TreeNode moveRedRight( TreeNode p) {
        flipColors(p);
        if (isRed(p.left.left)) {
            moveRotate+=1;
            p.left.color = p.color;
            p.color = RED;
            rotateRight(p);
            p = p.parent;
            flipColors(p);
        }
        return p;
    }

    public TreeNode balance(TreeNode p) {
        if (isRed(p.right) && !isRed(p.left)) {
            p.right.color = p.color;
            p.color = RED;
            rotateLeft(p);
            p = p.parent;
        }
        if (isRed(p.left) && isRed(p.left.left)) {
            p.left.color = p.color;
            p.color = RED;
            rotateRight(p);
            p = p.parent;
        }
        if (isRed(p.left) && isRed(p.right)) {
            flipColors(p);
        }
        return p;
    }

    private void rotateLeft(TreeNode<K,V> x) {
        leftRotate++;
        TreeNode<K,V> oldX = x;
        x = oldX.right;
        if (oldX.parent != null) {
            if (oldX == oldX.parent.left) {
                oldX.parent.left = x;
            }else {
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
        if (oldX == root) {
            root = x;
        }
    }
    private void rotateRight(TreeNode<K,V> x) {
        rightRotate++;
        TreeNode<K,V> oldX = x;
        x = oldX.left;
        if(oldX.parent != null) {
            if (oldX == oldX.parent.left) {
                oldX.parent.left = x;
            }else {
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
        if (oldX == root) {
            root = x;
        }
    }

    private int compare(Object var1, Object var2) {
        return ((Comparable)var1).compareTo(var2);
    }
    private boolean isLLRBT() {
        return is23() && isBalanced();
    }
    private boolean isBalanced() {
        int black = 0;     // number of black links on path from root to min
        TreeNode x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.left;
        }
        return isBalanced(root, black);
    }
    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(TreeNode x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.left, black) && isBalanced(x.right, black);
    }

    private boolean is23() { return is23(root); }
    private boolean is23(TreeNode x) {
        if (x == null) return true;
        if (isRed(x.right)) return false;
        if (x != root && isRed(x) && isRed(x.left))
            return false;
        return is23(x.left) && is23(x.right);
    }

    public static void main(String[] args) {
        int cycle = 1000000;
        LLRBTree llrbTree = new LLRBTree();
        Test.testMap(llrbTree,cycle);
        System.out.println("llrbTree.put = " + llrbTree.put);
        System.out.println("llrbTree.delete = " + llrbTree.delete);
        System.out.println("llrbTree.leftRotate = " + llrbTree.leftRotate);
        System.out.println("llrbTree.rightRotate = " + llrbTree.rightRotate);
        System.out.println("llrbTree.moveRotate = " + llrbTree.moveRotate);
        System.out.println("llrbTree.isLLRBT() = " + llrbTree.isLLRBT());
        while (true) {

        }
    }
}
