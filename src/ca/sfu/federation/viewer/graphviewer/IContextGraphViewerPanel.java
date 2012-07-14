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

import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.action.ScenarioNewInstanceAction;
import ca.sfu.federation.model.*;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 * Displays dependancy graph for an IContext.  Some display objects may
 * provide thumbnails or icon views which are then rendered in the graph.
 * TODO: need a proper set of controls on the toolbar, and key mappings to zoom/pan actions
 * @author  Davis Marques
 * @version 0.1.0
 */
public class IContextGraphViewerPanel extends JPanel implements MouseListener, Observer {

    private static final Logger logger = Logger.getLogger(IContextGraphViewerPanel.class.getName());
    
    // view components
    private JScrollPane jScrollPane;
    private JToolBar toolBar;
    
    // scene
    private ParametricModel model;
    private IContext context;
    private LinkedHashMap scenes;
    private MutableSceneModel scene;
    
    //-------------------------------------------------------------------------
    
    /**
     * IContextGraphViewerPanel constructor.
     */
    public IContextGraphViewerPanel() {
        // init
        this.model = ParametricModel.getInstance();
        this.context = (IContext) this.model.getViewState(ApplicationContext.VIEWER_CURRENT_CONTEXT);
        this.scenes = (LinkedHashMap) this.model.getViewState(ApplicationContext.VIEWER_ICONTEXTVIEWER_SCENES);
        // add scene view to scrollpane, scrollpane to panel
        this.addMouseListener(this);
        this.setLayout(new BorderLayout());
        // create the toolbar, add it to the panel
        this.toolBar = buildDefaultToolbar();
        // this.add(toolBar,BorderLayout.NORTH);
        // build the graph
        this.buildGraph();
        // observe the model for changes
        if (this.model instanceof Observable) {
            Observable o = (Observable) this.model;
            o.addObserver(this);
        }
    }
    
    //-------------------------------------------------------------------------
    
