package SimpleComputerEmulator;

public class CPU {
    public static final int BITS = 16;
    public static final int MAX  = 1<<(CPU.BITS-1); 
    

    private boolean running;
    private int pc;
    private Instruction ir;
    private int acc;
    
    public CPU() { reset(); }
    
    public void reset() {
        running = true;
        pc=0;
        ir= new Instruction();
        acc=0;
    }

    /************************************************************/
    
    public int getPC() { return pc; }
    public int getACC() { return acc; }
    public Instruction getIR() { return ir; }

    /************************************************************/
    
    public void setPC(int value) {
        if(value < Instruction.MEMORY_CELLS) pc = value;
    }
    
    public void setACC(int value) {
        if(value < CPU.MAX) acc = value;
    }
    
    public void setIR(Instruction value) {
        ir  = value;
    }


    /************************************************************/

    public void fetch(Instruction i) {
        if(!running) return;
        ir =  i;
        pc += 1;
    }

    /************************************************************/

    public void fetch() {
        fetch(Instruction.fromBitString(RAM.getInstance().getCell(pc).getText()));
    }        
    
    /************************************************************/

    public void step() {
        fetch();
        execute();
    }

    /************************************************************/
    
    public void execute() {
        RAM r = RAM.getInstance();
        int operand = ir.getOperand();
        
        switch(ir.getOpcode()) {
        case NOOP : return;

        case LOAD :
            if(ir.getNumber()) acc = operand;
            else   acc = Integer.parseInt(r.getCell(operand).getText());
            return;
            
        case STORE:
            if( ! ir.getNumber() ) r.getCell(operand).setText(Integer.toString(acc));
            return;
            
        case ADD:
            if(ir.getNumber()) acc += operand;
            else               acc += Integer.parseInt(r.getCell(operand).getText());
            return;

        case SUB:
            if(ir.getNumber()) acc -= operand;
            else               acc -= Integer.parseInt(r.getCell(operand).getText());
            return;

        case EQUAL:
            int x;
            if(ir.getNumber()) x= operand;
            else x= Integer.parseInt(r.getCell(operand).getText());
            if(acc == x) pc +=1;
            return;
  
        case JUMP:
            if(ir.getNumber()) pc = operand;
            else pc = Integer.parseInt(r.getCell(operand).getText());
            return;


        case HALT: running = false;
            return;
        }
    }
}
