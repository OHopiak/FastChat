import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class ClientTest {

	@Test
	public void testPrintln(){
		System.out.println("Hello");
		assertTrue(true);
	}
}
