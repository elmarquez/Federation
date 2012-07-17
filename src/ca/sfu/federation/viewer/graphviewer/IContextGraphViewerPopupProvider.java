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
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.action.CreateScenarioAction;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.ParametricModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 * Popup menu provider for graph views.
 * @author dmarques
 */
public final class IContextGraphViewerPopupProvider implements PopupMenuProvider, ActionListener {

    private static final Logger logger = Logger.getLogger(IContextGraphViewerPopupProvider.class.getName());

    private JPopupMenu menu;
    private AntialiasedScene scene;

    //----------------------------------------------------------------------
    
    /**
     * Popup menu provider.
     *
     * @param MyScene
     */
    public IContextGraphViewerPopupProvider(AntialiasedScene MyScene) {
        // init
        this.scene = MyScene;
        // create menu
        menu = new JPopupMenu("Model Object Actions");
        JMenuItem menuitem;
        // menu item - add Scenario
        IContext context = (IContext) Application.getContext().getViewState(ApplicationContext.VIEWER_CURRENT_CONTEXT);
        if (context instanceof ParametricModel) {
            ParametricModel model = (ParametricModel) context;
            menuitem = new JMenuItem("New Scenario");
            CreateScenarioAction snia = new CreateScenarioAction("New Scenario", null, "New Scenario", new Integer(KeyEvent.VK_S));
            menuitem.setAction(snia);
            menu.add(menuitem);
        }

        menu.add(new JSeparator());

        // menu item - properties
        menuitem = new JMenuItem("Properties");
        menu.add(menuitem);
    }

    //----------------------------------------------------------------------
    
    public JPopupMenu getPopupMenu(Widget widget) {
        return menu;
    }

    public void actionPerformed(ActionEvent e) {
        this.scene.setActiveTool(e.getActionCommand());
    }
    
}
