package org.simpleopenlpimporter.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang.UnhandledException;
import org.simpleopenlpimporter.domain.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqliteSongImportDao implements ISongImportDao
{
	private final static Logger logger = LoggerFactory
			.getLogger(SqliteSongImportDao.class);

	@Override
	public void importSongs(List<Song> songs)
	{
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = getConnection();
			String query = "insert into songs (id, title, alternate_title, lyrics, search_title, search_lyrics)"
					+ " values (?, ?, ?, ?, ?, ?)";
			statement = connection.prepareStatement(query);

			for (Song song : songs) {

				try {
					statement.setInt(1, getId(connection));
					statement.setString(2, song.getTitle());
					statement.setString(3, song.getAlternateTitle());
					statement.setString(4, song.getLyrics());
					statement.setString(5, song.getSearchTitle());
					statement.setString(6, song.getSearchLyrics());

					statement.executeUpdate();
				} catch (Exception ex) {
					logger.error("Error occured while importing the song "
							+ song, ex);
				}
			}
		} catch (Exception ex) {
			throw new UnhandledException("Error occured while importing songs",
					ex);
		} finally {

			if (statement != null) {
				try {
					statement.close();
					connection.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	protected Connection getConnection() throws Exception
	{
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection("jdbc:sqlite:" + getDbPath());
	}

	protected String getDbPath()
	{
		return System.getProperty("openlpdbpath");
	}

	protected int getId(Connection connection)
	{
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String query = "select max(id) from songs";
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				int id = resultSet.getInt(1) + 1;
				resultSet.close();
				return id;
			}
		} catch (Exception ex) {
			logger.error("Error occured while getting id", ex);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception ex) {
				}
			}
		}
		return -1;
	}
}
