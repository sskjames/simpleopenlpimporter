package org.simpleopenlpimporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.UnhandledException;
import org.simpleopenlpimporter.dao.ISongImportDao;
import org.simpleopenlpimporter.dao.SqliteSongImportDao;
import org.simpleopenlpimporter.domain.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongsImporter implements ISongsImporter
{
	private final static Logger logger = LoggerFactory
			.getLogger(SongsImporter.class);

	public void importSongs(File file)
	{
		List<Song> songs = new ArrayList<Song>();

		if (file.isDirectory()) {
			songs.addAll(getSongsInDir(file));
		} else {
			songs.add(getSongFromFile(file));
		}

		logger.info("Importing {} songs into database...", songs.size());
		getSongImportDao().importSongs(songs);
		logger.info("Finished importing the songs!");
	}

	protected List<Song> getSongsInDir(File dir)
	{
		logger.info("Getting songs from the directory {}", dir);
		List<Song> songs = new ArrayList<Song>();
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				songs.add(getSongFromFile(file));
			} else {
				// recurse
				songs.addAll(getSongsInDir(file));
			}
		}
		return songs;
	}

	protected Song getSongFromFile(File file)
	{
		logger.info("Getting song from the file {}", file);
		try {
			String title = FilenameUtils.getBaseName(file.getName());
			String content = FileUtils.readFileToString(file);
			return new Song(title, wrapLyricsInXml(content));
		} catch (Exception ex) {
			throw new UnhandledException(
					"Error occured while getting song from the file " + file,
					ex);
		}
	}

	protected String wrapLyricsInXml(String lyrics)
	{
		StringBuilder builder = new StringBuilder();
		// <xml>
		builder.append(getXmlHeader()).append("\n");
		// <song>
		builder.append(getOpenSongElement()).append("\n");
		// <lyrics>
		builder.append(getOpenLyricsElement("en")).append("\n");
		// <verse>
		builder.append(getOpenVerseElement("Verse", 1)).append("\n");
		// lyrics data
		builder.append(getCData(lyrics)).append("\n");
		// </verse>
		builder.append(getCloseVerseElement()).append("\n");
		// </lyrics>
		builder.append(getCloseLyricsElement()).append("\n");
		// </song>
		builder.append(getCloseSongElement()).append("\n");
		return builder.toString();
	}

	protected String getXmlHeader()
	{
		return "<?xml version='1.0' encoding='UTF-8'?>";
	}

	protected String getOpenSongElement()
	{
		return "<song version=\"1.0\">";
	}

	protected String getCloseSongElement()
	{
		return "</song>";
	}

	protected String getOpenLyricsElement(String languageCode)
	{
		return "<lyrics language=\"" + languageCode + "\">";
	}

	protected String getCloseLyricsElement()
	{
		return "</lyrics>";
	}

	protected String getOpenVerseElement(String type, int label)
	{
		return "<verse type=\"" + type + "\" label=\"" + label + "\">";
	}

	protected String getCloseVerseElement()
	{
		return "</verse>";
	}

	protected String getCData(String content)
	{
		return "<![CDATA[" + content + "]]>";
	}

	public ISongImportDao getSongImportDao()
	{
		return new SqliteSongImportDao();
	}
}
