package ksu.fall2018.seniorproject.group3.SeniorProject;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

/**
 * @author Mark Zeagler
 * @version 1.0
 */
public abstract class ConfigUser {

	private static Ini baseIni;
	private static Ini envIni;

	static {
		try {
			baseIni = new Ini( new File( "resources/config.ini" ) );
			String environment = baseIni.get( "Settings", "environment", String.class );
			envIni = new Ini( new File( "resources/" + environment + ".ini" ) );
		} catch ( IOException e ) {
			e.printStackTrace();
			// Go ahead and exit the system since it won't run correctly without the correct config
			System.exit( 1 );
		}
	}

	protected static Ini getBaseIni() {
		return baseIni;
	}

	protected static Ini getEnvIni(){
		return envIni;
	}
}
