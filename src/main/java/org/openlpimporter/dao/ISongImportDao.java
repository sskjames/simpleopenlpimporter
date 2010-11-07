package org.openlpimporter.dao;

import java.util.List;

import org.openlpimporter.domain.Song;

public interface ISongImportDao
{
	void importSongs(List<Song> songs);
}
