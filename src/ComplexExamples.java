//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    private static Person[] RAW_DATA = new Person[]{
            new Person(0, "Harry"),
            new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(8, "Amelia"),
            new Person(7, "Amelia"),
    };
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

        Map<String, Long> mapPerson = Arrays.stream(RAW_DATA)
                .distinct()                                                   //Убираем дубликаты
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Person::getId))                  //Отсортировываем по идентификатору
                .collect(groupingBy(Person::getName, Collectors.counting())); //Сгруппировываем по имени
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
        IntStream.range(0, array.length)
                .forEach(i -> IntStream.range(0, array.length)
                        .filter(j -> i < j && array[i] + array[j] == 10)
                        .forEach(j -> System.out.println(Arrays.toString(new int[]{array[i], array[j]}))));

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

    static boolean fuzzySearch(String keyWord, String text) {
        if (keyWord == null || text == null) {
            return false;
        }
        if (keyWord.equals("") || text.equals("")) {
            return false;
        }
        int count = 0;
        for (int i = 0; i < Objects.requireNonNull(keyWord).length(); i++) {
            char letterKeyWord = keyWord.charAt(i);
            boolean flag = false;
            for (int j = count; j < text.length(); j++) {
                char letterText = text.charAt(j);
                if (letterText == letterKeyWord) {
                    flag = true;
                    count = j + 1;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }
}