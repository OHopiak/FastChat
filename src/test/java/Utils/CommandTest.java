package Utils;
import com.fastchat.FastChat.util.Command;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


public class CommandTest {

	Command command = new Command("help","", (argv) -> {
		System.out.print("Do you really need help? ");
		return null;
	});

	@Test
	public void CommandNotNull(){
		assertNotNull(command.getName());
		assertNotNull(command.getHelp());
		assertNotNull(command.getBody());
	}

	@Test
	public void CommandEquals(){
		assertEquals("help", command.getName());
		assertEquals("",command.getHelp());

	}
}
