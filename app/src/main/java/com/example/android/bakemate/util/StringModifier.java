package com.example.android.bakemate.util;

public class StringModifier {

    public static String ingredientStringBuilder(Float quantity, String measure, String ingredientName) {

        return quantityToString(quantity) + " " + reformatUnitsOfMeasure(measure) + " of " + ingredientName;
    }

    private static String quantityToString(Float quantity) {
        return quantity.toString().replaceAll("\\.?0*$", "");
    }

    private static String reformatUnitsOfMeasure(String measure) {

        String output;

        switch (measure) {
            case "CUP":
                output = "cups";
                break;
            case "K":
                output = "kg";
                break;
            case "UNIT":
                output = "units";
                break;
            default:
                output = measure.toLowerCase();

        }

        return output;
    }

    public static String removeLeadingNumberFromDescription(String description, int step) {

        return description.replaceFirst(String.valueOf(step) + ". ", "");
    }
}
