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
package ca.sfu.federation.utils;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 * ImageIcon utilities.
 * @author dmarques
 */
public class ImageIconUtils {

    private static final Logger logger = Logger.getLogger(ImageIconUtils.class.getName());
    
    /**
     * Load image icon from path. Returns null if the icon could not be loaded.
     * @param path
     * @return 
     */
    public static ImageIcon loadImageIcon(String path) {
        return loadImageIcon(path,"");
    }

    /**
     * Load image icon from path. Returns null if the icon could not be loaded.
     * @param path
     * @param description
     * @return 
     */
    public static ImageIcon loadImageIcon(String path, String description) {
        URL imgURL = ImageIconUtils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            logger.log(Level.WARNING,"Could not load icon file {0}", path);
        }
        return null;
    }
    
} 
