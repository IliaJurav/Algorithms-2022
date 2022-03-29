package lesson1;
import kotlin.NotImplementedError;
import lesson1.Sorts;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortTimes(String inputName, String outputName) throws IOException, ParseException {
        List<Integer> times = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("hh:mm:ss aa");
        String line;
        try (Scanner sc = new Scanner(new File(inputName))){
            while (sc.hasNextLine()){
                times.add((int) df.parse(sc.nextLine()).getTime());
            }
        }
        times.sort(Integer::compareTo);
        Files.write(Path.of(outputName), (Iterable<String>) times.stream().map(df::format)::iterator);
    } // Трудоёмксоть - O(N*log(N)); Ресурсоёмкость - O(N)

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
     static public void sortAddresses(String inputName, String outputName) throws IOException {
        Comparator<String> byAddress = Comparator.comparing((String s) -> s.split(" ")[0])
                .thenComparing((String s) -> Integer.parseInt(s.split(" ")[1]));

        try (Stream<String> lines = Files.lines(Path.of(inputName))) {
            Map<String, TreeSet<String>> addresses = lines
                    .map(line -> line.split(" - "))
                    .collect(Collectors.groupingBy(s -> s[1],
                            () -> new TreeMap<>(byAddress), Collectors.mapping(s -> s[0],
                                    Collectors.toCollection(TreeSet::new))));

            try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputName))) {
                for(Map.Entry<String, TreeSet<String>> e:addresses.entrySet()) {
                    writer.write(e.getKey()+ " - " + String.join(", ", e.getValue()));
                    writer.newLine();
                }
            }
        }

    }
    // Трудоемкость - O(nlogn); Ресурсоемкость O(n)

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */

    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        ArrayList<Integer> entries = new ArrayList<>();

        try (BufferedReader file = Files.newBufferedReader(Paths.get(inputName))) {
            String inp;
            while ((inp = file.readLine()) != null) {
                entries.add(((int) (Double.parseDouble(inp) * 10)) + 2730);
            }
        }
        int[] entriesArr = entries.stream().mapToInt(i -> i).toArray();

        entriesArr = Sorts.countingSort(entriesArr, 7730);

        try (BufferedWriter out = Files.newBufferedWriter(Paths.get(outputName))) {
            for (int value : entriesArr) {
                out.write(String.valueOf((value - 2730) / 10.0));
                out.newLine();
            }
        }
    }
    //Трудоемкость: O(N + K) - Обработка данных - O(N), сортировка - O(N + K); Ресурсоемкость: O(K)

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */

    static public void sortSequence(String inputName, String outputName) throws IOException {
        try (FileReader fileReader = new FileReader(inputName)) {
            try (FileWriter fileWriter = new FileWriter(outputName)) {
                try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                    try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                        String line;
                        int minimum = Integer.MAX_VALUE;
                        int count = 0;
                        HashMap<Integer, Integer> result = new HashMap<>();
                        LinkedList<Integer> res = new LinkedList<>();
                        while ((line = bufferedReader.readLine()) != null) {
                            int num = Integer.parseInt(line);
                            res.add(num);
                            int c = 1;
                            if (result.containsKey(num))
                                c += result.getOrDefault(num,0);
                            result.put(num,c);

                            if (count < c) {
                                count = c;
                                minimum = num;
                            } else {
                                if (count == c)
                                    if (minimum > num)
                                        minimum = num;
                            }

                        }

                        for (Integer r : res)
                            if (r != minimum) {
                                bufferedWriter.write(r.toString());
                                bufferedWriter.newLine();
                            }
                        String mm = minimum +"\n";
                        while (count > 0) {
                            bufferedWriter.write(mm);
                            count--;
                        }
                    }
                }
            }
        }
    }
    // Трудоемкость - O(n); Ресурсоемкость O(n)

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        int i = 0;
        int j = first.length;
        int k = 0;
        while (i < first.length && j < second.length){
            if (first[i].compareTo(second[j]) < 0){
                second[k++] = first[i++];
            } else {
                second[k++] = second[j++];
            }
        }
        while (i < first.length) second[k++] = first[i++];
        while (j < second.length) second[k++] = second[j++];
    }
}
// Трудоемкость - O(n); Ресурсоемкость O(1)

