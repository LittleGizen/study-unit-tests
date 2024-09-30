package ru.innotech.study.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionsTask {

    private static final List<String> wordList = Arrays.asList(
            "Трансильвания",
            "Экзистенциальный",
            "Автомобиль",
            "Третий", "Второй"

    );
    private static final List<Integer> integerList = Arrays.asList(1, 3, 7, 7, 9, 14, 13, 10, 14);
    private static final List<Employee> employeeList = Arrays.asList(
            new Employee("Василий", 35, "Адвокат"),
            new Employee("Пётр", 60, "Директор"),
            new Employee("Ксения", 45, "Инженер"),
            new Employee("Иван", 55, "Инженер"),
            new Employee("Наталья", 30, "Инженер"),
            new Employee("Георгий", 20, "Инженер")
    );
    private static final String phrase = "работа не работается если нет душевного равновесия нет пути ремесло успокаивает работа нет души только у камня";
    private static final String[] phrasesArray = {
            "перваяОдин втораяОдин третьяОдин четвёртаяОдин пятаяОдин",
            "перваяДва втораяДва третьяДва самоеСамоеДлинноеСлово пятаяДва",
            "перваяТри самоеСамоеДлинноеСлово третьяТри четвёртаяТри пятаяТри"
    };

    public static void main(String[] args) {
        // Реализуйте удаление из листа всех дубликатов
        wordList.stream().distinct().collect(Collectors.toList());

        // Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10)
        integerList.stream().sorted(Collections.reverseOrder()).skip(2).findFirst().orElseThrow();

        // Найдите в списке целых чисел 3-е наибольшее «уникальное» число (пример: 5 2 10 9 4 3 10 1 13 => 9, в отличие от прошлой задачи здесь разные 10 считает за одно число)
        integerList.stream().distinct().sorted(Collections.reverseOrder()).skip(2).findFirst().orElseThrow();

        // Имеется список объектов типа Сотрудник (имя, возраст, должность), необходимо получить список имен 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста
        employeeList.stream().filter(employee -> "Инженер".equals(employee.getPosition())).sorted(Comparator.comparing(Employee::getAge).reversed()).limit(3).collect(Collectors.toList());

        // Имеется список объектов типа Сотрудник (имя, возраст, должность), посчитайте средний возраст сотрудников с должностью «Инженер»
        employeeList.stream().filter(employee -> "Инженер".equals(employee.getPosition())).mapToInt(Employee::getAge).average().orElseThrow();

        // Найдите в списке слов самое длинное
        wordList.stream().max(Comparator.comparing(String::length)).orElseThrow();

        // Имеется строка с набором слов в нижнем регистре, разделенных пробелом. Постройте хеш-мапы, в которой будут хранится пары: слово - сколько раз оно встречается во входной строке
        Stream.of(phrase.split(" ")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Отпечатайте в консоль строки из списка в порядке увеличения длины слова, если слова имеют одинаковую длины, то должен быть сохранен алфавитный порядок
        wordList.stream().sorted().sorted(Comparator.comparing(String::length)).forEach(System.out::println);

        // Имеется массив строк, в каждой из которых лежит набор из 5 строк, разделенных пробелом, найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них
        Arrays.stream(phrasesArray).flatMap(s -> Arrays.stream(s.split(" "))).sorted(Comparator.comparing(String::length).reversed()).limit(1);
    }
}
