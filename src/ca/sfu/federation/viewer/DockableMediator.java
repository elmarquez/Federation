/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.sfu.federation.viewer;

import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockableState;
import com.javadocking.dockable.action.DefaultDockableStateAction;
import com.javadocking.event.DockingEvent;
import com.javadocking.event.DockingListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Action;
import javax.swing.JMenuItem;

/**
 * A listener that listens when menu items with dockables are selected and
 * deselected. It also listens when dockables are closed or docked.
 * @author dmarques
 */
public class DockableMediator implements ItemListener, DockingListener {

    private Dockable dockable;
    private Action closeAction;
    private Action restoreAction;
    private JMenuItem dockableMenuItem;

    //--------------------------------------------------------------------------
    
    /**
     * DockableMediator constructor
     * @param dockable
     * @param dockableMenuItem 
     */
    public DockableMediator(Dockable dockable, JMenuItem dockableMenuItem) {
        this.dockable = dockable;
        this.dockableMenuItem = dockableMenuItem;
        closeAction = new DefaultDockableStateAction(dockable, DockableState.CLOSED);
        restoreAction = new DefaultDockableStateAction(dockable, DockableState.NORMAL);
    }

    //--------------------------------------------------------------------------

    /**
     * Docking changed event handler.
     * @param dockingEvent 
     */
    @Override
    public void dockingChanged(DockingEvent dockingEvent) {
        if (dockingEvent.getDestinationDock() != null) {
            dockableMenuItem.removeItemListener(this);
            dockableMenuItem.setSelected(true);
            dockableMenuItem.addItemListener(this);
        } else {
            dockableMenuItem.removeItemListener(this);
            dockableMenuItem.setSelected(false);
            dockableMenuItem.addItemListener(this);
        }
    }

    /**
     * Docking will change event handler.
     * @param dockingEvent 
     */
    @Override
    public void dockingWillChange(DockingEvent dockingEvent) {
    }
    
    /**
     * Item state changed event handler.
     * @param itemEvent 
     */
    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        dockable.removeDockingListener(this);
        if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
            // Close the dockable.
            closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
        } else {
            // Restore the dockable.
            restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
        }
        dockable.addDockingListener(this);
    }

} // end class
