package Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

enum ActionType {
    REDUCE, GOTO, SHIFT, ACCEPT, NoACTION
}
class Action {

    ActionType actionType;
    int num;

    public Action(String s) {
        char c = s.charAt(0);
        int num;
        if (c == '-') {
            this.actionType = ActionType.NoACTION;
            this.num = -1;
        }
        else if (c == 'g') {
            this.actionType = ActionType.GOTO;
            this.num = Integer.parseInt(s.substring(1));
        }
        else if (c == 'r') {
            this.actionType = ActionType.REDUCE;
            this.num = Integer.parseInt(s.substring(1));
        } else if (c == 's') {
            this.actionType = ActionType.SHIFT;
            this.num = Integer.parseInt(s.substring(1));
        } else if (c == 'a') {
            this.actionType = ActionType.ACCEPT;
            this.num = -1;
        }

    }
}
class LRrow {
    int productionNum;
    String lhsVar;  // left hand side variable
    int rhsl;  //right hand side lenght
    ArrayList<String> rhssr  = new ArrayList<String>(0); //right hand side semantic rule
}

public class ParserBU {
    static int numOfStates = 26;
    static int numOfTV = 21;
    static int indexBU;
    static ArrayList<Token> beTokenlist;
    static ArrayList<Token> eTokenListToSend;
    static Token tokenBU;
    static Action[][] parseTable‌‌bBU = new Action[numOfStates][numOfTV];
    static ArrayList<LRrow> LRTable = new ArrayList<LRrow>(0);
    static HashMap<String, Integer> tvToIndex = new HashMap<>();
    static HashMap<Integer,String> indexToTV = new HashMap<>();


    public ParserBU()
    {
        generateParseTable();
        generateLRTable();
    }

    private void generateParseTable() {

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("C:\\Compiler\\Compiler\\src\\Parser\\bottomUpParseTable.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int j = 0;
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            String[] ss = s.split(" ",-1);
            if(j == 0)
            {
                for (int i = 0; i < ss.length; i++)
                {
                    tvToIndex.put(ss[i] , i);
                    indexToTV.put(i , ss[i]);
                }
            }
            else
            {
                for (int i = 0; i < ss.length; i++) {
                    parseTable‌‌bBU[j-1][i] = new Action(ss[i]);
                }
            }

            j++;
        }
    }

    private void generateLRTable() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("C:\\Compiler\\Compiler\\src\\Parser\\buttomUpGrammer.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int j = 0;
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            String[] ss = s.split(" -> ",-1);
            LRrow lrRow = new LRrow();
            lrRow.productionNum=j;
            lrRow.lhsVar=ss[0];
            String[] sss = ss[1].split(" ",-1);
            for (int i = 0; i <sss.length ; i++) {
                if(sss[i].startsWith("@"))
                {
                    lrRow.rhssr.add(sss[i]);
                }
            }
            if(sss[0].equals("eps"))
                lrRow.rhsl=0;
            else
                lrRow.rhsl=sss.length-lrRow.rhssr.size();
            LRTable.add(lrRow);
            j++;
        }
    }

    public void doParserBU(ArrayList<Token> beTokenlistInput)
    {
        eTokenListToSend = new ArrayList<>(0);
        beTokenlist = beTokenlistInput;
        tokenBU = beTokenlist.get(0);
        int indexBU = 1;
        ActionType actionType;
        int num;
        beTokenlist.add(new Token(TokenType.SpecialToken,"$" , null));
        Stack<Integer> PS = new Stack<>();
        PS.push(0);


        int pTRow;
        int pTCol;
        while (true)
        {
//            System.out.println("bu ps top : " + PS.peek());
            pTRow = PS.peek();
//            System.out.println("bu token : " + tokenBU);
            //todo : complete for all possible E starters
            if(tokenBU.getType().equals(TokenType.IntegerNum) ||
                    tokenBU.getType().equals(TokenType.RealNum) ||
                    tokenBU.getType().equals(TokenType.Identifier)||
                            tokenBU.getValue().equals("+") ||
                tokenBU.getValue().equals("*") ||
                tokenBU.getValue().equals("-") ||
                tokenBU.getValue().equals("/"))

            {
                eTokenListToSend.add(tokenBU);
                tokenBU = beTokenlist.get(indexBU++);
            }

            else if(
            tokenBU.getValue().equals("||") ||
            tokenBU.getValue().equals("&&") ||
            tokenBU.getValue().equals("(") ||
            tokenBU.getValue().equals(")") ||
            tokenBU.getValue().equals("<") ||
            tokenBU.getValue().equals("=<") ||
            tokenBU.getValue().equals(">") ||
            tokenBU.getValue().equals(">=") ||
            tokenBU.getValue().equals("=") ||
            tokenBU.getValue().equals("!=") ||
            tokenBU.getValue().equals("$")
            )
            {

                pTCol = tvToIndex.get(tokenBU.getValue());
                actionType = parseTable‌‌bBU[pTRow][pTCol].actionType;
                num = parseTable‌‌bBU[pTRow][pTCol].num;
//                System.out.println("action type : " + actionType);
//                System.out.println("action num : " + num);

                switch (actionType){
                    case SHIFT:{
                        PS.push(num);

                        tokenBU = beTokenlist.get(indexBU++);

                        break;
                    }

                    case REDUCE:
                    {
                        for (int i = 0; i <LRTable.get(num).rhsl ; i++) {
                            PS.pop();
                        }

                        //do the semantic rules

                        for (String sr:LRTable.get(num).rhssr
                        ) {
                            Token rule =new Token(TokenType.SR, sr, null);
                            CodeGenerator.generate(rule, beTokenlist.get(indexBU - 2));
                        }

                        PS.push(parseTable‌‌bBU[PS.peek()][tvToIndex.get(LRTable.get(num).lhsVar)].num);
                        break;
                    }
                    case ACCEPT:
                    {
                        System.err.println("Accepted be");
                        ParserTD.token_index = ParserTD.prev_token_index;
                        ParserTD.currentTDTokenList = ParserTD.tokenList;
                        ParserTD.token = ParserTD.tokenList.get(ParserTD.token_index-1);

                        return;
                    }
                    case NoACTION:
                    {
                        throw new RuntimeException("Unexpected token at " + tokenBU.getPos() );

                    }
                }

            }
            else
            {
                throw new RuntimeException("Unexpected token at " + tokenBU.getPos() );
            }
        }
    }


}
