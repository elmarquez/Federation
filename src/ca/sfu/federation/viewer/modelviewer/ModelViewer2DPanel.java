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

package ca.sfu.federation.viewer.modelviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.*;

/**
 *
 * @author dmarques
 */
public class ModelViewer2DPanel extends JPanel {

    /**
     * ModelViewer2DPanel default constructor
     */
    public ModelViewer2DPanel() {
        this.setLayout(new BorderLayout());
        // this.add(view,BorderLayout.CENTER);

        GWindow gwindow = new GWindow();
        GScene scene = new GScene(gwindow);
        this.add(gwindow.getCanvas(),BorderLayout.CENTER);

        GObject hw = new HelloWorld();
        // scene.add(hw);
        
        gwindow.startInteraction(new ZoomInteraction(scene));
    }

    public class HelloWorld extends GObject {
        private GSegment star_;

        public HelloWorld() {
        star_ = new GSegment();
        GStyle starStyle = new GStyle();
        starStyle.setForegroundColor (new Color (255, 0, 0));
        starStyle.setBackgroundColor (new Color (255, 255, 0));
        starStyle.setLineWidth (3);
        setStyle (starStyle);
        addSegment (star_);

        GText text = new GText ("HelloWorld", GPosition.MIDDLE);
        GStyle textStyle = new GStyle();
        textStyle.setForegroundColor (new Color (100, 100, 150));
        textStyle.setBackgroundColor (null);
        textStyle.setFont (new Font ("Dialog", Font.BOLD, 48));
        text.setStyle (textStyle);
        star_.setText (text);
        }

        /**
        * This method is called whenever the canvas needs to redraw this
        * object. For efficiency, prepare as much of the graphic object
        * up front (such as sub object, segment and style setup) and
        * set geometry only in this method.
        */
        public void draw() {
        star_.setGeometry (Geometry.createStar (220, 220, 200, 80, 15));
        }
    }

}
