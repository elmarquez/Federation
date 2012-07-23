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
package ca.sfu.federation;

import ca.sfu.federation.viewer.ApplicationFrame;
import com.javadocking.util.LookAndFeelUtil;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Application class.
 * @author Davis Marques
 */
public class Application implements Runnable {

    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static ResourceBundle resource = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
    private static ApplicationContext context = new ApplicationContext();
    private static ApplicationFrame frame;

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    //--------------------------------------------------------------------------

    /**
     * Get the primary application frame.
     */
    public static ApplicationFrame getApplicationFrame() {
        return frame;
    }

    /**
     * Get the application context
     * @return 
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * Get the localized resource bundle
     * @return
     */
    public static ResourceBundle getResource() {
        return resource;
    }

    /**
     * Application entry point
     * @param args
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Application());
    }

    /**
     * Start the application.
     */
    @Override
    public void run() {
        logger.log(Level.INFO,"Starting application");
        // set the look and feel options
        try {
            logger.log(Level.FINE,"Setting look and feel options");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // JPopupMenu.setDefaultLightWeightPopupEnabled(false);
            LookAndFeelUtil.removeAllSplitPaneBorders();
        } catch (Exception e) {
            String stack = ExceptionUtils.getFullStackTrace(e);
            logger.log(Level.WARNING,"Could not set look and feel options\n\n{0}",stack);
        }
        // create the main application frame 
        logger.log(Level.FINE,"Creating main application frame");
        frame = new ApplicationFrame();
        // show the application
        frame.setVisible(true);
    }

}  
