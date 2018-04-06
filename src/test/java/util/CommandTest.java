package util;

import com.fastchat.FastChat.util.Command;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.testng.Assert.*;


public class CommandTest {

	private final Command command = new Command("help", "", (argv) -> {
		System.out.print("Do you really need help? ");
		return "Do you really need help? ";
	});

	@Test
	public void CommandNotNull() {
		assertNotNull(command.getName());
		assertNotNull(command.getHelp());
		assertNotNull(command.getBody());
	}

	@Test(dataProvider = "names", dataProviderClass = CommandTestData.class)
	public void TestCommandName(String name, Command command) {
		assertEquals(name, command.getName());
	}

	@Test
	public void CommandExecutedCorrectly() {
		AtomicBoolean variable = new AtomicBoolean(false);
		Command changesTheVariable = new Command("change_var", args -> {
			variable.set(true);
			return null;
		});

		assertFalse(variable.get());
		changesTheVariable.exec();
		assertTrue(variable.get());
	}

/*
	//Runs all tests in this class
	public static void main (String [] arg) {
		ITestNGListener tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { CommandTest.class });
		testng.addListener(tla);
		testng.setVerbose(2);
		testng.run();
	}
*/
}

class CommandTestData {
	@DataProvider(name = "names")
	public static Object[][] testCmdName() {
		return new Object[][]{
				{"help", new Command("help", "", null)},
				{"", new Command("", "", null)}
		};
	}
}
