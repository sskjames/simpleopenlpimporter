package org.simpleopenlpimporter.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.simpleopenlpimporter.dao.SqliteSongImportDao;
import org.simpleopenlpimporter.domain.Song;

public class SqliteSongImportDaoTest
{
	private String dbPath = "src/test/resources/db/songs.sqlite";
	private SqliteSongImportDao sqliteDao = new SqliteSongImportDao() {
		protected String getDbPath()
		{
			return dbPath;
		};
	};

	@Before
	public void setUp() throws Exception
	{
		Connection connection = sqliteDao.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate("delete from songs");
		statement.close();
		connection.close();
	}

	@Test
	public void testGetConnection() throws Exception
	{
		Connection connection = sqliteDao.getConnection();
		assert connection != null;
		connection.close();
	}

	@Test
	public void testImportSongs() throws Exception
	{
		List<Song> songs = new ArrayList<Song>();
		for (int i = 1; i <= 25; i++) {
			songs.add(new Song("Test song " + i, "Lyrics of test song " + i));			
		}
		sqliteDao.importSongs(songs);

		Connection connection = sqliteDao.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement
				.executeQuery("select count(*) from songs");
		if (resultSet.next()) {
			assert true;
			ResultSet resultSet2 = statement
					.executeQuery("select * from songs");
			while (resultSet2.next()) {
				System.out.print(resultSet2.getInt("id") + ", ");
				System.out.print(resultSet2.getString("title") + ", ");
				System.out
						.print(resultSet2.getString("alternate_title") + ", ");
				System.out.print(resultSet2.getString("lyrics") + ", ");
				System.out.print(resultSet2.getString("search_title") + ", ");
				System.out.print(resultSet2.getString("search_lyrics") + ", ");
				System.out.println("");
			}
		} else {
			fail("Test failed");
		}
		statement.close();
		connection.close();
	}
}
