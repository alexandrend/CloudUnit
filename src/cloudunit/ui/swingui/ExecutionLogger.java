/*
 * Copyright (c) 2002-2005 Universidade Federal de Campina Grande and Universidade Federal da Paraiba
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 * For more information: http://gridunit.sourceforge.net
 */
package cloudunit.ui.swingui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.smartfrog.services.junit.TestInfo;

import cloudunit.framework.CloudTestListener;

import junit.framework.Test;
import junit.framework.TestCase;


/**
 * 
 * Description: A graphical component to present the events fired during the normal execution of the
 * application
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class ExecutionLogger extends JPanel implements CloudTestListener {
    
	
	private static final long serialVersionUID = 1L;

	/**
	 * Time format. 
	 */
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
    
    /**
     * A graphical table.
     */
    private JTable theTable;
    
    /**
     * A frame in which this component will be coupled.
     */
    private JFrame parent;
    
    /**
     * @author Alexandre N—brega Duarte - alexandre@ci.ufpb.br
     * 
     * Description: This inner class implements a customized TableModel.
     * 
     * @version 1.0 Date: 23/05/2005
     * 
     * Copyright (c) 2002-2005 Universidade Federal de Campina Grande
     */
   	class MyTableModel extends AbstractTableModel {

        
		private static final long serialVersionUID = 1L;
		private String [] colums = { "Time" , "Event" };
        private Vector<Row> data = new Vector<Row>();
        
        
        public int getRowCount() {
            return data.size();
        }

        public int getColumnCount() {
            return colums.length;
        }
        

        public String getColumnName( int col ) {
            return colums[ col ];
        }
        
        public boolean isCellEditable( int row, int col ) {
            return false;
        }
        
        public Object getValueAt(int row, int col) {
            
            return data.elementAt( row ).getColumn( col );
            
        }
        
        public void insertRow( Row row ) {
            
            data.add( row );
            this.fireTableRowsInserted( data.size() - 2 , data.size() - 1);

        }
        
        public Class<?> getColumnClass( int col ) {
            if( col == 0 ) return Date.class;
            else return JLabel.class;
        }
    };
    
    
    /**
     * @author Alexandre N—brega Duarte - alexandre@ci.ufpb.br
     * 
     * Description: This inner class implements a customized row.
     * 
     * @version 1.0 Date: 23/05/2005
     * 
     * Copyright (c) 2002-2005 Universidade Federal de Campina Grande
     */
    private class Row {
        
    	/**
    	 * Array of objects representing columns.
    	 */
        private Object columns[];
        
        /**
         * Full constructor for a row.
         * @param data Data will showed at second column.
         */
        public Row( JLabel  data ){
            this.columns = new Object[2];
            columns[0] = new Date( System.currentTimeMillis());
            columns[1] = data;
        }
        
        /**
         * Returns a object contained in a column.
         * @param col A column
         * @return A object.
         */
        public Object getColumn( int col ) {
            return columns[col];
        }
        
    }
    
    /**
     * A TableModel
     */
    private MyTableModel model;
    
    /**
     * Store time, in milliseconds, logger starts.
     */
    private long start;
    
    /**
     * Full constructor for ExecutionLogger.
     * @param parent The component in which this panel will be coupled.
     */
    public ExecutionLogger(JFrame parent){
         
        this.parent = parent;
        setBackground( Color.white );
        setLayout( new BorderLayout() );
        
        model = new MyTableModel();
        
        theTable = new JTable( model );
        
        theTable.setDefaultRenderer( JLabel.class,  new TableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
               return  (JLabel)value;
                
            }
            
        });
        theTable.setDefaultRenderer( Date.class,  new TableCellRenderer() {

            public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
                return new JLabel( df.format( (Date) arg1));
            }
            
        });
        
        
        JLabel l = new JLabel( "GridUnit started");
        l.setIcon( IconManager.getIcon( IconManager.infoIcon));
        model.insertRow( new Row( l ));
        
                
        theTable.getColumnModel().getColumn(0).setMaxWidth(100);
        theTable.getColumnModel().getColumn(0).setMinWidth(100);
        theTable.getColumnModel().getColumn(0).setResizable(false);
        
        setLayout( new BorderLayout() );
        
        JScrollPane js = new JScrollPane(theTable);
        js.setAutoscrolls(true);
        add( js , BorderLayout.CENTER);
        add( createSaveButton(), BorderLayout.SOUTH );
        
           
    }
    
    /**
     * Creates the save button.
     * @return A component.
     */
    private JPanel createSaveButton() {
        JButton save = new JButton("Save");
        
        save.addActionListener( new ActionListener() {
            
            
            public void actionPerformed( ActionEvent e ) {
                new SaveDialog(theTable, parent);
            }
            
        });
        
        JPanel p = new JPanel();
        p.setLayout( new GridLayout(1,5) );
        p.add( new JPanel());
        p.add( new JPanel());
        p.add( save , BorderLayout.CENTER );
        p.add( new JPanel());
        p.add( new JPanel());
        return p;
        
        
    }

    /**
     * Called when a test is finished.
     * @param testInfo The test info.
     */
    public synchronized void testFinished( TestInfo testInfo ) {
        JLabel l = new JLabel( "Test " + testInfo.getClassname() + "." + testInfo.getText() + " finished at " + testInfo.getHostname());
        l.setIcon( IconManager.getIcon( IconManager.finishedIcon));
        model.insertRow( new Row( l ));
        
        repaint();
    }

    /**
     * Called when a test has been started
     * @param testInfo The test info.
     */
    public synchronized void testStarted( Test test) {
        JLabel l = new JLabel( "Test " + test.getClass().getName() + "." + ( ( TestCase) test ).getName() + " execution started");
        l.setIcon( IconManager.getIcon( IconManager.runningIcon));
        model.insertRow( new Row( l ));
        
        repaint();
    }

    /**
     * Called when a test has failed due to an unsatisfied assertion
     * @param testInfo The test info.
     */
    public synchronized void testFailed( TestInfo testInfo ) {
        JLabel l = new JLabel( "Test " + testInfo.getClassname() + "." + testInfo.getText() + " has failed at " + testInfo.getHostname());
        l.setIcon( IconManager.getIcon( IconManager.failureIcon));
        model.insertRow( new Row( l ));
        
        repaint();
        
    }

    /**
     * Called when a a test has failed due to an unanticipated error.
     * @param testInfo The test info.
     */
    public synchronized void testError( TestInfo testInfo ) {
        JLabel l = new JLabel( "Test " +  testInfo.getClassname() + "." + testInfo.getText()+ " has failed at " +  testInfo.getHostname() + " due to an unexpected error");
        l.setIcon( IconManager.getIcon( IconManager.errorIcon));
        model.insertRow( new Row( l ));
        
        repaint();
    }

    /**
     * Called when the test phase has been started.
     *
     */
    public void testPhaseFinished() {
                
        JLabel l = new JLabel( "Test phase finished. Total time: " + ( ( System.currentTimeMillis() - start )/1000 ) + " s");
        l.setIcon( IconManager.getIcon( IconManager.infoIcon));
        model.insertRow( new Row( l ));
        
        repaint();
    }

    /**
     * Called when all tests where executed (either successfully or unsuccessfully).
     *
     */
    public void testPhaseStarted() {
        start = System.currentTimeMillis();
        
        JLabel l = new JLabel( "Test phase started" );
        l.setIcon( IconManager.getIcon( IconManager.infoIcon));
        model.insertRow( new Row( l ));
        
        repaint();
    }
    
}
