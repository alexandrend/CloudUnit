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

 * For more information: http://Cloudunit.sourceforge.net
 */

package cloudunit.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * @author Alexandro de Souza Soares - alexandro@lsd.ufcg.edu.br
 * <br>
 * @version 1.0
 * <br>
 * Copyright (c) 2002-2005 Universidade Federal de Campina Grande
 */
public class Unzip {

	/**
	 * Copies data from input to ouput. 
	 * @param input An InputStream.
	 * @param output An OutputStream.
	 * @throws IOException An IOException is thrown when an I/O error occurs. 
	 */
	private static final void copyData( InputStream input, OutputStream output ) throws IOException {
		
		byte[] buffer = new byte[ 2048 ];

		int len;
	    while( ( len = input.read( buffer ) ) >= 0 ) {
	    	output.write( buffer, 0, len );
	    }

	    input.close();
	    output.close();
	    
	}
	
	/**
	 * Decompresses files from a ZipFile into a directory.
	 * @param zipFile A file containing compressed files.
	 * @param destination Name of destination directory.
	 * @throws IOException An IOException is thrown when an I/O error occurs.
	 */
	public static void unzip( ZipFile zipFile, File destination ) throws IOException {
		
		destination.mkdirs();
		
		Enumeration entries = zipFile.entries();
		while( entries.hasMoreElements() ) {
			
			ZipEntry entry = ( ZipEntry )entries.nextElement();

			if( entry.isDirectory() ) {
		
			    File uncompressedDirectory = new File( destination.getPath() + File.separator +  entry.getName() );
	        	uncompressedDirectory.mkdirs();
	        	
	        } else {

	            
	            File f = ( new File( destination.getPath() + File.separator + entry.getName() ) );
	            f.getParentFile().mkdirs();
	            
	        	copyData( zipFile.getInputStream( entry ), new BufferedOutputStream( new FileOutputStream( f ) ) );
	        	
	        }
			
	      }

		zipFile.close();
		
	}

	/**
	 * The main method. Through it, the application may be called by using a console.
	 * @param args The file name followed by name of destination directory.
	 */
	public static final void main( String[] args ) {
	    
		if( args.length != 2 ) {

	    	System.err.println( "CloudUnit Unzip Utility Tool" );
			System.err.println( "Usage: java Unzip <filename> <dir>" );
			System.err.println();
			System.err.println( "\t filename \t - file name; " );
			System.err.println( "\t dir \t \t - destination directory. " );
			System.err.println();
			System.err.println( "Copyright(c) 2005 - Universidade Federal de Campina Grande" );
			System.err.println();
			System.exit( 1 );
			
	    } else {
	    	
	    	try {
				
	    		unzip( new ZipFile( args[ 0 ] ), new File( args[ 1 ] ) );
	    		
			} catch ( IOException e ) {
				
				e.printStackTrace();
				System.exit( 1 );
				
			}
	 
	    }
		
	  }

}