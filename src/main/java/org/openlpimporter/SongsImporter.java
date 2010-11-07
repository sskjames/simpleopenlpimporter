package org.openlpimporter;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongsImporter implements ISongsImporter
{
	private final static Logger logger = LoggerFactory
			.getLogger(SongsImporter.class);

	public void importSongsFromFlatFile(File file)
	{

	}

	protected String wrapLyricsInXml(String lyrics)
	{
		StringBuilder builder = new StringBuilder();
		//<xml>
		builder.append(getXmlHeader()).append("\n");
		//<song>
		builder.append(getOpenSongElement()).append("\n");
		//<lyrics>
		builder.append(getOpenLyricsElement("en")).append("\n");
		//<verse>
		builder.append(getOpenVerseElement("Verse", 1)).append("\n");
		//lyrics data
		builder.append(getCData(lyrics)).append("\n");
		//</verse>
		builder.append(getCloseVerseElement()).append("\n");
		//</lyrics>
		builder.append(getCloseLyricsElement()).append("\n");
		//</song>
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
}
