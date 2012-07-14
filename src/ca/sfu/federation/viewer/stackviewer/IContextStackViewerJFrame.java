/**
 * IContextStackViewerJFrame.java
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

package ca.sfu.federation.viewer.stackviewer;

import ca.sfu.federation.viewer.stackviewer.IContextStackViewerPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;

/**
 * IContext stack viewer.
 * @author Davis Marques
 * @version 0.1.0
 */
public class IContextStackViewerJFrame extends JFrame {
    
    //--------------------------------------------------------------------------

    
    //--------------------------------------------------------------------------

    
    /**
     * IContextStackViewerJFrame constructor.
     */
    public IContextStackViewerJFrame() {
        Container contentpane = this.getContentPane();
        contentpane.setLayout(new BorderLayout());
        IContextStackViewerPanel panel = new IContextStackViewerPanel();
        contentpane.add(panel,BorderLayout.CENTER);
        this.setTitle("IContext Stack Viewer");
        // prepare and make visible
        this.pack();
        this.setVisible(true);
        this.setSize(300,600);        
        this.validate();
    }
    
    //--------------------------------------------------------------------------

    
} // end class
