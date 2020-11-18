package Parser;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Stack;

public class CodeGenerator {
    static int pc = 0;
    static String scope = "global";
    static CodeLine[] codeTable = new CodeLine[2000];
    static SymbolTable symbolTable = new SymbolTable();
    static int address = 0;
    static int funcAddress = 0;
    static String sp;
    static String funcId;
    static int paramCount = 0;
    static int tempAddress = 5000;
    static Stack<Token> semanticStack = new Stack<>();
    static Stack<Integer> breakStack = new Stack<>();

    static String setSP(){
        symbolTable.addDescriptor(new SimpleDescriptor("int", "sp", "simple", "global", address));
        address = 4;
        sp = String.valueOf(symbolTable.findDescriptor("sp", "global").getAddress());
        return sp;
    }

    static void generate(Token rule, Token lastToken){
        switch (rule.getValue()){
            case "@HERE" : {
                System.out.println(lastToken.getValue() + "herreeeee");
                break;
            }
            case "@SHOWSS" : {
                System.out.println(semanticStack.peek().getValue() + "  :::::::::::::::::::::::::  " + semanticStack.peek());
                break;
            }
            case "@SHOWTABLE" : {
                showCodeTable();
                break;
            }
            case "@PUSH" : {
                semanticStack.push(lastToken);
                System.out.println(lastToken.getValue() + "pushed");
                break;
            }

            case "@POP" : {
                System.out.println(semanticStack.pop().getValue() + " poped");
                break;
            }
            case "@ADD" : {
                System.out.println("add");
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                if (op1token.getType() == TokenType.IntegerNum || op1token.getType() == TokenType.RealNum){
                    op1 = "#" + op1token.getValue();
                }else op1 = op1token.getValue();

                if (op2token.getType() == TokenType.IntegerNum || op2token.getType() == TokenType.RealNum){
                    op2 = "#" + op2token.getValue();
                } else op2 = op2token.getValue();

                String temp = getTemp();
                codeTable[pc] = new CodeLine("ADD", op1, op2, temp);
                semanticStack.push(new Token(TokenType.ADRS, temp, null));
                pc++;

                showCodeTable();
                break;
            }

            case "@SUB" : {
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                if (op1token.getType() == TokenType.IntegerNum || op1token.getType() == TokenType.RealNum){
                    op1 = "#" + op1token.getValue();
                }else op1 = op1token.getValue();

                if (op2token.getType() == TokenType.IntegerNum || op2token.getType() == TokenType.RealNum){
                    op2 = "#" + op2token.getValue();
                } else op2 = op2token.getValue();

                String temp = getTemp();
                codeTable[pc] = new CodeLine("SUB", op1, op2, temp);
                semanticStack.push(new Token(TokenType.ADRS, temp, null));
                pc++;
                for (int i = 0; i < pc; i++) {
                    System.out.println(codeTable[i].toString());
                }
                break;
            }

            case "@MULT" : {
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                if (op1token.getType() == TokenType.IntegerNum || op1token.getType() == TokenType.RealNum){
                    op1 = "#" + op1token.getValue();
                }else op1 = op1token.getValue();

                if (op2token.getType() == TokenType.IntegerNum || op2token.getType() == TokenType.RealNum){
                    op2 = "#" + op2token.getValue();
                } else op2 = op2token.getValue();

                String temp = getTemp();
                codeTable[pc] = new CodeLine("MULT", op1, op2, temp);
                semanticStack.push(new Token(TokenType.ADRS, temp, null));
                pc++;
                for (int i = 0; i < pc; i++) {
                    System.out.println(codeTable[i].toString());
                }
                break;
            }

            case "@DIV" : {
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                if (op1token.getType() == TokenType.IntegerNum || op1token.getType() == TokenType.RealNum){
                    op1 = "#" + op1token.getValue();
                }else op1 = op1token.getValue();

                if (op2token.getType() == TokenType.IntegerNum || op2token.getType() == TokenType.RealNum){
                    op2 = "#" + op2token.getValue();
                } else op2 = op2token.getValue();

                String temp = getTemp();
                codeTable[pc] = new CodeLine("DIV", op1, op2, temp);
                semanticStack.push(new Token(TokenType.ADRS, temp, null));
                pc++;
                for (int i = 0; i < pc; i++) {
                    System.out.println(codeTable[i].toString());
                }
                break;
            }

            case "@MOD" : {
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                if (op1token.getType() == TokenType.IntegerNum || op1token.getType() == TokenType.RealNum){
                    op1 = "#" + op1token.getValue();
                }else op1 = op1token.getValue();

                if (op2token.getType() == TokenType.IntegerNum || op2token.getType() == TokenType.RealNum){
                    op2 = "#" + op2token.getValue();
                } else op2 = op2token.getValue();

                String temp = getTemp();
                codeTable[pc] = new CodeLine("MOD", op1, op2, temp);
                semanticStack.push(new Token(TokenType.ADRS, temp, null));
                pc++;
                for (int i = 0; i < pc; i++) {
                    System.out.println(codeTable[i].toString());
                }
                break;
            }

            case "@INC" : {
                Token op1token = semanticStack.pop();
                String op1 = "#" + op1token.getValue();

                String temp = getTemp();
                codeTable[pc] = new CodeLine("INC", op1, temp);
                semanticStack.push(new Token(TokenType.ADRS, temp, null));
                pc++;
                for (int i = 0; i < pc; i++) {
                    System.out.println(codeTable[i].toString());
                }
                break;
            }

            case "@DEC" : {
                Token op1token = semanticStack.pop();
                String op1 = "#" + op1token.getValue();

                String temp = getTemp();
                codeTable[pc] = new CodeLine("DEC", op1, temp);
                semanticStack.push(new Token(TokenType.ADRS, temp, null));
                pc++;
                for (int i = 0; i < pc; i++) {
                    System.out.println(codeTable[i].toString());
                }
                break;
            }

            case "@BE" : {
                System.out.println("ORdered to go in @BE");
                System.out.println("tokenIndex :" + ParserTD.token_index);
                ArrayList<Token> beTokenListToSend = new ArrayList<>(0);
                int i = ParserTD.token_index-1;
                System.out.println("adding to send to bu ");
                while(!ParserTD.tokenList.get(i).getValue().equals("{") && !ParserTD.tokenList.get(i).getValue().equals("do") && !ParserTD.tokenList.get(i).getValue().equals(";") )
                {
                    System.out.println(ParserTD.tokenList.get(i));
                    beTokenListToSend.add(ParserTD.tokenList.get(i));
                    i++;

                }
                ParserTD.token = ParserTD.tokenList.get(i);
                ParserTD.token_index = i+1;
                System.out.println("sooo now");
                System.out.println("token td = " + ParserTD.token );
                System.out.println("token index td = " + ParserTD.token_index );
                System.out.println("called bu parser");
                ParserMain.parserBU.doParserBU(beTokenListToSend);

                break;
            }
            case "@AND" : {
                System.out.println("and");
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                op1 = op1token.getValue();
                op2 = op2token.getValue();

                String temp = getTemp();
                codeTable[pc] = new CodeLine("AND", op1, op2, temp);
                semanticStack.push(new Token(TokenType.ADRS, temp, null));
                pc++;

                showCodeTable();
                break;
            }
            case "@OR" : {
                System.out.println("or");
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                op1 = op1token.getValue();
                op2 = op2token.getValue();

                String temp = getTemp();
                codeTable[pc] = new CodeLine("OR", op1, op2, temp);
                semanticStack.push(new Token(TokenType.ADRS, temp, null));
                pc++;

                showCodeTable();
                break;
            }

            case "@ISL" : {
                String result = getTemp();
                codeTable[pc] = new CodeLine("MOV", "#1",result);
                pc++;
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                op1 = op1token.getValue();
                op2 = op2token.getValue();
                //todo: at the end of each e the result is moved to a temp address => eResultToken.getValue() is an address
                codeTable[pc] = new CodeLine("JL", op1, op2 , Integer.toString(pc+2));
                pc++;
                codeTable[pc] = new CodeLine("MOV", "#0",result);
                pc++;
                semanticStack.push(new Token(TokenType.ADRS, result, null));


                showCodeTable();

                break;
            }

            case "@ISLEQ" : {
                String result = getTemp();
                codeTable[pc] = new CodeLine("MOV", "#1",result);
                pc++;
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                op1 = op1token.getValue();
                op2 = op2token.getValue();
                //todo: at the end of each e the result is moved to a temp address => eResultToken.getValue() is an address
                codeTable[pc] = new CodeLine("JLE", op1, op2 , Integer.toString(pc+2));
                pc++;
                codeTable[pc] = new CodeLine("MOV", "#0",result);
                pc++;
                semanticStack.push(new Token(TokenType.ADRS, result, null));


                showCodeTable();

                break;
            }

            case "@ISGR" : {
                String result = getTemp();
                codeTable[pc] = new CodeLine("MOV", "#1",result);
                pc++;
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                op1 = op1token.getValue();
                op2 = op2token.getValue();
                //todo: at the end of each e the result is moved to a temp address => eResultToken.getValue() is an address
                codeTable[pc] = new CodeLine("JG", op1, op2 , Integer.toString(pc+2));
                pc++;
                codeTable[pc] = new CodeLine("MOV", "#0",result);
                pc++;
                semanticStack.push(new Token(TokenType.ADRS, result, null));


                showCodeTable();

                break;
            }
            case "@ISGREQ" : {
                String result = getTemp();
                codeTable[pc] = new CodeLine("MOV", "#1",result);
                pc++;
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                op1 = op1token.getValue();
                op2 = op2token.getValue();
                //todo: at the end of each e the result is moved to a temp address => eResultToken.getValue() is an address
                codeTable[pc] = new CodeLine("JGE", op1, op2 , Integer.toString(pc+2));
                pc++;
                codeTable[pc] = new CodeLine("MOV", "#0",result);
                pc++;
                semanticStack.push(new Token(TokenType.ADRS, result, null));


                showCodeTable();

                break;
            }

            case "@ISEQ" : {
                String result = getTemp();
                codeTable[pc] = new CodeLine("MOV", "#1",result);
                pc++;
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                op1 = op1token.getValue();
                op2 = op2token.getValue();
                //todo: at the end of each e the result is moved to a temp address => eResultToken.getValue() is an address
                codeTable[pc] = new CodeLine("JE", op1, op2 , Integer.toString(pc+2));
                pc++;
                codeTable[pc] = new CodeLine("MOV", "#0",result);
                pc++;
                semanticStack.push(new Token(TokenType.ADRS, result, null));


                showCodeTable();

                break;
            }
            case "@ISNEQ" : {
                String result = getTemp();
                codeTable[pc] = new CodeLine("MOV", "#1",result);
                pc++;
                Token op2token = semanticStack.pop();
                Token op1token = semanticStack.pop();
                String op1;
                String op2;
                op1 = op1token.getValue();
                op2 = op2token.getValue();
                //todo: at the end of each e the result is moved to a temp address => eResultToken.getValue() is an address
                codeTable[pc] = new CodeLine("JNE", op1, op2 , Integer.toString(pc+2));
                pc++;
                codeTable[pc] = new CodeLine("MOV", "#0",result);
                pc++;
                semanticStack.push(new Token(TokenType.ADRS, result, null));


                showCodeTable();

                break;
            }
            case "@CHECKIDNUMVAL" : {
                String result = getTemp();
                codeTable[pc] = new CodeLine("MOV", "#0",result);
                pc++;
                Token eResultToken = semanticStack.pop();
                //todo: at the end of each e the result is moved to a temp address => eResultToken.getValue() is an address
                codeTable[pc] = new CodeLine("JZ", eResultToken.getValue(), Integer.toString(pc+2));
                pc++;
                codeTable[pc] = new CodeLine("MOV", "#1",result);
                pc++;
                semanticStack.push(new Token(TokenType.ADRS, result, null));


                showCodeTable();

                break;
            }
            case "@CHECKE" :{

                if(ParserBU.eTokenListToSend.size()<1)
                    throw new RuntimeException("Unexpected token at " + ParserTD.tokenList.get(ParserTD.token_index-ParserBU.beTokenlist.size()+ParserBU.indexBU) );
                ParserTD.sentToCheckETokenList = ParserBU.eTokenListToSend;
                ParserMain.parserTD.doParserTD("E");
                ParserBU.eTokenListToSend = new ArrayList<>(0);

                break;

            }

            case "@PUSHPC" : {
                semanticStack.push(new Token(TokenType.IntegerNum, String.valueOf(pc), null));
                System.out.println("pushpc");
                break;
            }

            case "@JZ" : {
                Token op1token = semanticStack.pop();
                String op1 = op1token.getValue();
                codeTable[pc] = new CodeLine("JZ", op1);
                semanticStack.push(new Token(TokenType.IntegerNum, String.valueOf(pc), null));
                pc++;
                System.out.println("jz");
                break;
            }

            case "@JMPCOMPLJZ" : {
                codeTable[pc] = new CodeLine();
                codeTable[pc].setOpc("JMP");
                String x = semanticStack.pop().getValue();
                codeTable[Integer.parseInt(x)].setOp2(String.valueOf(pc+1));
                x = semanticStack.pop().getValue();
                codeTable[pc].setOp1(x);
                pc++;
                System.out.println("jmpcmpl");
                showCodeTable();
                break;
            }

            case "@SETSIMPLEDEC" : {
//                System.out.println("sfvsvsfv");
                Token id = semanticStack.pop();
                Token type = semanticStack.peek();
                if (type.getType() == TokenType.Identifier)
                    if (symbolTable.findDescriptor(type.getValue(), scope) == null)
                        throw new RuntimeException(type.getValue() + " not found at " + lastToken.getPos());



                if (scope.equals("global")) {
                    symbolTable.addDescriptor(new SimpleDescriptor(type.getValue(), id.getValue(), "simple", scope, address));
                    address += getSize(type.getValue());
                }
                else {
                    symbolTable.addDescriptor(new SimpleDescriptor(type.getValue(), id.getValue(), "simple", scope, funcAddress));
                    funcAddress += getSize(type.getValue());
                }
                semanticStack.push(id);

                for (Descriptor descriptor : symbolTable.getSymbolTable()) {
                    System.out.println(descriptor);
                }
                break;
            }

            case "@SETARRAYDEC" : {
//                String size = semanticStack.pop().getValue();
                Token id = semanticStack.pop();
                Token type = semanticStack.peek();

//                address += getSize(type.getValue());
                if (scope.equals("global")) {
                    symbolTable.addDescriptor(new ArrayDescriptor(type.getValue(), id.getValue(), "array", scope, address, 1));
                    address += getSize(type.getValue());
                }
                else {
                    symbolTable.addDescriptor(new ArrayDescriptor(type.getValue(), id.getValue(), "array", scope, funcAddress, 1));
                    funcAddress += getSize(type.getValue());
                }
                semanticStack.push(id);

                break;
            }

            case "@SETARRAYSIZE" : {
                Token size = semanticStack.pop();
                Token name = semanticStack.pop();
                Token type = semanticStack.peek();
                int temp = ((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).getSize();
                ((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).setSize(((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).getSize() * Integer.parseInt(size.getValue()));
                if (scope.equals("global")) {
                    address += ((((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).getSize() - temp) * getSize(type.getValue()));

                }
                else {
                    funcAddress += ((((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).getSize() - temp) * getSize(type.getValue()));
                }

                ((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).dimentionSizes.add(Integer.parseInt(size.getValue()));
                semanticStack.push(name);
                System.out.println("address : " + address);
                break;
            }

            case "@SETEVALUE": {
                System.out.println("setid");
                Token asign = semanticStack.pop();
                System.out.println(asign.getValue());
                System.out.println(asign.getType());
                Token asignTo = semanticStack.pop();
                System.out.println(asignTo.getValue());
                if (asign.getType() == TokenType.ADRS){
//                    System.out.println("000000000000000000" + symbolTable.findDescriptor(asign.getValue(), scope));
                    if (symbolTable.findDescriptor(asignTo.getValue(), scope).getType().equals("int") || symbolTable.findDescriptor(asignTo.getValue(), scope).getType().equals("double") || symbolTable.findDescriptor(asignTo.getValue(), scope).getType().equals("float") ){
                        codeTable[pc] = new CodeLine("MOV", asign.getValue(), String.valueOf(symbolTable.findDescriptor(asignTo.getValue(), scope).getAddress()));
                        pc++;
                    } else {
                        throw new RuntimeException("symbol table error at " + lastToken.getPos());
                    }
                } else if (asign.getType() == TokenType.Identifier){
                    if (symbolTable.findDescriptor(asignTo.getValue(), scope).getType().equals(symbolTable.findDescriptor(asign.getValue(), scope).getType())) {
                        codeTable[pc] = new CodeLine("MOV", String.valueOf(symbolTable.findDescriptor(asign.getValue(), scope).getAddress()), String.valueOf(symbolTable.findDescriptor(asignTo.getValue(), scope).getAddress()));
                        pc++;
                        } else {
                        throw new RuntimeException("symbol table error at " + lastToken.getPos());
                        }
                } else if (asign.getType() == TokenType.StringLiteral){
                    if (symbolTable.findDescriptor(asignTo.getValue(), scope).getType().equals("string")){
                        codeTable[pc] = new CodeLine("MOV", asign.getValue(), String.valueOf(symbolTable.findDescriptor(asignTo.getValue(), scope).getAddress()));
                        pc++;
                    } else {
                        throw new RuntimeException("symbol table error at " + lastToken.getPos());
                    }
                } else if (asign.getType() == TokenType.Character){
                    if (symbolTable.findDescriptor(asignTo.getValue(), scope).getType().equals("char")){
                        codeTable[pc] = new CodeLine("MOV", asign.getValue(), String.valueOf(symbolTable.findDescriptor(asignTo.getValue(), scope).getAddress()));
                        pc++;
                    } else {
                        throw new RuntimeException("symbol table error at " + lastToken.getPos());
                    }
                } else System.out.println("error");
//                if (symbolTable.findDescriptor(asign.getValue(), scope) != null) {
//                    if (asign.getType() == TokenType.Identifier) {
//                        if (symbolTable.findDescriptor(asignTo.getValue(), scope).getType().equals(symbolTable.findDescriptor(asign.getValue(), scope).getName())) {
//                            codeTable[pc] = new CodeLine("MOV", asign.getValue(), asignTo.getValue());
//                            pc++;
//                        } else {
//                            System.out.println("not matches");
//                        }
//                    }
//                } else System.out.println("not defined");
                break;
            }

            case "@MOVTOTEMP" : {
                if (semanticStack.peek().getType() != TokenType.Identifier) {
                    Token token = semanticStack.pop();
                    String temp = getTemp();
                    codeTable[pc] = new CodeLine("MOV", token.getValue(), temp);
                    pc++;
                    semanticStack.push(new Token(TokenType.ADRS, temp, null));
                }
                break;
            }


            case "@FOR" : {
                Token to = semanticStack.pop();
                Token from = semanticStack.pop();
                Token name = semanticStack.pop();

                symbolTable.addDescriptor(new SimpleDescriptor("int", name.getValue(),"simple", scope, address));
                address += getSize("int");
                codeTable[pc] = new CodeLine("JG", String.valueOf(symbolTable.findDescriptor(name.getValue(), scope).getAddress()), to.getValue());
                semanticStack.push(new Token(TokenType.IntegerNum, String.valueOf(pc), null));
                pc++;
                codeTable[pc] = new CodeLine("INC", from.getValue());
                pc++;

                break;
            }

            case "@ENDFOR" : {
                String prePC = semanticStack.pop().getValue();
//                System.out.println("prepc : " + prePC);
                codeTable[Integer.parseInt(prePC)].setRes(String.valueOf(pc+1));
                codeTable[pc] = new CodeLine("JMP", prePC);
                pc++;

                while(!breakStack.peek().equals(-1))
                {
                    codeTable[breakStack.pop()].setOp1(String.valueOf(pc));

                }
                breakStack.pop();
                showCodeTable();

                break;
            }

            case "@COMPLBREAK" : {

                while(!breakStack.peek().equals(-1))
                {
                    codeTable[breakStack.pop()].setOp1(String.valueOf(pc));

                }
                breakStack.pop();
                break;
            }

            case "@PUSHFLAG" : {
//                System.out.println("-1 is pushed to flag start of repeat scope");
                breakStack.push(-1);
                break;
            }

            case "@JMPBREAK" : {
                codeTable[pc] = new CodeLine();
                System.out.println("break pc pushed");
                codeTable[pc].setOpc("JMP");
                breakStack.push(pc);
                pc++;
                break;

            }

            case "@COMPLJZIF" : {
                System.out.println("@COMPLJZIF");
                String x = semanticStack.pop().getValue();
                codeTable[Integer.parseInt(x)].setOp2(String.valueOf(pc));

                showCodeTable();
                break;

            }

            case "@JMPCOMPLJZIF" : {
                codeTable[pc] = new CodeLine();
                codeTable[pc].setOpc("JMP");
                pc++;
                String x = semanticStack.pop().getValue();
                codeTable[Integer.parseInt(x)].setOp2(String.valueOf(pc));
                semanticStack.push(new Token(TokenType.IntegerNum, String.valueOf(pc-1), null));
                System.out.println("@JMPCOMPLJZIF");
                showCodeTable();
                break;
            }

            case "@COMPLJMPIF" : {
                System.out.println("@COMPLJMPIF");
                String x = semanticStack.pop().getValue();
                codeTable[Integer.parseInt(x)].setOp1(String.valueOf(pc));

                showCodeTable();
                break;
            }

            case "@JNZ" : {
                Token be = semanticStack.pop();
                Token prevPC = semanticStack.pop();
                System.out.println("00000000000  " + be.getValue());
                System.out.println("00000000000  " + prevPC.getValue());
                codeTable[pc] = new CodeLine("JNZ", be.getValue(), prevPC.getValue());
                pc++;
                showCodeTable();
                break;
            }

            case "@CREATEUNION" : {
                Token name = semanticStack.peek();
                symbolTable.addDescriptor(new UnionDescriptor("union", name.getValue(), "union", scope, address, 0));
                break;
            }

            case "@ADDUNIONVALUE" : {
                Token value = semanticStack.pop();
                Token type = semanticStack.pop();
                Token name = semanticStack.peek();
                ((UnionDescriptor)(symbolTable.findDescriptor(name.getValue(), scope))).addValue(type.getValue(), value.getValue());
                ((UnionDescriptor)(symbolTable.findDescriptor(name.getValue(), scope))).addToAddress(getSize(type.getValue()));
                semanticStack.push(new Token(TokenType.StringLiteral, type.getValue(), null));

                System.out.println(type.getValue() + "   :   " + value.getValue() + " created in  " + name.getValue());
                break;
            }

            case "@CREATESTRUCT" : {
                Token name = semanticStack.peek();
                symbolTable.addDescriptor(new StructDescriptor("struct", name.getValue(), "struct", scope, address, 0));
                break;
            }

            case "@ADDSTRUCTVALUE" : {
                Token value = semanticStack.pop();
                Token type = semanticStack.pop();
                Token name = semanticStack.peek();
                ((StructDescriptor)(symbolTable.findDescriptor(name.getValue(), scope))).addValue(type.getValue(), value.getValue());
                ((StructDescriptor)(symbolTable.findDescriptor(name.getValue(), scope))).addToAddress(getSize(type.getValue()));
                semanticStack.push(new Token(TokenType.StringLiteral, type.getValue(), null));

                System.out.println(type.getValue() + "   :   " + value.getValue() + " created in  " + name.getValue());
                break;
            }

            case "@PUSHADDRESS" : {
                Descriptor descriptor = symbolTable.findDescriptor(semanticStack.pop().getValue(), scope);
                if (descriptor == null)
                    throw new RuntimeException(descriptor.getName() + " not found at " + lastToken.getPos());
                semanticStack.push(new Token(TokenType.ADRS, String.valueOf(descriptor.getAddress()), null));
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaa" + descriptor.getAddress());
                break;
            }

            case "@PUSHSTRUCTUNIONADDRESS" : {
                Token value = semanticStack.pop();
                Token name = semanticStack.pop();
                Descriptor descriptor = symbolTable.findDescriptor(name.getValue(), scope);
                if (descriptor == null)
                    throw new RuntimeException(descriptor.getName() + " not found at " + lastToken.getPos());
                int adrs = symbolTable.findDescriptor(name.getValue(), scope).getAddress();
                boolean b = false;
                for (Descriptor descriptor1 : symbolTable.getSymbolTable()) {
                    if (descriptor.getName().equals(name.getValue())){
                        if (descriptor.getDescriptorType().equals("struct")){
                            StructDescriptor structDescriptor = ((StructDescriptor)(descriptor));
                            for (Pair<String, String> pair : structDescriptor.getValues()) {
                                if (pair.getValue().equals(value.getValue())){
                                    semanticStack.push(new Token(TokenType.ADRS, String.valueOf(adrs), null));
                                    b = true;
                                    break;
                                } else adrs += getSize(pair.getKey());
                            }
                        } else {
                            UnionDescriptor unionDescriptor = ((UnionDescriptor)(descriptor));
                            for (Pair<String, String> pair : unionDescriptor.getValues()) {
                                if (pair.getValue().equals(value.getValue())){
                                    semanticStack.push(new Token(TokenType.ADRS, String.valueOf(adrs), null));
                                    b = true;
                                    break;
                                } else adrs += getSize(pair.getKey());
                            }
                        }
                    }
                }
                if (!b){
                    throw new RuntimeException(descriptor.getName() + " not found at " + lastToken.getPos());
                }
//                semanticStack.push(new Token(TokenType.ADRS, String.valueOf(descriptor.getAddress()), null));
//                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaa" + descriptor.getAddress());
                break;
            }

            case "@FINDSIMPLEADDRESS" : {
                semanticStack.pop();
                Token token = semanticStack.pop();
                semanticStack.pop();
                semanticStack.push(token);
                break;
            }

            case "@FINDARRAYADDRESS" : {
                ArrayList<Token> arrayList = new ArrayList<>(0);
                while (!semanticStack.peek().getValue().equals("#")){
                    arrayList.add(semanticStack.pop());
                }
                semanticStack.pop();
                Token adrs = semanticStack.pop();
                Token name = semanticStack.pop();
                int res = 0;
                int size = ((ArrayDescriptor) symbolTable.findDescriptor(name.getValue(), scope)).dimentionSizes.size();

                for (int i = 0; i < size; i++) {
                    String temp = getTemp();
                    codeTable[pc] = new CodeLine("MULT", String.valueOf(((ArrayDescriptor) symbolTable.findDescriptor(name.getValue(), scope)).dimentionSizes.get(i)), String.valueOf(arrayList.get(size - 1 - i)), temp);
                    pc++;
                    codeTable[pc] = new CodeLine("ADD", adrs.getValue(), temp, adrs.getValue());
                    pc++;
                }
                semanticStack.push(adrs);
                break;
            }

            case "@ASIGN" : {
                Token asign = semanticStack.pop();
                Token asignTo = semanticStack.pop();

                if (asign.getType() == TokenType.Identifier){
                    if (symbolTable.findDescriptor(asign.getValue(), scope) == null || symbolTable.findDescriptorWithAddress(asignTo.getValue(), scope) == null)
                        throw new RuntimeException("symbol table error at " + lastToken.getPos());

                    if (symbolTable.findDescriptor(asign.getValue(), scope).getType().equals(symbolTable.findDescriptorWithAddress(asignTo.getValue(), scope).getType())) {
                        codeTable[pc] = new CodeLine("MOV", String.valueOf(symbolTable.findDescriptor(asign.getValue(), scope).getAddress()), asignTo.getValue());
                        pc++;
                    } else {
                        throw new RuntimeException("symbol table error at " + lastToken.getPos());
                    }
                } else if (asign.getType() == TokenType.ADRS){
                    if (symbolTable.findDescriptorWithAddress(asignTo.getValue(), scope) == null)
                        System.out.println("ddddddddddddddddddddddddd" + asignTo.getValue());
//                        throw new RuntimeException("symbol table error at " + lastToken.getPos());

                    if (symbolTable.findDescriptorWithAddress(asignTo.getValue(), scope).getType().equals("int") || symbolTable.findDescriptorWithAddress(asignTo.getValue(), scope).getType().equals("bool")) {
                        codeTable[pc] = new CodeLine("MOV", asign.getValue(), asignTo.getValue());
                        pc++;
                    } else {
                        System.out.println(symbolTable.findDescriptorWithAddress(asignTo.getValue(), scope).getType());
                        throw new RuntimeException("symbol table error at " + lastToken.getPos());
                    }
                }
                showCodeTable();
//                System.out.println("weweeeeeeeeeeeeeeeeeewew  " + asign.getType());
                break;
            }

            case "@JMPL0" : {

                semanticStack.push(new Token(TokenType.SpecialToken , "##" , null));
                codeTable[pc] = new CodeLine();
                codeTable[pc].setOpc("JMP");


                pc++;
                break;
            }

            case "@JMPOUT" : {
                codeTable[pc] = new CodeLine();
                codeTable[pc].setOpc("JMP");

                pc++;
                break;
            }

            case "@PUSHDEFAULT":
            {

                semanticStack.push(new Token(TokenType.IntegerNum , "-1" , null));
                break;

            }
            case "@SWITCH" : {
                SwitchNode p;
                int count = 0;

                ArrayList<SwitchNode> linkedlist = new ArrayList<>(0);
                int label , value;
                while(!semanticStack.peek().getValue().equals("##"))
                {
                    label = Integer.parseInt(semanticStack.pop().getValue());
                    value = Integer.parseInt(semanticStack.pop().getValue());
                    p = new SwitchNode();
                    p.setLabel(label);
                    p.setValue(value);
                    linkedlist.add(p);
                    count++;

                }
                int out = pc + count;

                semanticStack.pop();

                Token tetoken = semanticStack.pop();
                String te = tetoken.getValue();
                if(tetoken.getType().equals(TokenType.Identifier))
                {
                    try {
                        te = String.valueOf(symbolTable.findDescriptor(tetoken.getValue(), scope).getAddress());

                    }catch (RuntimeException ex)
                    {
                        throw new NullPointerException("Undefined Variable at " + tetoken.getPos());
                    }
                }
                else
                {
                    te = tetoken.getValue();
                }
                int listpointer = linkedlist.size()-1;


                while(listpointer>=0)
                {

                    p = linkedlist.get(listpointer);

                    listpointer--;

                    if(p.getValue()==-1)
                    {
                        codeTable[pc] = new CodeLine("JMP" , "#"+String.valueOf(p.getLabel()));
                        codeTable[p.getLabel()-1].setOp1("#"+String.valueOf(out));
                        pc++;
                    }
                    else
                    {
                        codeTable[pc] = new CodeLine();
                        codeTable[pc].setOpc("JE");
                        codeTable[pc].setOp1(te);
                        codeTable[pc].setOp2("#" + String.valueOf(p.getValue()));
                        codeTable[pc].setRes("#"+String.valueOf(p.getLabel()));
                        codeTable[p.getLabel()-1].setOp1("#"+String.valueOf(out));
                        pc++;
                    }
                }
                codeTable[pc - count - 1].setOp1("#"+String.valueOf(out));
                codeTable[linkedlist.get(linkedlist.size()-1).getLabel()-1].setOp1("#"+String.valueOf(pc-count));
                break;
            }

            case "@CREATEFUNCDES" : {
                Token name = semanticStack.pop();
                Token type = semanticStack.pop();
                FunctionDescrptor descrptor = new FunctionDescrptor(type.getValue(), name.getValue(), "function", "global", address);
                funcAddress = 4 + getSize(type.getValue());
                symbolTable.addDescriptor(descrptor);
                scope = name.getValue();

                break;
            }

            case "@SETSIMPLEFUNC" : {
//                System.out.println("sfvsvsfv");
                Token id = semanticStack.pop();
                Token type = semanticStack.peek();

                if (symbolTable.findDescriptor(id.getValue(), scope) != null)
                    throw new RuntimeException(id.getValue() + " was declared in this scope");

                symbolTable.addDescriptor(new SimpleDescriptor(type.getValue(), id.getValue(), "simple", scope, funcAddress));
                ((FunctionDescrptor)(symbolTable.findDescriptor(scope, "global"))).getArgumentsAddress().add(funcAddress);
                funcAddress += getSize(type.getValue());
                ((FunctionDescrptor)(symbolTable.findDescriptor(scope, "global"))).getArguments().add(type.getValue());
                for (String global : ((FunctionDescrptor) (symbolTable.findDescriptor(scope, "global"))).getArguments()) {
                    System.out.println(global);
                }

                break;
            }

            case "@SETARRAYFUNC" : {
//                String size = semanticStack.pop().getValue();
                Token id = semanticStack.pop();
                Token type = semanticStack.peek();

                if (symbolTable.findDescriptor(id.getValue(), scope) != null)
                    throw new RuntimeException(id.getValue() + " was declared in this scope");

                symbolTable.addDescriptor(new ArrayDescriptor(type.getValue(), id.getValue(), "array", scope, funcAddress, 1));
                ((FunctionDescrptor)(symbolTable.findDescriptor(scope, "global"))).getArgumentsAddress().add(funcAddress);
                funcAddress += getSize(type.getValue());
                semanticStack.push(id);
                ((FunctionDescrptor)(symbolTable.findDescriptor(scope, "global"))).getArguments().add(type.getValue());

                break;
            }

            case "@SETARRAYSIZEFUNC" : {
                Token size = semanticStack.pop();
                Token name = semanticStack.pop();
                Token type = semanticStack.peek();
                int temp = ((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).getSize();
                ((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).setSize(((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).getSize() * Integer.parseInt(size.getValue()));
                funcAddress += ((((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).getSize() - temp) * getSize(type.getValue()));
                ((ArrayDescriptor)symbolTable.findDescriptor(name.getValue(), scope)).dimentionSizes.add(Integer.parseInt(size.getValue()));
                semanticStack.push(name);
//                System.out.println("address : " + address);
                break;
            }

            case "@SETSTARTOFBODYREGION" : {
                ((FunctionDescrptor)(symbolTable.findDescriptor(scope, "global"))).setStartCodeRegion(pc);

                break;
            }

            case "@RETURN" : {
                String temp = getTemp();
                String spADRS = String.valueOf(symbolTable.findDescriptor("sp" , "global").getAddress());

                codeTable[pc] = new CodeLine("ADD", spADRS, "#0", temp);
                pc++;
                ((FunctionDescrptor)(symbolTable.findDescriptor(scope, "global"))).setEndCodeRegion(pc);
                codeTable[pc] = new CodeLine("JMP", "#"+temp);
                pc++;
                ((FunctionDescrptor)(symbolTable.findDescriptor(scope, "global"))).setARSize(funcAddress);
                for (int i = ((FunctionDescrptor)(symbolTable.findDescriptor(scope, "global"))).getStartCodeRegion(); i < ((FunctionDescrptor) (symbolTable.findDescriptor(scope, "global"))).getEndCodeRegion(); i++) {
                    if (codeTable[i].getOp2() != null)
                        if (codeTable[i].getOp2().equals("#callerARsize"))
                            codeTable[i].setOp2("#" + String.valueOf(funcAddress));
                }
//                System.out.println(funcAddress + "   444444444444444444444444444444");
                scope = "global";
                break;
            }

            case "@CALLSP" :
            {
                funcId = semanticStack.pop().getValue();
                if(symbolTable.findDescriptor(funcId , "global")== null)
                    throw new RuntimeException("Function not found");

                String spADRS = String.valueOf(symbolTable.findDescriptor("sp" , "global").getAddress());

                codeTable[pc]= new CodeLine("ADD" , spADRS , "#callerARsize" , spADRS);
                pc++;

                paramCount = 0;
                break;
            }
            case "@ASNFUNCPARAM" :
            {
                Token toSendToken = semanticStack.pop();


                FunctionDescrptor moveToDesc = (FunctionDescrptor) symbolTable.findDescriptor(funcId , "global");
                String moveToType = moveToDesc.getArguments().get(paramCount);
                String moveToADRS = String.valueOf(moveToDesc.getArgumentsAddress().get(paramCount));

                if(!toSendToken.getType().equals(TokenType.ADRS))
                {
                    if((!toSendToken.getType().equals(TokenType.StringLiteral) && moveToType.equals("string")) ||
                            (!toSendToken.getType().equals(TokenType.Character) && moveToType.equals("char")))

                        throw new RuntimeException("Wrong Function Param");

                    if(toSendToken.getType().equals(TokenType.Identifier))
                    {
                        if(symbolTable.findDescriptor(toSendToken.getValue() , scope)==null)
                        {
                            throw new RuntimeException("Undefined Variable");
                        }
                    }
                }
                String temp = getTemp();
                String spADRS = String.valueOf(symbolTable.findDescriptor("sp" , "global").getAddress());

                codeTable[pc] = new CodeLine("ADD" , spADRS , "#"+moveToADRS , temp);
                pc++;
                codeTable[pc] = new CodeLine("MOV" , toSendToken.getValue() , "@" + temp);
                pc++;
                paramCount++;
                break;
            }

            case "@CALL" : {

                String spADRS = String.valueOf(symbolTable.findDescriptor("sp" , "global").getAddress());
                String temp = getTemp();
                codeTable[pc] = new CodeLine("ADD" , spADRS , "#0" , temp);
                pc++;
                codeTable[pc] = new CodeLine("MOV" , "#"+String.valueOf(pc+2) , "@" + temp);
                pc++;
                FunctionDescrptor moveToDesc = (FunctionDescrptor) symbolTable.findDescriptor(funcId , "global");

                codeTable[pc] = new CodeLine("JMP" ,"#"+String.valueOf(moveToDesc.getStartCodeRegion()));
                pc++;
                codeTable[pc] = new CodeLine("SUB" ,spADRS  , "#callerARsize" , spADRS);
                pc++;
                break;
            }

            case "@ASIGNCALLER" : {
                String temp = getTemp();
                codeTable[pc] = new CodeLine("ADD", String.valueOf(symbolTable.findDescriptor("sp", "global").getAddress()), "#4", temp);
                pc++;
                codeTable[pc] = new CodeLine("MOV", temp, semanticStack.pop().getValue());
                pc++;

                break;
            }

            case "@ASIGNCALL" : {

            }
        }
    }

    static String getTemp() {
        int temp = tempAddress;
        tempAddress += 4;
        return Integer.toString(temp);
    }

    static int getSize(String type) {
        if (type.equals("int"))
            return  4;
        else if (type.equals("float"))
            return  4;
        else if (type.equals("char"))
            return  2;
        else if (type.equals("bool"))
            return  1;
        else if (type.equals("double"))
            return  8;
        else return 4;
    }

    static void showCodeTable(){
        for (int i = 0; i < pc; i++) {
            System.out.println(i + ")  " + codeTable[i].toString());
        }
    }
}
