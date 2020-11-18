package Scanner;

import java.io.*;
import java.util.*;

enum TokenType {KW, SpecialToken, Identifier, RealNum ,IntegerNum,Character, StringLiteral,Error,Include};

class UnknownSymbol extends RuntimeException
{
    private char unknown;
    @Override
    public String getMessage() {
        return "Unknown symbol " + unknown + " with ascii " + (int)unknown;
    }

    public UnknownSymbol (char ch)
    {
        unknown = ch;
    }
}

class Token
{
    private TokenType type;
    private String value;
    private String pos;

    public Token(TokenType type, String value , String pos) {
        this.type = type;
        this.value = value;
        this.pos = pos;

    }

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public String getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return "<" + type.toString() + " , " + value + " , " + pos + ">\n";
    }
}

public class Scanner
{
    private static ArrayList <Token> tokenList;
    private static java.util.Scanner reader;
    private static int rowNumber = 1;
    private static int colNumber = 0;
    private static Set <String> reserved;

    private static char read()
    {
        char ch = reader.next().charAt(0);
        colNumber++;
        if(ch == '\n')
        {
            rowNumber++;
            colNumber = 0;
        }
        return ch;
    }

    private static boolean isalnum(char ch)
    {
        if(ch >= '0' && ch <= '9')
            return true;
        if(ch >= 'a' && ch <= 'z')
            return true;
        if(ch >= 'A' && ch <= 'Z')
            return true;
        return false;
    }

    private static void loadReserved()
    {
        File reservedFile;
        java.util.Scanner in = new java.util.Scanner(System.in);

        try {
            reservedFile = new File("C:\\Compiler\\Compiler\\src\\reserved.txt");
            in = new java.util.Scanner(reservedFile);
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Reserved File Not Found");
        }
        while(in.hasNext())
            reserved.add(in.nextLine());
        in.close();
    }

    public static void initStream(String path)
    {

//        // path format "src\\slm.txt" /// "E:\\Uni_Data\\Term_4\\Compiler\\Compiler\\src\\slm.txt"
        File codeFile;
        try {
            codeFile = new File(path);
            RandomAccessFile randomAccessFile = new RandomAccessFile(codeFile,"rw");
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.write('\n');
            randomAccessFile.write('$');
            reader = new java.util.Scanner(codeFile);
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Code Not Found");
        } catch (IOException e) {
            System.err.println("io exception for append");
        }
        reader.useDelimiter("");
    }

