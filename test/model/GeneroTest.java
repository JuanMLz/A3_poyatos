
package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.swing.JOptionPane;

import dao.Conexao;

public class GeneroTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setup() throws Exception {
        // Mock objetos JDBC básicos
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Mock a criação e execução do PreparedStatement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Sucesso padrão para atualizações

        // Mock comportamento básico do ResultSet
        when(mockResultSet.next()).thenReturn(false); // Padrão para nenhum resultado
    }

    @Test
    public void testGetGeneros_RetornaListaDeGeneros() throws Exception {
        // Arrange: Mock Conexao.getConexao() para retornar nosso mockConnection
        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class)) {
            mockedConexao.when(Conexao::getConexao).thenReturn(mockConnection);

            // Arrange: Configura o mock do ResultSet para retornar dois gêneros
            when(mockResultSet.next()).thenReturn(true, true, false); // Simula duas linhas
            when(mockResultSet.getInt("id")).thenReturn(1, 2);
            when(mockResultSet.getString("nome")).thenReturn("Rock", "Pop");

            // Act: Chama o método sendo testado
            List<Genero> generos = Genero.getGeneros();

            // Assert: Verifica os resultados
            assertNotNull(generos);
            assertEquals(2, generos.size());

            assertEquals(1, generos.get(0).id);
            assertEquals("Rock", generos.get(0).nome);
            assertEquals(2, generos.get(1).id);
            assertEquals("Pop", generos.get(1).nome);

            // Verifica se o SQL correto foi preparado e executado
            verify(mockConnection).prepareStatement("SELECT id, nome FROM genero ORDER BY nome");
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    public void testGetGeneros_TrataSQLException() throws Exception {
        // Arrange: Mock Conexao.getConexao() para lançar SQLException
        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class);
             MockedStatic<JOptionPane> mockedOptionPane = mockStatic(JOptionPane.class)) {

            SQLException sqlException = new SQLException("Erro de banco de dados");
            mockedConexao.when(Conexao::getConexao).thenThrow(sqlException);

            // Act: Chama o método sendo testado
            List<Genero> generos = Genero.getGeneros();

            // Assert: Verifica se a lista está vazia e se JOptionPane foi chamado
            assertNotNull(generos);
            assertTrue(generos.isEmpty());
            mockedOptionPane.verify(() -> JOptionPane.showMessageDialog(null, "Erro ao obter gêneros: Erro de banco de dados"));
        }
    }

    // --- Testes para métodos privados (indiretamente via públicos) ou que usam JOptionPane --- 

    // Teste para cadastrar (usado por cadastrarGeneroDialog)
    // Mockando JOptionPane.showInputDialog e Conexao
    @Test
    public void testCadastrarGeneroDialog_Sucesso() throws Exception {
        String novoGeneroNome = "Samba";
        int expectedId = 5;

        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class);
             MockedStatic<JOptionPane> mockedOptionPane = mockStatic(JOptionPane.class)) {

            // Mock entrada do JOptionPane
            mockedOptionPane.when(() -> JOptionPane.showInputDialog(any(), anyString())).thenReturn(novoGeneroNome);

            // Mock Conexao e operações de banco de dados
            mockedConexao.when(Conexao::getConexao).thenReturn(mockConnection);

            // Mockando verificação de gênero existente (existeGenero) - assume que não existe
            ResultSet mockResultSetExists = mock(ResultSet.class);
            PreparedStatement mockPsExists = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(contains("SELECT COUNT(*) FROM genero"))).thenReturn(mockPsExists);
            when(mockPsExists.executeQuery()).thenReturn(mockResultSetExists);
            when(mockResultSetExists.next()).thenReturn(true);
            when(mockResultSetExists.getInt(1)).thenReturn(0); // Gênero não existe

            // Mockando a operação de inserção (cadastrar)
            ResultSet mockResultSetGeneratedKeys = mock(ResultSet.class);
            when(mockConnection.prepareStatement(contains("INSERT INTO genero"), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);
            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSetGeneratedKeys);
            when(mockResultSetGeneratedKeys.next()).thenReturn(true);
            when(mockResultSetGeneratedKeys.getInt(1)).thenReturn(expectedId);

            // Act
            Integer resultId = Genero.cadastrarGeneroDialog(null);

            // Assert
            assertEquals(expectedId, resultId);
            verify(mockPreparedStatement).setString(1, novoGeneroNome);
            verify(mockPreparedStatement).executeUpdate();
            mockedOptionPane.verify(() -> JOptionPane.showMessageDialog(null, "🎶 Gênero cadastrado: " + novoGeneroNome));
        }
    }

    @Test
    public void testCadastrarGeneroDialog_JaExiste() throws Exception {
        String generoExistente = "Rock";

        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class);
             MockedStatic<JOptionPane> mockedOptionPane = mockStatic(JOptionPane.class)) {

            // Mock entrada do JOptionPane
            mockedOptionPane.when(() -> JOptionPane.showInputDialog(any(), anyString())).thenReturn(generoExistente);

            // Mock Conexao
            mockedConexao.when(Conexao::getConexao).thenReturn(mockConnection);

            // Mockando verificação de gênero existente (existeGenero) - assume que EXISTE
            ResultSet mockResultSetExists = mock(ResultSet.class);
            PreparedStatement mockPsExists = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(contains("SELECT COUNT(*) FROM genero"))).thenReturn(mockPsExists);
            when(mockPsExists.executeQuery()).thenReturn(mockResultSetExists);
            when(mockResultSetExists.next()).thenReturn(true);
            when(mockResultSetExists.getInt(1)).thenReturn(1); // Gênero EXISTE

            // Act
            Integer resultId = Genero.cadastrarGeneroDialog(null);

            // Assert
            assertNull(resultId); // Deve retornar null se não inserido
            verify(mockPreparedStatement, never()).executeUpdate(); // Inserção não deve ocorrer
            mockedOptionPane.verify(() -> JOptionPane.showMessageDialog(null, "Gênero já cadastrado: " + generoExistente));
        }
    }
}

