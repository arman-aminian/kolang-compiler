package Parser;
import java.io.*;
import java.lang.reflect.Array;
import java.sql.Struct;
import java.util.*;

//enum TokenType {KW, ST, id, Number, STRING, VAR, SR};
enum TokenType {KW, SpecialToken, Identifier, RealNum ,IntegerNum,Character, StringLiteral,Error,Include ,VAR, SR, PC, ADRS};

class GrammarLine {
    Token var;
    ArrayList<Token> definition;

    public static Token getToken(String s) {
        if (Parser.vars.contains(s)) {
            return new Token(TokenType.VAR, s , null);
        }
        if (s.equals("identifier")) {
            return new Token(TokenType.Identifier, s , null);
        }
        if (s.equals("kw")) {
            return new Token(TokenType.KW, s , null);
        }
        if (s.equals("realnum")) {
            return new Token(TokenType.RealNum, s , null);
        }
        if (s.equals("integernum")) {
            return new Token(TokenType.IntegerNum, s , null);
        }
        if (s.equals("character")) {
            return new Token(TokenType.Character, s , null);
        }
        if (s.equals("stringliteral")) {
            return new Token(TokenType.StringLiteral, s , null);
        }
        if (s.equals("specialtoken")) {
            return new Token(TokenType.SpecialToken, s , null);
        }
        if (s.equals("error")) {
            return new Token(TokenType.Error, s , null);
        }
        if (s.equals("include")) {
            return new Token(TokenType.Include, s , null);
        }
        if (s.length()>0) {
            if (s.charAt(0) == '@') {
                return new Token(TokenType.SR, s, null);
            }
        }
        return new Token(TokenType.SpecialToken, s , null);

    }

    public GrammarLine(String s) {
        definition = new ArrayList<>();
        String ss[] = s.split(" ", -1);
        var = getToken(ss[0]);
        for (int i = 2; i < ss.length; i++) {
            definition.add(getToken(ss[i]));
        }
    }
}


class Parser {
    static int sizeOfVariable;
    static ArrayList<String> grammarStrings;
    static String[][] myFirst;
    static String[][] myFollow;
    static HashMap<Integer, String> variables;
    static HashMap<String, Integer> varToInt;
    static HashMap<Integer, ArrayList<String>> definition;
    static HashSet<String> terminals;
    static HashMap<String, Integer> termToInt;
    static String[] done = new String[sizeOfVariable];
    static String[] first;
    static String[] f;
    static HashSet<String> vars;
    static ArrayList<String>[] allFirst;
    static ArrayList<String>[] allFollow;
    static HashMap<String, Integer> VarToInt = new HashMap<>();
    static ArrayList<GrammarLine> grammarLines;
    static ArrayList<Token>[] predict;
    static int[][] parseTable;
    static  HashMap<String , Integer> numberOfCall;
    static int n = 0, m = 0;

    public Parser() {
        newAllBeforGrammar();
        readGrammer();
        newAllAfterGrammar();
        init();
        findPredict();
        parseTableGenerator();
    }

