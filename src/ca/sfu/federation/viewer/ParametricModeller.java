/**
 * ParametricModeller.java
 * Copyright (c) 2006 Davis M. Marques <dmarques@sfu.ca>
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

package ca.sfu.federation.viewer;

import ca.sfu.federation.model.Assembly;
import ca.sfu.federation.model.Behavior;
import ca.sfu.federation.model.ConfigManager;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.Scenario;
import ca.sfu.federation.model.geometry.CoordinateSystem;
import ca.sfu.federation.model.geometry.Line;
import ca.sfu.federation.model.geometry.Plane;
import ca.sfu.federation.model.geometry.Point;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.viewer.action.ApplicationAboutAction;
import ca.sfu.federation.viewer.action.ApplicationExitAction;
import ca.sfu.federation.viewer.action.AbstractTreeExplorerNewInstanceAction;
import ca.sfu.federation.viewer.action.IContextGraphViewerNewInstance;
import ca.sfu.federation.viewer.action.IContextModelViewerNewInstance;
import ca.sfu.federation.viewer.action.IContextStackViewerNewInstance;
import ca.sfu.federation.viewer.action.ParametricModelCloseAction;
import ca.sfu.federation.viewer.action.ParametricModelNewInstanceAction;
import ca.sfu.federation.viewer.action.ParametricModelOpenAction;
import ca.sfu.federation.viewer.action.ParametricModelSaveAsAction;
import ca.sfu.federation.viewer.graphviewer.IContextGraphViewerPanel;
import ca.sfu.federation.viewer.propertysheet.AssemblyPropertySheet;
import ca.sfu.federation.viewer.propertysheet.LinePropertySheet;
import ca.sfu.federation.viewer.propertysheet.CoordinateSystemPropertySheet;
import ca.sfu.federation.viewer.propertysheet.ParametricModelPropertySheet;
import ca.sfu.federation.viewer.propertysheet.PlanePropertySheet;
import ca.sfu.federation.viewer.propertysheet.PointPropertySheet;
import ca.sfu.federation.viewer.propertysheet.ScenarioPropertySheet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

/**
 * @author Davis Marques
 * @version 0.1.0
 */
public class ParametricModeller extends JFrame implements Observer {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private static final String appName = "Parametric Modeller";
    private static ParametricModel model;
    private JPanel propertySheetPanel;
    private JMenuBar Menu;
    private Scenario sc0;
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * ParametricModeller constructor.
     */
    public ParametricModeller() {
        // init
        ResourceBundle config = ResourceBundle.getBundle("ca/sfu/federation/resources/ParametricModeller");
        // register property editors
        ConfigManager.registerPropertyEditors();
        
        // create a default model
        this.model = new ParametricModel("MyModel");
        this.initDemoModel();
        
        // listen for changes on the model
        if (this.model instanceof ParametricModel) {
            Observable o = (Observable) this.model;
            o.addObserver(this);
        }
        // build the interface
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPopupMenu.setDefaultLightWeightPopupEnabled(false); // for mixing light and heavyweight components
        this.buildDefaultMenuBar();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
        this.setTitle(config.getString("application-title"));
        
        // layout
        Container contentpane = this.getContentPane();
        contentpane.setLayout(new BorderLayout());

        // panels
        IContextGraphViewerPanel graphpanel = new IContextGraphViewerPanel();
        // PropertySheetPanel propertysheet = new PropertySheetPanel();
        propertySheetPanel = new JPanel();
        propertySheetPanel.setLayout(new BorderLayout());

        // split pane
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,graphpanel,propertySheetPanel);
        split.setDividerLocation(0.8);
        contentpane.add(split,BorderLayout.CENTER);