    /**
     * Build the default toolbar.
     * @return JToolBar with default configuration.
     */
    public JToolBar buildDefaultToolbar() {
        JToolBar toolBar = new JToolBar("Default Toolbar",JToolBar.HORIZONTAL);
        toolBar.setFloatable(true);
        JButton btnNewScenario = new JButton("New Scenario");
        JButton btnNewAssembly = new JButton("New Assembly");
        JButton btnNewComponent = new JButton("New Component");
        btnNewScenario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                ParametricModel model = ParametricModel.getInstance();
                Scenario scenario = new Scenario(model);
            }
        });
        btnNewAssembly.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                IContext context = (IContext) ParametricModel.getInstance().getViewState(ApplicationContext.VIEWER_CURRENT_CONTEXT);
                Assembly assembly = new Assembly(context);
            }
        });
        toolBar.add(btnNewScenario);
        toolBar.add(btnNewAssembly);
        toolBar.add(btnNewComponent);
        // return result
        return toolBar;
    }
    
    /**
     * Build a visual graph to depict the Context model.
     */
    private void buildGraph() {
        // get the current context
        this.context = (IContext) this.model.getViewState(ApplicationContext.VIEWER_CURRENT_CONTEXT);
        System.out.println("INFO: IContextGraphViewerPanel set to " + this.context.getCanonicalName());
        if (this.context == null) {
            return;
        }
        // observe the context for changes
        if (this.context instanceof Observable) {
            Observable o = (Observable) this.context;
            o.addObserver(this);
        }
        // if we have already created a scene for this context, restore it
        if (this.scenes.containsKey(this.context.getCanonicalName())) {
            logger.log(Level.INFO,"IContextGraphViewerPanel retrieving scene for {0}", this.context.getCanonicalName());
            // fetch the existing scene from the scene list
            this.scene = (MutableSceneModel) this.scenes.get(this.context.getCanonicalName());
            this.scene.validate();
            // clear the panel
            this.removeAll();
            // add the scene view to the scrollpane
            JComponent view = this.scene.createView();
            JScrollPane jScrollPane = new JScrollPane(view);
            this.add(jScrollPane);
            // revalidate and repaint the panel
            this.validate();
            return;
        } else {
            logger.log(Level.INFO,"IContextGraphViewerPanel creating new scene for {0}", this.context.getCanonicalName());
            // create a new scene, add it to the scene list
            this.scene = new MutableSceneModel(this.context);
            this.scenes.put(this.context.getCanonicalName(),this.scene);
            // clear the panel
            this.removeAll();
            // add the scene view to the scrollpane
            JComponent view = this.scene.createView();
            view.addMouseListener(this);
            JScrollPane jScrollPane = new JScrollPane(view);
            this.add(jScrollPane);
            // revalidate and repaint the panel
            this.validate();
        }
    }
    
    /**
     * Build the Navigation panel.
     */
    public void buildNavPanel() {
        ArrayList elements = new ArrayList();
        if (this.context instanceof ParametricModel) {
            // just add a widget for the model
            JButton btn = new JButton(this.context.getName());
            btn.setBackground(ApplicationContext.BACKGROUND_BRIGHT);
            btn.setForeground(ApplicationContext.TEXT_DARK);
            // this.add(btn,BorderLayout.SOUTH);
            // this.navpanel.add(btn);
        } else {
            ArrayList parents = (ArrayList) this.context.getParents();
            // add a widget for each element
            Iterator e = parents.iterator();
            JButton btn = null;
            while (e.hasNext()) {
                Object object = e.next();
                if (object instanceof INamed) {
                    INamed named = (INamed) object;
                    btn = new JButton(named.getName());
                    // JButton btn = new JButton("Named");
                    btn.setBackground(ApplicationContext.BACKGROUND_LIGHT);
                    btn.setForeground(ApplicationContext.TEXT_DARK);
                    // this.add(btn,BorderLayout.SOUTH);
                    // this.navpanel.add(btn);
                }
            }
            btn.setBackground(ApplicationContext.BACKGROUND_BRIGHT);
            btn.setForeground(ApplicationContext.TEXT_DARK);
        }
    }
    
    /**
     * Get the coordinates for the last mouse release event.
     * @param e Mouse event.
     */
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
        logger.log(Level.INFO,"mouse released {0}", e.getPoint().getLocation().toString());
        Point p = e.getPoint();
        this.model.setViewState(ApplicationContext.VIEWER_LAST_MOUSERELEASE,p);
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    /**
     * Update event.
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            logger.log(Level.INFO,"IContextGraphViewerPanel received event notification id {0}", eventId);
            switch (eventId) {
                case ApplicationContext.EVENT_CONTEXT_CHANGE:
                    logger.log(Level.INFO,"IContextGraphViewerPanel fired context change");
                    // stop listening on the previous context
                    if (this.context instanceof Observable  && this.context != this.model) {
                        Observable ob = (Observable) this.context;
                        ob.deleteObserver(this);
                    }
                    // update the graph
                    this.buildGraph();
                    break;
                case ApplicationContext.EVENT_STATE_CHANGE:
                    logger.log(Level.WARNING,"IContextGraphViewerPanel state change not implemented");
                    break;
            }
        }
    }
    
    //--------------------------------------------------------------------------
    // PRIVATE CLASSES
    
    /**
     * Popup menu provider.
     */
    private final class IContextGraphViewerPanelPopupProvider implements PopupMenuProvider, ActionListener {
        
        private JPopupMenu menu;
        private AntialiasedScene scene;
        
        //----------------------------------------------------------------------
    
        
        /**
         * Popup menu provider.
         * @param MyScene
         */
        public IContextGraphViewerPanelPopupProvider(AntialiasedScene MyScene) {
            // init
            this.scene = MyScene;
            // create menu
            menu = new JPopupMenu("Model Object Actions");
            JMenuItem menuitem;
            // menu item - add Scenario
            IContext context = (IContext) ParametricModel.getInstance().getViewState(ApplicationContext.VIEWER_CURRENT_CONTEXT);
            if (context instanceof ParametricModel) {
                ParametricModel model = (ParametricModel) context;
                menuitem = new JMenuItem("New Scenario");
                ScenarioNewInstanceAction snia = new ScenarioNewInstanceAction("New Scenario",null,"New Scenario",new Integer(KeyEvent.VK_S));
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
    
} // end class