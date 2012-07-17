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
package ca.sfu.federation.action;

import ca.sfu.federation.ApplicationContext;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Launch a web browser to show the online help web site.
 * @author dmarques
 */
public class ShowWebSiteAction extends AbstractAction {

    private static final Logger logger = Logger.getLogger(ShowWebSiteAction.class.getName());

    public ShowWebSiteAction() {
        super("Project Web Site", null);
        this.putValue(Action.LONG_DESCRIPTION, "Project Web Site");
        this.putValue(Action.SHORT_DESCRIPTION, "Project Web Site");
    }
    
    /**
     * Handle action performed event.
     * @param ae Event
     */
    public void actionPerformed(ActionEvent ae) {
        try {
            URI uri = new URI(ApplicationContext.PROJECT_WEBSITE_URL);
            // open the default web browser for the HTML page
            logger.log(Level.INFO,"Opening desktop browser to {0}",uri.toString()); 
            Desktop.getDesktop().browse(uri);
        } catch (Exception ex) {
            String stack = ExceptionUtils.getFullStackTrace(ex);
            logger.log(Level.WARNING,"Could not open browser for URL {0}\n\n{1}", 
                    new Object[]{ApplicationContext.PROJECT_WEBSITE_URL,stack});
        } 
    }
    
}
