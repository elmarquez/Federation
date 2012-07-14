/**
 * MutableSceneModelPopupProvider.java
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

import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.action.GraphViewerAddAnnotationAction;
import ca.sfu.federation.action.PropertySheetSetFocusAction;
import ca.sfu.federation.action.ScenarioNewInstanceAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 * Popup menu provider.
 * @author Davis Marques
 * @version 0.1.0
 */
public class MutableSceneModelPopupProvider implements PopupMenuProvider, ActionListener {
    
    //----------------------------------------------------------------------

    
    private MutableSceneModel scene;
    private JPopupMenu menu;
    
    //----------------------------------------------------------------------

    
    /**
     * Popup menu provider.
     * @param MyScene
     */
    public MutableSceneModelPopupProvider(MutableSceneModel MyScene) {
        // init
        this.scene = MyScene;
        // create menu
        menu = new JPopupMenu("Model Object Actions");
        // menu item - add Scenario
        ScenarioNewInstanceAction snia = new ScenarioNewInstanceAction("New Scenario",null,"New Scenario",new Integer(KeyEvent.VK_N));
        menu.add(snia);
        // menu item - add Annotation
        GraphViewerAddAnnotationAction vaaa = new GraphViewerAddAnnotationAction("New Annotation",null,"New Annotation",new Integer(KeyEvent.VK_A),MyScene);
        menu.add(vaaa);
        // menu item - separator
        menu.add(new JSeparator());
        // menu item - properties
        PropertySheetSetFocusAction pssfa = new PropertySheetSetFocusAction("Properties",null,"Properties",new Integer(KeyEvent.VK_P),(INamed)ParametricModel.getInstance());
        menu.add(pssfa);
    }
    
    //----------------------------------------------------------------------

    
    public JPopupMenu getPopupMenu(Widget widget) {
        return menu;
    }
    
    public void actionPerformed(ActionEvent e) {
        this.scene.setActiveTool(e.getActionCommand());
    }
    
} // end class
