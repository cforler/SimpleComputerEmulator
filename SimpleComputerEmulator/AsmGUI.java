package SimpleComputerEmulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.lang.StringBuffer;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AsmGUI implements Serializable {
  private Instruction ins;

  private JComboBox<Opcode> jopcode;
  private JComboBox<Boolean> jnum;
  private JSpinner joperand;

  AsmGUI() {
    ins = new Instruction();
    jopcode = new JComboBox<Opcode>(Opcode.values());
    jopcode.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ins.setOpcode((Opcode)jopcode.getSelectedItem());
      }
    });

    jnum = new JComboBox<Boolean>();
    jnum.addItem(Boolean.TRUE);
    jnum.addItem(Boolean.FALSE);
    jnum.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ins.setNumber((Boolean)jnum.getSelectedItem());
      }
    });

    SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 65535, 1);
    joperand = new JSpinner(model);
    joperand.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        ins.setOperand((Integer)joperand.getValue());
      }
    });
  }

  /************************************************************************/

  public JComboBox<Opcode> getJopcode() { return jopcode; }

  public JComboBox<Boolean> getJnum() { return jnum; }

  public JSpinner getJoperand() { return joperand; }

  /************************************************************************/

  public String toString() {
    String n = ins.getNumber() ? "1" : "0";
    return Instruction.RESERVED + ins.getOpcode().getBitString() + n +
        Instruction.toBitString(6, ins.getOperand());
  }

  /**********************************************************************/

  public void set(Instruction ins) {
    jopcode.setSelectedItem(ins.getOpcode());
    jnum.setSelectedItem(ins.getNumber());
    joperand.setValue(ins.getOperand());
  }

  /**********************************************************************/

  public Instruction getInstruction() { return ins; }

  /**********************************************************************/

  public void set(AsmGUI ag) { set(ag.getInstruction()); }

  /**********************************************************************/
}
