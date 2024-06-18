package nz.geek.goodwin.melinoe.framework.internal.rest;

/**
 * @author Goodie
 */

public final class Prettifier {
    private Prettifier() {
    }

    public static String prettify(final String json) {
        if (json == null || json.isEmpty()) {
            return "";
        }

        int indentModifier = 4;
        int currentIndent = 0;
        StringBuilder returnString = new StringBuilder();


        String lineSeparator = System.lineSeparator();
        for (int i = 0; i < json.length(); i++) {
            var chari = json.charAt(i);

            if (chari == '{') {
                returnString.append(indentCalc(indentModifier, currentIndent)).append("{").append(lineSeparator);
                currentIndent++;
            } else if (chari == '}') { //Check, if the next character is a ',' don't newline
                currentIndent--;
                returnString.append(indentCalc(indentModifier, currentIndent)).append('}');
                if(i + 1 != json.length() && json.charAt(i + 1) == ',') {
                    returnString.append(',');
                    i++;
                } else {
                    returnString.append(lineSeparator);
                }
            } else if (chari == '[') {
                returnString.append("[");
                currentIndent++;
            } else if (chari == ']') { //Check, if the next character is a ',' don't newline
                currentIndent--;
                returnString.append(indentCalc(indentModifier, currentIndent)).append(']');
                if(json.charAt(i + 1) == ',') {
                    returnString.append(',').append(lineSeparator);
                    i++;
                }
            } else if (chari == '"') {
                int originalI = i;
                i = json.indexOf('"', i+1);

                if(json.charAt(i+2) == '"') {
                    i = json.indexOf('"', i+3);
                }

                returnString.append(indentCalc(indentModifier, currentIndent)).append(json.subSequence(originalI, i+1));
            } else if (chari == ',') {
                returnString.append(chari).append(lineSeparator);
            }else {
                returnString.append(chari);
            }

            if(i + 1 == json.length()) {
                break;
            }
            if(json.charAt(i + 1) == '{' || json.charAt(i + 1) == '}') {
                returnString.append(lineSeparator);
            }
        }

        return returnString.toString();
    }

    private static String indentCalc(int indentModifier, int currentIdent) {
        return " ".repeat(indentModifier).repeat(currentIdent);
    }
}
