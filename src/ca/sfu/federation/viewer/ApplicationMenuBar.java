/**
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package ca.sfu.federation.viewer;

import ca.sfu.federation.action.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.JPopupMenu.Separator;

/**
 * Application menu bar.
 * @author dmarques
 */
public class ApplicationMenuBar extends JMenuBar {

    /**
     * ApplicationMenuBar constructor
     */
    public ApplicationMenuBar() {
        init();
    }

    /**
     * Get the help menu
     * @return 
     */
    private JMenu getAboutMenu() {
        JMenu menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menu.add(new JSeparator());
        ApplicationAboutAction aaa = new ApplicationAboutAction("About this Application",null,"About this Application",KeyEvent.VK_A);
        menu.add(aaa);
        // return result
        return menu;
    }
    
    /**
     * Get the edit menu
     * @return 
     */
    private JMenu getEditMenu() {
        JMenu menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        
        UndoAction undo = new UndoAction();
        RedoAction redo = new RedoAction();
        CutAction cut = new CutAction();
        CopyAction copy = new CopyAction();
        PasteAction paste = new PasteAction();
        DeleteINamedAction delete = new DeleteINamedAction();

        JMenuItem selectall = new JMenuItem("Select All", KeyEvent.VK_A);
        JMenu selectby = new JMenu("Select");
        selectby.setMnemonic(KeyEvent.VK_S);
        JMenuItem type = new JMenuItem("Type");
        JMenuItem context = new JMenuItem("Contextual");
        JMenuItem trans = new JMenuItem("Transactional");
        JMenuItem property = new JMenuItem("Property");
        JMenuItem find = new JMenuItem("Find", KeyEvent.VK_F);

        undo.setEnabled(false);
        
        menu.add(undo);
        menu.add(redo);
        menu.add(new Separator());
        menu.add(cut);
        menu.add(copy);
        menu.add(paste);
        menu.add(delete);
        menu.add(new Separator());
        menu.add(selectall);
        menu.add(selectby);
        selectby.add(type);
        selectby.add(property);
        selectby.add(context);
        selectby.add(trans);
        menu.add(new Separator());
        menu.add(find);
        // return result
        return menu;
    }
    
    /**
     * Get the file menu.
     * @return 
     */
    private JMenu getFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        // menu items
        NewProjectAction np = new NewProjectAction("New Project",null,"New Project",KeyEvent.VK_N);
        OpenProjectAction op = new OpenProjectAction("Open Project",null,"Open Project",KeyEvent.VK_O);
        SaveProjectAction sp = new SaveProjectAction("Save Project",null,"Save Project",KeyEvent.VK_S);
        ApplicationExitAction ax = new ApplicationExitAction("Exit",null,"Exit",KeyEvent.VK_X);
        menu.add(np);
        menu.add(op);
        menu.add(sp);        
        menu.add(new JSeparator());
        menu.add(ax);        
        // return result
        return menu;
    }

    /**
     * Get the modeling menu
     * @return 
     */
    private JMenu getModelMenu() {
        JMenu menu = new JMenu("Model");
        menu.setMnemonic(KeyEvent.VK_M);
        
        JMenuItem newassembly = new JMenuItem("Assembly");
        newassembly.setMnemonic(KeyEvent.VK_A);
        menu.add(newassembly);

        Separator s1 = new Separator();
        menu.add(s1);
        
        JMenuItem newpoint = new JMenuItem("Point");
        newpoint.setMnemonic(KeyEvent.VK_A);
        menu.add(newpoint);

        JMenuItem newline = new JMenuItem("Line");
        newline.setMnemonic(KeyEvent.VK_L);
        menu.add(newline);

        JMenuItem newrect = new JMenuItem("Rectangle");
        newrect.setMnemonic(KeyEvent.VK_R);
        menu.add(newrect);

        JMenuItem newellipse = new JMenuItem("Ellipse");
        newellipse.setMnemonic(KeyEvent.VK_E);
        menu.add(newellipse);
        
        Separator s2 = new Separator();
        menu.add(s2);

        JMenuItem newbehavior = new JMenuItem("Behavior");
        newbehavior.setMnemonic(KeyEvent.VK_E);
        menu.add(newbehavior);

        return menu;
    }

    private JMenu getScenarioMenu() {
        JMenu menu = new JMenu("Scenario");
        menu.setMnemonic(KeyEvent.VK_S);

        JMenuItem selectcontextual = new JMenuItem("Select Contextual");
        selectcontextual.setMnemonic(KeyEvent.VK_C);
        menu.add(selectcontextual);
        
        JMenuItem selecttrans = new JMenuItem("Select Transactional");
        selecttrans.setMnemonic(KeyEvent.VK_T);
        menu.add(selecttrans);

        return menu;
    }
    
    /**
     * Get the view menu
     * @return 
     */
    private JMenu getViewMenu() {
        JMenu menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);

        IContextGraphViewerNewInstance sv = new IContextGraphViewerNewInstance("Scenario View",null,"Graph View",new Integer(KeyEvent.VK_G));
        IContextGraphViewerNewInstance gv = new IContextGraphViewerNewInstance("Graph View",null,"Graph View",new Integer(KeyEvent.VK_G));
        IContextModelViewerNewInstance mv = new IContextModelViewerNewInstance("Model View",null,"Model View",new Integer(KeyEvent.VK_M));
        IContextStackViewerNewInstance stv = new IContextStackViewerNewInstance("Stack View",null,"Stack View",new Integer(KeyEvent.VK_S));
        AbstractTreeExplorerNewInstanceAction tv = new AbstractTreeExplorerNewInstanceAction("Tree View",null,"Tree View",new Integer(KeyEvent.VK_A));
        JMenu toolbars = new JMenu("Toolbars");
        JMenuItem projecttoolbar = new JMenuItem("Project");
        JMenuItem modeltoolbar = new JMenuItem("Model");

        menu.add(sv);
        menu.add(gv);
        menu.add(mv);
        menu.add(stv);
        menu.add(tv);
        menu.add(new Separator());
        menu.add(toolbars);
        toolbars.add(projecttoolbar);
        toolbars.add(modeltoolbar);
        
        // return result
        return menu;
    }

    /**
     * Initialize the menu bar.
     */
    private void init() {
        setOpaque(true);
        add(getFileMenu());
        add(getEditMenu());
        add(getViewMenu());
        add(getScenarioMenu());
        add(getModelMenu());
        add(getAboutMenu());
    }
    
} // end class
