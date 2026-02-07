package SimpleComputerEmulator;

public class Test {
  public static void main(String[] args) {
    Instruction is1 = Instruction.fromCommand("NOOP");
    Instruction is2 = Instruction.fromCommand("HALT");
    Instruction is3 = Instruction.fromCommand("ADD #12");
    Instruction is4 = Instruction.fromCommand("SUB 100");

    System.out.println(is1.toCommand());
    System.out.println(is2.toCommand());
    System.out.println(is3.toCommand());
    System.out.println(is4.toCommand());
  }
}
