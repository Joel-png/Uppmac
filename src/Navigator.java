import java.util.Scanner;
import java.util.regex.Pattern;

public class Navigator {
    enum State {
        NTA,
        TEMPLATE,
        LOCATION
    }

    Scanner scanner = new Scanner(System.in);
    Nta nta;
    String input;
    State state;
    Pattern number = Pattern.compile("^\\d+$");
    String fileName = "";

    int templateIndex = -1;
    int locationIndex = -1;

    Identifier identifier;

    public Navigator(Nta nta, String fileName) {
        this.nta = nta;
        this.state = State.NTA;
        this.fileName = fileName;
        this.identifier = new Identifier(nta);
    }

    public String sub(String string, int start, int end) {
        if (string.length() >= end) {
            return string.substring(start, end);
        } else if (string.length() > start) {
            return string.substring(start);
        } else {
            return "";
        }
    }

    public String sub(String string, int start) {
        if (string.length() > start) {
            return string.substring(start);
        } else {
            return "";
        }
    }

    public void getProperties() {
        
        // NTA
        System.out.print("<");
        System.out.println("NTA has Templates:");
        nta.printTemplates(1);
        System.out.println();
        for (int i = 0; i < nta.templates.length; i++) {
            // TEMPLATES
            templateIndex = i;
            indent(1);
            System.out.print("<");
            System.out.print("[" + templateIndex + "] ");
            nta.templates[templateIndex].printData(0);
            nta.templates[templateIndex].printLocations(2);
            System.out.println();
            indent(2);
            System.out.println("[" + templateIndex + "] Template  properties:");
            identifier.printTemplateProperties(nta.templates[templateIndex].locations, nta.templates[templateIndex].transitions, 3);
            for (int j = 0; j < nta.templates[templateIndex].locations.length; j++) {
                // LOCATION
                locationIndex = j;
                System.out.println();
                indent(2);
                System.out.print("<");
                nta.templates[templateIndex].locations[locationIndex].printData(0);
                nta.templates[templateIndex].locations[locationIndex].printTransitions(3);
                System.out.println();
                indent(3);
                System.out.println("[" + locationIndex + "] Location properties:");
                identifier.printLocationProperties(nta.templates[templateIndex].locations[locationIndex], nta.templates[templateIndex].transitions, 4);
                indent(2);
                System.out.println("[" + locationIndex + "] Location: " + nta.templates[templateIndex].locations[locationIndex].name + "/>");
            }
            indent(1);
            System.out.print("[" + templateIndex + "] Template: " + nta.templates[templateIndex].name + "/>");
            System.out.println();
        }
        System.out.print("NTA/>");
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public void navigate() {
        boolean exit = false;
        System.out.println();
        System.out.println("-- Welcome to UPPMAC --");
        System.out.println();
        System.out.println("Commands: info | goto | exit");
        System.out.println();
        while(!exit) {
            switch (state) {
                case NTA:
                    System.out.print(fileName + " : NTA> ");
                    break;
                case TEMPLATE:
                    System.out.print(fileName + " : NTA\\Template[" + nta.templates[templateIndex].name + "]> ");
                    break;
                case LOCATION:
                    System.out.print(fileName + " : NTA\\Template[" + nta.templates[templateIndex].name + "]\\Location[" + nta.templates[templateIndex].locations[locationIndex].id + "]> ");
                    break;
            }
            input = scanner.nextLine();


            if (input.equals("exit")) {
                exit = true;
                continue;


            } else if (input.equals("info")) {
                switch (state) {
                    case NTA:
                        nta.printTemplates(0);
                        break;
                    case TEMPLATE:
                        nta.templates[templateIndex].printData(0);
                        nta.templates[templateIndex].printLocations(0);
                        break;
                    case LOCATION:
                        nta.templates[templateIndex].locations[locationIndex].printData(0);
                        nta.templates[templateIndex].locations[locationIndex].printTransitions(0);
                        break;
                }


            } else if (input.equals("identify")) {
                switch (state) {
                    case NTA:
                        
                        break;
                    case TEMPLATE:
                        identifier.printTemplateProperties(nta.templates[templateIndex].locations, nta.templates[templateIndex].transitions, 0);
                        break;
                    case LOCATION:
                        identifier.printLocationProperties(nta.templates[templateIndex].locations[locationIndex], nta.templates[templateIndex].transitions, 0);
                        break;
                }


            } else if (sub(input, 0, 4).equals("goto")) {
                switch (state) {
                    case NTA:
                        switch (sub(input, 4, 13)) {
                            case (" template"):
                                // if anything past "template " is a number, try to jump to that template
                                
                                if (number.matcher(sub(input, 14)).find()) {
                                    int tempIndex = Integer.valueOf(sub(input, 14));
                                    if (nta.templates.length > tempIndex) {
                                        templateIndex = tempIndex;
                                        state = State.TEMPLATE;
                                        System.out.println("> Template: " + templateIndex);
                                    } else if (nta.templates.length == 0) {
                                        System.out.println("There are no templates to goto");
                                    } else {
                                        System.out.println("[n] needs to be in the range of 0-" + (nta.templates.length - 1));
                                    }
                                } else {
                                    System.out.println("Command needs to be in the form: goto [template] [n]");
                                }
                                break;
                            default:
                                System.out.println("Command needs to be in the form: goto [template] [n]");
                                break;
                        }
                        break;


                    case TEMPLATE:
                        switch (sub(input, 4, 13)) {
                            case (" nta"):
                                state = State.NTA;
                                System.out.println("> NTA");
                                break;
                            case (" location"):
                                // if anything past "location " is a number, try to jump to that location
                                if (number.matcher(sub(input, 14)).find()) {
                                    int tempIndex = Integer.valueOf(sub(input, 14));
                                    if (nta.templates[templateIndex].locations.length > tempIndex) {
                                        locationIndex = tempIndex;
                                        state = State.LOCATION;
                                        System.out.println("> Location: " + locationIndex);
                                    } else if (nta.templates[templateIndex].locations.length == 0) {
                                        System.out.println("There are no locations to goto");
                                    } else {
                                        System.out.println("[n] needs to be in the range of 0-" + (nta.templates[templateIndex].locations.length - 1));
                                    }
                                } else {
                                    System.out.println("Command needs to be in the form: goto [location] [n]");
                                }
                                break;
                            default:
                                System.out.println("Command needs to be in the form: goto [nta] | goto [location] [n]");
                                break;
                        }
                        break;


                    case LOCATION:
                        switch (sub(input, 4, 15)) {
                            case (" template"):
                                state = State.TEMPLATE;
                                System.out.println("> Template: " + templateIndex);
                                break;
                            default:
                                System.out.println("Command needs to be in the form: goto [template]");
                                break;
                        }
                        break;
                }


            } else if (input.equals("")){
                
            } else {
                System.out.println("Commands: info | goto | exit");
            }
        }
    }

    public static void indent(int amount) {
        for (int i = 0; i < amount; i++) {
            System.out.print("    ");
        }
    }
}


