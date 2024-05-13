package com.uppmacparser;

import java.util.regex.*;

public class Declaration {
    String content;
    
    public Declaration(String content) {
        this.content = content;
    }

    public int countFunctions() {
        int functionCount = 0;

        String pattern = "(?m)^\\s*(int|bool|void|double|string)\\s+\\w+\\s*\\([^\\)]*\\)\\s*\\{";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);
        while (m.find()) {
            functionCount++;
        }

        return functionCount;
    }
    

    public int countVariables() {
        int variableCount = 0;

        String pattern = "(?m)^\\s*(urgent|broadcast|meta|const)?\\s*(void|int|clock|chan|bool|double|string|int|scalar|struct)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);
        while (m.find()) {
            variableCount++;
        }

        return variableCount - countFunctions();
    }
}
