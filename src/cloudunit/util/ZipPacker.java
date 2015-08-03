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
package cloudunit.util;

/**
 * @author Alexandre N—brega Duarte - alexandre@ci.ufpb.br
 * 
 * Description: Utility class to deal with zip files.
 * 
 * @version 1.0 Date: 18/04/2005
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipPacker {

    
    private static Map<File, File> bundles = new HashMap<File, File>();
    
    /**
     * Compress the files of a TestApplication.
     * @param dir The application to be compressed   
     * @return The zip file
     * @throws IOException
     */
    public static File pack( File dir ) throws IOException {

        if( bundles.get(dir) == null ) {
            
            bundles.put( dir , createZip( dir ) );
            
        }
        
        return bundles.get( dir );
    }
    
    public static File createZip( File dir ) throws IOException {
        
        File zipFile = File.createTempFile( "cloudunit" , ".zip");
        
        
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream( "/tmp/" + zipFile.getName()));
        
        createZip( dir , out );
        out.close();
        return zipFile;
        
    }
    
    private static final void createZip( File f , ZipOutputStream out ) throws IOException {
        
        if( f.isFile() ) {
                    
            FileInputStream in = new FileInputStream(f);
        
            //Absolute path without the first /.
            out.putNextEntry(new ZipEntry(f.getAbsolutePath().substring(1)));
            byte[] buf = new byte[2048];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        } else if( f.isDirectory()){
            
            File fs[] = f.listFiles();
            for( int i = 0 ; i <  fs.length; i++ ) {
                createZip( fs[ i ] , out );
            }
            
        }
        
    }
    
    public static void main( String args[] ) throws IOException, InterruptedException {
    	ZipPacker.pack( new File( args[1]));
    }
    
    
}