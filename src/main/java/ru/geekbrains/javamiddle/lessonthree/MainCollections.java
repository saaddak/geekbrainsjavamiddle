package ru.geekbrains.javamiddle.lessonthree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

public class MainCollections {
    public static void main(String[] args) {
        final String manifesto = "Призрак бродит по Европе - призрак коммунизма. Все силы старой Европы объединились для священной травли этого призрака: папа и царь, Меттерних и Гизо, французские радикалы и немецкие полицейские. Где та оппозиционная партия, которую ее противники, стоящие у власти, не ославили бы коммунистической? Где та оппозиционная партия, которая в свою очередь не бросала бы клеймящего обвинения в коммунизме как более передовым представителям оппозиции, так и своим реакционным противникам?";
        collector(manifesto);

        System.out.println();

        PhoneBook muhosransk = new PhoneBook("Мухосранский автономный округ", "666");
        muhosransk.addPerson("Лоханкин", new PersonData("012-03-45", "the-only-one-true-lohankin@muhoskansk.mail.ru"));
        muhosransk.addPerson("Пупкин", new PersonData("011-01-11", "pupkin@muhoskansk.mail.ru"));
        muhosransk.addPerson("Лоханкин", new PersonData("022-02-22", "lohankin@muhoskansk.mail.ru"));
        muhosransk.addPerson("Пердильский", new PersonData("033-03-33", "perdillio@muhoskansk.mail.ru"));
        muhosransk.addPerson("Лоханкин", new PersonData("044-04-44", "another-lohankin@muhoskansk.mail.ru"));
        phoneManiac(muhosransk, "Лоханкин");
        emailManiac(muhosransk, "Лоханкин");
    }

    private static void collector(String manifesto) {
        Pattern manifestoRegex = Pattern.compile("(\\s-\\s)|(\\?\\s)|(,\\s)|(\\.\\s)|(!\\s)|(;\\s)|(:\\s)|\\?|,|\\.|!|;|:|\\(|\\)|\\s|\\n|\\r");
        String[] manifestoArray = manifestoRegex.split(manifesto);

        HashMap<String, Integer> manifestoMap = new HashMap<>();
        for(int i = 0; i < manifestoArray.length; i++) {
            if(manifestoMap.containsKey(manifestoArray[i])) {
                manifestoMap.put(manifestoArray[i], manifestoMap.get(manifestoArray[i]) + 1);
            } else {
                manifestoMap.put(manifestoArray[i], 1);
            }
        }

        Set<String> kSet = manifestoMap.keySet();
        System.out.format("Частота появления слов внутри фрагмента: '%s':%n", manifesto);
        for (String key : kSet) {
            System.out.format("%s: %d%n", key, manifestoMap.get(key));
        }
    }

    static void phoneManiac(PhoneBook theBook, String victim) {
        ArrayList<String> somePhones = theBook.getPhone(victim);
        System.out.format("Телефоны людей с фамилией '%s' в области '%s': %n", victim, theBook.getRegion());
        for(String number : somePhones) {
            System.out.println(number);
        }
        System.out.println();
    }

    static void emailManiac(PhoneBook theBook, String victim) {
        ArrayList<String> someEmails = theBook.getEmail(victim);
        System.out.format("Электронная почта людей с фамилией '%s' в области '%s': %n", victim, theBook.getRegion());
        for(String number : someEmails) {
            System.out.println(number);
        }
        System.out.println();
    }
}
