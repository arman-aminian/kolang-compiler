package Parser;

public class SwitchNode {
    private int label;
    private int value;


    public SwitchNode (int label , int value)
    {
        this.label = label;
        this.value = value;
    }

    public SwitchNode (){}

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
