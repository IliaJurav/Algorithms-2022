package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.util.Arrays;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     * <p>
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     * <p>
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     * <p>
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        throw new NotImplementedError();
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     * <p>
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 5
     * <p>
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 х
     * <p>
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 х 3
     * 8   4
     * 7 6 Х
     * <p>
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     * <p>
     * 1 Х 3
     * х   4
     * 7 6 Х
     * <p>
     * 1 Х 3
     * Х   4
     * х 6 Х
     * <p>
     * х Х 3
     * Х   4
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   х
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   Х
     * Х х Х
     * <p>
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     * <p>
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */
    static public String longestCommonSubstring2(String first, String second) {
        int maxk = 0;
        int indx = 0;
        for (int i = 0; i + maxk < first.length() + second.length() - 1; i++) {
            int i1 = 0;
            if (i >= second.length()) i1 = i - second.length() + 1;
            int i2 = second.length() - i - 1;
            if (i2 < 0) i2 = 0;
            int k = 0;
            int j;
            i2 -= i1;
            for (j = i1; j + maxk - k < first.length() && j + i2 < second.length(); j++) {
                if (first.charAt(j) == second.charAt(j + i2)) {
                    k++;
                } else {
                    if (k > maxk) {
                        maxk = k;
                        indx = j - k;
                    }
                    k = 0;
                }
            }
            if (k > maxk) {
                maxk = k;
                indx = first.length() - k;
            }

        }
        if (maxk == 0) return "";
        return first.substring(indx, indx + maxk);
    }

    static public String longestCommonSubstring3(String first, String second) {
        int maxk = 0;
        int indx = 0;
        for (int i = 0; i + maxk < first.length(); i++) {
            for (int j = 0; j + maxk < second.length(); j++)
                if (first.charAt(i) == second.charAt(j)) {
                    int k;
                    for (k = 1; k + i < first.length() && j + k < second.length(); k++) {
                        if (first.charAt(k + i) != second.charAt(k + j)) break;
                    }
                    if (k > maxk) {
                        maxk = k;
                        indx = i;
                    }
                    j += k;
                }
        }
        if (maxk == 0) return "";
        return first.substring(indx, indx + maxk);
    }

    static public String longestCommonSubstring(String first, String second) {
        int maxk = 0;
        int indx = 0;
        int lenf = first.length();
        int lens = second.length();
        for (int i = 0; i + maxk < lenf; i++) {
            for (int j = 0; j < lens - maxk; j++)
                if (first.charAt(i) == second.charAt(j)) {
                    int k;
                    for (k = 1; k + i < lenf && k + j < lens; k++) {
                        if (first.charAt(k + i) != second.charAt(k + j)) break;
                    }
                    if (k > maxk) {
                        maxk = k;
                        indx = i;
                    }
                    j += k;
                }
        }
        if (maxk == 0) return "";
        return first.substring(indx, indx + maxk);
    }
    //
//    public static void main(String[] aa) {
//        System.out.println(longestCommonSubstring("213000000000000000223", "21340012823456789223"));
//        //System.out.println(longestCommonSubstring2("ОБСЕРВАТОРИЯ", "КОНСЕРВАТОРЫ"));
//
//    }

    /**
     * Число простых чисел в интервале
     * Простая
     * <p>
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     * <p>
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     * Время: O(n*Log(n))
     * Память: S(n)
     */
    static public int calcPrimesNumber(int limit) {
        if (limit <= 1) return 0;
        boolean[] isPrime = new boolean[limit + 1];
        Arrays.fill(isPrime, true);
        int count = 0;

        for (int i = 2; i <= limit; i++) {
            if (isPrime[i]) {
                count++;
                for (int j = 2 * i; j <= limit; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        return count;
    }
}
