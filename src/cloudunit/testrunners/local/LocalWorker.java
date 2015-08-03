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
package cloudunit.testrunners.local;

import junit.framework.TestFailure;
import junit.framework.TestResult;

import org.smartfrog.services.junit.TestInfo;

import cloudunit.framework.BaseCloudWorker;

/**
 * Description: This Test Worker is able to run a test locally.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class LocalWorker extends BaseCloudWorker {

    private boolean done = false;
        
    public LocalWorker() {
        
    }
        
    /* (non-Javadoc)
     * @see gridunit.testrunners.GridWorker#workIsDone()
     */
    public boolean workIsDone() {
        return done;
        
    }

    /* (non-Javadoc)
     * @see gridunit.testrunners.GridWorker#stop()
     */
    public void stop() {
       
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
            
        if( test != null && listener != null ) {
            
            TestResult result = new TestResult();
        
            test.run( result );
                    
            TestInfo testInfo = new TestInfo(test);
            testInfo.setHostname( "localhost");
                    
            if( result.failureCount() == 0 && result.errorCount() == 0 ) {
                listener.testFinished(testInfo);
            } else if( result.errorCount() > 0 ) {
                testInfo.addFaultInfo(( ( TestFailure ) result.errors().nextElement() ).thrownException() );
                listener.testError( testInfo );
            } else { 
                testInfo.addFaultInfo(( ( TestFailure ) result.failures().nextElement() ).thrownException() );
                listener.testFailed( testInfo );
            }

        }
        
        done = true;
    }
    
    

}
