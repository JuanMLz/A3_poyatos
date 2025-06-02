package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import dao.Conexao;

public class ShowTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setup() throws Exception {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    public void testGetShows_ReturnsListOfShows() throws Exception {
        // Mock do Conexao.getConexao() para retornar mockConnection
        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class)) {
            mockedConexao.when(Conexao::getConexao).thenReturn(mockConnection);

            String sql = "SELECT id, nome, data, codGenero, codLocal, link FROM shows ORDER BY nome";

            when(mockConnection.prepareStatement(sql)).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            // Configura o mock para simular dados retornados pelo ResultSet
            when(mockResultSet.next()).thenReturn(true, true, false); // 2 registros
            when(mockResultSet.getInt("id")).thenReturn(1, 2);
            when(mockResultSet.getString("nome")).thenReturn("Show A", "Show B");
            when(mockResultSet.getString("data")).thenReturn("01/01", "02/02");
            when(mockResultSet.getInt("codGenero")).thenReturn(10, 20);
            when(mockResultSet.getInt("codLocal")).thenReturn(100, 200);
            when(mockResultSet.getString("link")).thenReturn("linkA", "linkB");

            List<Show> shows = Show.getShows();

            assertNotNull(shows);
            assertEquals(2, shows.size());

            Show first = shows.get(0);
            assertEquals(1, first.id);
            assertEquals("Show A", first.nome);
            assertEquals("01/01", first.data);
            assertEquals(10, first.codGenero);
            assertEquals(100, first.codLocal);
            assertEquals("linkA", first.link);
        }
    }

    @Test
    public void testCadastrarShow_CallsExecuteUpdate() throws Exception {
        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class)) {
            mockedConexao.when(Conexao::getConexao).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            doNothing().when(mockPreparedStatement).setString(anyInt(), anyString());
            doNothing().when(mockPreparedStatement).setInt(anyInt(), anyInt());
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Show show = new Show(0, "Show Teste", "10/10", 1, 1, "link");

            // Método estático que insere no banco
            Show.cadastrar(show);

            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }

    @Test
    public void testRemoverShow_DeletesShow() throws Exception {
        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class)) {
            mockedConexao.when(Conexao::getConexao).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Show show = new Show();
            show.id = 123;

            Show.removerShow(show);

            verify(mockPreparedStatement).setInt(1, 123);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

}