    static void readGrammer() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("C:\\Compiler\\Compiler\\src\\Parser\\grammer.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String g = scanner.nextLine();
            grammarStrings.add(g);
//            System.out.println(g);
            String s[] = g.split(" ", -1);
            String var = s[0];
//            System.out.println(s[1]);
            if (!varToInt.containsKey(var)) {
//                System.out.println(var + " = " + (sizeOfVariable));
                varToInt.put(var, sizeOfVariable);
            }
            vars.add(var);
            variables.put(sizeOfVariable++, var);
            ArrayList<String> ss = new ArrayList<>();
            for (int i = 2; i < s.length; i++) {
//                System.out.println(sizeOfVariable-1 +" " + s[i]);
                ss.add(s[i]);

            }
            definition.put(sizeOfVariable - 1, ss);
        }
        int num = 0;
        for (String ss : vars) {
//            System.out.println(ss);
            VarToInt.put(ss, num++);
        }
//        System.out.println(num);
//        System.out.println(terminals.size());
        for (String s : grammarStrings) {
            String[] ss = s.split(" ", -1);
            grammarLines.add(new GrammarLine(s));
            for (String sss : ss) {
                if (!vars.contains(sss) && !sss.equals("->") && !sss.equals("eps")) {
                    if (!terminals.contains(sss)) {
                        termToInt.put(sss, terminals.size());
                        terminals.add(sss);
                    }
                }
            }
        }
    }

    static int getIndex(Token token) {
        TokenType tokenType = token.getType();
        String s = token.getValue();
        if (s.equals("$")) {
            return terminals.size();
        } else if (vars.contains(s)) {
            return VarToInt.get(s);
        }
//        System.out.println("name =" +token.getType().name().toLowerCase());
        if (token.getType().equals(TokenType.RealNum) || token.getType().equals(TokenType.IntegerNum) || token.getType().equals(TokenType.Identifier)  || token.getType().equals(TokenType.StringLiteral) || token.getType().equals(TokenType.Character)){
            // System.out.println(token);
            return termToInt.get(token.getType().name().toLowerCase());

        }
//        System.out.println(s);
        // System.out.println("checking");
//        for (String x: termToInt.keySet()
//             ) {
//            System.out.println(x);
//
//        }
//        System.out.println("giving  " + s );
        if(!tokenType.equals(TokenType.Error))

            if(!termToInt.get(s).equals(null))
                return termToInt.get(s);
            else
                throw new RuntimeException(" it is null pointer " + token.getPos());

        else
            throw new RuntimeException(s);
    }

    static int getIndex(String s) {
//        String s = token.getValue();
        if (s.equals("$")) {
            return terminals.size();
        } else if (vars.contains(s)) {
            return VarToInt.get(s);
        }
//        System.out.println("checking");
//        for (String x: termToInt.keySet()
//        ) {
//            System.out.println(x);
//
//        }
//        System.out.println("giving  " + s );
        return termToInt.get(s);
    }

    static void init() {
        int ptr = -1;
        int jm = 0;
        int km = 0;
        for (int k = 0; k < sizeOfVariable; k++) {
            for (int kay = 0; kay < 100000; kay++) {
                myFirst[k][kay] = "!";
            }
        }
        int point1 = 0, point2, xxx;
        for (int k = 0; k < sizeOfVariable; k++) {
            String c = variables.get(k);
            point2 = 0;
            xxx = 0;
            for (int kay = 0; kay <= ptr; kay++)
                if (c.equals(done[kay]))
                    xxx = 1;

            if (xxx == 1)
                continue;
            findfirst(c, 0, 0);
            ptr += 1;
            done[ptr] = c;
//            System.out.println("k = " + k);
//            System.out.println("point1 = " + point1);
//            System.out.println("ptr = " + ptr);
//            System.out.printf("\n First(%s) = { ", c);
            allFirst[k].add(c);
            myFirst[point1][point2++] = c;
            for (int i = jm; i < n; i++) {
                int lark = 0, chk = 0;

                for (lark = 0; lark < point2; lark++) {

                    if (first[i].equals(myFirst[point1][lark])) {
                        chk = 1;
                        break;
                    }
                }
                if (chk == 0) {
//                    System.out.print(first[i] + ", ");
                    allFirst[k].add(first[i]);
                    myFirst[point1][point2++] = first[i];
                }
            }
//            System.out.print("}\n");
            jm = n;
            point1++;
        }

        String[] donee = new String[sizeOfVariable];
        ptr = -1;

        for (int k = 0; k < sizeOfVariable; k++) {
            for (int kay = 0; kay < 100000; kay++) {
                myFollow[k][kay] = "!";
            }
        }
        point1 = 0;
        for (int e = 0; e < sizeOfVariable; e++) {
            String ck = variables.get(e);
            xxx = 0;
            point2 = 0;
            for (int kay = 0; kay <= ptr; kay++)
                if (ck.equals(donee[kay]))
                    xxx = 1;

            if (xxx == 1)
                continue;
            numberOfCall.clear();
            numberOfCall.put(ck,1);
            follow(ck);
            ptr += 1;
            allFollow[e].add(ck);
            for (int i = 0 + km; i < m; i++) {
                int lark = 0, chk = 0;
                for (lark = 0; lark < point2; lark++) {
                    if (f[i].equals(myFollow[point1][lark])) {
                        chk = 1;
                        break;
                    }
                }
                if (chk == 0) {
                    myFollow[point1][point2++] = f[i];
                    allFollow[e].add(f[i]);
                }
            }
            km = m;
            donee[ptr] = ck;
            point1++;
        }
    }

    static void follow(String c) {
        if (numberOfCall.containsKey(c)){
            numberOfCall.replace(c,numberOfCall.get(c)+1);
        }
        else numberOfCall.put(c,1);
        if (numberOfCall.containsKey(c) && numberOfCall.get(c)>20) return;
        if (variables.get(0).equals(c)) {
            f[m++] = "$";
        }
        for (int i = 0; i < sizeOfVariable; i++) {
            ArrayList<String> terms = definition.get(i);
            for (int j = 0; j < terms.size(); j++) {
                if (terms.get(j).equals(c)) {
                    if (j + 1 < terms.size()) {
                        followfirst(terms.get(j + 1), i, (j + 2));
                    }

                    if (j >= terms.size() - 1 && !c.equals(variables.get(i))) {
                        follow(variables.get(i));
                    }
                }
            }
        }
    }

    static void findfirst(String c, int q1, int q2) {
        int j;
        if (!vars.contains(c)) {
            first[n++] = c;
        }
        for (j = 0; j < sizeOfVariable; j++) {
            ArrayList<String> terms = definition.get(j);
            if (variables.get(j).equals(c)) {
                if (terms.get(0).equals("eps")) {
                    if (q1 >= sizeOfVariable || (q2 != 0 && q2 >= definition.get(q1).size()))
                        first[n++] = "eps";
                    else if (!(q1 >= sizeOfVariable || (q2 != 0 && q2 >= definition.get(q1).size())) && (q1 != 0 || q2 != 0)) {
                        findfirst(definition.get(q1).get(q2), q1, (q2 + 1));
                    } else
                        first[n++] = "eps";
                } else if (!vars.contains(terms.get(0))) {
                    first[n++] = terms.get(0);
                } else {
                    findfirst(terms.get(0), j, 1);
                }
            }
        }
    }

    static void followfirst(String c, int c1, int c2) {
        if (numberOfCall.containsKey(c)){
            numberOfCall.replace(c,numberOfCall.get(c)+1);
        }
        else numberOfCall.put(c,1);
        if (numberOfCall.containsKey(c) && numberOfCall.get(c)>20) return;
        if (!(vars.contains(c)))
        {
            f[m++] = c;

        }
        else {
            int i = 0, j = 1;
            for (i = 0; i < sizeOfVariable; i++) {
                if (myFirst[i][0].equals(c))
                    break;
            }

            while (!myFirst[i][j].equals("!")) {
                if (!myFirst[i][j].equals("eps")) {
                    f[m++] = myFirst[i][j];
                } else {
                    if ((c1 >= sizeOfVariable || (c2 != 0 && c2 == definition.get(c1).size()))) {
                        follow(variables.get(c1));
                    } else {
                        followfirst(definition.get(c1).get(c2), c1, c2 + 1);
                    }
                }
                j++;
            }
        }
    }

    static int mustFindNext;

    static void addFirst(int to, int from) {
//        mustFindNext = 0;
        for (int i = 1; i < allFirst[from].size(); i++) {
//            if (myFirst[from][i].equals("!")) break;
            if (allFirst[from].get(i).equals("eps")) {
                mustFindNext++;
                continue;
            }
            predict[to].add(GrammarLine.getToken(allFirst[from].get(i)));
        }
    }

    static void addFollow(int to, int from) {
//        System.out.println(grammarLines.get(to).var.getValue());
//        System.out.println(varToInt.get(grammarLines.get(to).var.getValue()));
//        System.out.println(to + " -- " + (from));
        for (int i = 1; i < allFollow[from].size(); i++) {
//            if (myFollow[from][i].equals("!")) break;
            predict[to].add(GrammarLine.getToken(allFollow[from].get(i)));
        }
    }

    static void findPredict() {
        for (int i = 0; i < sizeOfVariable; i++) {
            mustFindNext = 0;
            boolean setFollow = false;
            for (int j = 0; j < grammarLines.get(i).definition.size(); j++) {
//                System.out.println(grammarLines.get(i));
                String s = grammarLines.get(i).definition.get(j).getValue();
                if (!vars.contains(s)) {
                    if (s.equals("eps")) {
                        mustFindNext++;
                        setFollow = true;
                        continue;
                    }
                    predict[i].add(GrammarLine.getToken(s));
                    break;
                }
//                mustFindNext=0;
                addFirst(i, varToInt.get(s));
                if (mustFindNext == j)
                    break;
            }
            if (setFollow) {
                addFollow(i, varToInt.get(grammarLines.get(i).var.getValue()));
            } else if (mustFindNext == grammarLines.get(i).definition.size()) {
//                System.out.println("now here");
                addFollow(i, varToInt.get(grammarLines.get(i).var.getValue()));
            }
        }
    }

    static void newAllBeforGrammar() {
        sizeOfVariable = 0;
        grammarStrings = new ArrayList<>();
        grammarLines = new ArrayList<>();
        variables = new HashMap<>();
        definition = new HashMap<>();
        vars = new HashSet<>();
        varToInt = new HashMap<>();
        terminals = new HashSet<>();
        termToInt = new HashMap<>();
        numberOfCall = new HashMap<>();

    }

    static void newAllAfterGrammar() {
        allFirst = new ArrayList[sizeOfVariable];
        allFollow = new ArrayList[sizeOfVariable];
        for (int i = 0; i < sizeOfVariable; i++) {
            allFollow[i] = new ArrayList<>();
            allFirst[i] = new ArrayList<>();
        }
        done = new String[sizeOfVariable];
        myFirst = new String[sizeOfVariable][100000];
        myFollow = new String[sizeOfVariable][100000];
        first = new String[100000];
        f = new String[100000];
        predict = new ArrayList[sizeOfVariable];
        for (int i = 0; i < sizeOfVariable; i++) {
            predict[i] = new ArrayList<Token>();
        }
        parseTable = new int[vars.size()][terminals.size() + 1];
        for (int i = 0; i < parseTable.length; i++) {
            Arrays.fill(parseTable[i], -1);
        }
    }

    static void parseTableGenerator() {
        for (int i = 0; i < sizeOfVariable; i++) {
            for (int j = 0; j < predict[i].size(); j++) {
                String s = variables.get(i);
                parseTable[getIndex(s)][getIndex(predict[i].get(j))] = i;
            }
        }
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        for (int i = 0; i < sizeOfVariable; i++) {
//            System.out.print(i + " : ");
            for (int j = 0; j < predict[i].size(); j++) {
//                System.out.print(predict[i].get(j).getValue() + " ");
            }
//            System.out.println("\n");
        }
//        for (int i = 0; i < parser.parseTable.length; i++) {
//            for (int j = 0; j < parser.parseTable[i].length; j++) {
////                System.out.print(parser.parseTable[i][j] + " ");
//            }
////            System.out.println();
//        }
    }
}


