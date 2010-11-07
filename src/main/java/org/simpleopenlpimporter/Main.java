package org.simpleopenlpimporter;

import java.io.File;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;

public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Scanner keyboard = new Scanner(System.in);

		// source file
		System.out
				.println("Enter the lyrics file or directory you would like to import (For example, /your-home-dir/lyrics): ");
		String sourceFilePath = keyboard.next();

		// database
		String dbPath = getDbPath(keyboard);

		if (StringUtils.isNotBlank(sourceFilePath)
				&& StringUtils.isNotBlank(dbPath)) {
			File file = new File(sourceFilePath);
			if (file.exists() && new File(dbPath).exists()) {
				ISongsImporter songsImporter = new SongsImporter();
				songsImporter.importSongs(file);
			} else {
				System.out.println("Either " + file + " or " + dbPath + " does not exist!");
			}
		}
	}

	private static String getDbPath(Scanner keyboard)
	{
		String dbPath = System.getProperty("openlpdbpath");

		if (StringUtils.isBlank(dbPath)) {
			System.out
					.println("Enter the path of your sqlite database"
							+ " (For example, /your-home-dir/.local/share/openlp/songs/songs.sqlite): ");
			dbPath = keyboard.next();
			System.setProperty("openlpdbpath", dbPath);
		}
		return dbPath;
	}
}
