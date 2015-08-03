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
package cloudunit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Description: An utility class to deal with object serialization/deserialization.
 * 
 * @author Alexandre Duarte - alexandre@ci.ufpb.br
 */
public class Serializer {
    
	/**
	 * Serializes an object to file
	 * @param obj The object to be serialized
	 * @param file The file where the object will be saved.
	 * @throws IOException If some IO problem occur.
	 */
	public static void writeObject(Object obj, File file ) throws IOException {
		
		ObjectOutputStream output = new ObjectOutputStream( new FileOutputStream( file));
		output.writeObject( obj);
		output.close();
	}

	/**
	 * Deserializes an object from a file 
	 * @param file The file with the serialized object
	 * @return The deserialized object
	 * @throws IOException If some IO problem occurs.
	 * @throws ClassNotFoundException If the object could not be instanciated.
	 */
	public static Object readObject(File file) throws IOException, ClassNotFoundException {
	    
	    ObjectInputStream input = new ObjectInputStream(new FileInputStream( file ));
		Object obj = input.readObject();
		input.close();

		return obj;
	}
}