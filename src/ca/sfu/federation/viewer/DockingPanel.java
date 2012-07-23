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

import ca.sfu.federation.utils.ImageIconUtils;
import ca.sfu.federation.viewer.console.ConsoleOutputPanel;
import ca.sfu.federation.viewer.graphviewer.GraphViewerPanel;
import ca.sfu.federation.viewer.modelviewer.ModelViewer2DPanel;
import ca.sfu.federation.viewer.projectexplorer.ProjectExplorerPanel;
import ca.sfu.federation.viewer.propertysheet.PropertySheetPanel;
import ca.sfu.federation.viewer.stackviewer.StackViewerPanel;
import ca.sfu.federation.viewer.toolbars.EditToolbar;
import ca.sfu.federation.viewer.toolbars.ModelingToolbar;
import ca.sfu.federation.viewer.toolbars.ProjectToolbar;
import ca.sfu.federation.viewer.toolbars.ViewToolbar;
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
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
        GraphViewerPanel graphViewer = new GraphViewerPanel();
        StackViewerPanel stackViewer = new StackViewerPanel();
        ModelViewer2DPanel modelViewer1 = new ModelViewer2DPanel();
        ProjectExplorerPanel explorerPanel = new ProjectExplorerPanel();
        PropertySheetPanel propertysheetPanel = new PropertySheetPanel();
        ConsoleOutputPanel outputPanel = new ConsoleOutputPanel();

        // create the dockables around the content components
        Dockable projectGraphDockable = createDockable("Project", graphViewer, "Project", ImageIconUtils.loadIconById("view-graphviewer-icon"), "Project");
        Dockable modelView1Dockable = createDockable("Model View 1", modelViewer1, "Model View 1", ImageIconUtils.loadIconById("view-graphviewer-icon"), "Model View 1");
        Dockable stackViewDockable = createDockable("Stack", stackViewer, "Stack", ImageIconUtils.loadIconById("view-stackviewer-icon"), "Context Dependency Stack");
        Dockable explorerDockable = createDockable("Project", explorerPanel, "Project", ImageIconUtils.loadIconById("view-explorer-icon"), "Project Explorer");
        Dockable outputDockable = createDockable("Output", outputPanel, "Output", ImageIconUtils.loadIconById("view-outputwindow-icon"), "Output");
        Dockable propertySheetDockable = createDockable("Properties", propertysheetPanel, "Properties", ImageIconUtils.loadIconById("view-propertysheet-icon"), "Properties");

        // add dockables to list
        dockables.add(projectGraphDockable);
        dockables.add(modelView1Dockable);
        dockables.add(stackViewDockable);
        dockables.add(explorerDockable);
        dockables.add(outputDockable);
        dockables.add(propertySheetDockable);

        // add actions to dockables
        for (int index = 0; index < dockables.size(); index++) {
            // dockables[index] = addLimitActions(dockables[index]);
            addAllActions(dockables.get(index));
        }

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
        centerTabbedDock.addDockable(projectGraphDockable, new Position(0));
        centerTabbedDock.addDockable(modelView1Dockable, new Position(1));
        
        bottomTabbedDock.addDockable(outputDockable, new Position(0));
        leftTabbedDock.addDockable(explorerDockable, new Position(0));
        leftTabbedDock.addDockable(stackViewDockable, new Position(1));
        rightTabbedDock.addDockable(propertySheetDockable, new Position(0));

        leftTabbedDock.setSelectedDockable(explorerDockable);
        centerTabbedDock.setSelectedDockable(projectGraphDockable);

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

        // add top edge horizontal toolbars
        compositeToolBarDock1.addChildDock(new ProjectToolbar(), new Position(0));
        compositeToolBarDock1.addChildDock(new EditToolbar(), new Position(1));
        compositeToolBarDock1.addChildDock(new ViewToolbar(), new Position(2));
        compositeToolBarDock1.addChildDock(new ModelingToolbar(), new Position(3));

        // add the paths of the docked dockables to the model with the docking paths
        Iterator<Dockable> it = dockables.iterator();
        while (it.hasNext()) {
            Dockable d = it.next();
            addDockingPath(d);
        }
        
        // force the graph panel to repaint itself
        graphViewer.repaint();
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

}
