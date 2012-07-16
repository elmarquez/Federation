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

package ca.sfu.federation.viewer.graphviewer;

import ca.sfu.federation.Application;
import ca.sfu.federation.action.AddGraphViewerAnnotationAction;
import ca.sfu.federation.action.CreateScenarioAction;
import ca.sfu.federation.action.SetPropertySheetFocusAction;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.ParametricModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 * Popup menu provider.
 * @author Davis Marques
 */
public class MutableSceneModelPopupProvider implements PopupMenuProvider, ActionListener {

    private static final Logger logger = Logger.getLogger(MutableSceneModelPopupProvider.class.getName());
    
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
        CreateScenarioAction snia = new CreateScenarioAction("New Scenario",null,"New Scenario",new Integer(KeyEvent.VK_N));
        menu.add(snia);
        // menu item - add Annotation
        AddGraphViewerAnnotationAction vaaa = new AddGraphViewerAnnotationAction("New Annotation",null,"New Annotation",new Integer(KeyEvent.VK_A),MyScene);
        menu.add(vaaa);
        // menu item - separator
        menu.add(new JSeparator());
        // menu item - properties
        SetPropertySheetFocusAction pssfa = new SetPropertySheetFocusAction("Properties",null,"Properties",new Integer(KeyEvent.VK_P),
                (INamed)Application.getContext().getModel());
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
