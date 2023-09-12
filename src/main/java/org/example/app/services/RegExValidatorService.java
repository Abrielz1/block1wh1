package org.example.app.services;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExValidatorService {

    private final String query;

    private String processedQuery;

    private final String idRegex = "id:[0-9]+";

    private final String authorRegex = "author:[a-zA-Z]+";

    private final String titleRegex = "title:[a-zA-Z]+";

    private final String sizeRegex = "size:[0-9]+";

    public RegExValidatorService(String regex) {
        this.query = regex;
    }

    public boolean regexValidator() throws IllegalAccessException {

        Field[] array = RegExValidatorService.class.getDeclaredFields();

        for (Field i : array) {

            if (i.getName().equals("query") && i.get(this) != null) {

                Pattern pattern = Pattern.compile(i.get(this).toString());
                Matcher matcher = pattern.matcher(query);

                if (matcher.find()) {
                    processedQuery = matcher.group(0);
                    return true;
                }
            }
        }
        return false;
    }
}
