/**
 * ModelViewer3DPanel.java
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

package ca.sfu.federation.viewer.modelviewer;

import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.viewer.action.ModelViewerUpdateThumbnailAction;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * IContext 3D viewer panel.
 * @author Davis Marques
 * @version 0.1.0
 */
public class ModelViewerPanel extends JPanel implements MouseListener {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private ParametricModel model;
    private IContext context;
    private ModelViewerCanvas3D canvas;
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * ModelViewerIContextAdapter constructor.
     */
    public ModelViewerPanel() {
        // init
        this.setLayout(new BorderLayout());

        // create canvas3d
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        this.canvas = new ModelViewerCanvas3D(config);
        this.add(canvas,BorderLayout.CENTER);
        
        // add mouselistener, popup menu to canvas
        this.canvas.addMouseListener(this);
        this.canvas.add(new Canvas3DPopupMenu());
        
        // add a toolbar to the panel
        JToolBar toolbar = new JToolBar("3D Viewer Tools",JToolBar.HORIZONTAL);
        toolbar.setFloatable(true);
        JButton btn = new JButton();
        btn.setAction(new ModelViewerUpdateThumbnailAction("Set Thumbnail",null,"Set Thumbnail",null,this.canvas));
        toolbar.add(btn);
        this.add(toolbar,BorderLayout.NORTH);
    }
    
    //--------------------------------------------------------------------------
    // METHODS
    
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Mouse pressed event.
     * @param e Mouse event.
     */
    public void mousePressed(MouseEvent e) {
    }
    
    /**
     * Mouse released event.
     * @param e Mouse event.
     */
    public void mouseReleased(MouseEvent e) {
    }
    
    /**
     * Mouse entered event.
     * @param e Mouse event.
     */
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     * Mouse exited event.
     * @param e Mouse event.
     */
    public void mouseExited(MouseEvent e) {
    }
    
} // end class
