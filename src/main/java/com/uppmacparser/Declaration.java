package com.uppmacparser;

import java.util.regex.*;

public class Declaration {
    String content;
    
    public Declaration(String content) {
        this.content = content;
    }

    public int countFunctions() {
        int functionCount = 0;

        // Regular expression to match function definition
        String pattern = "(?m)^\\s*(int|bool|void|double|string)\\s+\\w+\\s*\\([^\\)]*\\)\\s*\\{";
        Pattern r = Pattern.compile(pattern);

        // Use a Matcher to find all occurrences of the pattern
        Matcher m = r.matcher(content);
        while (m.find()) {
            functionCount++;
        }

        return functionCount;
    }
    

    public void countVariables() {

    }
}
