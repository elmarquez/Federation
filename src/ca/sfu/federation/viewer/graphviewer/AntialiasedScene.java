/**
 * AntialiasedScene.java
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

package ca.sfu.federation.viewer.graphviewer;

import java.awt.RenderingHints;
import org.netbeans.api.visual.widget.Scene;

/**
 * @author Davis Marques
 * @version 0.1.0
 */
public class AntialiasedScene extends Scene {
    
    //--------------------------------------------------------------------------


    //--------------------------------------------------------------------------

    
    /**
     * AntialiasedScene constructor.
     */
    public AntialiasedScene() {
    }
    
    //--------------------------------------------------------------------------

    
    /**
     * Paint canvas.
     */
    public void paintChildren () {
        Object anti = getGraphics().getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        Object textAnti = getGraphics().getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintChildren();
        getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, anti);
        getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAnti);
    }
    
} 
