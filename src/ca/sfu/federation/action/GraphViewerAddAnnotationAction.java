/**
 * ViewerAddAnnotationAction.java
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

package ca.sfu.federation.action;

import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.viewer.graphviewer.MutableSceneModel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.widget.Scene;

/**
 * Add an annotation to the Scenario graph.  We assume that for this action to 
 * occur, an instance of the graph viewer must already be running.
 * @author Davis Marques
 * @version 0.1.0
 */
public class GraphViewerAddAnnotationAction extends AbstractAction {
    
    //--------------------------------------------------------------------------

    
    private Scene scene;       // the scenario graph scene representation
    
    //--------------------------------------------------------------------------

    
    /**
     * GraphViewerAddAnnotationAction constructor.
     * 
     * @param Name The fully qualified context name to set as the current context.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     * @param MyScene Scene target.
     */
    public GraphViewerAddAnnotationAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId, Scene MyScene) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
        ParametricModel model = ParametricModel.getInstance();
        this.scene = MyScene;
    }
    
    //--------------------------------------------------------------------------


    /**
     * Set the current context.
     */
    public void actionPerformed(ActionEvent e) {
        String text = JOptionPane.showInputDialog("Enter annotation text");
        Point location = (Point) ParametricModel.getInstance().getViewState(ApplicationContext.VIEWER_LAST_MOUSERELEASE);
        if (this.scene instanceof MutableSceneModel) {
            MutableSceneModel myscene = (MutableSceneModel) this.scene;
            myscene.addAnnotation(text,location);
        }
    }
    
} // end class
