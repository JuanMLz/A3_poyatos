package model;

// Comando para iniciar os testes
// java -cp "bin;lib/*" org.junit.platform.console.ConsoleLauncher --scan-class-path


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShowTest {

    @Test
    public void testeSimples() {
        assertEquals(4, 2 + 2);
    }
}
