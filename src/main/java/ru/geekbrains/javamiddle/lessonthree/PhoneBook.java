package ru.geekbrains.javamiddle.lessonthree;

import java.util.ArrayList;
import java.util.HashMap;

public class PhoneBook {
    private HashMap<String, ArrayList<PersonData>> phoneBook = new HashMap<>();
    private String region;
    private String areaCode;

    PhoneBook(String region, String areaCode) {
        this.region = region;
        this.areaCode = areaCode;
    }

    String getRegion() { return region; }
    void setRegion(String region) { this.region = region; }

    String getAreaCode() { return areaCode; }
    void setAreaCode(String areaCode) { this.areaCode = areaCode; }

    void addPerson(String name, PersonData person) {
        if (!phoneBook.containsKey(name)) {
            phoneBook.put(name, new ArrayList<PersonData>());
        }
        phoneBook.get(name).add(person);
    }

    ArrayList<String> getPhone(String name) {
        ArrayList<String> phoneList = new ArrayList<>();
        for(String personName : phoneBook.keySet()){
            if(personName.equals(name)) {
                ArrayList<PersonData> personData = phoneBook.get(personName);
                for (int i = 0; i < personData.size(); i++) {
                    phoneList.add(personData.get(i).getPhone());
                }
            }
        }
        return phoneList;
    }

    ArrayList<String> getEmail(String name) {
        ArrayList<String> emailList = new ArrayList<>();
        for(String personName : phoneBook.keySet()){
            if(personName.equals(name)) {
                ArrayList<PersonData> personData = phoneBook.get(personName);
                for (int i = 0; i < personData.size(); i++) {
                    emailList.add(personData.get(i).getEmail());
                }
            }
        }
        return emailList;
    }
}
