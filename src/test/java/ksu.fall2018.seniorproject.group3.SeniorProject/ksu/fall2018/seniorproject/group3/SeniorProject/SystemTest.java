package ksu.fall2018.seniorproject.group3.SeniorProject;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Black box tests on the system as a whole. This will mostly deal with overall system performance and accuracy of
 * processing.
 *
 * @author Mark Zeagler, Aisha Siddiqui, Drashtee Parmar, Michael Bowman, Shamita Hattangady
 * @version 1.0
 */
class SystemTest {
	// Test that the system can handle the required ~500 messages per second and twice the required ~500 messages per second
	@ParameterizedTest @ValueSource( ints = { 500, 1000 } ) void testSystemBandwidth( int messagesToHandle ) {
		// TODO Make sure all setup is done before here, so after the startTime is recorded, the system is fully functional
		long startTime = System.currentTimeMillis();
		for ( int i = 0; i < messagesToHandle; i++ ) {
			// TODO Send the messages
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
