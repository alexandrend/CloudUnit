/*
 * Copyright (c) 2002-2015 Universidade Federal de Campina Grande and Universidade Federal da Paraiba
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
package cloudunit.framework;

import junit.framework.TestSuite;


/**
 * Description: A CloudTestRunner is an entity able to execute all JUnit tests in a given TestSuite
 * and fire to its CloudTestListeners events regarding the status of the execution.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public interface CloudTestRunner {
    
    /**
     * Executes all JUnit tests in a given TestSuite.
     * @param testSuite A TestSuite.
     */
    public void run( TestSuite testSuite );

    /**
     * Adds a CloudTestListener to receive events from this test execution.
     * @param listener A GridTestListener.
     */
    public void addCloudTestListener( CloudTestListener listener );

    /**
     * Stops the test suite execution. 
     */
    public void stop();
    
    
}