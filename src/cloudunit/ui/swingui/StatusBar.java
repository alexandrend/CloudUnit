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

 * 
 */
package cloudunit.ui.swingui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.smartfrog.services.junit.TestInfo;

import cloudunit.framework.CloudTestListener;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * 
 * Description: This class presents some information in the form of a status bar for the main window.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class StatusBar extends JPanel implements CloudTestListener {

    private static final long serialVersionUID = 1L;

	private JLabel message;	
    
    private JProgressBar status;
    
    private JLabel runs;
    private int runsC;
   
    private JLabel errors;
    private int errorsC;
        
    private JLabel failures;
    private int failuresC;

    private long start;
    
    private int ntests;
    
    public StatusBar(int ntests ) {
                
        this.ntests = ntests;
        message = new JLabel( "CloudUnit started");
        message.setIcon( IconManager.getIcon( IconManager.infoIcon));
        
        runs = new JLabel( "0/" + ntests);
        runsC = 0;
        runs.setIcon( IconManager.getIcon( IconManager.finishedIcon ));
        errors  = new JLabel( "0");
        errorsC = 0;
        errors.setIcon( IconManager.getIcon( IconManager.errorIcon));
        failures = new JLabel( "0");
        failuresC = 0;
        failures.setIcon( IconManager.getIcon( IconManager.failureIcon));
        
        status = new JProgressBar();
        
        status.setForeground( Color.GREEN );
        status.setMaximum(ntests);
        status.setMinimum(0);
        status.setValue(0);
        status.setStringPainted(true);
        status.setForeground( Color.green );
        
        runs.setBorder( BorderFactory.createLoweredBevelBorder());
        message.setBorder( BorderFactory.createLoweredBevelBorder());
        errors.setBorder( BorderFactory.createLoweredBevelBorder());
        failures.setBorder( BorderFactory.createLoweredBevelBorder());
        
        setLayout( new GridLayout() );
        
        add( message , BorderLayout.CENTER );
        
        JPanel jp1 = new JPanel();
        
        jp1.setLayout( new GridLayout(1 ,2 ) );
        
        JPanel jp = new JPanel();
        jp.setLayout( new GridLayout( 1 , 3 ));
        jp.add(runs);
        jp.add(errors);
        jp.add(failures);
        
        
        jp1.add(jp);
        jp1.add(status);
        add(jp1 , BorderLayout.EAST);
        
        
    }
    
    /**
     * Called when a test is finished.
     * @param testInfo The test info.
     */
    public void testFinished( TestInfo testInfo ) {
        
        runs.setText( Integer.toString( ++runsC ) + "/" + ntests);
        synchronized ( status ) {
            status.setValue( status.getValue() + 1 );
        }
        
        synchronized( message ) {
            message.setText( "Test " + testInfo.getClassname() + "." + testInfo.getText() + " finished at " + testInfo.getHostname());
            message.setIcon( IconManager.getIcon( IconManager.finishedIcon));
        }
        
        repaint();
    }

    /**
     * Called when a test has been started
     * @param testInfo The test info.
     */
    public void testStarted( Test test) {
        
        
        synchronized( message ) {
            message.setText( "Test " + test.getClass().getName() + "." + ((TestCase)test).getName() + " execution started");
            message.setIcon(IconManager.getIcon( IconManager.runningIcon));
        }
        
        repaint();
        
    }

    /**
     * Called when a test has failed due to an unsatisfied assertion
     * @param testInfo The test info.
     */
    public void testFailed( TestInfo testInfo ) {
        
        runs.setText( Integer.toString( ++runsC ) + "/" + ntests);
        failures.setText( Integer.toString( ++failuresC ) );
        synchronized ( status ) {
            status.setValue( status.getValue() + 1 );
        }
        
        if( errorsC == 0 && failuresC == 1 ) {
            status.setForeground( Color.RED );
        }
        
        synchronized( message ) {
            message.setText( "Test " + testInfo.getClassname() + "." + testInfo.getText() + " has failed at " + testInfo.getHostname());
            message.setIcon(IconManager.getIcon( IconManager.failureIcon));
        }
        
        repaint();
        
    }
    
    /**
     * Called when a a test has failed due to an unanticipated error.
     * @param testInfo The test info.
     */
    public void testError( TestInfo testInfo ) {
        
        runs.setText( Integer.toString( ++runsC ) + "/" + ntests);
        errors.setText( Integer.toString( ++errorsC ) );
        
        synchronized ( status ) {
            status.setValue( status.getValue() + 1 );
        }
        
        if( errorsC == 1 ) {
            status.setForeground( Color.GRAY );
        }
        
        synchronized( message ) {
            message.setText( "Test " + testInfo.getClassname() + "." + testInfo.getText() + " has failed at " + testInfo.getHostname() + " due to an unexpected error");
            message.setIcon( IconManager.getIcon( IconManager.errorIcon));
        }
        
        repaint();
        
    }

    /**
     * Called when the test phase has been started.
     *
     */
    public void testPhaseFinished() {
        message.setText( "Test phase finished. Total time: " + ( ( System.currentTimeMillis() - start )/1000 ) + " s");
        message.setIcon( IconManager.getIcon( IconManager.infoIcon));
        
        
    }
    /**
     * Called when the test phase has been started.
     *
     */
    public void testPhaseStarted() {
        start = System.currentTimeMillis();
        message.setText( "Test phase started");
        message.setIcon( IconManager.getIcon( IconManager.infoIcon));
        
    }

        
}
