/**
 * IContextGraphViewerPanel.java
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

import ca.sfu.federation.model.ConfigManager;
import ca.sfu.federation.model.Assembly;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.Scenario;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.viewer.action.ScenarioNewInstanceAction;
import gnu.trove.THashMap;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
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
    
    //-------------------------------------------------------------------------
    // FIELDS
    
    // view components
    private JScrollPane jScrollPane;
    private JToolBar toolBar;
    
    // scene
    private ParametricModel model;
    private IContext context;
    private THashMap scenes;
    private MutableSceneModel scene;
    
    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * IContextGraphViewerPanel constructor.
     */
    public IContextGraphViewerPanel() {
        // init
        this.model = ParametricModel.getInstance();
        this.context = (IContext) this.model.getViewState(ConfigManager.VIEWER_CURRENT_CONTEXT);
        this.scenes = (THashMap) this.model.getViewState(ConfigManager.VIEWER_ICONTEXTVIEWER_SCENES);
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
    // METHODS
    
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
                IContext context = (IContext) ParametricModel.getInstance().getViewState(ConfigManager.VIEWER_CURRENT_CONTEXT);
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
        this.context = (IContext) this.model.getViewState(ConfigManager.VIEWER_CURRENT_CONTEXT);
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
            System.out.println("INFO: IContextGraphViewerPanel retrieving scene for " + this.context.getCanonicalName());
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
            System.out.println("INFO: IContextGraphViewerPanel creating new scene for " + this.context.getCanonicalName());
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
        Vector elements = new Vector();
        if (this.context instanceof ParametricModel) {
            // just add a widget for the model
            JButton btn = new JButton(this.context.getName());
            btn.setBackground(ConfigManager.BACKGROUND_BRIGHT);
            btn.setForeground(ConfigManager.TEXT_DARK);
            // this.add(btn,BorderLayout.SOUTH);
            // this.navpanel.add(btn);
        } else {
            Vector parents = (Vector) this.context.getParents();
            // add a widget for each element
            Enumeration e = parents.elements();
            JButton btn = null;
            while (e.hasMoreElements()) {
                Object object = e.nextElement();
                if (object instanceof INamed) {
                    INamed named = (INamed) object;
                    btn = new JButton(named.getName());
                    // JButton btn = new JButton("Named");
                    btn.setBackground(ConfigManager.BACKGROUND_LIGHT);
                    btn.setForeground(ConfigManager.TEXT_DARK);
                    // this.add(btn,BorderLayout.SOUTH);
                    // this.navpanel.add(btn);
                }
            }
            btn.setBackground(ConfigManager.BACKGROUND_BRIGHT);
            btn.setForeground(ConfigManager.TEXT_DARK);
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
        System.out.println("INFO: mouse released " + e.getPoint().getLocation().toString());
        Point p = e.getPoint();
        this.model.setViewState(ConfigManager.VIEWER_LAST_MOUSERELEASE,p);
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
            System.out.println("INFO: IContextGraphViewerPanel received event notification id " + eventId);
            switch (eventId) {
                case ConfigManager.EVENT_CONTEXT_CHANGE:
                    System.out.println("INFO: IContextGraphViewerPanel fired context change.");
                    // stop listening on the previous context
                    if (this.context instanceof Observable  && this.context != this.model) {
                        Observable ob = (Observable) this.context;
                        ob.deleteObserver(this);
                    }
                    // update the graph
                    this.buildGraph();
                    break;
                case ConfigManager.EVENT_STATE_CHANGE:
                    System.err.println("WARNING: IContextGraphViewerPanel state change not implemented.");
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
        
        //----------------------------------------------------------------------
        // FIELDS
        
        private JPopupMenu menu;
        private AntialiasedScene scene;
        
        //----------------------------------------------------------------------
        // CONSTRUCTORS
        
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
            IContext context = (IContext) ParametricModel.getInstance().getViewState(ConfigManager.VIEWER_CURRENT_CONTEXT);
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
        // METHODS
        
        public JPopupMenu getPopupMenu(Widget widget) {
            return menu;
        }
        
        public void actionPerformed(ActionEvent e) {
            this.scene.setActiveTool(e.getActionCommand());
        }
        
    }
    
} // end class