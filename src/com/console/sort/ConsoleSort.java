package com.console.sort;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsoleSort {
    public static void main(String[] args) {
        System.out.println("Поместите текстовый файл в папку toSort приложения ConsoleSort");
        System.out.println("Укажите полное имя файла в формате \"ConsoleTest.txt\":");
        Scanner in = new Scanner(System.in);
        String file = "toSort/" + in.nextLine();
        System.out.println("Выберите тип сортировки:");
        System.out.println("1 - По алфавиту");
        System.out.println("2 - По количеству символов в строке");
        System.out.println("3 - По порядковому номеру слова в строке");
        int sortingType = in.nextInt();
        try (Stream<String> stream = Files.lines(Paths.get(file), Charset.defaultCharset())) {
            File sorted = new File("sorted", "sorted.txt");
            List<String> lines = stream.collect(Collectors.toList());
            Map<String, Long> countMap = lines.stream()
                    .collect(Collectors.groupingBy(
                            Function.identity(), LinkedHashMap::new, Collectors.counting()));
            List<String> countedLines = lines.stream()
                    .map(line -> line = line + " " + countMap.get(line))
                    .collect(Collectors.toList());
            switch (sortingType) {
                case 1 -> sortByAbc(countedLines, sorted);
                case 2 -> sortByLength(countedLines, sorted);
                case 3 -> sortByNumber(countedLines, in, sorted);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sortByAbc(List<String> countLines, File sorted) throws IOException {
        countLines.sort(Comparator.naturalOrder());
        Files.write(Path.of(String.valueOf(sorted)), countLines, StandardOpenOption.CREATE);
        System.out.println("Данные отсортированы по алфавиту");
        System.out.println("Создан файл sorted.txt");
        System.out.println("Файл с отсортированными данные находится в папке \\ConsoleSort\\sorted");
        System.out.println("Спасибо за использование ConsoleSort");
    }

    private static void sortByLength(List<String> countedLines, File sorted) throws IOException {
        countedLines.sort(Comparator.comparingInt(String::length));
        Files.write(Path.of(String.valueOf(sorted)), countedLines, StandardOpenOption.CREATE);
        System.out.println("Данные отсортированы по количеству символов в строке");
        System.out.println("Создан файл sorted.txt");
        System.out.println("Файл с отсортированными данные находится в папке \\ConsoleSort\\sorted");
        System.out.println("Спасибо за использование ConsoleSort");
    }

    private static void sortByNumber(List<String> countedLines, Scanner in, File sorted) throws IOException {
        int orderLimit = countedLines.stream()
                .map(line -> line.split(" ").length)
                .min(Comparator.naturalOrder())
                .get() - 1;

        System.out.println("Укажите порядковый номер слова от 1 до " + orderLimit + ":");
        int orderNumber = in.nextInt();
        if (orderNumber <= 0 | orderNumber > orderLimit) {
            System.out.println("Порядковый номер должен быть от 1 до " + orderLimit);
            System.out.println("Перезапустите программу и укажите корректный порядковый номер слова");
            System.exit(-1);
        } else {
            countedLines.sort((String s1, String s2) -> {
                String[] strings1 = s1.split(" ");
                String[] strings2 = s2.split(" ");
                return strings1[orderNumber - 1].compareTo(strings2[orderNumber - 1]);
            });
        }
        Files.write(Path.of(String.valueOf(sorted)), countedLines, StandardOpenOption.CREATE);
        System.out.println("Данные отсортированы по " + orderNumber + " слову в строке");
        System.out.println("Создан файл sorted.txt");
        System.out.println("Файл с отсортированными данные находится в папке \\ConsoleSort\\sorted");
        System.out.println("Спасибо за использование ConsoleSort");
    }
}