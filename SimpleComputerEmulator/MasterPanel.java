package SimpleComputerEmulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class MasterPanel {
  private static final int MAX_LINES = 20;

  private static MasterPanel instance = null;
  private RAM ram;
  private CPU cpu;
  private ArrayList<AsmGUI> code;

  private JLabel jacc;
  private JLabel jir;
  private JLabel jpc;

  JPanel panel;

  /********************************************************************/

  private MasterPanel() {}

  /********************************************************************/

  private void syncCPU() {
    instance.jpc.setText(Integer.toString(instance.cpu.getPC()));
    instance.jir.setText(instance.cpu.getIR().toCommand());
    instance.jacc.setText(Integer.toString(instance.cpu.getACC()));
  }

  /********************************************************************/

  private static JPanel genWest() {
    JPanel west = new JPanel(new SpringLayout());

    west.add(new JLabel("PC: "));
    instance.jpc = new JLabel();
    west.add(instance.jpc);

    west.add(new JLabel("IR: "));
    instance.jir = new JLabel();
    west.add(instance.jir);

    west.add(new JLabel("ACC:"));
    instance.jacc = new JLabel();
    west.add(instance.jacc);

    instance.syncCPU();

    SpringUtilities.makeCompactGrid(west, 3, 2, 5, 5, 15, 15);
    west.setPreferredSize(new Dimension(300, 10));

    return west;
  }

  /********************************************************************/

  private static JScrollPane genCenter() {
    JPanel center = new JPanel(new SpringLayout());
    instance.ram = RAM.getInstance();
    center.add(new JLabel("A"));
    center.add(new JLabel("Value"));

    int i = 0;
    for (JLabel jtf : instance.ram.getCells()) {
      center.add(new JLabel(Integer.toString(i)));
      center.add(jtf);
      i += 1;
    }
    SpringUtilities.makeCompactGrid(center, Instruction.MEMORY_CELLS + 1, 2, 5,
                                    5, 15, 15);

    return new JScrollPane(center, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                           JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  }

  /********************************************************************/

  private static JPanel genSouth() {
    JPanel south = new JPanel(new FlowLayout());

    JButton run = new JButton("Run Program");
    run.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          while (instance.cpu.getIR().getOpcode() != Opcode.HALT) {
            MasterPanel.getInstance().cpu.step();
            MasterPanel.getInstance().syncCPU();
          }
        } catch (IndexOutOfBoundsException ie) {
          JOptionPane.showMessageDialog(null, ie.getMessage(), "Exception",
                                        JOptionPane.ERROR_MESSAGE);
          instance.cpu.fetch(new Instruction(Opcode.HALT, false, 0));
          instance.cpu.execute();
          MasterPanel.getInstance().syncCPU();
        }
      }
    });

    JButton next = new JButton("Execute Next Instruction");
    next.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          MasterPanel.getInstance().cpu.step();
        } catch (IndexOutOfBoundsException ie) {
          JOptionPane.showMessageDialog(null, ie.getMessage(), "Exception",
                                        JOptionPane.ERROR_MESSAGE);
          instance.cpu.fetch(new Instruction(Opcode.HALT, false, 0));
          instance.cpu.execute();
        }
        MasterPanel.getInstance().syncCPU();
      }
    });

    JButton reset = new JButton("Reset Computer");
    reset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MasterPanel.getInstance().reset();
      }
    });

    JButton load = new JButton("Load Program into RAM");
    load.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int i = 0;
        for (AsmGUI asm : instance.code) {
          JLabel jtf = instance.ram.getCell(i);
          jtf.setText(asm.toString());
          i++;
        }
      }
    });

    south.add(run);
    south.add(new JLabel("        "));
    south.add(next);
    south.add(new JLabel("        "));
    south.add(reset);
    south.add(new JLabel("        "));
    south.add(load);

    return south;
  }

  /********************************************************************/

  private static JScrollPane genEast() {
    JPanel panel = new JPanel(new SpringLayout());

    instance.code = new ArrayList<AsmGUI>();
    panel.setAutoscrolls(true);

    panel.add(new JLabel("A"));
    panel.add(new JLabel("Opcode"));
    panel.add(new JLabel("Number"));
    panel.add(new JLabel("Operand"));

    for (int i = 0; i < MAX_LINES; i++) {
      AsmGUI ag = new AsmGUI();
      instance.code.add(ag);
      panel.add(new JLabel(Integer.toString(i)));
      panel.add(ag.getJopcode());
      panel.add(ag.getJnum());
      panel.add(ag.getJoperand());
    }

    SpringUtilities.makeCompactGrid(panel, MAX_LINES + 1, 4, 5, 5, 15, 15);

    return new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                           JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  }

  /********************************************************************/

  public static MasterPanel getInstance() {
    if (instance == null) {
      instance = new MasterPanel();
      instance.ram = RAM.getInstance();
      instance.cpu = new CPU();
      instance.panel = new JPanel(new BorderLayout());

      instance.panel.add(instance.genWest(), BorderLayout.WEST);
      instance.panel.add(instance.genCenter(), BorderLayout.CENTER);
      instance.panel.add(instance.genSouth(), BorderLayout.SOUTH);
      instance.panel.add(instance.genEast(), BorderLayout.EAST);
      instance.reset();
    }
    return instance;
  }

  /********************************************************************/

  public void reset() {
    instance.cpu = new CPU();
    instance.syncCPU();
    for (JLabel e : ram.getCells())
      e.setText(Instruction.toBitString(16, 0));
  }

  /********************************************************************/

  public JPanel getPanel() { return panel; }

  /********************************************************************/

  public RAM getRAM() { return ram; }

  /********************************************************************/

  public List<AsmGUI> getCode() { return code; }

  /********************************************************************/

  public boolean objectLoad(FileInputStream fis) {
    try {
      ObjectInputStream ois = new ObjectInputStream(fis);
      Object o = ois.readObject();

      if (o instanceof ArrayList) {
        @SuppressWarnings("unchecked")
        ArrayList<AsmGUI> nc = (ArrayList<AsmGUI>)o;

        for (int i = 0; i < Math.min(code.size(), nc.size()); i++)
          code.get(i).set(nc.get(i));
        return true;
      }

      else
        return false;

    } catch (Exception e) {
      return false;
    }
  }

  /********************************************************************/

  private void clearCode() {
    Instruction is = new Instruction();
    for (int i = 0; i < code.size(); i++)
      code.get(i).set(is);
  }

  /********************************************************************/

  private void textLoad(FileInputStream fis) {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(fis));
      String cmd;
      boolean first = true;
      int i = 0;

      while ((cmd = br.readLine()) != null) {
        if (Instruction.isComment(cmd))
          continue;

        Instruction is = Instruction.fromCommand(cmd);
        if (is == null) {
          JOptionPane.showMessageDialog(null, cmd, "Syntax Error",
                                        JOptionPane.ERROR_MESSAGE);
          System.err.println("Syntax Error: " + cmd);
          return;
        }
        if (first) {
          this.clearCode();
          first = false;
        }
        code.get(i).set(is);
        i += 1;
      }
      br.close();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.toString(), "Syntax Error",
                                    JOptionPane.ERROR_MESSAGE);
      System.err.println(e);
    }
    return;
  }

  /********************************************************************/

  public void load(FileInputStream fis) { textLoad(fis); }

  /********************************************************************/

  public void saveObject(FileOutputStream fos) {
    try {
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(code);
      oos.flush();
      oos.close();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.toString(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  /********************************************************************/

  public void save(FileOutputStream fos) {
    PrintWriter pw = new PrintWriter(fos);
    for (AsmGUI ag : code) {
      pw.println(ag.getInstruction().toCommand());
    }
    pw.flush();
    pw.close();
  }

  /********************************************************************/
}
