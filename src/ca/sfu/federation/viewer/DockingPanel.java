/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package ca.sfu.federation.viewer;

import ca.sfu.federation.viewer.console.ConsolePanel;
import ca.sfu.federation.viewer.explorer.AbstractTreeExplorerPanel;
import ca.sfu.federation.viewer.propertysheet.PropertySheetPanel;
import com.javadocking.DockingManager;
import com.javadocking.dock.*;
import com.javadocking.dock.docker.BorderDocker;
import com.javadocking.dock.factory.CompositeToolBarDockFactory;
import com.javadocking.dock.factory.LeafDockFactory;
import com.javadocking.dock.factory.ToolBarDockFactory;
import com.javadocking.dockable.*;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;
import com.javadocking.drag.DragListener;
import com.javadocking.model.DefaultDockingPath;
import com.javadocking.model.DockingPath;
import com.javadocking.model.FloatDockModel;
import com.javadocking.util.ToolBarButton;
import com.javadocking.visualizer.DockingMinimizer;
import com.javadocking.visualizer.FloatExternalizer;
import com.javadocking.visualizer.SingleMaximizer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

/**
 * Application docking panel.
 * @author dmarques
 */
public class DockingPanel extends JPanel {

    private final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    
    private final int LEFT_DOCK_WIDTH = 220;
    private final int RIGHT_DOCK_WIDTH = 220;
    private final int BOTTOM_DOCK_HEIGHT = 160;
    private final int TOOLBAR_WIDTH = 30;
    
    private ArrayList<Dockable> dockables = new ArrayList<Dockable>();
    private Dockable[] buttonDockables = new Dockable[42];

    //--------------------------------------------------------------------------
    
