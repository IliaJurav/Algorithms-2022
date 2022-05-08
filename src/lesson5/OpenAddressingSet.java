package lesson5;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        storage = new Object[capacity];
    }

    private final Object removed = new Object();

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int startingIndex = startingIndex(o);
        int index = startingIndex(o);
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % capacity;
             /* если все ячейки таблицы будут заполнены, и мы будем искать объект,
            которого в таблице нет, произойдёт зацикливание (грубо говоря без условия ниже
            проверка на наличие элемента (8) в собственном тесте #1 к remove будет длиться вечно)*/
            if (index == startingIndex) break;
            current = storage[index];
        }
        return false;
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && current != removed) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        int startingIndex = startingIndex(o);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                storage[index] = removed;
                size--;
                return true;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) break;
            current = storage[index];
        }
        return false;
    } // Трудоёмксоть - O(N); Ресурсоёмкость - O(N)

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() { return new OpenAddressingSetIterator();}

    public class OpenAddressingSetIterator implements Iterator<T> {

        private int index = 0;
        private int iterations = 0;
        Object current;

        @Override
        public boolean hasNext() {
            return iterations < size;
        } // Трудоёмкость - O(1); Ресурсоёмкость - O(1)

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (hasNext()) {
                while (storage[index] == null || storage[index] == removed)
                    if(++index == capacity) throw new NoSuchElementException();;
                current = storage[index];
                index++;
                iterations++;
                return (T) current;
            } else throw new NoSuchElementException();
        } // Трудоёмкость - O(N); Ресурсоёмкость - O(N)

        @Override
        public void remove() {
            if (current != null && current != removed) {
                storage[index - 1] = removed;
                iterations--;
                size--;
            } else throw new IllegalStateException();
        } // Трудоёмкость - O(1); Ресурсоёмкость - O(1)
    }
}
