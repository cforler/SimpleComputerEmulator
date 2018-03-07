package SimpleComputerEmulator;

public enum Opcode {
    NOOP("000",0), LOAD("001",1), STORE("010",2), ADD("011",3),
    SUB("100",4), EQUAL("101",5), JUMP("110",6), HALT("111", 7);


    private String bitString;
    private int value;

    Opcode(String bitString, int value) {
        this.bitString = bitString;
        this.value = value;
    }

    public String getBitString() { return bitString; }
    public int value() { return value; }
    
};
  


