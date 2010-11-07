package org.simpleopenlpimporter;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.simpleopenlpimporter.dao.SqliteSongImportDao;
import org.simpleopenlpimporter.domain.Song;

public class SongsImporterTest
{
	private File file = new File("src/test/resources/songs/song1.txt");
	private SongsImporter songsImporter = new SongsImporter() {
		public org.simpleopenlpimporter.dao.ISongImportDao getSongImportDao()
		{
			return new SqliteSongImportDao() {
				protected String getDbPath()
				{
					return "src/test/resources/db/songs.sqlite";
				};
			};
		};
	};

	@Test
	public void testGetXmlHeader()
	{
		String expected = "<?xml version='1.0' encoding='UTF-8'?>";
		String actual = songsImporter.getXmlHeader();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetOpenSongElement()
	{
		String expected = "<song version=\"1.0\">";
		String actual = songsImporter.getOpenSongElement();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCloseSongElement()
	{
		String expected = "</song>";
		String actual = songsImporter.getCloseSongElement();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetOpenLyricsElement()
	{
		String expected = "<lyrics language=\"en\">";
		String actual = songsImporter.getOpenLyricsElement("en");
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCloseLyricsElement()
	{
		String expected = "</lyrics>";
		String actual = songsImporter.getCloseLyricsElement();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetOpenVerseElement()
	{
		String expected = "<verse type=\"Verse\" label=\"1\">";
		String actual = songsImporter.getOpenVerseElement("Verse", 1);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCloseVerseElement()
	{
		String expected = "</verse>";
		String actual = songsImporter.getCloseVerseElement();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCData()
	{
		String content = "This is a test song";
		String expected = "<![CDATA[" + content + "]]>";
		String actual = songsImporter.getCData(content);
		assertEquals(expected, actual);
	}

	@Test
	public void testWrapLyricsInXml()
	{
		String lyrics = "Jesus loves me";

		String expected = songsImporter.getXmlHeader() + "\n";
		expected += songsImporter.getOpenSongElement() + "\n";
		expected += songsImporter.getOpenLyricsElement("en") + "\n";
		expected += songsImporter.getOpenVerseElement("Verse", 1) + "\n";
		expected += songsImporter.getCData(lyrics) + "\n";
		expected += songsImporter.getCloseVerseElement() + "\n";
		expected += songsImporter.getCloseLyricsElement() + "\n";
		expected += songsImporter.getCloseSongElement() + "\n";

		System.out.println("Expected:\n" + expected);

		String actual = songsImporter.wrapLyricsInXml(lyrics);
		System.out.println("Result:\n" + actual);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetSongFromFile() throws Exception
	{
		Song song = songsImporter.getSongFromFile(file);
		assertEquals(FilenameUtils.getBaseName(file.getName()), song.getTitle());
		assertEquals(
				songsImporter.wrapLyricsInXml(FileUtils.readFileToString(file)),
				song.getLyrics());
	}

	@Test
	public void testGetSongsInDir()
	{
		List<Song> songs = songsImporter.getSongsInDir(file.getParentFile());
		assert !songs.isEmpty();
	}

	@Test
	public void testImportSongs()
	{
		songsImporter.importSongs(file.getParentFile());
		assert true;
	}
}
