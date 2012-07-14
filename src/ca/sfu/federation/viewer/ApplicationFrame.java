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
package ca.sfu.federation.viewer;

import ca.sfu.federation.Application;
import ca.sfu.federation.ApplicationContext;
import com.javadocking.util.LookAndFeelUtil;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;

/**
 * Main application frame.
 * @author Davis Marques
 */
public class ApplicationFrame extends JFrame implements Observer {

    private static ApplicationFrame instance;
    private DockingPanel dockingpanel;

    private static final Logger logger = Logger.getLogger(ApplicationFrame.class.getName());

    //--------------------------------------------------------------------------

    /**
     * ApplicationFrame default constructor.
     */
    public ApplicationFrame() {
        // define the layout and add content
        Container contentpane = getContentPane();
        contentpane.setLayout(new BorderLayout());
        contentpane.add(new DockingPanel(this),BorderLayout.CENTER);
        setTitle(Application.getResource().getString("application-title"));
        setJMenuBar(new ApplicationMenuBar());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
        // listen for changes on the application context
        Application.getContext().addObserver(this);
        // pack the frame, then set the starting size to screen dimensions
        pack();
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //--------------------------------------------------------------------------

    /**
     * Update event.
     * @param o Observable object.
     * @param arg Update argument.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            logger.log(Level.INFO,"ApplicationFrame received event \'{0}\'", eventId);
            String appname = Application.getResource().getString("application-title");
            switch (eventId) {
                // update the frame title
                case ApplicationContext.MODEL_LOADED:
                case ApplicationContext.MODEL_RENAMED:
                    String modelname = Application.getContext().getModel().getName();
                    setTitle(appname + " - " + modelname);
                    break;
                case ApplicationContext.MODEL_CLOSED:
                    setTitle(appname);
                    break;
            }
        }
    }

} // end class