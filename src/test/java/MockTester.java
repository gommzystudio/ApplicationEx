import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.gommzy.applicationex.Applicationex;
import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

public class MockTester {
    private ServerMock server;
    private Applicationex plugin;

    @Before
    public void setUp()
    {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Applicationex.class);
    }

    @After
    public void tearDown()
    {
        MockBukkit.unmock();
    }

    @Test
    public void test() {
        PlayerMock player = server.addPlayer("Gommzy");

        //Teste alle Commands in zufälliger Reihenfolge:
        ArrayList<String> commands = new ArrayList<>();
        commands.add("ae info");
        commands.add("ae info gommzy");
        commands.add("ae user");
        //commands.add("ae user gommzy addgroup test"); MockBukkit supported keine GUIS
        commands.add("ae user gommzy removegroup test");
        commands.add("ae groups");
        commands.add("ae groups list");
        commands.add("ae groups remove test");
        commands.add("ae groups edit test prefix &c");
        commands.add("ae groups edit test permissions list");
        commands.add("ae groups edit test permissions add *");
        commands.add("ae groups edit test permissions remove *");
        commands.add("ae sign");
        //commands.add("ae sign Gommzy"); MockBukkit supported kein #getTargetBlock()

        for (int i = 0; i < 10; i++) {
            Collections.shuffle(commands);
            for (String command : commands) {
                player.performCommand(command);
            }
        }

        player.setOp(true);
        player.chat("Hey!");

        //Teste Commands nochmal, nur mit OP Rechten
        for (int i = 0; i < 10; i++) {
            Collections.shuffle(commands);
            for (String command : commands) {
                player.performCommand(command);
            }
        }

        player.remove();
    }
}
