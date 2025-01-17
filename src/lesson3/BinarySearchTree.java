package lesson3;

import java.util.*;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }

        public boolean isLeaf() {
            return (left == null && right == null);
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Добавление элемента в дерево
     *
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     *
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     *
     * Пример
     */

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     *
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        List<Node<T>> list = findWithParent(root, (T) o);
        if (list == null) return false;
        else if (list.size() == 2) remove(list.get(0), list.get(1));
        else remove(list.get(0), null);
        return true;
    } // Трудоёмксоть - O(log(N)); Ресурсоёмкость - O(N)

    private void reassign(Node<T> parent, Node<T> current, Node<T> node) {
        if (parent.left != null && parent.left.value.equals(current.value)) parent.left = node;
        else parent.right = node;
    }

    private List<Node<T>> findWithParent(Node<T> start, T value) {
        if (start == null) return null;
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return List.of(start);
        }
        else if (comparison < 0) {
            if (start.left == null) return null;
            else if (start.left.value == value) {
                return List.of(start.left, start);
            } else return findWithParent(start.left, value);
        }
        else {
            if (start.right == null) return null;
            else if (start.right.value == value) {
                return List.of(start.right, start);
            } else return findWithParent(start.right, value);
        }
    }

    private Node<T> findSmallestParent(Node<T> start) {
        while (start.left.left != null) {
            start = start.left;
        }
        return start;
    }

    @SuppressWarnings("unchecked")
    private void remove(Node<T> current, Node<T> parent) {
        if (current.isLeaf()) {
            if (parent != null)
                reassign(parent, current, null);
            else root = null;
        }
        else if (current.left != null && current.right == null) {
            if (parent != null)
                reassign(parent, current, current.left);
            else root = current.left;
        }
        else if (current.left == null) {
            if (parent != null)
                reassign(parent, current, current.right);
            else root = current.right;
        }
        else  {
            if (current.right.left == null) {
                current.right.left = current.left;
                if (parent != null)
                    reassign(parent, current, current.right);
                else root = current.right;
            } else {
                Node<T> smallestP = findSmallestParent(current.right);
                Node<T> node = new Node(smallestP.left.value);
                if (smallestP.left.right != null) {
                    smallestP.left = smallestP.left.right;
                } else smallestP.left = null;
                node.left = current.left;
                node.right = current.right;
                if (parent != null)
                    reassign(parent, current, node);
                else root = node;
            }
        }
        size--;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }

    public class BinarySearchTreeIterator implements Iterator<T> {

        private final Stack<Node<T>> stack;
        private Node<T> current;
        private Node<T> parent;
        private boolean removed;

        private BinarySearchTreeIterator() {
            stack = new Stack<>();
            fillStack(root);
            current = null;
            removed = true;
        }

        /**
         * Проверка наличия следующего элемента
         *
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         *
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         *
         * Средняя
         */

        @Override
        public boolean hasNext() {
            return !stack.empty();
        } // Трудоёмкость - O(1); Ресурсоёмкость - O(1)

        /**
         * Получение следующего элемента
         *
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         *
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         *
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         *
         * Средняя
         */
        // Центрированный обход
        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            parent = current;
            current = stack.pop();
            if (current == root) parent = null;
            if (!stack.empty()) {
                Node<T> previous = stack.peek();
                if ((previous.left != null && previous.left.value.equals(current.value))
                        || (previous.right != null && previous.right.value.equals(current.value)))
                    parent = previous;
            }
            if (current.right != null) fillStack(current.right);
            removed = false;
            return current.value;
        } // Трудоёмксоть - O(N); Ресурсоёмкость - O(N)

        private void fillStack (Node<T> start) {
            while (start != null) {
                stack.push(start);
                start = start.left;
            }
        }

        /**
         * Удаление предыдущего элемента
         *
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         *
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         *
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         *
         * Сложная
         */

        @Override
        public void remove() {
            if (removed) throw new IllegalStateException();
            BinarySearchTree.this.remove(current, parent);
            removed = true;
        } // Трудоёмксоть - O(log(N)), в худшем O(N); Ресурсоёмкость - O(N)
    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if (fromElement == null || toElement == null) throw new NullPointerException();
        BinarySearchTree<T> tree = this;
        SortedSet<T> set = new TreeSet<>() {

            @Override
            public boolean add(T t) {
                if (fromElement.compareTo(t) > 0 || toElement.compareTo(t) <= 0) throw new IllegalArgumentException();
                tree.add(t);
                return super.add(t);
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean remove(Object o) {
                if (fromElement.compareTo((T) o) > 0 || toElement.compareTo((T) o) <= 0) throw new IllegalArgumentException();
                tree.remove(o);
                return super.remove(o);
            }

            @Override
            public boolean contains(Object o) {
                tree.fillSubSet(this, fromElement, toElement);
                return super.contains(o);
            }

            @Override
            public int size() {
                tree.fillSubSet(this, fromElement, toElement);
                return super.size();
            }

        };
        for (T value : tree) {
            if (value.compareTo(fromElement) >= 0 && value.compareTo(toElement) < 0) set.add(value);
        }
        return set;
    }

    private void fillSubSet (TreeSet<T> set, T fromElement, T toElement) {
        set.clear();
        for (T value : this) {
            if (value.compareTo(fromElement) >= 0 && value.compareTo(toElement) < 0) set.add(value);
        }
    } // Трудоёмксоть - O(N); Ресурсоёмкость - O(N)

    /**
     * Подмножество всех элементов строго меньше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

}