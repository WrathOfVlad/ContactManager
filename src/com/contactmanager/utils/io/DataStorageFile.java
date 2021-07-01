package com.contactmanager.utils.io;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.contactmanager.datamodel.Contact;
import com.contactmanager.datamodel.Log;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class DataStorageFile extends DataStorageHandler{
	private static final String FILENAME_NOTES = "notes.txt";
	private static final String FILENAME_LOGS = "logs.csv";
	private static final String FILENAME_PROFILE_PICTURE = "profilePicture";
	private static final String FILENAME_MAIN = "Main.csv";
	private static final String DIRPATH_BACKUPS = File.separator + "backups" + File.separator;
	
	private String path = ConfigFileData.getInstance().getPath();
	

	private File checkIfFileExistsInIdDir(int id, String fileName) {
		String idPath= path + File.separator + String.format(MAX_ID_FORMATTING, id);
		String filePath = idPath + File.separator + fileName;
		checkIfDirExists(idPath);
		return checkIfFileExists(filePath);
		
	}
	
	private File checkIfFileExists(String filePath) {
		File file = new File(filePath);
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	private void checkIfDirExists(String idPath) {
		File directory = new File(idPath);
		if(!directory.isDirectory()) {
			directory.mkdir();
		}
	}
	
	@Override
	public String getNotes(int id) {
		File file = checkIfFileExistsInIdDir(id, FILENAME_NOTES);
		
		String notesTxt;
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				notesTxt = br.lines().collect(Collectors.joining("\n"));
				br.close();
			} catch (Exception e) {				
				notesTxt = "";
				e.printStackTrace();
			}		
		return notesTxt;
	}
	@Override
	public void saveNotes(int id, String notes) {
		File file = checkIfFileExistsInIdDir(id, FILENAME_NOTES);
		
		try (FileWriter fw = new FileWriter(file)){
			fw.write(notes);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public List<String[]> getLogs(int id) {
		File file = checkIfFileExistsInIdDir(id, FILENAME_LOGS);		
		List<String[]> allLogs = new ArrayList<>();
		
		try (Reader fr = new FileReader(file, StandardCharsets.UTF_8)){
			
			var reader = new CSVReader(fr);
			allLogs = reader.readAll();
			reader.close();		
			
		} catch (IOException |CsvException e) {
			e.printStackTrace();
			
		}
		if (allLogs.size() != 0) {
			//allLogs.remove(0);
		}
		return allLogs;
	}

	@Override
	public void saveLogs(int id, List<String[]> logs) {
		File file = checkIfFileExistsInIdDir(id, FILENAME_LOGS);
		
		try (var fos = new FileOutputStream(file)){
			var osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
	        var writer = new CSVWriter(osw);
	        List<String> columnsList = Log.getColumns();
	        String[] columns = columnsList.toArray(new String[columnsList.size()]);
	        writer.writeNext(columns);
			for (String[] log : logs) {
				writer.writeNext(log);
			}
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Image getProfileImage(int id) {
		String parsedId = String.format(MAX_ID_FORMATTING, id);
		String idPath = path + File.separator + parsedId;
		checkIfDirExists(idPath);
		String fileName =  idPath + File.separator + FILENAME_PROFILE_PICTURE;
		String[] allExtensions = DataStorageHandler.IMAGE_EXTENSIONS;
		
		File profilePictureFile = null;
		URL noImageStream = null;	
		
		try {			
			for (String extension: allExtensions) {
				profilePictureFile = new File(fileName +"."+ extension);
				if (profilePictureFile.exists()) {
					Image profilePictureImage = ImageIO.read(profilePictureFile);
					return profilePictureImage;
				}
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
		try {
			noImageStream = getClass().getResource(NO_PROFILE_IMAGE_PATH);
			Image noImage = ImageIO.read(noImageStream);
			return noImage;
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
	}
	@Override
	public void saveProfileImage(int id, Image image) {
		String parsedId = String.format(MAX_ID_FORMATTING, id);
		String idPath = path + File.separator + parsedId;
		checkIfDirExists(idPath);
		
		try {
			ImageIO.write((BufferedImage)image, "png", new File(idPath+"/"+FILENAME_PROFILE_PICTURE+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public List<String[]> getContactData() {
		String file = path + File.separator + FILENAME_MAIN;
		List<String[]> allRows = new ArrayList<String[]>();
		
		try (Reader fr = new FileReader(file, StandardCharsets.UTF_8)){
			
			var reader = new CSVReader(fr);
			allRows = reader.readAll();
			reader.close();		
			return allRows;
			
		} catch (IOException |CsvException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void saveContactData(List<Contact> data) {
		String file = path + File.separator + FILENAME_MAIN;
		
		try (var fos = new FileOutputStream(file)){			
	        var osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
	        var writer = new CSVWriter(osw);
	        List<String> columnsList = ConfigFileData.getInstance().getColumns(true);
	        String[] columns = columnsList.toArray(new String[columnsList.size()]);
	        writer.writeNext(columns);
	        for (int i = 0; i < data.size(); i++) {
	        	Contact contact = data.get(i);
	        	List<String> rowList = contact.getElementsAsList(false);
				String[] row = rowList.toArray(new String[rowList.size()]);
				writer.writeNext(row);
			} 
	        writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(data.get(0).toString());
		
	}
	
	@Override
	public void createBackup(int id) {
		deleteOldBackups();
		String parsedId = String.format(MAX_ID_FORMATTING, id);
		checkIfDirExists(path + DIRPATH_BACKUPS);
		
		Date date = new Date();
		long timeMilli = date.getTime();
		String backupPath = path + DIRPATH_BACKUPS + timeMilli;
		checkIfDirExists(backupPath);
		
		
		File main = new File(path + File.separator + FILENAME_MAIN);
		File mainCopy = new File(backupPath + File.separator + FILENAME_MAIN);		
		try {
			FileUtils.copyFile(main, mainCopy);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String fullPath =  path + File.separator+parsedId;
		File idDirFile = new File(fullPath);
		if(!idDirFile.exists()) {return;};
		
		File originalFile = new File(fullPath);
		File newFile = new File(backupPath + File.separator +parsedId );
		
		try {
			FileUtils.copyDirectory(originalFile, newFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	@Override
	public void deleteOldBackups() {
		checkIfDirExists(path + DIRPATH_BACKUPS);
		File  backupDir = new File(path+DIRPATH_BACKUPS);
		String[] backups = backupDir.list();
		
		int backupTimeLimit = ConfigFileData.getInstance().getBackupTimeLimit();
		
		for (String backup : backups) {
			long backupCreationEpoch = Long.parseLong(backup);
			long dayDifference = Instant.ofEpochMilli(backupCreationEpoch).until(Instant.now(), ChronoUnit.DAYS);
			if (dayDifference >= backupTimeLimit) {
				try {
					FileUtils.deleteDirectory(new File(path+ DIRPATH_BACKUPS + backup));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public void goToPath(int id) {
		
		Desktop desktop = null;
		String dirPath = path + File.separator + String.format(MAX_ID_FORMATTING, id);
		File file = new File(dirPath);
		try {
			if (Desktop.isDesktopSupported()) {
			   desktop = Desktop.getDesktop();
			   desktop.open(file);
			}
			else {
			   System.out.println("desktop is not supported");
			}
	    }
	    catch (IOException e){  }
	}
	
	

	
}
