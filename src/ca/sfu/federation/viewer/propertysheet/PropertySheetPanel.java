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
package ca.sfu.federation.viewer.propertysheet;

import ca.sfu.federation.Application;
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.Assembly;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.Scenario;
import ca.sfu.federation.model.geometry.CoordinateSystem;
import ca.sfu.federation.model.geometry.Line;
import ca.sfu.federation.model.geometry.Plane;
import ca.sfu.federation.model.geometry.Point;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

/**
 * Property sheet panel.
 * @author dmarques
 */
public class PropertySheetPanel extends javax.swing.JPanel implements Observer {

    /**
     * PropertySheetPanel default constructor.
     */
    public PropertySheetPanel() {
        initComponents();
        Application.getContext().addObserver(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    /**
     * Build property sheet.
     */
    private void buildPropertySheet() {
        INamed named = (INamed) Application.getContext().getViewState(ApplicationContext.VIEWER_SELECTION);
        JPanel panel = null;
        if (named instanceof ParametricModel) {
            panel = new ParametricModelPropertySheetPanel();
        } else if (named instanceof Scenario) {
            panel = new ScenarioPropertySheet();
        } else if (named instanceof Assembly) {
            panel = new AssemblyPropertySheet();
        } else if (named instanceof CoordinateSystem) {
            panel = new CoordinateSystemPropertySheet();
        } else if (named instanceof Point) {
            panel = new PointPropertySheet();
        } else if (named instanceof Line) {
            panel = new LinePropertySheet();
        } else if (named instanceof Plane) {
            panel = new PlanePropertySheet();
        }
        if (named != null) {
            jScrollPane1.getViewport().removeAll();            
            jScrollPane1.setViewportView(panel);
        }
    }

    /**
     * Handle update event.
     * @param o Observable
     * @param arg Argument
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            switch (eventId) {
                case ApplicationContext.MODEL_CLOSED:
                    this.jScrollPane1.getViewport().removeAll();
                    break;
                case ApplicationContext.MODEL_LOADED:
                case ApplicationContext.EVENT_SELECTION_CHANGE:
                    buildPropertySheet();
                    break;
            }
        }
    }

}
