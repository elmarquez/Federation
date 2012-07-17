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
package ca.sfu.federation.viewer;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author dmarques
 */
public class StatusBarPanel extends JLabel {

    private final int MIN_HEIGHT = 24;
    
    public StatusBarPanel() {
        super();
        int inset = 10;
        Border b = BorderFactory.createMatteBorder(1,0,0, 0, Color.LIGHT_GRAY);
        this.setBorder(b);
        this.getInsets().set(inset,inset,inset,inset*5);
        setPreferredSize(new Dimension(200, MIN_HEIGHT));
        this.setText("Ready");
    }
    
}
