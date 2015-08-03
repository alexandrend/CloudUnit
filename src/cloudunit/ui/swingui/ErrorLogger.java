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

import javax.swing.JFrame;

import org.smartfrog.services.junit.TestInfo;


/**
 * Description: A graphical panel able to show unanticipated errors captured during the test execution.
 * 
 * @author Alexandre N—brega Duarte - alexandre@ci.ufpb.br
 */
public class ErrorLogger extends FailureLogger {
 
    
   
	private static final long serialVersionUID = 1L;

	/**
     * Full constructor for ErrorLogger.
     * @param parent Component in which this panel will be coupled.
     */
    public ErrorLogger( JFrame parent ) {
        super(parent);
    }
    
    /**
     * Called when a test has failed due to an unsatisfied assertion
     * @param testInfo The test info.
     */
    public void testFailed(TestInfo testInfo) {
                
        
    }

    /**
     * Called when a a test has failed due to an unanticipated error.
     * @param testInfo The test info.
     */
    public synchronized void testError( TestInfo testInfo ) {
       
        addException( testInfo );
    }

}
    
