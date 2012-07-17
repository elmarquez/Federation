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
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.ParametricModel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * @author Davis Marques
 * @version 0.1.0
 */
public class IContextGraphViewerJFrame extends JFrame {
    
    //--------------------------------------------------------------------------

    
    private ParametricModel model;
    
    //--------------------------------------------------------------------------

    
    /**
     * IContextGraphViewerJFrame constructor.
     */
    public IContextGraphViewerJFrame() {
        Container contentpane = this.getContentPane();
        contentpane.setLayout(new BorderLayout());
        IContextGraphViewerPanel panel = new IContextGraphViewerPanel();
        contentpane.add(panel,BorderLayout.CENTER);
        if (!Application.getContext().getViewState().containsKey(ApplicationContext.VIEWER_CURRENT_CONTEXT)) {
            this.model.setViewState(ApplicationContext.VIEWER_CURRENT_CONTEXT,this.model);
        }
        IContext context = (IContext) Application.getContext().getViewState(ApplicationContext.VIEWER_CURRENT_CONTEXT);
        this.setTitle("IContext Graph Viewer - " + context.getName());
        this.pack();
        this.setVisible(true);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());        
    }
    
    //--------------------------------------------------------------------------


} 
