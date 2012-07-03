/**
 * BehaviorPropertySheet.java
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

package ca.sfu.federation.viewer.propertysheet;

import ca.sfu.federation.model.Assembly;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.ConfigManager;
import com.developer.rose.BeanProxy;
import java.beans.IntrospectionException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

/**
 * @author  Davis Marques
 * @version 0.1.0
 */
public class BehaviorPropertySheet extends JPanel implements Observer {

    //--------------------------------------------------------------------------
    // FIELDS
    
    private ParametricModel model;
    private Assembly target;

    private javax.swing.JTextField jtfAction;
    private javax.swing.JTextField jtfCanonicalName;
    private javax.swing.JTextField jtfClass;
    private javax.swing.JTextField jtfCondition;
    private javax.swing.JTextField jtfName;
    private javax.swing.JLabel lblAction;
    private javax.swing.JLabel lblCanonicalName;
    private javax.swing.JLabel lblClass;
    private javax.swing.JLabel lblCondition;
    private javax.swing.JLabel lblINamedObject;
    private javax.swing.JLabel lblName;

    //--------------------------------------------------------------------------
    // CONSTRUCTORS

    /** 
     * ParametricModelSheet constructor.
     */
    public BehaviorPropertySheet() {
        lblINamedObject = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblCondition = new javax.swing.JLabel();
        lblCanonicalName = new javax.swing.JLabel();
        lblClass = new javax.swing.JLabel();
        lblAction = new javax.swing.JLabel();
        
        jtfCondition = new javax.swing.JTextField();
        jtfClass = new javax.swing.JTextField();
        jtfName = new javax.swing.JTextField();
        jtfCanonicalName = new javax.swing.JTextField();
        jtfAction = new javax.swing.JTextField();

        lblINamedObject.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblINamedObject.setText("jLabel1");
        lblName.setText("Name");
        lblCondition.setText("Condition");
        lblCanonicalName.setText("Canonical Name");
        lblClass.setText("Class");
        lblAction.setText("Action");

        this.jtfCanonicalName.setEditable(false);
        this.jtfClass.setEditable(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblINamedObject)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblCanonicalName)
                            .add(lblClass)
                            .add(lblName)
                            .add(lblCondition)
                            .add(lblAction))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jtfName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfClass, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfCondition, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jtfCanonicalName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                            .add(jtfAction, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(lblINamedObject)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblClass))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCanonicalName)
                    .add(jtfCanonicalName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCondition)
                    .add(jtfCondition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblAction)
                    .add(jtfAction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        // get the current object
        this.model = ParametricModel.getInstance();
        // set field values
        this.setValues();
        // add action listeners
        jtfName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNameActionListener(evt);
            }
        });
        jtfCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfConditionActionListener(evt);
            }
        });
    }
    
    //--------------------------------------------------------------------------
    // METHODS

    private void jtfConditionActionListener(java.awt.event.ActionEvent evt) {
        String command = evt.getActionCommand();
        System.out.println("INFO: AssemblySheet jtfDescriptionActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("description",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jtfNameActionListener(java.awt.event.ActionEvent evt) {                                       
        String command = evt.getActionCommand();
        System.out.println("INFO: AssemblySheet jtfNameActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("name",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }                                      
    
    private void setValues() {
        // target
        this.target = (Assembly) this.model.getViewState(ConfigManager.VIEWER_SELECTION);
        // listen for changes on the target
        if (this.target instanceof Observable) {
            Observable o = (Observable) this.target;
            o.addObserver(this);
        }
        // set field values
        this.lblINamedObject.setText(this.target.getName());
        this.jtfClass.setText(this.target.getClass().toString());
        this.jtfName.setText(this.target.getName());
        this.jtfCanonicalName.setText(this.target.getCanonicalName());
    }
    
    /**
     * Update event.
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            System.out.println("INFO: AssemblySheet received event notification id " + eventId);
            switch (eventId) {
                case ConfigManager.EVENT_PROPERTY_CHANGE:
                    System.out.println("INFO: AssemblySheet fired property change event.");
                    this.setValues();
                    break;
            }
        }
    }
    
} // end class
