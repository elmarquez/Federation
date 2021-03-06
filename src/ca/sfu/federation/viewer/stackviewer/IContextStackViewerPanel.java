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

package ca.sfu.federation.viewer.stackviewer;

import ca.sfu.federation.Application;
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.ParametricModel;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.openide.util.Utilities;

/**
 * Renders an order list of predecessor IContext's for the current context in 
 * focus.
 * @author Davis Marques
 */
public class IContextStackViewerPanel extends JPanel implements Observer {
    
    private static final Logger logger = Logger.getLogger(IContextStackViewerPanel.class.getName());

    private IContext context;
    
    private int backgroundmode;
    private Image thumbnail;
    
    //--------------------------------------------------------------------------

    /**
     * StackViewerPanel constructor.
     */
    public IContextStackViewerPanel() {
        this.setLayout(new BorderLayout());
        // listen for changes on model
        ParametricModel model = Application.getContext().getModel();
        model.addObserver(this);
        // styles
        this.backgroundmode = 1;
        // load thumbnail
        ResourceBundle config = ResourceBundle.getBundle(ApplicationContext.APPLICATION_PROPERTIES);
        String path = config.getString("view-stackviewer-thumbnail");
        this.thumbnail = Utilities.loadImage(path);
    }
    
    //--------------------------------------------------------------------------

    
    /**
     * Draw panel background.
     * @param G Graphics canvas.
     */
    private void drawBackground(Graphics G) {
        Graphics2D g = (Graphics2D) G;
        Color gray = ApplicationContext.BACKGROUND_LIGHT2;
        Color gray2 = ApplicationContext.BACKGROUND_MEDIUM2;
        switch(this.backgroundmode) {
            case 0:
                GradientPaint gradient = new GradientPaint(0,0,gray,0,(float) this.getSize().getWidth(),gray2);        
                g.setPaint(gradient);
                g.fillRect(0,0,(int)this.getSize().getWidth(),(int)this.getSize().getHeight());
                break;
            case 1:
                g.setPaint(gray2);
                g.fillRect(0,0,(int)this.getSize().getWidth(),(int)this.getSize().getHeight());
                break;
        }
    }
    
    /**
     * Draw the scenario block.
     * @param G Graphics canvas.
     * @param X X coordinate for top left of block.
     * @param Y Y coordinate for top left of block.
     * @param Height Height of block.
     * @param LeadingLine
     * @param BackgroundFill
     * @param TrailingLine
     */
    private void drawScenarioBlock(Graphics G, int X, int Y, int Height, String Label, boolean LeadingLine, boolean BackgroundFill, boolean TrailingLine) {
        Graphics2D g = (Graphics2D) G;
        // set dimension
        int offset = 5;
        int text_offset = 95;
        int text_height = 5;
        int width = (int) this.getSize().getWidth() - (offset*2);
        // draw box
        if (BackgroundFill) {
            Color c = new Color(95,95,95);
            g.setPaint(c);
            g.fillRect(X+offset,Y+offset,width+1,Height);
            c = Color.lightGray;
            g.setPaint(c);
            int x1 = X+offset;
            int x2 = X+width+offset;
            int y1 = Y+offset;
            int y2 = Y+offset+Height;
            g.draw(new Line2D.Double(x1,y1,x2,y1));
            g.draw(new Line2D.Double(x1,y2,x2,y2));
        } else {
            if (LeadingLine) {
                Color c = Color.darkGray;
                g.setPaint(c);
                g.draw(new Line2D.Double(X+offset,Y+offset,X+width+offset,Y+offset));
            }
            if (TrailingLine) {
                Color c = Color.darkGray;
                g.setPaint(c);
                g.draw(new Line2D.Double(X+offset,Y+Height+offset,X+width+offset,Y+Height+offset));
            }
        }
        // draw thumbnail
        BufferedImage buffered = toBufferedImage(this.thumbnail);
        g.drawImage(buffered,null,X+(offset*2),Y+(offset*2));
        // draw label
        Color c = Color.white;
        g.setPaint(c);
        g.drawString(Label,X+offset+text_offset,Y+offset+(Height/2)+text_height);
    }
    
    /**
     * This method returns true if the specified image has transparent pixels
     * Excerpted from: http://javaalmanac.com/egs/java.awt.image/HasAlpha.html
     * @param image
     * @return True if the image has an alpha channel, false otherwise.
     */
    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }
    
        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
         PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
    
        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
    
    @Override
    public void paint(Graphics G) {
        Graphics2D g = (Graphics2D) G;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // if the current context has changed
        IContext currentcontext = (IContext) Application.getContext().getViewState(ApplicationContext.VIEWER_CURRENT_CONTEXT);
        if (this.context != currentcontext) {
            if (this.context != null) {
                if (this.context instanceof Observable && !(this.context instanceof ParametricModel)) {
                    Observable o = (Observable) this.context;
                    o.deleteObserver(this);
                }
            }
            this.context = currentcontext;
        }
        // draw background
        // this.drawBackground(g);
        // draw model objects
        if (this.context != null) {
            ArrayList parents = (ArrayList) this.context.getParents();
            int y = 0;
            boolean leading = false;
            boolean fill = false;
            boolean trailing = false;
            int leadingBlock = -1;
            Iterator<IContext> it = parents.iterator();
            int count = 0;
            while (it.hasNext()) {
                IContext parent = it.next();
                if (count+1==parents.size()) {
                    leading = false;
                    fill = true;
                    trailing = false;
                }
                String name = parent.getName();
                this.drawScenarioBlock(g,0,y,60,name,leading,fill,trailing);
                y += 60;
                leading = true;
                fill = false;
                trailing = true;
            }
        }
    }
    
    /**
     * This method returns a buffered image with the contents of an image.
     * Excertped from: http://javaalmanac.com/egs/java.awt.image/Image2Buf.html?l=rel
     * @param image
     * @return Buffered image.
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        // return image
        return bimage;
    }

    /**
     * Update this object when an Observed object changes state.
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            logger.log(Level.INFO,"IContextStackViewerPanel received event notification id {0}", eventId);
            switch (eventId) {
                case ApplicationContext.EVENT_CONTEXT_CHANGE:
                case ApplicationContext.EVENT_ELEMENT_ADD:
                case ApplicationContext.EVENT_ELEMENT_CHANGE:
                case ApplicationContext.EVENT_ELEMENT_DELETED:
                case ApplicationContext.EVENT_ELEMENT_RENAME:
                    this.repaint();
                    break;
            }
        }
    }

} 
