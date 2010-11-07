package org.simpleopenlpimporter.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Song
{
	private int id;
	private String title;
	private String alternateTitle;
	private String lyrics;
	private String searchTitle;
	private String searchLyrics;

	public Song()
	{
		// TODO Auto-generated constructor stub
	}
	
	public Song(String title, String lyrics)
	{
		this.title = title;
		this.alternateTitle = title;
		this.lyrics = lyrics;
		this.searchTitle = title;
		this.searchLyrics = title;
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getAlternateTitle()
	{
		return alternateTitle;
	}

	public void setAlternateTitle(String alternateTitle)
	{
		this.alternateTitle = alternateTitle;
	}

	public String getLyrics()
	{
		return lyrics;
	}

	public void setLyrics(String lyrics)
	{
		this.lyrics = lyrics;
	}

	public String getSearchTitle()
	{
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle)
	{
		this.searchTitle = searchTitle;
	}

	public String getSearchLyrics()
	{
		return searchLyrics;
	}

	public void setSearchLyrics(String searchLyrics)
	{
		this.searchLyrics = searchLyrics;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public boolean equals(Object obj)
	{
		if(obj instanceof Song) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{	
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
