package de.danielweisser.home;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fazecast.jSerialComm.SerialPort;

/**
 * A simple test program to send commands to Somfy shutter via a CUL.
 * Documentation can be found at http://culfw.de/commandref.html#cmd_Y
 *
 * @author Daniel Weisser
 *
 */
public class SomfyCULTest {

    private static final Logger log = Logger.getLogger(SomfyCULTest.class.getName());

    public static void main(String[] args) throws IOException {
        log.info("Opening port ttyACM0");
        SerialPort comPort = SerialPort.getCommPort("/dev/ttyACM0");
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        OutputStream out = comPort.getOutputStream();
        try {
            String encryptionKey = "A1";
            // C - Command (1 = My, 2 = Up, 4 = Down, 8 = Prog)
            String command = "4";
            String rollingCode = "07F2";
            String address = "000029";
            String somfyCommand = "Ys" + encryptionKey + command + "0" + rollingCode + address + "\n";
            log.info("Send code " + somfyCommand);
            out.write(somfyCommand.getBytes());
            long newRollingCode = Long.decode("0x" + rollingCode) + 1;
            log.info("new rolling code: " + String.format("%04X", newRollingCode));
            out.close();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        comPort.closePort();
        log.info("Closed port");
    }

}
