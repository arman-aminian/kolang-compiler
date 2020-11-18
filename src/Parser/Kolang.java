package Parser;

import Scanner.Scanner;

public class Kolang {
    public static void main(String[] args) {
//        System.out.println(args.length);
//        System.out.println(args[0]);
//        return;
        System.out.println("please enter code file path");
//        java.util.Scanner canner = new java.util.Scanner(System.in);
//        String path = canner.nextLine();


        String path = null;
        if(args.length < 1)
        {
            java.util.Scanner canner = new java.util.Scanner(System.in);
        path = canner.nextLine();
        }

        else
            path = args[0];
        Scanner scanner = new Scanner();
        scanner.doScanner(path);
        new ParserMain();
        ParserMain.parserTD.doParserTD("S");
    }
}
