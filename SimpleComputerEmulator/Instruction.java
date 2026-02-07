package SimpleComputerEmulator;

import SimpleComputerEmulator.Opcode;
import java.io.Serializable;
import java.lang.NumberFormatException;
import java.lang.StringBuffer;

public class Instruction implements Serializable {
  public static final int OPERAND_SIZE = 6;
  public static final int RESERVED_BITS = 6;
  public static final String RESERVED = "000000";
  public static final int MEMORY_CELLS = 1 << (Instruction.OPERAND_SIZE);

  private Opcode op;
  private boolean num;
  private int operand;

  /*********************************************************************/

  public Instruction() {
    op = Opcode.NOOP;
    num = true;
    operand = 0;
  }

  /********************************************************************/

  public Instruction(Opcode op, boolean num, int operand) {
    this.op = op;
    this.num = num;
    this.operand = operand % MEMORY_CELLS;
  }

  /*********************************************************************/

  public Opcode getOpcode() { return op; }
  public boolean getNumber() { return num; }
  public int getOperand() { return operand; }

  /*********************************************************************/

  public void setOpcode(Opcode opcode) { op = opcode; }
  public void setNumber(boolean number) { num = number; }
  public void setNumber(Boolean number) { num = number; }
  public void setOperand(int operand) { this.operand = operand; }

  /*********************************************************************/

  public String toCommand() {
    if (op == Opcode.NOOP || op == Opcode.HALT)
      return op.toString();
    String s = num ? "#" : "";
    return op + " " + s + operand;
  }

  /*********************************************************************/

  public String toString() {
    String n = num ? "1" : "0";
    return Instruction.RESERVED + op.getBitString() + n +
        Instruction.toBitString(OPERAND_SIZE, operand);
  }

  /****************************************************************/

  public static String toBitString(int len, int value) {
    String s = Integer.toBinaryString(value);

    if (s.length() >= len)
      return s.substring(s.length() - len, s.length());

    StringBuffer zeros = new StringBuffer("");
    for (int i = s.length(); i < len; i++)
      zeros.append("0");
    return zeros + s;
  }

  /**************************************************************/

  public static Instruction fromBitString(String bitString) {
    Instruction in = new Instruction();

    if (bitString.length() != 16)
      return null;

    if (!bitString.startsWith(Instruction.RESERVED))
      return null;

    try {
      in.operand = Integer.parseInt(bitString.substring(10, 16), 2);
      int idx = Integer.parseInt(bitString.substring(6, 9), 2);
      in.op = Opcode.values()[idx];
    } catch (NumberFormatException nfe) {
      return null;
    }

    if (bitString.charAt(9) == '0')
      in.num = false;
    else if (bitString.charAt(9) == '1')
      in.num = true;
    else
      return null;

    return in;
  }

  /**************************************************************/

  public static boolean isComment(String cmd) {
    String s = cmd.trim().replaceAll("\\s+", " "); // Remove unneeded
                                                   // whitespaces
    return "".equals(s) || s.charAt(0) == '#' || s.charAt(0) == ';';
  }

  /**************************************************************/

  private static String normalizeCommand(String cmd) {
    if (cmd == null)
      return null;
    String s = cmd.trim().replaceAll("\\s+", " "); // Remove unneeded
                                                   // whitespaces
    if (Instruction.isComment(cmd))
      return null;
    return s;
  }

  /**************************************************************/

  public static Instruction fromCommand(String cmd) {
    cmd = Instruction.normalizeCommand(cmd);
    if (cmd == null)
      return null;

    String tmp[] = cmd.split(" ");
    if (tmp.length == 1) {
      Opcode op = Opcode.valueOf(tmp[0]);
      if (op == Opcode.NOOP)
        return new Instruction();
      if (op == Opcode.HALT)
        return new Instruction(Opcode.HALT, true, 0);
      return null;
    }

    if (tmp.length == 2) {
      Opcode op = Opcode.valueOf(tmp[0]);
      boolean num = tmp[1].charAt(0) == '#';

      int operand;
      if (num)
        operand = Integer.parseInt(tmp[1].substring(1, tmp[1].length()), 10);
      else
        operand = Integer.parseInt(tmp[1], 10);

      return new Instruction(op, num, operand);
    }
    return null;
  }

  /**************************************************************/
}
