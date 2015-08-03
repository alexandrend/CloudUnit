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

import org.smartfrog.services.junit.TestInfo;
import junit.framework.Test;

/**
 * Description: A CloudTestListener is a entity interested in receive <i>events</i> regarding the current status of the execution of
 * a TestSuite.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public interface CloudTestListener {
    
    /**
     * Called when a test is finished.
     * @param testInfo The test info.
     */
    public void testFinished( TestInfo testInfo );
    
    /**
     * Called when a test has been started
     * @param test The test.
     */
    public void testStarted( Test test);
    
    /**
     * Called when a test has failed due to an unsatisfied assertion
     * @param testInfo The test info.
     */
    public void testFailed( TestInfo testInfo );
   
    /**
     * Called when a a test has failed due to an unanticipated error.
     * @param testInfo The test info.
     */
    public void testError( TestInfo testInfo );
    
    /**
     * Called when the test phase has been started.
     *
     */
    public void testPhaseStarted();
    
    /**
     * Called when all tests where executed (either successfully or unsuccessfully).
     *
     */
    public void testPhaseFinished();
    

}
