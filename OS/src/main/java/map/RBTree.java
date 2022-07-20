package map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author leo
 * @date 20220715 13:38:34
 */
public class RBTree<K,V> implements Map<K,V> {
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
    private TreeNode<K,V> root;
    private int size;

    int put;
    int delete;

    int leftRotate;

    int rightRotate;

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
        return getTreeNode(o) != null;
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
        size = 0;
        root = null;
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

    private TreeNode<K,V> getTreeNode(Object key) {
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
        TreeNode<K, V> node = getTreeNode(key);
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
        fixAfterInsertion(x);
        size++;
        put++;
        return null;
    }

    public void fixAfterInsertion(TreeNode<K,V> x) {
        TreeNode<K,V> p;
        while (isRed((p = x.parent))) {
            TreeNode<K,V> gp;
            TreeNode<K,V> uncle;
            if ((gp = p.parent) == null) {
                break;
            }
            if (p == gp.left) {
                uncle = gp.right;
                if (isRed(uncle)) {
                    gp.color = RED;
                    p.color = BLACK;
                    uncle.color = BLACK;
                    x = gp;
                }else {
                    if (x == p.right) {
                        rotateLeft(p);
                        x = p;
                    }
                    x.parent.color = BLACK;
                    gp.color = RED;
                    rotateRight(gp);
                    break;
                }
            } else {
                uncle = gp.left;
                if (isRed(uncle)) {
                    gp.color = RED;
                    p.color = BLACK;
                    uncle.color = BLACK;
                    x = gp;
                }else {
                    if (x == p.left) {
                        rotateRight(p);
                        x = p;
                    }
                    x.parent.color = BLACK;
                    gp.color = RED;
                    rotateLeft(gp);
                    break;
                }
            }
        }
        root.color = BLACK;
    }

    @Override
    public V remove(Object key) {
        TreeNode<K, V> x = getTreeNode(key);
        if (x == null) return null;
        V oldVal = x.value;
        if (x.left != null && x.right != null) {
            TreeNode<K, V> successor = successor(x);
            x.key = successor.key;
            x.value = successor.value;
            x = successor;
        }
        fixAfterRemove(x);
        size--;
        delete++;
        return oldVal;
    }

    public void fixAfterRemove(TreeNode x) {
        if(x.left != null) {
            x.key = x.left.key;
            x.value = x.left.value;
            delete(x.left);
        } else if (x.right != null) {
            x.key = x.right.key;
            x.value = x.right.value;
            delete(x.right);
        }else {
            TreeNode oldX = x;
            while (!isRed(x) && x != root) {
                TreeNode p = x.parent;
                if (x.parent.left == x) {
                    TreeNode sibling = x.parent.right;
                    if (isRed(sibling)) {
                        sibling.color = BLACK;
                        p.color = RED;
                        rotateLeft(p);
                    }else {
                        if (!isRed(sibling.left) && !isRed(sibling.right)) {
                            sibling.color = RED;
                            x = p;
                        }else if (isRed(sibling.left)) {
                            sibling.color = RED;
                            sibling.left.color = BLACK;
                            rotateRight(sibling);
                        }else{
                            sibling.color = p.color;
                            p.color = BLACK;
                            sibling.right.color = BLACK;
                            rotateLeft(p);
                            break;
                        }
                    }
                }else {
                    TreeNode sibling = x.parent.left;
                    if (isRed(sibling)) {
                        sibling.color = BLACK;
                        p.color = RED;
                        rotateRight(p);
                    }else {
                        if (!isRed(sibling.left) && !isRed(sibling.right)) {
                            sibling.color = RED;
                            x = p;
                        }else if (isRed(sibling.right)) {
                            sibling.color = RED;
                            sibling.right.color = BLACK;
                            rotateLeft(sibling);
                        }else {
                            sibling.color = p.color;
                            p.color = BLACK;
                            sibling.left.color = BLACK;
                            rotateRight(p);
                            break;
                        }
                    }
                }
            }
            x.color = BLACK;
            delete(oldX);
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
    private boolean isRBT() {
        return is234() && isBalanced();
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

    private boolean is234() {
        return is234(root);
    }

    private boolean is234(TreeNode x) {
        if (x == null) return true;
        if (isRed(x) && (isRed(x.left) || isRed(x.right))) {
            return false;
        }
        return is234(x.left) && is234(x.right);
    }

    public static void main(String[] args) {
        int cycle = 1000000;
        RBTree rbTree = new RBTree();
        Test.testMap(new TreeMap(),cycle);
        Test.testMap(rbTree,cycle);
        System.out.println("rbTree.put = " + rbTree.put);
        System.out.println("rbTree.delete = " + rbTree.delete);
        System.out.println("rbTree.leftRotate = " + rbTree.leftRotate);
        System.out.println("rbTree.rightRotate = " + rbTree.rightRotate);
        System.out.println("rbTree.isRBT() = " + rbTree.isRBT());
        while (true) {

        }
    }
}
