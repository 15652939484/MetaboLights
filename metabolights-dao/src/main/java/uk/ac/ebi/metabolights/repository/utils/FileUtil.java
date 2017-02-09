/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 4/11/14 5:12 PM
 * Modified by:   conesa
 *
 *
 * ©, EMBL, European Bioinformatics Institute, 2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package uk.ac.ebi.metabolights.repository.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.HashSet;
import java.util.Set;


public class FileUtil {

	private static Logger logger = LoggerFactory.getLogger (FileUtil.class);
	
	public static void replace (String fileToSearchIn, String textToSearch, String textToReplace) throws IOException{
		String text;
		
		//Get the file into a string
		text = file2String(fileToSearchIn);
		
		//Replace the content
		text = StringUtils.replace(text, textToSearch, textToReplace);
		
		//Save the file
		String2File(text, fileToSearchIn, false);
		
		
	}
	/**
	 * Returns a string with the contents of a file.
	 * @param fileToUse
	 * @return
	 * @throws java.io.IOException
	 */
	public static String file2String (String fileToUse) throws IOException{

		//Instantiate a file object
		File file = new File (fileToUse);

		//Use a buffered reader
		BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        String text = "";

        //Go through the file
        while((line = reader.readLine()) != null)
        {
            //Add the final carriage return and line feed
        	text += line + "\r\n";
        }

        //Close the reader
        reader.close();

        //Return the String
        return text;
	}
	/**
	 * Takes the String passed and saves it in a file
	 * @param text : Text to save
	 * @param fileToSave : File to save (create) with "text" inside.
	 * @param backUp
	 * @throws java.io.IOException
	 */
	public static void String2File(String text, String fileToSave, boolean backUp) throws IOException{

		// Audit the file if required
		if (backUp )FileAuditUtil.backUpFile(new File(fileToSave));

		//instantiate a FileWriter
        FileWriter writer = new FileWriter(fileToSave);
        
        //Write the text
        writer.write(text);
        
        //Close the writer
        writer.close();
	}
	/**
	 * Deletes all files and subdirectories under dir.
	 * 
	 * @param dir to delete
	 * @return Returns true if all deletions were successful.
	 * If a deletion fails, the method stops attempting to delete and returns false.
	 */
	public static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
	}

	public static boolean fileExists(String path){
		File file = new File(path);

		return file.exists();
	}

	public static Path createFolder(String newFolderPath) throws IOException {

		// create the folder
		File newFolder = new File(newFolderPath);
		Path folderPath = newFolder.toPath();
		if (!newFolder.mkdir()) throw new IOException();

		// set folder owner, group and access permissions
		// 'chmod 770'
		UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();
		Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
		// owner permissions
		perms.add(PosixFilePermission.OWNER_READ);
		perms.add(PosixFilePermission.OWNER_WRITE);
		perms.add(PosixFilePermission.OWNER_EXECUTE);
		// group permissions
		perms.add(PosixFilePermission.GROUP_READ);
		perms.add(PosixFilePermission.GROUP_WRITE);
		perms.add(PosixFilePermission.GROUP_EXECUTE);
		// apply changes
		Files.getFileAttributeView(folderPath, PosixFileAttributeView.class, LinkOption.NOFOLLOW_LINKS).setPermissions(perms);

		return folderPath;
	}

	public static boolean fileExists(String path, boolean throwException) throws FileNotFoundException{

		return fileExists(new File(path), throwException);
		
	}

	public static boolean filesExists(File[] files, boolean throwException) throws FileNotFoundException{

		//Check existence of the files or folders
		for (File file:files)
		{
			if (fileExists(file, true))
			{
				return false;
			};
		}

		return true;

	}

	public static boolean fileExists(File file, boolean throwException) throws FileNotFoundException{

		boolean result = file.exists();

		if (throwException && !result){

			throw new FileNotFoundException ("Path (" + file.getAbsolutePath() + ") not found.");
		}

		return result;

	}


	/**
	 * Copy contents of a folder to another
	 *
	 */
	public static boolean copyFiles(String sourceFolder, String destionationFolder){

		File source = new File(sourceFolder);
		File dest = new File(destionationFolder);

		try {

			FileUtils.copyDirectory(source, dest);

			return true;

		} catch (IOException e) {

			e.printStackTrace();

		}

		return false;

	}


}
