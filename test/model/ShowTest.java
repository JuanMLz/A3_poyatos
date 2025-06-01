package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class ShowTest {

    @Test
    public void testGetShows() throws Exception {
        // Mock dos objetos do JDBC
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockPs = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        // Mock estático do método Conexao.getConexao()
        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class)) {
            mockedConexao.when(Conexao::getConexao).thenReturn(mockConn);

            when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
            when(mockPs.executeQuery()).thenReturn(mockRs);

            // Configura o ResultSet para simular dois shows
            when(mockRs.next()).thenReturn(true, true, false); // 2 registros + fim
            when(mockRs.getInt("id")).thenReturn(1, 2);
            when(mockRs.getString("nome")).thenReturn("Show 1", "Show 2");
            when(mockRs.getString("data")).thenReturn("01/01", "02/02");
            when(mockRs.getInt("codGenero")).thenReturn(10, 20);
            when(mockRs.getInt("codLocal")).thenReturn(100, 200);
            when(mockRs.getString("link")).thenReturn("link1", "link2");

            List<Show> shows = Show.getShows();

            assertEquals(2, shows.size());

            Show primeiro = shows.get(0);
            assertEquals(1, primeiro.id);
            assertEquals("Show 1", primeiro.nome);
            assertEquals("01/01", primeiro.data);
            assertEquals(10, primeiro.codGenero);
            assertEquals(100, primeiro.codLocal);
            assertEquals("link1", primeiro.link);
        }
    }
}