    /**
     * DockingPanel constructor
     * @param ParentFrame Application frame
     */
    public DockingPanel(JFrame ParentFrame) {
        super(new BorderLayout());

        // create the dock model for the docks, minimizer and maximizer
        FloatDockModel dockModel = new FloatDockModel("workspace.dck");
        String frameId = "frame";
        dockModel.addOwner(frameId, ParentFrame);

        // give the dock model to the docking manager
        DockingManager.setDockModel(dockModel);

        // create the application viewers
        JButton button1 = new JButton("Scenario");
        JButton button2 = new JButton("Graph View 1");
        JButton button3 = new JButton("Graph View 2");
        JButton button4 = new JButton("Model View 1");
        JButton button5 = new JButton("Model View 2");
        JButton button6 = new JButton("Stack View");
        AbstractTreeExplorerPanel explorerPanel = new AbstractTreeExplorerPanel();
        PropertySheetPanel propertysheetPanel = new PropertySheetPanel();
        ConsolePanel consolePanel = new ConsolePanel();

        // create the dockables around the content components
        Dockable scenarioViewDockable = createDockable("Project", button1, "Project", new ImageIcon(getClass().getResource("/com/javadocking/resources/images/text12.gif")), "Project");
        Dockable graphView1Dockable = createDockable("Graph View 1", button2, "Graph View 1", new ImageIcon(getClass().getResource("/com/javadocking/resources/images/text12.gif")), "Graph View 1");
        Dockable graphView2Dockable = createDockable("Graph View 2", button3, "Graph View 2", new ImageIcon(getClass().getResource("/com/javadocking/resources/images/text12.gif")), "Graph View 2");
        Dockable modelView1Dockable = createDockable("Model View 1", button4, "Model View 1", new ImageIcon(getClass().getResource("/com/javadocking/resources/images/text12.gif")), "Model View 1");
        Dockable modelView2Dockable = createDockable("Model View 2", button5, "Model View 2", new ImageIcon(getClass().getResource("/com/javadocking/resources/images/text12.gif")), "Model View 2");
        Dockable stackViewDockable = createDockable("Stack", button6, "Stack", new ImageIcon(getClass().getResource("/com/javadocking/resources/images/text12.gif")), "Stack Panel");
        Dockable explorerDockable = createDockable("Project", explorerPanel, "Project", new ImageIcon(getClass().getResource("/com/javadocking/resources/images/table12.gif")), "Project");
        Dockable outputDockable = createDockable("Output", consolePanel, "Command", new ImageIcon(getClass().getResource("/com/javadocking/resources/images/graph12.gif")), "Command");
        Dockable propertySheetDockable = createDockable("Properties", propertysheetPanel, "Properties", new ImageIcon(getClass().getResource("/com/javadocking/resources/images/table12.gif")), "Properties");

        // add dockables to list
        dockables.add(scenarioViewDockable);
        dockables.add(graphView1Dockable);
        dockables.add(graphView2Dockable);
        dockables.add(modelView1Dockable);
        dockables.add(modelView2Dockable);
        dockables.add(stackViewDockable);
        dockables.add(explorerDockable);
        dockables.add(outputDockable);
        dockables.add(propertySheetDockable);
        
        // add actions to dockables
        for (int index = 0; index < dockables.size(); index++) {
            // dockables[index] = addLimitActions(dockables[index]);
            addAllActions(dockables.get(index));
        }

        // Create the buttons with a dockable around.
        buttonDockables[0] = createButtonDockable("ButtonDockableAdd", "Add", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/add.png")), "Add!");
        buttonDockables[1] = createButtonDockable("ButtonDockableAccept", "Accept", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/accept.png")), "Accept!");
        buttonDockables[2] = createButtonDockable("ButtonDockableCancel", "Cancel", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/cancel.png")), "Cancel!");
        buttonDockables[3] = createButtonDockable("ButtonDockableUndo", "Undo", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/undo.png")), "Undo!");
        buttonDockables[4] = createButtonDockable("ButtonDockableRedo", "Redo", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/redo.png")), "Redo!");
        buttonDockables[5] = createButtonDockable("ButtonDockableRefresh", "Refresh", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/refresh.png")), "Refresh!");
        buttonDockables[6] = createButtonDockable("ButtonDockableBin", "Bin", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/bin.png")), "Bin!");
        buttonDockables[7] = createButtonDockable("ButtonDockableIcons", "Icons", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/icons.png")), "Icons!");
        buttonDockables[8] = createButtonDockable("ButtonDockableList", "List", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/list.png")), "List!");
        buttonDockables[9] = createButtonDockable("ButtonDockableImages", "Images", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/images.png")), "Images!");
        buttonDockables[10] = createButtonDockable("ButtonDockableDivide", "Divide", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/divide.png")), "Divide!");
        buttonDockables[11] = createButtonDockable("ButtonDockableJoin", "Join", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/join.png")), "Join!");
        buttonDockables[12] = createButtonDockable("ButtonDockableSwitch", "Switch", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/switch.png")), "Switch!");
        buttonDockables[13] = createButtonDockable("ButtonDockableAsterisk", "Asterisk", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/asterisk.png")), "Asterisk!");
        buttonDockables[14] = createButtonDockable("ButtonDockableAnchor", "Anchor", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/anchor.png")), "Anchor!");
        buttonDockables[15] = createButtonDockable("ButtonDockableTerminal", "Terminal", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/terminal.png")), "Terminal!");
        buttonDockables[16] = createButtonDockable("ButtonDockableStar", "Well Done", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/star.png")), "Well Done!");
        buttonDockables[17] = createButtonDockable("ButtonDockableWakeUp", "Wake Up", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/wake_up.png")), "Wake up!");
        buttonDockables[18] = createButtonDockable("ButtonDockableAddToBasket", "Add to Basket", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/add_to_basket.png")), "Add to Basket!");
        buttonDockables[19] = createButtonDockable("ButtonDockableRemoveFromBasket", "Remove from Basket", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/remove_from_basket.png")), "Remove from Basket!");
        
        buttonDockables[37] = createButtonDockable("ButtonDockableChartBar", "Bar Chart", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/chart_bar.png")), "Bar Chart!");
        buttonDockables[38] = createButtonDockable("ButtonDockableChartCurve", "Curve Chart", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/chart_curve.png")), "Curve Chart!");
        buttonDockables[39] = createButtonDockable("ButtonDockableChartLine", "Line Chart", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/chart_line.png")), "Chart!");
        buttonDockables[40] = createButtonDockable("ButtonDockableChartOrganisation", "Organisation Chart", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/chart_organisation.png")), "Oraganisation Chart!");
        buttonDockables[41] = createButtonDockable("ButtonDockableChartPie", "Pie Chart", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/chart_pie.png")), "Pie Chart!");

        // Give the float dock a different child dock factory.
        // We don't want the floating docks to be splittable.
        FloatDock floatDock = dockModel.getFloatDock(ParentFrame);
        floatDock.setChildDockFactory(new LeafDockFactory(false));

        // Create the tab docks.
        TabDock centerTabbedDock = new TabDock();
        TabDock bottomTabbedDock = new TabDock();
        TabDock leftTabbedDock = new TabDock();
        TabDock rightTabbedDock = new TabDock();

        // Add the dockables to these tab docks.
        centerTabbedDock.addDockable(scenarioViewDockable, new Position(0));
        centerTabbedDock.addDockable(graphView1Dockable, new Position(1));
        centerTabbedDock.addDockable(graphView2Dockable, new Position(2));
        centerTabbedDock.addDockable(modelView1Dockable, new Position(3));
        centerTabbedDock.addDockable(modelView2Dockable, new Position(4));
        bottomTabbedDock.addDockable(outputDockable, new Position(0));
        leftTabbedDock.addDockable(explorerDockable, new Position(0));
        leftTabbedDock.addDockable(stackViewDockable, new Position(1));        
        rightTabbedDock.addDockable(propertySheetDockable, new Position(0));
        
        leftTabbedDock.setSelectedDockable(explorerDockable);
        centerTabbedDock.setSelectedDockable(scenarioViewDockable);

        // define the four screen regions: center, center/right, center/bottom, left
        SplitDock centerSplitDock = new SplitDock();
        centerSplitDock.addChildDock(centerTabbedDock, new Position(Position.CENTER));
        centerSplitDock.addChildDock(rightTabbedDock, new Position(Position.RIGHT));
        centerSplitDock.setDividerLocation(SCREEN_WIDTH - RIGHT_DOCK_WIDTH - LEFT_DOCK_WIDTH - (TOOLBAR_WIDTH * 2));
        
        SplitDock bottomSplitDock = new SplitDock();
        bottomSplitDock.addChildDock(bottomTabbedDock, new Position(Position.CENTER));
        
        SplitDock rightSplitDock = new SplitDock();
        rightSplitDock.addChildDock(centerSplitDock, new Position(Position.CENTER));
        rightSplitDock.addChildDock(bottomSplitDock, new Position(Position.BOTTOM));
        rightSplitDock.setDividerLocation(SCREEN_HEIGHT - BOTTOM_DOCK_HEIGHT - (TOOLBAR_WIDTH * 3));
        
        SplitDock leftSplitDock = new SplitDock();
        leftSplitDock.addChildDock(leftTabbedDock, new Position(Position.CENTER));
        
        SplitDock totalSplitDock = new SplitDock();
        totalSplitDock.addChildDock(leftSplitDock, new Position(Position.LEFT));
        totalSplitDock.addChildDock(rightSplitDock, new Position(Position.RIGHT));
        totalSplitDock.setDividerLocation(LEFT_DOCK_WIDTH);

        // add the root dock to the dock model
        dockModel.addRootDock("totalDock", totalSplitDock, ParentFrame);

        // create a maximizer and add it to the dock model
        SingleMaximizer maximizePanel = new SingleMaximizer(totalSplitDock);
        dockModel.addVisualizer("maximizer", maximizePanel, ParentFrame);

        // create a docking minimizer and add it to the dock model
        BorderDock minimizerBorderDock = new BorderDock(new ToolBarDockFactory());
        minimizerBorderDock.setMode(BorderDock.MODE_MINIMIZE_BAR);
        minimizerBorderDock.setCenterComponent(maximizePanel);
        BorderDocker borderDocker = new BorderDocker();
        borderDocker.setBorderDock(minimizerBorderDock);
        DockingMinimizer minimizer = new DockingMinimizer(borderDocker);
        dockModel.addVisualizer("minimizer", minimizer, ParentFrame);

        // add an externalizer to the dock model
        dockModel.addVisualizer("externalizer", new FloatExternalizer(ParentFrame), ParentFrame);

        // create the tool bar border dock for the buttons
        BorderDock toolBarBorderDock = new BorderDock(new CompositeToolBarDockFactory(), minimizerBorderDock);
        toolBarBorderDock.setMode(BorderDock.MODE_TOOL_BAR);
        CompositeLineDock compositeToolBarDock1 = new CompositeLineDock(CompositeLineDock.ORIENTATION_HORIZONTAL, false,
                new ToolBarDockFactory(), DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        CompositeLineDock compositeToolBarDock2 = new CompositeLineDock(CompositeLineDock.ORIENTATION_VERTICAL, false,
                new ToolBarDockFactory(), DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        toolBarBorderDock.setDock(compositeToolBarDock1, Position.TOP);
        toolBarBorderDock.setDock(compositeToolBarDock2, Position.LEFT);

        // add this dock also as root dock to the dock model
        dockModel.addRootDock("toolBarBorderDock", toolBarBorderDock, ParentFrame);

        // add the tool bar border dock to this panel
        this.add(toolBarBorderDock, BorderLayout.CENTER);

        // The line docks and one grid dock for the buttons.
        LineDock toolBarDock1 = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        LineDock toolBarDock2 = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        LineDock toolBarDock3 = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        LineDock toolBarDock4 = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        LineDock toolBarDock5 = new LineDock(LineDock.ORIENTATION_VERTICAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        LineDock toolBarDock6 = new LineDock(LineDock.ORIENTATION_VERTICAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        GridDock toolGridDock = new GridDock(DockingMode.TOOL_GRID);

        // Add the button dockables to the line docks.
        toolBarDock1.addDockable(buttonDockables[0], new Position(0));
        toolBarDock1.addDockable(buttonDockables[1], new Position(1));
        toolBarDock1.addDockable(buttonDockables[2], new Position(2));
        toolBarDock1.addDockable(buttonDockables[3], new Position(3));
        toolBarDock1.addDockable(buttonDockables[4], new Position(4));
        toolBarDock1.addDockable(buttonDockables[5], new Position(5));
        toolBarDock1.addDockable(buttonDockables[6], new Position(6));
        toolBarDock2.addDockable(buttonDockables[7], new Position(0));
        toolBarDock2.addDockable(buttonDockables[8], new Position(1));
        toolBarDock2.addDockable(buttonDockables[9], new Position(2));
        toolBarDock3.addDockable(buttonDockables[10], new Position(0));
        toolBarDock3.addDockable(buttonDockables[11], new Position(1));
        toolBarDock3.addDockable(buttonDockables[12], new Position(2));
        toolBarDock4.addDockable(buttonDockables[13], new Position(0));
        toolBarDock4.addDockable(buttonDockables[14], new Position(1));
        toolBarDock4.addDockable(buttonDockables[15], new Position(2));
        toolBarDock4.addDockable(buttonDockables[16], new Position(3));
        toolBarDock4.addDockable(buttonDockables[17], new Position(4));
        toolBarDock5.addDockable(buttonDockables[18], new Position(0));
        toolBarDock5.addDockable(buttonDockables[19], new Position(1));
        toolBarDock6.addDockable(buttonDockables[37], new Position(0));
        toolBarDock6.addDockable(buttonDockables[38], new Position(1));
        toolBarDock6.addDockable(buttonDockables[39], new Position(2));
        toolBarDock6.addDockable(buttonDockables[40], new Position(3));
        toolBarDock6.addDockable(buttonDockables[41], new Position(4));

        // Add the line docks to their composite parents.
        compositeToolBarDock1.addChildDock(toolBarDock1, new Position(0));
        compositeToolBarDock1.addChildDock(toolBarDock2, new Position(1));
        compositeToolBarDock1.addChildDock(toolBarDock3, new Position(2));
        compositeToolBarDock1.addChildDock(toolBarDock4, new Position(3));
        compositeToolBarDock2.addChildDock(toolBarDock5, new Position(0));
        compositeToolBarDock2.addChildDock(toolBarDock6, new Position(1));

        // minimize the output panel
        minimizer.visualizeDockable(outputDockable);

        // add the paths of the docked dockables to the model with the docking paths
        Iterator<Dockable> it = dockables.iterator();
        while (it.hasNext()) {
            Dockable d = it.next();
            addDockingPath(d);
        }
    }

    //--------------------------------------------------------------------------
    
    /**
     * Decorates the given dockable with all state actions.
     * @param dockable	The dockable to decorate.
     * @return	The wrapper around the given dockable, with actions.
     */
    private Dockable addAllActions(Dockable dockable) {
        Dockable wrapper = new StateActionDockable(dockable, new DefaultDockableStateActionFactory(), DockableState.statesClosed());
        wrapper = new StateActionDockable(wrapper, new DefaultDockableStateActionFactory(), DockableState.statesAllExceptClosed());
        return wrapper;
    }

    /**
     * Creates a docking path for the given dockable. It contains the
     * information how the dockable is docked now. The docking path is added to
     * the docking path model of the docking manager.
     * @param D The dockable for which a docking path has to be created.
     * @return The docking path model. Null if the dockable is not docked.
     */
    private DockingPath addDockingPath(Dockable D) {
        if (D.getDock() != null) {
            // create the docking path of the dockable
            DockingPath dockingPath = DefaultDockingPath.createDockingPath(D);
            DockingManager.getDockingPathModel().add(dockingPath);
            return dockingPath;
        }
        return null;
    }

    /**
     * Decorates the given dockable with some state actions (not maximized).
     * @param dockable	The dockable to decorate.
     * @return	The wrapper around the given dockable, with actions.
     */
    private Dockable addLimitActions(Dockable dockable) {
        Dockable wrapper = new StateActionDockable(dockable, new DefaultDockableStateActionFactory(), DockableState.statesClosed());
        int[] limitStates = {DockableState.NORMAL, DockableState.MINIMIZED, DockableState.EXTERNALIZED};
        wrapper = new StateActionDockable(wrapper, new DefaultDockableStateActionFactory(), limitStates);
        return wrapper;
    }

    /**
     * Creates a dockable with a button as content.
     * @param id	The ID of the dockable that has to be created.
     * @param title	The title of the dialog that will be displayed.
     * @param icon	The icon that will be put on the button.
     * @param message	The message that will be displayed when the action is
     * performed.
     * @return	The dockable with a button as content.
     */
    private Dockable createButtonDockable(String id, String title, Icon icon, String message) {
        MessageAction action = new MessageAction(this, title, icon, message);
        ToolBarButton button = new ToolBarButton(action);
        ButtonDockable buttonDockable = new ButtonDockable(id, button);
        createDockableDragger(buttonDockable);
        return buttonDockable;
    }

    /**
     * Creates a dockable for a given content component.
     * @param id The ID of the dockable. The IDs of all dockables should be
     * different.
     * @param content The content of the dockable.
     * @param title The title of the dockable.
     * @param icon The icon of the dockable.
     * @return	The created dockable.
     * @throws IllegalArgumentException	If the given ID is null.
     */
    private Dockable createDockable(String id, Component content, String title, Icon icon, String description) {
        DefaultDockable dockable = new DefaultDockable(id, content, title, icon);
        dockable.setDescription(description);
        return dockable;
    }

    /**
     * Adds a drag listener on the content component of a dockable.
     */
    private void createDockableDragger(Dockable dockable) {
        DragListener dragListener = DockingManager.getDockableDragListenerFactory().createDragListener(dockable);
        dockable.getContent().addMouseListener(dragListener);
        dockable.getContent().addMouseMotionListener(dragListener);
    }
    
} // end class