public class ParserTD {
    public static ArrayList<Token> tokenList;
    public static ArrayList<Token> sentToCheckETokenList;
    public static ArrayList<Token> currentTDTokenList;
    public static ArrayList<Token>[] follows;
    public static Map<Token, Integer> tokenIndexer;
    private static ArrayList<Token> tokenAtIndex;
    private static int[][] PT;
    public static  Token token;
    static int token_index = 1;
    static int prev_token_index = 1;

    public ParserTD() {
        init();
        Parser parser = new Parser();
//        System.out.println("Here");
        loadTokens();
        CodeGenerator.setSP();
    }

    private static Token parseLine(String line) {
        if (line.length() < 3)
            return null;
        line = line.substring(1);
        line = line.substring(0, line.length() - 1);
        String tokenListRegex [] = line.split(" , ");
        String tt = tokenListRegex[0];
        String val = tokenListRegex[1];
        String pos = tokenListRegex[2];
        TokenType type = TokenType.KW;

        switch (tt) {
            case "KW":
                type = TokenType.KW;
                break;
            case "SpecialToken":
                type = TokenType.SpecialToken;
                break;
            case "Identifier":
                type = TokenType.Identifier;
                break;
            case "RealNum":
                type = TokenType.RealNum;
                break;
            case "IntegerNum":
                type = TokenType.IntegerNum;
                break;
            case "Character":
                type = TokenType.Character;
                break;
            case "StringLiteral":
                type = TokenType.StringLiteral;
                break;
            case "Error":
                type = TokenType.Error;
                break;
            case "Include":
                type = TokenType.Include;
                break;
            default:
                return null;
        }
        return new Token(type, val , pos);
    }

