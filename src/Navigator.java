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

    public Navigator(Nta nta, String fileName) {
        this.nta = nta;
        this.state = State.NTA;
        this.fileName = fileName;
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
                        nta.printTemplates();
                        break;
                    case TEMPLATE:
                        nta.templates[templateIndex].printData();
                        nta.templates[templateIndex].printLocations();
                        break;
                    case LOCATION:
                        nta.templates[templateIndex].locations[locationIndex].printData();
                        nta.templates[templateIndex].locations[locationIndex].printTransitions();
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
}
