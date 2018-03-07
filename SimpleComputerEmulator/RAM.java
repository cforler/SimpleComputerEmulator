package SimpleComputerEmulator;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

public class RAM {
    private List<JLabel>  cells;

    private static RAM instance = null;
    
    public RAM() {}

    /**********************************************************************/

    public static RAM getInstance() {
        if(instance == null) {
            instance = new RAM();
            instance.cells = new ArrayList<JLabel>(Instruction.MEMORY_CELLS);
            for(int i=0; i< Instruction.MEMORY_CELLS; i++) {
                JLabel jtf = new JLabel();
                jtf.setText( Instruction.toBitString(16,0) );
                //  jtf.setEnabled(false);
                instance.cells.add(jtf);
            }
        }
        return instance;
    }

    /**********************************************************************/
        
        public List<JLabel> getCells() { return cells; }
    
    /**********************************************************************/

    public JLabel getCell(int index) {
        return cells.get(index);
    }
}
