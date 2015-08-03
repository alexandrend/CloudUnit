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

import cloudunit.util.Serializer;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import junit.framework.TestCase;
import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * Description: A CloudTester is an entity able to execute a given unit TestCase and to serialize the results of the test execution. 
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class CloudTester {
	
    /**
     * The TestCase class name
     */
    private String testClassName;
    
    /**
     * The TestCase test method
     */
    private String testMethod;
    
    /**
     * Builds a tester.
     * @param testClass The TestCase class name.
     * @param testMethod The TestCase test method.
     */
	public CloudTester( String testClass, String testMethod){ 
	    this.testClassName = testClass;
	    this.testMethod = testMethod;
	}
	
	/**
	 * Runs the specified TestCase saving the results of the execution in testResult
	 * @param testResultFile The file where the results of the execution will be saved.
	 * @throws IOException If it could not save the test results
	 */
	public void run( File testResultFile ) throws IOException  {
	    
	     TestCase test = null;
	     TestResult testResult = new TestResult();
	     Class<?> tClass;
	     	     	    	    
	     try {
	         tClass = Class.forName(testClassName);
	         test = (TestCase) tClass.newInstance();
	         test.setName( testMethod );
	         test.run(testResult);
	         	         
	     } catch (Exception e) {
	         testResult.addError( new TestSuite(testClassName + "." + testMethod), e );
	     }
	     
	     TestInfo info = new TestInfo( test );
	     info.setHostname( InetAddress.getLocalHost().toString());
	     
	     if( testResult.errorCount() > 0 ) {
	         info.addFaultInfo(((TestFailure) testResult.errors().nextElement() ).thrownException());
	         Serializer.writeObject(info, testResultFile);
	         
	     } else if( testResult.failureCount() > 0 ) {
	         info.addFaultInfo(((TestFailure) testResult.failures().nextElement() ).thrownException());
	         Serializer.writeObject(info, testResultFile);
	     } else {
	         Serializer.writeObject(info, testResultFile);
	     }
	     
	}
	
	
	/**
	 * Invokes the CloudTester to run a single test method of a given Test Case. 
	 * The results of the execution will be serialized to a file called args[0].args[1].ser. 
	 * @param args args[0] = TestCase full class name. args[1] = TestCase test method name.
	 * @throws IOException if it could not save the test results.
	 */
	public static void main( String args[] ) throws IOException {
	    
	    ( new CloudTester(args[0] , args[1]) ).run( new File(args[0] + "." + args[1] + ".ser") );
	    
	    	        
	}
		
}