package lesson6;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {

    //Трудоемкость - O(n), n - размер графа
    //Ресурсоемкость - O(n), n -размер графа
    isCyclic(graph);
    Set<Graph.Vertex> vertices = graph.getVertices();
    Set<Graph.Vertex> result = new HashSet<>(vertices);
        for (Graph.Vertex vertex : vertices) {
        if (!result.contains(vertex)) continue;
        Set<Graph.Vertex> neighbours = graph.getNeighbors(vertex);
        neighbours.forEach(result::remove);
    }
        return result;
}

    private static void isCyclic(Graph graph) {
        // Трудоемкость  - O(n*n)?
        //Ресурсоемкость - O(n) , n - размер графа
        Set<Graph.Vertex> vertices = graph.getVertices();
        Set<Graph.Edge> visited = new HashSet<>();
        Map<Graph.Vertex ,Graph.Edge> rootEdges;
        Set<Graph.Edge> previous = null;
        for (Graph.Vertex vertex: vertices) {
            visited.clear();
            dfs(visited,vertex,null, graph);
        }
    }


    private static void dfs(Set<Graph.Edge> visited, Graph.Vertex current,Graph.Edge previous, Graph graph) {
        Map<Graph.Vertex, Graph.Edge> rootEdges = graph.getConnections(current);

        for (Map.Entry<Graph.Vertex, Graph.Edge> keyVal : rootEdges.entrySet()) {
            Graph.Edge edge = keyVal.getValue();
            //если дважды проходим по одному и тому же ребру - цикл
            if (visited.contains(edge) && previous != edge) {
                throw new IllegalArgumentException();
            }
            visited.add(edge);
            if (previous != edge) dfs(visited, keyVal.getKey(), edge, graph);
        }
    }
        /**
         * Наидлиннейший простой путь.
         * Сложная
         *
         * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
         * Простым считается путь, вершины в котором не повторяются.
         * Если таких путей несколько, вернуть любой из них.
         *
         * Пример:
         *
         *      G -- H
         *      |    |
         * A -- B -- C -- D
         * |    |    |    |
         * E    F -- I    |
         * |              |
         * J ------------ K
         *
         * Ответ: A, E, J, K, D, C, H, G, B, F, I
         */
    public static Path longestSimplePath(Graph graph) {

        // Трудоемкость - O(n+v) , n- число вершин, v - число ребер
        //Ресурсоемкость - O(n) , n- число вершин
        Set<Graph.Vertex> vertices = graph.getVertices();
        if (vertices.isEmpty()) return new Path();
        PriorityQueue<Path> queue = new PriorityQueue<>();
        vertices.forEach(vertex -> queue.add(new Path(vertex)));
        Path longest = queue.poll();
        Path currentPath;
        while ((currentPath = queue.poll()) != null) {
            List<Graph.Vertex> currentPathVerticles = currentPath.getVertices();
            Set<Graph.Vertex> neighbours = graph.getNeighbors(currentPathVerticles.get(currentPathVerticles.size() - 1)); // соседи последнего узла в Path
            for (Graph.Vertex vertex : neighbours) {
                if (!currentPath.contains(vertex)) { // проверка , не содержится ли узел в Path
                    Path newPath = new Path(currentPath, graph, vertex);
                    queue.add(newPath);
                    if (newPath.getLength() > longest.getLength())
                        longest = newPath;
                }
            }
        }
        return longest;
    }

    /**
     * Балда
     * Сложная
     *
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
