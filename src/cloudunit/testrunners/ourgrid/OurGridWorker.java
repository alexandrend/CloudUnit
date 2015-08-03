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
package cloudunit.testrunners.ourgrid;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.ourgrid.mygrid.ui.ConcreteUIServices;
import org.ourgrid.specs.IOBlock;
import org.ourgrid.specs.IOEntry;
import org.ourgrid.specs.JobSpec;
import org.ourgrid.specs.JobSpecificationException;
import org.ourgrid.specs.TaskSpec;
import org.ourgrid.specs.TaskSpecificationException;
import org.smartfrog.services.junit.TestInfo;
import org.smartfrog.services.junit.ThrowableTraceInfo;

import cloudunit.framework.BaseCloudWorker;
import cloudunit.util.Serializer;
import cloudunit.util.ZipPacker;


/**
 * Description: This is class is the responsible to convert one JUnit TestCase to one OurGrid job description, 
 * submit the job for execution, recover the results of the execution and send it to a GridTestListener.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class OurGridWorker extends BaseCloudWorker {

    private boolean done = false;
    private int jobId;
    
    
    public OurGridWorker() {
        
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
        if( !done && jobId != -1 ) { 
            try {
                ConcreteUIServices.getInstance().cancelJob(jobId);
            } catch (Exception e) {
            } 
        }
        
        done = true;

    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        done = false;
        jobId = -1;
        
        try {
            
            synchronized( OurGridWorker.class ) {
                jobId = ConcreteUIServices.getInstance().addJob( createJobSpec() );
            }

            listener.testStarted( test );
            ConcreteUIServices.getInstance().waitForJob( jobId );
                    
            
            TestInfo info = getTestInfo(test);
                	
            if( info.getFault() == null ) {
                listener.testFinished( info );
            } else {
                	    
                ThrowableTraceInfo fault = info.getFault();
                	    
                if( fault.getClassname().equals( AssertionFailedError.class.getName() ) ) {
                    listener.testFailed( info );
                } else {
                    listener.testError( info  );
                }
            }
            
        } catch( ThreadDeath td ) {
            throw td;
            
        } catch ( Throwable t ) {
            listener.testError( new TestInfo( test , new Exception( "Could not execute the test. See your grid broker for more details.", t)) );
             
        }
             
        done = true;

    }

  
    /**
     * Gets the Testinfo generated in the remote grid machine.
     * @param test The test executed.
     * @return The TestInfo   
     * @throws IOException An IOException may be thrown.
     * @throws ClassNotFoundException A ClassNotFoundException may be thrown.
     */
    private TestInfo getTestInfo(TestCase test) throws ClassNotFoundException , IOException  {
        return (TestInfo) Serializer.readObject( new File(  "/tmp/" + test.getClass().getName() + "." + test.getName() + ".ser" ) );
    }

   
    /**
     * Creates specifications for OurGrid Jobs.
     * @return A specification for a MyGrid Job.
     * @throws JobSpecificationException A JobSpecificationException may be thrown. 
     * @throws TaskSpecificationException A TaskSpecificationException may be thrown.
     * @throws IOException An IOException may be thrown.
     * @throws InterruptedException An InterruptedException may be thrown.
     */
    private JobSpec createJobSpec() throws JobSpecificationException, TaskSpecificationException, IOException, InterruptedException {
        
        JobSpec job = new JobSpec(test.getClass()+ "." + test.getName());
        job.setTaskSpecs( createTaskSpec());
        job.setExpression( "OS=Linux");
        return job;      

    }
    
    /**
     * Creates specifications for MyGrid tasks.
     * @return A specification for a MyGrid Task.
     * @throws TaskSpecificationException A TaskSpecificationException may be thrown.
     * @throws IOException An IOException may be thrown.
     * @throws InterruptedException An InterruptedException may be thrown.
     */
    private List<TaskSpec> createTaskSpec() throws TaskSpecificationException, IOException, InterruptedException {
        
        IOBlock input = new IOBlock();
                      
        File dir = new File( "." );
        String baseDir = dir.getAbsolutePath().substring(1);
        File bundle = ZipPacker.pack( dir );
                
        input.putEntry( new IOEntry( "put" , bundle.getAbsolutePath() , "$PLAYPEN/" + bundle.getName()) );
        
        File f = new File( System.getProperty( "gridunit.runtime") );
        
        input.putEntry( new IOEntry( "put" , f.getAbsolutePath() , "$PLAYPEN/" + f.getName()));
        
        StringTokenizer st = new StringTokenizer( System.getProperty( "java.class.path"), File.pathSeparator );
        StringBuffer classP = new StringBuffer( ".:$PLAYPEN/" + f.getName());
        while( st.hasMoreTokens() ) {

            classP.append(File.pathSeparatorChar);
            classP.append( "$PLAYPEN" );
            classP.append( (new File(st.nextToken())).getAbsolutePath() );

        }
        
        String classpath = classP.toString();
        
        List<TaskSpec> tasks = new Vector<TaskSpec>();       
        IOBlock output = new IOBlock();
        
        output.putEntry( new IOEntry( "get" , "$PLAYPEN/" + baseDir + "/" + test.getClass().getName() + "." + test.getName() + ".ser" , "/tmp/" + test.getClass().getName() + "." + test.getName() + ".ser" ) );
        
        String remote = "java -cp " + f.getName() + " gridunit.util.Unzip " + bundle.getName() + " . > /dev/null 2> /dev/null; cd $PLAYPEN/" + baseDir + " ;  java -cp " + classpath.toString() + " gridunit.framework.GridTester " + test.getClass().getName() + " " + test.getName() ;
        tasks.add( new TaskSpec(input, remote, output));
        
        return tasks;
        
    }
}