        // prep and display
        this.pack();
        this.setVisible(true);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    }
    
    //--------------------------------------------------------------------------
    // METHODS
    
    /**
     * Build main menu bar.
     */
    public void buildDefaultMenuBar() {
        // init
        JMenuBar menubar = new JMenuBar();
        menubar.setOpaque(true);
        JMenu menu = null;
        JMenuItem menuitem = null;
        // File Menu
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        ParametricModelNewInstanceAction pmnp = new ParametricModelNewInstanceAction("New Project",null,"New Project",KeyEvent.VK_N);
        menu.add(pmnp);
        ParametricModelOpenAction pmoa = new ParametricModelOpenAction("Open Project",null,"Open Project",KeyEvent.VK_O);
        menu.add(pmoa);
        menu.add(new JSeparator());
        ParametricModelOpenAction pmsa = new ParametricModelOpenAction("Save Project",null,"Save Project",KeyEvent.VK_S);
        menu.add(pmsa);
        ParametricModelSaveAsAction pmsaa = new ParametricModelSaveAsAction("Save Project As",null,"Save Project As",KeyEvent.VK_A);
        menu.add(pmsaa);
        ParametricModelCloseAction pmca = new ParametricModelCloseAction("Close Project",null,"Close Project",KeyEvent.VK_C);
        menu.add(pmca);
        menu.add(new JSeparator());
        ApplicationExitAction aea = new ApplicationExitAction("Exit",null,"Exit",KeyEvent.VK_X);
        menu.add(aea);
        menubar.add(menu);
        // Edit Menu
//        menu = new JMenu("Edit");
//        menu.setMnemonic(KeyEvent.VK_E);
//        INamedObjectDeleteAction inda = new INamedObjectDeleteAction("Delete",null,"Delete",KeyEvent.VK_D);
//        menu.add(inda);
//        menubar.add(menu);
        // View Menu
        menu = new JMenu("View");
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
        menubar.add(menu);
        // About Menu
        menu = new JMenu("About");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.add(new JSeparator());
        ApplicationAboutAction aaa = new ApplicationAboutAction("About this Application",null,"About this Application",KeyEvent.VK_A);
        menu.add(aaa);
        menubar.add(menu);
        // add the menubar to the frame
        this.setJMenuBar(menubar);
    }
    
    /**
     * Build property sheet.
     */
    private void buildPropertySheet() {
        INamed named = (INamed) this.model.getViewState(ConfigManager.VIEWER_SELECTION);
        if (named instanceof ParametricModel) {
            this.propertySheetPanel.removeAll();
            ParametricModelPropertySheet panel = new ParametricModelPropertySheet();
            this.propertySheetPanel.add(panel,BorderLayout.CENTER);
            this.validate();            
        } else if (named instanceof Scenario) {
            this.propertySheetPanel.removeAll();
            ScenarioPropertySheet panel = new ScenarioPropertySheet();
            this.propertySheetPanel.add(panel,BorderLayout.CENTER);
            this.validate();
        } else if (named instanceof Assembly) {
            this.propertySheetPanel.removeAll();
            AssemblyPropertySheet panel = new AssemblyPropertySheet();
            this.propertySheetPanel.add(panel,BorderLayout.CENTER);
            this.validate();
        } else if (named instanceof CoordinateSystem) {
            this.propertySheetPanel.removeAll();
            CoordinateSystemPropertySheet panel = new CoordinateSystemPropertySheet();
            this.propertySheetPanel.add(panel,BorderLayout.CENTER);
            this.validate();
        } else if (named instanceof Point) {
            this.propertySheetPanel.removeAll();
            PointPropertySheet panel = new PointPropertySheet();
            this.propertySheetPanel.add(panel,BorderLayout.CENTER);
            this.validate();
        } else if (named instanceof Line) {
            this.propertySheetPanel.removeAll();
            LinePropertySheet panel = new LinePropertySheet();
            this.propertySheetPanel.add(panel,BorderLayout.CENTER);
            this.validate();
        } else if (named instanceof Plane) {
            this.propertySheetPanel.removeAll();
            PlanePropertySheet panel = new PlanePropertySheet();
            this.propertySheetPanel.add(panel,BorderLayout.CENTER);
            this.validate();
        }
    }    
    
    /**
     * Create a demo model.
     */
    public void initDemoModel() {
        // scenario 0
        sc0 = new Scenario("scenario0",this.model);
        Assembly a0 = new Assembly("myAssembly0",sc0);
        CoordinateSystem cs1 = new CoordinateSystem("baseCS",a0);
        Point p11 = new Point("Point01",a0);
        Point p12 = new Point("Point02",a0);
        Line line11 = new Line("Line01",a0);
        try {
            cs1.setUpdateMethod("updateDefault");
            p11.setUpdateMethod("updateByCSAndCoordinates");
            p12.setUpdateMethod("updateByCSAndCoordinates");
            line11.setUpdateMethod("updateByPoints");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        p11.setInput("MyCoordinateSystem","baseCS");
        p11.setInput("X","0.5");
        p11.setInput("Y","0.5");
        p11.setInput("Z","0.5");
        p12.setInput("MyCoordinateSystem","baseCS");
        p12.setInput("X","3.0");
        p12.setInput("Y","0.5");
        p12.setInput("Z","0.5");
        line11.setInput("StartPoint","Point01");
        line11.setInput("EndPoint","Point02");
        Assembly a1 = new Assembly("myAssembly1",sc0);
        CoordinateSystem cs2 = new CoordinateSystem("baseCS",a1);
        Point p21 = new Point("Point01",a1);
        Point p22 = new Point("Point02",a1);
        Line line21 = new Line("Line01",a1);
        try {
            cs2.setUpdateMethod("updateDefault");
            p21.setUpdateMethod("updateByCSAndCoordinates");
            p22.setUpdateMethod("updateByCSAndCoordinates");
            line21.setUpdateMethod("updateByPoints");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        p21.setInput("MyCoordinateSystem","baseCS");
        p21.setInput("X","0.5");
        p21.setInput("Y","2.0");
        p21.setInput("Z","0.5");
        p22.setInput("MyCoordinateSystem","baseCS");
        p22.setInput("X","3.0");
        p22.setInput("Y","2.0");
        p22.setInput("Z","0.5");
        line21.setInput("StartPoint","Point01");
        line21.setInput("EndPoint","Point02");
        boolean success = a0.update();
        success = a1.update();
        // scenario1
        Scenario sc1 = new Scenario("scenario1",this.model);
        try {
            sc1.addContextual(a0);
            sc1.addContextual(a1);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        Assembly a2 = new Assembly("myAssembly1",sc1);
        // scenario2
        Scenario sc2 = new Scenario("scenario2",this.model);
        try {
            sc2.addContextual(a2);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        Assembly a3 = new Assembly("myAssembly3",sc2);
        CoordinateSystem cs3 = new CoordinateSystem("baseCS",a3);
        Point p31 = new Point("Point01",a3);
        Point p32 = new Point("Point02",a3);
        Line line31 = new Line("Line01",a3);
        try {
            cs3.setUpdateMethod("updateDefault");
            p31.setUpdateMethod("updateByCSAndCoordinates");
            p32.setUpdateMethod("updateByCSAndCoordinates");
            line31.setUpdateMethod("updateByPoints");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        p31.setInput("MyCoordinateSystem","baseCS");
        p31.setInput("X","1.0");
        p31.setInput("Y","2.0");
        p31.setInput("Z","3.0");
        p32.setInput("MyCoordinateSystem","baseCS");
        p32.setInput("X","6.0");
        p32.setInput("Y","7.0");
        p32.setInput("Z","8.0");
        line31.setInput("StartPoint","Point01");
        line31.setInput("EndPoint","Point02");
        Assembly a4 = new Assembly("myAssembly4",sc2);
        CoordinateSystem cs4 = new CoordinateSystem("baseCS",a4);
        Point p41 = new Point("Point01",a4);
        Point p42 = new Point("Point02",a4);
        Line line41 = new Line("Line01",a4);
        try {
            cs4.setUpdateMethod("updateDefault");
            p41.setUpdateMethod("updateByCSAndCoordinates");
            p42.setUpdateMethod("updateByCSAndCoordinates");
            line41.setUpdateMethod("updateByPoints");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        p41.setInput("MyCoordinateSystem","baseCS");
        p41.setInput("X","1.0");
        p41.setInput("Y","2.0");
        p41.setInput("Z","3.0");
        p42.setInput("MyCoordinateSystem","baseCS");
        p42.setInput("X","6.0");
        p42.setInput("Y","7.0");
        p42.setInput("Z","8.0");
        line41.setInput("StartPoint","Point01");
        line41.setInput("EndPoint","Point02");
        Behavior behavior = new Behavior(a3);
        // behavior.setUpdateCondition("SELECT * WHERE name=='myAssembly4' AND name!='somethingelse' LIMIT 1");
        // behavior.setUpdateCondition("SELECT * WHERE name=='myAssembly4' AND name!='somethingelse' OR X>5 AND Y<3 LIMIT 1");
        behavior.setUpdateCondition("SELECT * WHERE name=='myAssembly4'");
        // behavior.setUpdateAction("");
        // Step 13: Remove the target assembly.  Display the assembly state.
        sc2.remove(a4);
        // Step 14: Add the target assembly back in.  Display the assembly state.
        a4 = new Assembly("myAssembly4",sc2);
        cs4 = new CoordinateSystem("baseCS",a4);
        p41 = new Point("Point01",a4);
        p42 = new Point("Point02",a4);
        line41 = new Line("Line01",a4);
        try {
            cs4.setUpdateMethod("updateDefault");
            p41.setUpdateMethod("updateByCSAndCoordinates");
            p42.setUpdateMethod("updateByCSAndCoordinates");
            line41.setUpdateMethod("updateByPoints");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        p41.setInput("MyCoordinateSystem","baseCS");
        p41.setInput("X","1.0");
        p41.setInput("Y","2.0");
        p41.setInput("Z","3.0");
        p42.setInput("MyCoordinateSystem","baseCS");
        p42.setInput("X","6.0");
        p42.setInput("Y","7.0");
        p42.setInput("Z","8.0");
        line41.setInput("StartPoint","Point01");
        line41.setInput("EndPoint","Point02");
        // scenario3
        Scenario sc3 = new Scenario("scenario3",this.model);
        try {
            sc3.addContextual(a2);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ParametricModeller();
            }
        });
    }
    
    /**
     * Update event.
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            System.out.println("INFO: PropertySheetform_Limited received event notification id " + eventId);
            switch (eventId) {
                case ConfigManager.EVENT_SELECTION_CHANGE:
                    this.buildPropertySheet();
                    break;
            }
        }
    }

} // end class
