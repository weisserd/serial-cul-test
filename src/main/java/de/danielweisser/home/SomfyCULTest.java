package de.danielweisser.home;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.openmuc.jrxtx.Parity;
import org.openmuc.jrxtx.SerialPort;
import org.openmuc.jrxtx.SerialPortBuilder;

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
		SerialPort port = SerialPortBuilder.newBuilder("/dev/ttyACM0").setBaudRate(9600).setParity(Parity.NONE).build();
		OutputStream out = port.getOutputStream();

		String commandLEDOn = "l01\n";
		String commandLEDOff = "l00\n";

		String encryptionKey = "A1";
		// C - Command (1 = My, 2 = Up, 4 = Down, 8 = Prog)
		String command = "2";
		String rollingCode = "001D";
		String address = "000029";
		String somfyCommand = "Ys" + encryptionKey + command + "0" + rollingCode + address + "\n";
		out.write(somfyCommand.getBytes());
		out.close();
		port.close();

		log.info("Closed port");
	}

}
