/**
 * SceneModelPopupProvider.java
 * * Copyright (c) 2006 Davis M. Marques <dmarques@sfu.ca>
 *
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

package ca.sfu.federation.viewer.graphviewer;

import ca.sfu.federation.model.Assembly;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.Scenario;
import ca.sfu.federation.model.geometry.CoordinateSystem;
import ca.sfu.federation.model.geometry.Line;
import ca.sfu.federation.model.geometry.Plane;
import ca.sfu.federation.model.geometry.Point;
import ca.sfu.federation.viewer.action.AssemblyNewInstanceAction;
import ca.sfu.federation.viewer.action.ComponentNewInstanceAction;
import ca.sfu.federation.viewer.action.ScenarioNewInstanceAction;
import ca.sfu.federation.viewer.action.GraphViewerAddAnnotationAction;
import ca.sfu.federation.viewer.action.IContextSetCurrentAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 * Popup menu provider.
 * @author Davis Marques
 * @version 0.1.0
 */
public class StaticSceneModelPopupProvider implements PopupMenuProvider, ActionListener {
    
    //----------------------------------------------------------------------
    // FIELDS
    
    private Scene scene;
    private IContext context;
    
    private JPopupMenu menu;
    private java.awt.Point lastMouseRelease;
    
    //----------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * Popup menu provider.
     * @param MyScene
     */
    public StaticSceneModelPopupProvider(Scene MyScene, IContext MyContext) {
        this.scene = MyScene;
        this.context = MyContext;
    }
    
    //----------------------------------------------------------------------
    // METHODS
    
    public JPopupMenu getPopupMenu(Widget widget) {
        JPopupMenu menu = new JPopupMenu();
        // create menu
        menu = new JPopupMenu("Model Object Actions");
        JMenuItem item = null;
        JMenu submenu = null;
        // add menu
        submenu = new JMenu("Add");
        submenu.setMnemonic(new Integer(KeyEvent.VK_A));
        
        if (this.context instanceof ParametricModel) {
            // add Annotation
            GraphViewerAddAnnotationAction vaaa = new GraphViewerAddAnnotationAction("Annotation",null,"Annotation",new Integer(KeyEvent.VK_A),this.scene);
            submenu.add(vaaa);
        }
        if (this.context instanceof Scenario || this.context instanceof Assembly) {
            // add Assembly
            AssemblyNewInstanceAction ania = new AssemblyNewInstanceAction("Assembly",null,"Assembly",null,this.context);
            submenu.add(ania);
        }
        if (this.context instanceof Assembly) {
            // add Behavior
            item = new JMenuItem("Behavior");
            submenu.add(item);
        }
        if (this.context instanceof Scenario || this.context instanceof Assembly) {
            // add Component
            JMenu subsubmenu = new JMenu("Component");
            submenu.setMnemonic(new Integer(KeyEvent.VK_C));
            ComponentNewInstanceAction cnia = null;
            cnia = new ComponentNewInstanceAction("CoordinateSystem",null,"CoordinateSystem",null,this.context,CoordinateSystem.class);
            subsubmenu.add(cnia);
            cnia = new ComponentNewInstanceAction("Point",null,"Point",null,this.context,Point.class);
            subsubmenu.add(cnia);
            cnia = new ComponentNewInstanceAction("Line",null,"Line",null,this.context,Line.class);
            subsubmenu.add(cnia);
            cnia = new ComponentNewInstanceAction("Plane",null,"Plane",null,this.context,Plane.class);
            subsubmenu.add(cnia);
            submenu.add(subsubmenu);
        }
        if (this.context instanceof ParametricModel) {
            // add Scenario
            ScenarioNewInstanceAction snia = new ScenarioNewInstanceAction("Scenario",null,"Scenario",new Integer(KeyEvent.VK_S));
            submenu.add(snia);
        }
        menu.add(submenu);
        // separator
        menu.add(new JSeparator());
        // set view
        submenu = new JMenu("Set Current Context");
        Vector parents = (Vector) this.context.getParents();
        Enumeration e = parents.elements();
        while (e.hasMoreElements()) {
            IContext c = (IContext) e.nextElement();
            String name = c.getCanonicalName();
            IContextSetCurrentAction vscfa = new IContextSetCurrentAction(name,null,name,null,c);
            submenu.add(vscfa);
        }
        menu.add(submenu);
        // return the menu
        return menu;
    }
    
    public void actionPerformed(ActionEvent e) {
        this.scene.setActiveTool(e.getActionCommand());
    }
    
} // end class
