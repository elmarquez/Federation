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

import ca.sfu.federation.action.EditDeleteAction;
import ca.sfu.federation.action.RenameINamedAction;
import ca.sfu.federation.action.SetCurrentIContextAction;
import ca.sfu.federation.action.SetPropertySheetFocusAction;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.INamed;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 * Popup menu provider.
 */
public class ContainerVisualWidgetPopupProvider implements PopupMenuProvider, ActionListener {

    private static final Logger logger = Logger.getLogger(ContainerVisualWidgetPopupProvider.class.getName());
    
    private Scene scene;
    private ContainerVisualWidget widget;
    private INamed target;
    
    //----------------------------------------------------------------------
    
    /**
     * Popup menu provider.
     * @param MyScene
     */
    public ContainerVisualWidgetPopupProvider(Scene MyScene, ContainerVisualWidget MyWidget) {
        this.scene = MyScene;
        this.widget = MyWidget;
        this.target = this.widget.getTarget();
    }
    
    //----------------------------------------------------------------------
    
    public JPopupMenu getPopupMenu(Widget widget) {
        // create menu
        JPopupMenu menu = new JPopupMenu("Model Object Actions");
        // open scenario
        if (this.target instanceof IContext) {
            IContext context = (IContext) this.target;
            SetCurrentIContextAction setfocus = new SetCurrentIContextAction("Set as Current Context",null,"Set as Current Context",new Integer(KeyEvent.VK_S),context);
            menu.add(setfocus);
        } else {
            SetCurrentIContextAction setfocus = new SetCurrentIContextAction("Set as Current Context",null,"Set as Current Context",new Integer(KeyEvent.VK_S),null);
            setfocus.setEnabled(false);
            menu.add(setfocus);
        }
        // rename object
        RenameINamedAction rename = new RenameINamedAction("Rename Object",null,"Rename Object",new Integer(KeyEvent.VK_R),target);
        menu.add(rename);
        // delete object
        EditDeleteAction delete = new EditDeleteAction("Delete Object",null,"Delete Object",new Integer(KeyEvent.VK_D),target);
        menu.add(delete);
        // separator
        menu.add(new JSeparator());        
        // properties
        SetPropertySheetFocusAction pssfa = new SetPropertySheetFocusAction("Properties",null,"Properties",new Integer(KeyEvent.VK_P),target);
        menu.add(pssfa);
        return menu;
    }
    
    public void actionPerformed(ActionEvent e) {
        this.scene.setActiveTool(e.getActionCommand());
    }
    
} 