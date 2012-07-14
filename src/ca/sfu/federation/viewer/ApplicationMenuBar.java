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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JSeparator;

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
     * Get the about menu
     * @return 
     */
    private JMenu getAboutMenu() {
        JMenu menu = new JMenu("About");
        menu.setMnemonic(KeyEvent.VK_A);
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
        
        JMenuItem undo = new JMenuItem("Undo", KeyEvent.VK_U);
        JMenuItem redo = new JMenuItem("Redo", KeyEvent.VK_R);
        Separator s1 = new Separator();
        JMenuItem cut = new JMenuItem("Cut", KeyEvent.VK_T);
        JMenuItem copy = new JMenuItem("Copy", KeyEvent.VK_Y);
        JMenuItem paste = new JMenuItem("Paste", KeyEvent.VK_P);
        JMenuItem delete = new JMenuItem("Delete", KeyEvent.VK_D);
        Separator s2 = new Separator();
        JMenuItem selectall = new JMenuItem("Select All", KeyEvent.VK_A);
        JMenuItem selectby = new JMenuItem("Select By", KeyEvent.VK_B);
        Separator s3 = new Separator();
        JMenuItem find = new JMenuItem("Find", KeyEvent.VK_F);

        menu.add(undo);
        menu.add(redo);
        menu.add(s1);
        menu.add(cut);
        menu.add(copy);
        menu.add(paste);
        menu.add(delete);
        menu.add(s2);
        menu.add(selectall);
        menu.add(selectby);
        menu.add(s3);
        menu.add(find);
        // return result
        return menu;
    }
    
    /**
     * Get the file menu
     * @return 
     */
    private JMenu getFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        NewProjectAction pmnp = new NewProjectAction("New Project",null,"New Project",KeyEvent.VK_N);
        menu.add(pmnp);
        OpenProjectAction pmoa = new OpenProjectAction("Open Project",null,"Open Project",KeyEvent.VK_O);
        menu.add(pmoa);
        menu.add(new JSeparator());
        OpenProjectAction pmsa = new OpenProjectAction("Save Project",null,"Save Project",KeyEvent.VK_S);
        menu.add(pmsa);
        SaveProjectAsAction pmsaa = new SaveProjectAsAction("Save Project As",null,"Save Project As",KeyEvent.VK_A);
        menu.add(pmsaa);
        CloseProjectAction pmca = new CloseProjectAction("Close Project",null,"Close Project",KeyEvent.VK_C);
        menu.add(pmca);
        menu.add(new JSeparator());
        ApplicationExitAction aea = new ApplicationExitAction("Exit",null,"Exit",KeyEvent.VK_X);
        menu.add(aea);
        // return result
        return menu;
    }

    /**
     * Get the view menu
     * @return 
     */
    private JMenu getViewMenu() {
        JMenu menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        // ApplicationSetLayoutAsParametricModelGraph vslapmg = new ApplicationSetLayoutAsParametricModelGraph("Parametric Model Graph",null,"Parametric Model Graph",new Integer(KeyEvent.VK_P));
        // menu.add(vslapmg);
        IContextModelViewerNewInstance icmvni = new IContextModelViewerNewInstance("IContext Model Viewer",null,"IContext Model Viewer",new Integer(KeyEvent.VK_M));
        menu.add(icmvni);
        IContextGraphViewerNewInstance icgvni = new IContextGraphViewerNewInstance("IContext Graph View",null,"IContext Graph View",new Integer(KeyEvent.VK_G));
        menu.add(icgvni);
        IContextStackViewerNewInstance icsvni = new IContextStackViewerNewInstance("IContext Stack View",null,"IContext Stack View",new Integer(KeyEvent.VK_S));
        menu.add(icsvni);
        AbstractTreeExplorerNewInstanceAction evni = new AbstractTreeExplorerNewInstanceAction("Abstract Tree Explorer View",null,"Abstract Tree Explorer View",new Integer(KeyEvent.VK_A));
        menu.add(evni);
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
        add(getAboutMenu());
    }
    
} // end class