    public  void doScanner(String path) {
        reserved = new HashSet<String>();
        loadReserved();
        initStream(path);
        tokenList = new ArrayList<>();
        char ch = read();
        do {
            switch (ch)
            {
                case '+':
                    int commentLine = rowNumber;
                    int commentCol = colNumber;
                    ch = read();
                    if(ch == '+')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "++" , "<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "+=" , "<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, "+","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '-':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '-')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "--","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "-=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else if(ch == '>')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "->","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, "-","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;



                case '*':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "*=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }

                    else  if(ch == '*')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "**","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, "*","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '/':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "/=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else if(ch == '/')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "//","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
//                        ch = read();
//                        if(!reader.hasNext())
//                            break;
//                        boolean fileFinish = false;
//                        while(ch != '\n')
//                        {
//                            ch = read();
//                            if(!reader.hasNext())
//                            {
//                                fileFinish = true;
//                                break;
//                            }
//                        }
//                        if(fileFinish)
//                            break;
//                        ch = read();
                    }
                    else if(ch == '*')
                    {
                        try {

                            ch = read();
                            char prv = ch;
                            ch = read();
                            while (!(prv == '*' && ch == '/')) {
                                prv = ch;
                                ch = read();
                            }
                            ch = read();
                        }

                        // add error token ----
                        catch (Exception ex)
                        {
                            tokenList.add(new Token(TokenType.Error, "unfinished comment" , "<row : " + commentLine+ ">,<col : " + (commentCol) + ">"));
                            System.err.println("Source code has unfinished comment which has started at line " + commentLine + " and column " + (commentCol));
                        }
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, "/","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '%':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "%=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, "%","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '(':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, "(","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case ')':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, ")","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '[':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, "[","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case ']':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, "]","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '{':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, "{","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '}':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, "}","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '<':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '<')
                    {
                        ch = read();
                        if (ch == '='){
                            tokenList.add(new Token(TokenType.SpecialToken, "<<=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                            ch = read();
                        } else {
                            tokenList.add(new Token(TokenType.SpecialToken, "<<","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        }
                    }
                    else if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "<=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, "<","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '>':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '>') {
                        ch = read();
                        if (ch == '='){
                            tokenList.add(new Token(TokenType.SpecialToken, ">>=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                            ch = read();
                        } else {
                            tokenList.add(new Token(TokenType.SpecialToken, ">>","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        }

                    }
                    else if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, ">=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, ">","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;


                // complete

                case '\"':
                    ch = read();
                    if (ch == '\"') {
                        ch=read();
                        if (ch == '\"'){
                            commentLine = rowNumber;
                            commentCol = colNumber;
                                        try {

                                        ch = read();
                                        if (ch=='\n'){
                                            tokenList.add(new Token(TokenType.Error, "unfinished comment" , "<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                                            System.err.println("Source code has unfinished comment which has started at line " + commentLine + " and column " + (commentCol - 3));
                                            continue;
                                }
                                char prv1 = ch;
                                ch = read();
                                if (ch=='\n'){
                                    tokenList.add(new Token(TokenType.Error, "unfinished comment" , "<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                                    System.err.println("Source code has unfinished comment which has started at line " + commentLine + " and column " + (commentCol - 3));
                                    continue;
                                }
                                char prv2 = ch;
                                ch = read();
                                if (ch=='\n'){
                                    tokenList.add(new Token(TokenType.Error, "unfinished comment" ,"<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                                    System.err.println("Source code has unfinished comment which has started at line " + commentLine + " and column " + (commentCol - 3));
                                    continue;
                                }
                                while (!(prv1 == '\"' && prv2 == '\"' &&ch == '\"')) {
                                    prv1 = prv2;
                                    prv2=ch;
                                    ch = read();
                                    if (ch=='\n'){
                                        tokenList.add(new Token(TokenType.Error, "unfinished comment" , "<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                                        System.err.println("Source code has unfinished comment which has started at line " + commentLine + " and column " + (commentCol - 3));
                                        break;
                                    }
                                }
                                ch = read();
                            }

                            // add error token ----
                            catch (Exception ex)
                            {
                                tokenList.add(new Token(TokenType.Error, "unfinished comment" , "<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                                System.err.println("Source code has unfinished comment which has started at line " + commentLine + " and column " + (commentCol - 3));
                            }

                        }
                    } else {
                        commentLine = rowNumber;
                        commentCol = colNumber;
                        StringBuilder str = new StringBuilder("");
//                    tokenList.add(new Token(TokenType.SpecialToken, "\""));
                        try {
                            while (true) {

                               if (ch == '\"')
                                    break;
                                if (ch == '\\'){
                                    ch = read();
                                    if(ch == 'n')
                                    {
                                        str.append('\n');

                                    }
                                    else if(ch == 't')
                                    {
                                        str.append('\t');

                                    }
                                    else
                                    str.append(ch);
                                }
                                else
                                {
                                    str.append(ch);

                                }
                                ch = read();

                            }
                            ch = read();

                        } catch (Exception ex) {
                            tokenList.add(new Token(TokenType.Error, "unfinished string" , "<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                            System.err.println("Source code has unfinished String.");
                        }
                        tokenList.add(new Token(TokenType.StringLiteral, str.toString(), "<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    }
                    break;


                case '\'':
                    StringBuilder str = new StringBuilder("");
                    str = new StringBuilder("");
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '\\')
                    {
                        ch = read();
                        if(ch == 'n')
                        {
                            str.append('\n');

                        }
                        else if(ch == 't')
                        {
                            str.append('\t');

                        }
                        else
                            str.append(ch);
                    }
                    else
                        str.append(ch);


                    ch = read();
                    if(ch != '\'')
                    {
                        tokenList.add(new Token(TokenType.Error, "unfinished character" , "<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        throw new RuntimeException("UnMatching Braces");

                    }
                    tokenList.add(new Token(TokenType.Character, str.toString(),"<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    ch = read();
                    break;

                case ';':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, ";","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case ':':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, ":=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, ":","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '=':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '<')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "=<", "<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }else
                        tokenList.add(new Token(TokenType.SpecialToken, "=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;


                case '!':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "!=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, "!","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '?':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, "?","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '^':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "^=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, "^","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '&':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '&')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "&&","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }else if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "&=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, "&","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '|':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    if(ch == '|')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "||","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }else if(ch == '=')
                    {
                        tokenList.add(new Token(TokenType.SpecialToken, "|=","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                        ch = read();
                    }
                    else
                        tokenList.add(new Token(TokenType.SpecialToken, "|","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '~':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, "~","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '#':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, "#","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case ',':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, ",","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '.':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    ch = read();
                    tokenList.add(new Token(TokenType.SpecialToken, ".","<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case '_':
                    str = new StringBuilder("");
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    do
                    {
                        str.append(ch);
                        ch = read();
                    }
                    while(isalnum(ch) || ch == '_');
                    String word = str.toString();
                    if(reserved.contains(word))
                        tokenList.add(new Token(TokenType.KW, word,"<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    else
                        tokenList.add(new Token(TokenType.Identifier, word,"<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    str = new StringBuilder("");
                    boolean isInteger = true;
                    do
                    {
                        str.append(ch);
                        ch = read();
                    }
                    while((ch >= '0' && ch <= '9'));
                    if(ch == '.')
                    {
                        isInteger=false;
                        do
                        {
                            str.append(ch);
                            ch = read();
                        }
                        while((ch >= '0' && ch <= '9'));
                    }
                    word = str.toString();
                    if (isInteger)
                        tokenList.add(new Token(TokenType.IntegerNum, word,"<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    else tokenList.add(new Token(TokenType.RealNum, word,"<row: " + commentLine+ ">,<col: " + (commentCol) + ">"));
                    break;

                case ' ':
                case '\t':
                case '\n': // LF
                case '\r': // CR
                    ch = read();
                    break;

                default:
                    commentLine = rowNumber;
                    commentCol = colNumber;
                    tokenList.add(new Token(TokenType.Error, "wrong identifier definition" , "<" + commentLine+ ">,<" + (commentCol) + ">"));
                    try {
                        throw new UnknownSymbol(ch);
                    } catch (Exception e){
                        tokenList.add(new Token(TokenType.Error, "wrong identifier definition" , "<" + commentLine+ ">,<" + (commentCol) + ">"));
                    }
            }
        }
        while(reader.hasNext());

        try {
            FileWriter fw = new FileWriter("C:\\Compiler\\Compiler\\tokenList.txt");
            for (Token token : tokenList)
                fw.write(token.toString());
            fw.close();
        }
        catch (IOException ex)
        {
            System.err.println("Could not write tokens to file");
        }

        deleteAppended(path);
    }


    private static void deleteAppended(String path) {
        File codeFile;
        codeFile = new File(path);
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(codeFile,"rw");
            randomAccessFile.setLength(randomAccessFile.length()-2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}