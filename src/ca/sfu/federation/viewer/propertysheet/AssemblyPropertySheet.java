/**
 * AssemblyPropertySheet.java
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author  Davis Marques
 * @version 0.1.0
 */
public class AssemblyPropertySheet extends JPanel implements Observer {

    //--------------------------------------------------------------------------
    // FIELDS
    
    private ParametricModel model;
    private Assembly target;

    private javax.swing.JTextField jtfCanonicalName;
    private javax.swing.JTextField jtfClass;
    private javax.swing.JTextField jtfDescription;
    private javax.swing.JTextField jtfName;
    private javax.swing.JTextField jtfParentContext;
    private javax.swing.JTextField jtfX;
    private javax.swing.JTextField jtfY;
    private javax.swing.JTextField jtfZ;
    private javax.swing.JLabel lblCanonicalName;
    private javax.swing.JLabel lblClass;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblINamedObject;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblParentContext;
    private javax.swing.JLabel lblX;
    private javax.swing.JLabel lblY;
    private javax.swing.JLabel lblZ;
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS

    /** 
     * ParametricModelSheet constructor.
     */
    public AssemblyPropertySheet() {
        lblINamedObject = new JLabel();
        lblName = new JLabel();
        lblDescription = new JLabel();
        lblCanonicalName = new JLabel();
        lblClass = new JLabel();
        lblParentContext = new JLabel();
        lblX = new JLabel();
        lblY = new JLabel();
        lblZ = new JLabel();
        jtfClass = new JTextField();
        jtfName = new JTextField();
        jtfCanonicalName = new JTextField();
        jtfDescription = new JTextField();
        jtfParentContext = new JTextField();
        jtfX = new JTextField();
        jtfY = new JTextField();
        jtfZ = new JTextField();

        lblINamedObject.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblINamedObject.setText("jLabel1");
        lblName.setText("Name");
        lblDescription.setText("Description");
        lblCanonicalName.setText("Canonical Name");
        lblClass.setText("Class");
        lblX.setText("X Coordinate");
        lblY.setText("Y Coordinate");
        lblZ.setText("Z Coordinate");
        lblParentContext.setText("Parent Context");

        jtfCanonicalName.setEditable(false);
        jtfClass.setEditable(false);
        jtfParentContext.setEditable(false);

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
                            .add(lblDescription)
                            .add(lblParentContext)
                            .add(lblX)
                            .add(lblZ)
                            .add(lblY))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jtfName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfClass, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jtfCanonicalName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                            .add(jtfParentContext, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfX, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfY, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfZ, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))))
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
                    .add(lblDescription)
                    .add(jtfDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblParentContext)
                    .add(jtfParentContext, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblX)
                    .add(jtfX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfY, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblY))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfZ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblZ))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        
        // get the current object
        this.model = ParametricModel.getInstance();
        
        // disable non editable fields
        this.jtfCanonicalName.setEditable(false);
        this.jtfClass.setEditable(false);
        this.jtfParentContext.setEditable(false);
        
        // set field values
        this.setValues();
        
        // add action listeners
        jtfName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNameActionListener(evt);
            }
        });
        jtfDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDescriptionActionListener(evt);
            }
        });
        jtfX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfXActionListener(evt);
            }
        });
        jtfY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfYActionListener(evt);
            }
        });
        jtfZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfZActionListener(evt);
            }
        });
    }
    
    //--------------------------------------------------------------------------
    // METHODS

    private void jtfDescriptionActionListener(java.awt.event.ActionEvent evt) {                                              
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

    private void jtfXActionListener(java.awt.event.ActionEvent evt) {                                    
        String command = evt.getActionCommand();
        System.out.println("INFO: AssemblySheet jtfXActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("X",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }                                   

    private void jtfYActionListener(java.awt.event.ActionEvent evt) {                                    
        String command = evt.getActionCommand();
        System.out.println("INFO: AssemblySheet jtfYActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("Y",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }                                   

    private void jtfZActionListener(java.awt.event.ActionEvent evt) {                                    
        String command = evt.getActionCommand();
        System.out.println("INFO: AssemblySheet jtfZActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("Z",evt.getActionCommand());
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
        this.jtfDescription.setText(this.target.getDescription());
        this.jtfCanonicalName.setText(this.target.getCanonicalName());
        this.jtfParentContext.setText(this.target.getContext().getName());
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