    public static void loadTokens() {
        tokenList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Compiler\\Compiler\\tokenList.txt"));
//            System.out.println("here");
            String line = br.readLine();
//            System.out.println("Here");
            while (line != null) {
//                System.out.println(line);
                Token token = parseLine(line);
                if (token == null)
                    break;
//                System.out.println(token.getValue());
                /*boolean hasP = false;
                boolean goBreak = false;
                if (token.getValue().equals("while")){
                    tokenList.add(token);
                    line = br.readLine();
                    if (line==null){
                        goBreak = true;
                        break;
                    }
                    token = parseLine(line);
                    if (token == null){
                        goBreak = true;
                        break;
                    }
                    if (token.getValue().equals("(")){
                        tokenList.add(token);
                        hasP = true;
                    }

                    while (true){
                        Token prevToken = token;
                        line = br.readLine();
                        if (line==null){
                            goBreak = true;
                            break;
                        }
                        token = parseLine(line);

                        if (token == null){
                            goBreak = true;
                            break;
                        }

                        if (token.getValue().equals("do")){
                            if (prevToken.getValue().equals(")") && hasP){
                                tokenList.add(prevToken);
                                tokenList.add(token);
                            } else if (!hasP)
                                tokenList.add(token);
                        }
                    }
                    if (goBreak)
                        break;
                }*/

                tokenList.add(token);
                line = br.readLine();
            }
            for (int i = 0; i < tokenList.size(); i++) {
//                System.out.println(tokenList.get(i));
            }
            br.close();
        } catch (IOException ex) {
            System.err.println("Couldn't read tokens");
        }
    }

    private static int getTokenIndex(Token token) {
        if (tokenIndexer.containsKey(token))
            return tokenIndexer.get(token);
        int sz = tokenIndexer.size();
        tokenIndexer.put(token, sz);
        tokenAtIndex.add(token);
        return tokenIndexer.get(token);
    }


    private static void init() {
        tokenIndexer = new HashMap<>();
        tokenAtIndex = new ArrayList<>();
    }

    public void doParserTD(String stackStart) {

        int currentTokenIndex;
        if(stackStart.equals("S"))
        {
            currentTDTokenList = tokenList;

        }
        else if(stackStart.equals("E"))
        {
            currentTDTokenList = sentToCheckETokenList;
            prev_token_index = token_index;
            token_index = 1;

        }


        currentTDTokenList.add(new Token(TokenType.SpecialToken,"$" , null));
        Stack<Token> PS = new Stack<>();
        PS.push(new Token(TokenType.SpecialToken, "$" , null));
        PS.push(new Token(TokenType.VAR, stackStart , null));
        token = currentTDTokenList.get(0);
        System.err.println("HERE");
        while (true) {
//            System.out.println("1");
//            System.out.println(token.getType());
//            System.out.println(token.getValue());
//            System.out.println(PS.peek().getType());
//            System.out.println(PS.peek().getValue());
//            System.out.println("1");

//            System.out.println("top : " + PS.peek());
//            System.out.println("token : " + token);
            if (PS.peek().getType() == TokenType.VAR) {
                if((PS.peek().getValue().equals("TT") ||
                        PS.peek().getValue().equals("EE")  ||
                        PS.peek().getValue().equals("BB")
                )&&( token.getValue().equals("$")
                        || token.getValue().equals(";")))
                    PS.pop();
                else  if((PS.peek().getValue().equals("STLL")
                )&& (token.getValue().equals("case") || token.getValue().equals("default")))
                    PS.pop();
                else if ((PS.peek().getValue().equals("ARRARG")) && (token.getValue().equals(")") || token.getValue().equals(","))){
                    Token rule = PS.pop();
                    CodeGenerator.generate(new Token(TokenType.SR, "@SETSIMPLEFUNC", rule.getPos()), currentTDTokenList.get(token_index - 2));
                }else if ((PS.peek().getValue().equals("ARRARGP")) && (token.getValue().equals(")") || token.getValue().equals(","))){
                    PS.pop();
//                    CodeGenerator.generate(new Token(TokenType.SR, "@SETSIMPLEFUNC", rule.getPos()), currentTDTokenList.get(token_index - 2));
                }
                else {
//                    System.out.println(token.getValue());
//                System.out.println(Parser.getIndex(token));
//                System.out.println(Parser.getIndex("E"));
                    int prod = getProduction(token, PS.pop());
                    // rhst
//                    System.out.println("prod :" + prod);

                    GrammarLine line = Parser.grammarLines.get(prod);
                    for (int i = line.definition.size() - 1; i >= 0; i--)
                        PS.push(line.definition.get(i));
                }


            }

            else if (PS.peek().getValue().equals("$")) {
                if (token.getValue().equals("$")) {
                    System.err.println("Accepted");
                    codeTableFile();
                    PS.pop();
                    token_index = prev_token_index;
                    return;
                } else
                {
                    token_index = prev_token_index;

                    throw new RuntimeException("Code shall not continue");


                }
            } else if (PS.peek().getValue().equals("eps")) {
                PS.pop();
            } else if (PS.peek().getType() == TokenType.SR) {
                Token rule = PS.pop();
//                System.out.println(currentTDTokenList.get(token_index - 2).getValue());
                CodeGenerator.generate(rule, currentTDTokenList.get(token_index - 2));
//                token = tokenList.get(token_index++);
            } else // terminal
            {
//                System.out.println(token.getType());
//                System.out.println(PS.peek().getType());
                if ((PS.peek().getType().equals(TokenType.RealNum) || PS.peek().getType().equals(TokenType.IntegerNum) || PS.peek().getType().equals(TokenType.StringLiteral) || PS.peek().getType().equals(TokenType.Character)|| PS.peek().getType().equals(TokenType.Identifier)) && PS.peek().getType().equals(token.getType())) {
                    PS.pop();
//                    if (token_index!=tokenList.size())
                    token = currentTDTokenList.get(token_index++);
                }
                else if (!(PS.peek().getType().equals(TokenType.RealNum) || PS.peek().getType().equals(TokenType.IntegerNum) || PS.peek().getType().equals(TokenType.StringLiteral)  || PS.peek().getType().equals(TokenType.Character)|| PS.peek().getType().equals(TokenType.Identifier)) && PS.peek().getValue().equals(token.getValue()))
                {
                    PS.pop();
//                    if (token_index!=tokenList.size())
                    token = currentTDTokenList.get(token_index++);
                }
                else {



                    throw new RuntimeException("Unexpected token at " + token.getPos() );
                }
            }
        }
    }

    private void codeTableFile() {
        try {
//            FileWriter fw = new FileWriter("E:\\Uni_Data\\Term_4\\Compiler\\Compiler\\codeTable.txt");
            FileWriter fw = new FileWriter("C:\\Compiler\\Compiler\\codeTable.txt");

            fw.write("PC   |  OPC  |  OPR1  |  OPR1  |  RES   |" + "\n");
            fw.write("-----------------------------------------" + "\n");
            for (int i = 0; i < CodeGenerator.pc; i++) {

                String opc = CodeGenerator.codeTable[i].getOpc();
                String op1= CodeGenerator.codeTable[i].getOp1();
                String op2 = CodeGenerator.codeTable[i].getOp2();
                String res = CodeGenerator.codeTable[i].getRes();
                String line = "";
                if(i<10)
                    line = (i + ")   |  ") ;
                else if(i>= 10 && i<100)
                    line = (String.valueOf(i) + ")  |  ") ;
                else if(i>= 100)
                    line = (String.valueOf(i) + ") |  ") ;

                if(opc.length() == 2)
                    line += (opc + "   | " );
                else if(opc.length() == 3)
                    line += (opc + "  | " );

                if(op1== null)
                    line += ( "       | " );
                else if(op1.length()==1)
                    line += (op1 + "      | " );
                else if(op1.length()==2)
                    line += (op1 + "     | " );
                else if(op1.length()==3)
                    line += (op1 + "    | " );
                else if(op1.length()==4)
                    line += (op1 + "   | " );
                else if(op1.length()==5)
                    line += (op1 + "  | " );
                else if(op1.length()==6)
                    line += (op1 + " | " );


                if(op2== null)
                    line += ("       | " );
                else if(op2.length()==1)
                    line += (op2 + "      | " );
                else if(op2.length()==2)
                    line += (op2 + "     | " );
                else if(op2.length()==3)
                    line += (op2 + "    | " );
                else if(op2.length()==4)
                    line += (op2 + "   | " );
                else if(op2.length()==5)
                    line += (op2 + "  | " );
                else if(op2.length()==6)
                    line += (op2 + " | " );

                if(res== null)
                    line += ("       | " );
                else if(res.length()==1)
                    line += (res + "      | " );
                else if(res.length()==2)
                    line += (res + "     | " );
                else if(res.length()==3)
                    line += (res + "    | " );
                else if(res.length()==4)
                    line += (res + "   | " );
                else if(res.length()==5)
                    line += (res + "  | " );
                else if(res.length()==6)
                    line += (res + " | " );

                fw.write(line + "\n");
            }
            fw.write("-----------------------------------------");



            fw.close();
        }
        catch (IOException ex)
        {
            System.err.println("Could not write tokens to file");
        }

    }

    public static int getProduction(Token token, Token topPS) {
        int terminal = Parser.getIndex(token);//Parser.termToInt.get(token);
        int variable = Parser.getIndex(topPS.getValue());
        // check shavad
//        if (token.getValue().equals("else") && topPS.getValue().equals("else"))
//            return 141;
//        System.out.println("2");
//        System.out.println(token.getValue());
//        System.out.println(topPS.getValue());
//        System.out.println("2");
//        System.out.println(topPS.getValue());
//        System.out.println(terminal + " " + variable);
        int res = Parser.parseTable[variable][terminal];

//        System.out.println(res);
        if (res == -1) {
            throw new RuntimeException("No Prediction Error at " + token.getPos());
        }
        return res;
    }
}

class Token {
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

    public String getPos() {
        return pos;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "<" + type.toString() + " , " + value + " , " + pos + ">\n";
    }
}
