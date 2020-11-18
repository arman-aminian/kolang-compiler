package Parser;

import javafx.util.Pair;

import java.util.ArrayList;

public class SymbolTable {
    private ArrayList<Descriptor> symbolTable = new ArrayList<>(0);

    void addDescriptor(Descriptor descriptor){
        symbolTable.add(descriptor);

        System.out.println("    *****  " + descriptor.getType() + "  :  " + descriptor.getName());
    }

    Descriptor findDescriptor (String name, String scope){
        for (Descriptor descriptor : symbolTable) {
            if (descriptor.getName().equals(name) && descriptor.getScope().equals(scope))
                return descriptor;
        }
        return null;
    }

    Descriptor findDescriptorWithAddress (String adrs, String scope){
        for (Descriptor descriptor : symbolTable) {
            if (String.valueOf(descriptor.getAddress()).equals(adrs) && descriptor.getScope().equals(scope))
                return descriptor;
        }
        return null;
    }

    public ArrayList<Descriptor> getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(ArrayList<Descriptor> symbolTable) {
        this.symbolTable = symbolTable;
    }
}

class Descriptor {
    private String type;
    private String name;
    private String descriptorType;
    private String scope;
    private int address;

    public Descriptor(String type, String name, String descriptorType, String scope, int address) {
        this.type = type;
        this.name = name;
        this.descriptorType = descriptorType;
        this.scope = scope;
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptorType() {
        return descriptorType;
    }

    public void setDescriptorType(String descriptorType) {
        this.descriptorType = descriptorType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name + "  ##:##  " + type + "  " + scope + "  ==  " + descriptorType;
    }
}

class SimpleDescriptor extends Descriptor {

    public SimpleDescriptor(String type, String name, String descriptorType, String scope, int address) {
        super(type, name, descriptorType, scope, address);
    }
}

class ArrayDescriptor extends Descriptor {
    private int size;
   public ArrayList<Integer> dimentionSizes = new ArrayList<>(0);



    public ArrayDescriptor(String type, String name, String descriptorType, String scope, int address, int size) {
        super(type, name, descriptorType, scope, address);
        this.size = size;
    }

    public ArrayDescriptor(String type, String name, String descriptorType, String scope, int address) {
        super(type, name, descriptorType, scope, address);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

class FunctionDescrptor extends Descriptor {
    private int startCodeRegion;
    private int endCodeRegion;
    private int ARSize;
    private int startOfLocalAddress;
    private ArrayList<String> arguments = new ArrayList<>(0);
    private ArrayList<Integer> argumentsAddress = new ArrayList<>(0);

    public FunctionDescrptor(String type, String name, String descriptorType, String scope, int address) {
        super(type, name, descriptorType, scope, address);
    }

    public int getStartCodeRegion() {
        return startCodeRegion;
    }

    public void setStartCodeRegion(int startCodeRegion) {
        this.startCodeRegion = startCodeRegion;
    }

    public int getEndCodeRegion() {
        return endCodeRegion;
    }

    public void setEndCodeRegion(int endCodeRegion) {
        this.endCodeRegion = endCodeRegion;
    }

    public int getARSize() {
        return ARSize;
    }

    public void setARSize(int ARSize) {
        this.ARSize = ARSize;
    }

    public int getStartOfLocalAddress() {
        return startOfLocalAddress;
    }

    public void setStartOfLocalAddress(int startOfLocalAddress) {
        this.startOfLocalAddress = startOfLocalAddress;
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    public ArrayList<Integer> getArgumentsAddress() {
        return argumentsAddress;
    }

    public void setArgumentsAddress(ArrayList<Integer> argumentsAddress) {
        this.argumentsAddress = argumentsAddress;
    }
}

class UnionDescriptor extends Descriptor {
    private ArrayList<Pair<String, String>> values = new ArrayList<>(0);
    private int sizeAddress;

    public ArrayList<Pair<String, String>> getValues() {
        return values;
    }

    public void setValues(ArrayList<Pair<String, String>> values) {
        this.values = values;
    }

    void addValue(String type, String name){
        values.add(new Pair<>(type, name));
    }

    void addToAddress (int x){
        sizeAddress += x;
    }

    public UnionDescriptor(String type, String name, String descriptorType, String scope, int address, int sizeAddress) {
        super(type, name, descriptorType, scope, address);
        this.sizeAddress = sizeAddress;
    }
}

class StructDescriptor extends Descriptor {
    private ArrayList<Pair<String, String>> values = new ArrayList<>(0);
    private int sizeAddress;

    void addValue(String type, String name){
        values.add(new Pair<>(type, name));
    }

    public ArrayList<Pair<String, String>> getValues() {
        return values;
    }

    public void setValues(ArrayList<Pair<String, String>> values) {
        this.values = values;
    }

    void addToAddress (int x){
        sizeAddress += x;
    }

    public StructDescriptor(String type, String name, String descriptorType, String scope, int address, int sizeAddress) {
        super(type, name, descriptorType, scope, address);
        this.sizeAddress = sizeAddress;
    }
}