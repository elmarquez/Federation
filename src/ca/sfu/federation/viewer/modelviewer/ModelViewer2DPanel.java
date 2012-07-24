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
package ca.sfu.federation.viewer.modelviewer;

import ca.sfu.federation.Application;
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.ParametricModel;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * 2D model viewer.
 * @author dmarques
 */
public class ModelViewer2DPanel extends JPanel implements Observer {

    private static final Logger logger = Logger.getLogger(ModelViewer2DPanel.class.getName());

    private IContext context;
    private boolean lock;

    //--------------------------------------------------------------------------

    /**
     * ModelViewer2DPanel default constructor
     */
    public ModelViewer2DPanel() {
        // listen for changes on the application context
        ApplicationContext appcontext = Application.getContext();
        appcontext.addObserver(this);
    }

    /**
     * ModelViewer2DPanel default constructor
     * @param Context Context to draw
     */
    public ModelViewer2DPanel(IContext Context) {
        context = Context;
        // listen for changes on the application context
        ApplicationContext appcontext = Application.getContext();
        appcontext.addObserver(this);
    }

    //--------------------------------------------------------------------------

//    public void paint(Graphics g) {}

    /**
     * Update the scene.
     */
    public void updateScene() {
        ParametricModel model = Application.getContext().getModel();
        if (model != null) {
        }
    }

    /**
     * Update the display state.
     * @param o
     * @param arg
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            switch (eventId) {
                case ApplicationContext.EVENT_REPAINT_REQUEST:
                case ApplicationContext.MODEL_LOADED:
                    updateScene();
                    break;
                case ApplicationContext.MODEL_CLOSED:
                    // clear the panel
                    break;
            }
        }
    }

}
