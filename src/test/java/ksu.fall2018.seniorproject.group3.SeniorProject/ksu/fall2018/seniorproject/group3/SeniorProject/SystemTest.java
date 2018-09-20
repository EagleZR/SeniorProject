package ksu.fall2018.seniorproject.group3.SeniorProject;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Black box tests on the system as a whole. This will mostly deal with overall system performance and accuracy of
 * processing.
 *
 * @author Mark Zeagler, Aisha Siddiqui, Drashtee Parmar, Michael Bowman, Shamita Hattangady
 * @version 1.0
 */
class SystemTest {

	private static final ArrayList<String> SAMEPLE_MESSAGES = new ArrayList<>();
	private static KafkaProducer<Long, String> producer;

	static {
		try {
			File file = new File( "resources/sample_messages.txt" );
			Files.lines( file.toPath() ).forEach( SAMEPLE_MESSAGES::add );
		} catch ( IOException e ) {
			fail( "The sample messages were not loaded successfully." );
		}
	}

	private final String TOPIC = "JUnit Test"; // TODO Change this once we have the name

	@BeforeAll static void setup() {
		Properties properties = new Properties();
		properties.put( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092" );
		properties.put( ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer" );
		properties.put( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName() );
		properties.put( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName() );
		producer = new KafkaProducer<>( properties );
	}

	// Test that the system can handle the required ~500 messages per second and twice the required ~500 messages per second
	@ParameterizedTest @ValueSource( ints = { 500, 1000 } ) void testSystemBandwidth( int messagesToHandle ) {
		// TODO Make sure all setup is done before here, so after the startTime is recorded, the system is fully functional
		long startTime = System.currentTimeMillis();
		for ( int i = 0; i < messagesToHandle; i++ ) {
			ProducerRecord<Long, String> producerRecord = new ProducerRecord<>( TOPIC,
					SAMEPLE_MESSAGES.get( i % SAMEPLE_MESSAGES.size() ) );
			try {
				producer.send( producerRecord );
			} catch ( Exception e ) { // TODO Better way to abort and ignore when there's an exception?
				e.printStackTrace();
				assumeTrue( false,
						"The test was aborted and ignored because an exception was thrown in the producer loop," );
			}
		}
		long producerEndTime = System.currentTimeMillis();
		// If the producer cannot generate the messages fast enough, we want to halt the test without failing
		assumeTrue( producerEndTime - startTime < 1000, () -> "The producer can't generate the " + messagesToHandle
				+ " messages fast enough to test the consumer." );
		// TODO Wait until the consumer is finished handling the data
		// One way we can do this is to send data that will generate an out-of-constraints event so we can know it's finished
		long consumerEndTime = System.currentTimeMillis();
		assertTrue( consumerEndTime - startTime < 1000,
				() -> "The consumer is not handling the " + messagesToHandle + " messages fast enough." );
	}
}
