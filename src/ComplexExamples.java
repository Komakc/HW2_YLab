import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class ComplexExamples {

    static class Person {
        final int id;

        final String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;
            return getId() == person.getId() && getName().equals(person.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName());
        }
    }

    private static Person[] RAW_DATA = new Person[]{new Person(0, "Harry"), new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(8, "Amelia"),
            new Person(7, "Amelia"),};
        /*  Raw data:

        0 - Harry
        0 - Harry
        1 - Harry
        2 - Harry
        3 - Emily
        4 - Jack
        4 - Jack
        5 - Amelia
        5 - Amelia
        6 - Amelia
        7 - Amelia
        8 - Amelia

        **************************************************

        Duplicate filtered, grouped by name, sorted by name and id:

        Amelia:
        1 - Amelia (5)
        2 - Amelia (6)
        3 - Amelia (7)
        4 - Amelia (8)
        Emily:
        1 - Emily (3)
        Harry:
        1 - Harry (0)
        2 - Harry (1)
        3 - Harry (2)
        Jack:
        1 - Jack (4)
     */

    public static void main(String[] args) {
        System.out.println("Raw data:");
        System.out.println();

        for (Person person : RAW_DATA) {
            System.out.println(person.id + " - " + person.name);
        }

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();
        System.out.println("Duplicate filtered, grouped by name, sorted by name and id:");
        System.out.println();

        /*
        Task1
            Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени

            Что должно получиться
                Key: Amelia
                Value:4
                Key: Emily
                Value:1
                Key: Harry
                Value:3
                Key: Jack
                Value:1
         */

        Map<String, Long> mapPerson = Arrays.stream(RAW_DATA).filter(Objects::nonNull)
                .distinct()                                                   //Убираем дубликаты
                .sorted(Comparator.comparing(Person::getId))                  //Отсортировываем по идентификатору
                .collect(groupingBy(Person::getName, Collectors.counting()))
                .entrySet()                                                   //Сгруппировываем по имени
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors
                        .toMap(Map.Entry::getKey, Map.Entry::getValue, (first, conflict) -> first, LinkedHashMap::new));
        System.out.println(mapPerson);

        /*
        Task2

            [3, 4, 2, 7], 10 -> [3, 7] - вывести пару именно в скобках, которые дают сумму - 10
         */

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();
        System.out.println("Solving task 2:");
        System.out.println();

        int[] array = {3, 4, 2, 7};
        int sum = 10;
        System.out.println(Arrays.toString(findPairEqualsSum(array, sum)));

        /*
        Task3
            Реализовать функцию нечеткого поиска
            
                    fuzzySearch("car", "ca6$$#_rtwheel"); // true
                    fuzzySearch("cwhl", "cartwheel"); // true
                    fuzzySearch("cwhee", "cartwheel"); // true
                    fuzzySearch("cartwheel", "cartwheel"); // true
                    fuzzySearch("cwheeel", "cartwheel"); // false
                    fuzzySearch("lw", "cartwheel"); // false
         */

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();
        System.out.println("Solving task 3:");
        System.out.println();

        System.out.println("Assert: true --> result: " + fuzzySearch("car", "ca6$$#_rtwheel")); // true
        System.out.println("Assert: true --> result: " + fuzzySearch("cwhl", "cartwheel")); // true
        System.out.println("Assert: true --> result: " + fuzzySearch("cwhee", "cartwheel")); // true
        System.out.println("Assert: true --> result: " + fuzzySearch("cartwheel", "cartwheel")); // true
        System.out.println("Assert: false --> result: " + fuzzySearch("cwheeel", "cartwheel")); // false
        System.out.println("Assert: false --> result: " + fuzzySearch("lw", "cartwheel")); // false

    }

    static int[] findPairEqualsSum(int[] arr, int sum) {
        if (arr == null) {
            return new int[0];
        }
        Arrays.sort(arr);
        int l = 0;
        int r = arr.length - 1;
        while (l < r) {
            int s = arr[l] + arr[r];
            if (s == sum) {
                return new int[]{arr[l], arr[r]};
            }
            if (s < sum) {
                l++;
            } else {
                r--;
            }
        }
        return new int[0];
    }

    static boolean fuzzySearch(String keyWord, String text) {
        if (keyWord == null || text == null) {
            return false;
        }
        if (keyWord.equals("") || text.equals("")) {
            return false;
        }
        int index = 0;
        for (int i = 0; i < Objects.requireNonNull(keyWord).length(); i++) {
            char letterKeyWord = keyWord.charAt(i);
            if ((index = text.indexOf(letterKeyWord, index)) == -1) {
                return false;
            }
            index++;
        }
        return true;
    }
}
