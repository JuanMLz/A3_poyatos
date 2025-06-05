
package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.List;
import java.awt.Window;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.swing.JOptionPane;

import dao.Conexao;

public class LocalTest {

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
    public void testGetLocais_RetornaListaDeLocais() throws Exception {
        // Arrange: Mock Conexao.getConexao() para retornar nosso mockConnection
        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class)) {
            mockedConexao.when(Conexao::getConexao).thenReturn(mockConnection);

            // Arrange: Configura o mock do ResultSet para retornar dois locais
            when(mockResultSet.next()).thenReturn(true, true, false); // Simula duas linhas
            when(mockResultSet.getInt("id")).thenReturn(101, 102);
            when(mockResultSet.getString("nome")).thenReturn("Estádio A", "Teatro B");

            // Act: Chama o método sendo testado
            List<Local> locais = Local.getLocais();

            // Assert: Verifica os resultados
            assertNotNull(locais);
            assertEquals(2, locais.size());

            assertEquals(101, locais.get(0).id);
            assertEquals("Estádio A", locais.get(0).nome);
            assertEquals(102, locais.get(1).id);
            assertEquals("Teatro B", locais.get(1).nome);

            // Verifica se o SQL correto foi preparado e executado
            verify(mockConnection).prepareStatement("SELECT id, nome FROM local ORDER BY nome");
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    public void testGetLocais_TrataSQLException() throws Exception {
        // Arrange: Mock Conexao.getConexao() para lançar SQLException
        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class);
             MockedStatic<JOptionPane> mockedOptionPane = mockStatic(JOptionPane.class)) {

            SQLException sqlException = new SQLException("Falha na conexão com DB");
            mockedConexao.when(Conexao::getConexao).thenThrow(sqlException);

            // Act: Chama o método sendo testado
            List<Local> locais = Local.getLocais();

            // Assert: Verifica se a lista está vazia e se JOptionPane foi chamado
            assertNotNull(locais);
            assertTrue(locais.isEmpty());
            mockedOptionPane.verify(() -> JOptionPane.showMessageDialog(null, "Erro ao obter locais: Falha na conexão com DB"));
        }
    }

    @Test
    public void testCadastrarLocalDialog_Sucesso() throws Exception {
        String novoLocalNome = "Arena C";
        int expectedId = 205;
        Window mockOwner = mock(Window.class); // Mock um proprietário de janela para o diálogo

        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class);
             MockedStatic<JOptionPane> mockedOptionPane = mockStatic(JOptionPane.class)) {

            // Mock entrada do JOptionPane
            mockedOptionPane.when(() -> JOptionPane.showInputDialog(any(Window.class), anyString())).thenReturn(novoLocalNome);

            // Mock Conexao e operações de banco de dados
            mockedConexao.when(Conexao::getConexao).thenReturn(mockConnection);

            // Mockando verificação de local existente (existeLocal) - assume que não existe
            ResultSet mockResultSetExists = mock(ResultSet.class);
            PreparedStatement mockPsExists = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(contains("SELECT COUNT(*) FROM local"))).thenReturn(mockPsExists);
            when(mockPsExists.executeQuery()).thenReturn(mockResultSetExists);
            when(mockResultSetExists.next()).thenReturn(true);
            when(mockResultSetExists.getInt(1)).thenReturn(0); // Local não existe

            // Mockando a operação de inserção (cadastrar)
            ResultSet mockResultSetGeneratedKeys = mock(ResultSet.class);
            when(mockConnection.prepareStatement(contains("INSERT INTO local"), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);
            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSetGeneratedKeys);
            when(mockResultSetGeneratedKeys.next()).thenReturn(true);
            when(mockResultSetGeneratedKeys.getInt(1)).thenReturn(expectedId);

            // Act
            Integer resultId = Local.cadastrarLocalDialog(mockOwner);

            // Assert
            assertEquals(expectedId, resultId);
            verify(mockPreparedStatement).setString(1, novoLocalNome);
            verify(mockPreparedStatement).executeUpdate();
            mockedOptionPane.verify(() -> JOptionPane.showMessageDialog(mockOwner, "📍 Local cadastrado: " + novoLocalNome));
        }
    }

    @Test
    public void testCadastrarLocalDialog_JaExiste() throws Exception {
        String localExistente = "Estádio A";
        Window mockOwner = mock(Window.class);

        try (MockedStatic<Conexao> mockedConexao = mockStatic(Conexao.class);
             MockedStatic<JOptionPane> mockedOptionPane = mockStatic(JOptionPane.class)) {

            // Mock entrada do JOptionPane
            mockedOptionPane.when(() -> JOptionPane.showInputDialog(any(Window.class), anyString())).thenReturn(localExistente);

            // Mock Conexao
            mockedConexao.when(Conexao::getConexao).thenReturn(mockConnection);

            // Mockando verificação de local existente (existeLocal) - assume que EXISTE
            ResultSet mockResultSetExists = mock(ResultSet.class);
            PreparedStatement mockPsExists = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(contains("SELECT COUNT(*) FROM local"))).thenReturn(mockPsExists);
            when(mockPsExists.executeQuery()).thenReturn(mockResultSetExists);
            when(mockResultSetExists.next()).thenReturn(true);
            when(mockResultSetExists.getInt(1)).thenReturn(1); // Local EXISTE

            // Act
            Integer resultId = Local.cadastrarLocalDialog(mockOwner);

            // Assert
            assertNull(resultId); // Deve retornar null se não inserido
            verify(mockPreparedStatement, never()).executeUpdate(); // Inserção não deve ocorrer
            mockedOptionPane.verify(() -> JOptionPane.showMessageDialog(null, "Local já cadastrado: " + localExistente)); // Nota: O código original usa proprietário nulo aqui
        }
    }

    @Test
    public void testCadastrarLocalDialog_InputCancelado() throws Exception {
        Window mockOwner = mock(Window.class);

        try (MockedStatic<JOptionPane> mockedOptionPane = mockStatic(JOptionPane.class)) {
            // Mock entrada do JOptionPane para retornar null (usuário cancelou)
            mockedOptionPane.when(() -> JOptionPane.showInputDialog(any(Window.class), anyString())).thenReturn(null);

            // Act
            Integer resultId = Local.cadastrarLocalDialog(mockOwner);

            // Assert
            assertNull(resultId);
            // Verifica se nenhuma interação com o banco de dados ocorreu
            verify(mockConnection, never()).prepareStatement(anyString());
        }
    }
}

