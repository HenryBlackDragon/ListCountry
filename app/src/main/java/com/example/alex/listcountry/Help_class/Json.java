package com.example.alex.listcountry.Help_class;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;

public class Json {

    private String result;

    public Json(String result) {
        this.result = result;
    }

    private HashMap<String, String> mapNamesCapital(String region) {
        JSONArray countryObject = null;
        HashMap<String, String> namesCapital = new HashMap<>();
        try {
            countryObject = (JSONArray) JSONValue.parseWithException(result);
            for (Object aCountryObject : countryObject) {
                JSONObject fromJson = (JSONObject) aCountryObject;
                if (region.equals(fromJson.get("region").toString())) {
                    namesCapital.put(fromJson.get("name").toString(), fromJson.get("capital").toString());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return namesCapital;
    }

    private HashMap<String, String> mapNamesCapital() {
        JSONArray countryObject = null;
        HashMap<String, String> namesCapital = new HashMap<>();
        try {
            countryObject = (JSONArray) JSONValue.parseWithException(result);
            for (Object aCountryObject : countryObject) {
                JSONObject fromJson = (JSONObject) aCountryObject;
                namesCapital.put(fromJson.get("name").toString(), fromJson.get("capital").toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return namesCapital;
    }

    public HashMap<String, String> namesLatlng() {
        JSONArray countryObject = null;
        HashMap<String, String> namesLatlng = new HashMap<>();
        try {

            countryObject = (JSONArray) JSONValue.parseWithException(result);
            for (Object aCountryObject : countryObject) {
                JSONObject fromJson = (JSONObject) aCountryObject;
                if (!fromJson.get("latlng").toString().equals("[]")) {
                    namesLatlng.put(fromJson.get("name").toString(), fromJson.get("latlng").toString());
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return namesLatlng;
    }

    public String[] getNames(String region) {
        HashMap<String, String> mapNamesCapital = mapNamesCapital(region);
        String[] name;
        String interval = "";
        for (HashMap.Entry<String, String> pair : mapNamesCapital.entrySet()
                ) {
            interval += pair.getKey() + "[";
        }

        name = interval.split("\\[");
        return name;
    }

    public String[] getNamesCapitals(String region) {
        HashMap<String, String> mapNamesCapital = mapNamesCapital(region);
        String[] namesCapitals = new String[mapNamesCapital.size()];
        String[] names = getNames(region);
        String[] capital;
        String interval = "";
        for (HashMap.Entry<String, String> pair : mapNamesCapital.entrySet()
                ) {
            interval += pair.getValue() + ",";
        }
        capital = interval.split(",");
        for (int i = 0; i < namesCapitals.length; i++) {
            namesCapitals[i] = names[i] + "[" + capital[i] + "]";
        }
        return namesCapitals;
    }

    public String[] searchCountry(String search) {
        HashMap<String, String> mapNamesCapital = mapNamesCapital();
        ArrayList<String> arrayCountry = new ArrayList<>();
        for (HashMap.Entry<String, String> pair : mapNamesCapital.entrySet()
                ) {
            if (pair.getKey().toLowerCase().contains(search.toLowerCase())) {
                arrayCountry.add(pair.getKey() + "[" + pair.getValue() + "]");

            } else if (pair.getValue().toLowerCase().contains(search.toLowerCase())) {
                arrayCountry.add(pair.getKey() + "[" + pair.getValue() + "]");
            }
        }
        String[] searchCountry = new String[arrayCountry.size()];
        for (int i = 0; i < searchCountry.length; i++) {
            searchCountry[i] = arrayCountry.get(i);
        }
        return searchCountry;
    }

    public String[] searchNames(String search) {
        HashMap<String, String> mapNamesCapital = mapNamesCapital();
        ArrayList<String> arrayNames = new ArrayList<>();
        for (HashMap.Entry<String, String> pair : mapNamesCapital.entrySet()
                ) {
            if (pair.getKey().toLowerCase().contains(search.toLowerCase())) {
                arrayNames.add(pair.getKey());

            } else if (pair.getValue().toLowerCase().contains(search.toLowerCase())) {
                arrayNames.add(pair.getKey());
            }
        }
        String[] searchNames = new String[arrayNames.size()];
        for (int i = 0; i < searchNames.length; i++) {
            searchNames[i] = arrayNames.get(i);
        }
        return searchNames;
    }

    public ArrayList<String> mapInfo() {
        JSONArray countryObject = null;
        ArrayList<String> infoMap = new ArrayList<>();
        try {
            countryObject = (JSONArray) JSONValue.parseWithException(result);
            for (Object aCountryObject : countryObject) {
                JSONObject fromJson = (JSONObject) aCountryObject;
                infoMap.add("Name: " + fromJson.get("name").toString());
                infoMap.add("Top level domain: " + fromJson.get("topLevelDomain").toString());
                infoMap.add("Alpha 2 code: " + fromJson.get("alpha2Code").toString());
                infoMap.add("Alpha 3 code: " + fromJson.get("alpha2Code").toString());
                infoMap.add("Calling codes: " + fromJson.get("callingCodes").toString());
                infoMap.add("Capital: " + fromJson.get("capital").toString());
                infoMap.add("Alt spellings: " + fromJson.get("altSpellings").toString());
                infoMap.add("Region: " + fromJson.get("region").toString());
                infoMap.add("Subregion: " + fromJson.get("subregion").toString());
                infoMap.add("Latlng: " + fromJson.get("latlng").toString());
                infoMap.add("Demonym: " + fromJson.get("demonym").toString());
                infoMap.add("Area: " + fromJson.get("area").toString());
                if (fromJson.get("gini") == null) {
                    infoMap.add("Gini: " + "-");

                } else {
                    infoMap.add("Gini: " + fromJson.get("gini").toString());
                }
                infoMap.add("Timezones: " + fromJson.get("timezones").toString());
                infoMap.add("Borders: " + fromJson.get("borders").toString());
                infoMap.add("Native name: " + fromJson.get("nativeName").toString());
                infoMap.add("Numeric code: " + fromJson.get("numericCode").toString());
                infoMap.add("Currencies: " + fromJson.get("currencies").toString());
                infoMap.add("Languages: " + fromJson.get("languages").toString());
                infoMap.add("Translations: " + fromJson.get("translations").toString());
                infoMap.add("Regional blocs: " + fromJson.get("regionalBlocs").toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return infoMap;
    }
}