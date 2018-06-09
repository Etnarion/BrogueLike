package client.view;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.Terminal;

import javax.swing.text.View;
import java.io.IOException;

public class MenuView {
    private final String MIDDLE_OFFSET = "                          ";
    public final int NBCHOICES = 2;
    private enum choices {NEWGAME, QUIT}
    private Terminal terminal = DungeonView.getDungeonView().getTerminal();
    private int currentChoice;

    public MenuView() throws IOException {
        currentChoice = 0;
        ViewUtils.putString("^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^\n" +
        "< @@@@@                                                                        >" + "\n" +
        "> @@@ @@@                                            @@@       @@@  @@@        <" + "\n" +
        "< @@@ @@@  @@@@@,@@@@@@  @@@@@@@ @@@@ @@@  @@@@@     @@@   @@@ @@@ @@   @@@@@  >" + "\n" +
        "> @@@@@@@  @@@  @@@@ @@@ @@@ @@@ @@@@ @@@ @@@ @@@ == @@@   @@@ @@@@@@  @@@ @@@ >" + "\n" +
        "< @@@ @@@  @@@  @@@@ @@@ @@@@@@@ @@@@ @@@ @@@@@      @@@   @@@ @@@ @@@ @@@@@   <" + "\n" +
        "> @@@@@@@  @@@   @@@@@@       @@  @@@@@@@  @@@@@@    @@@@@ @@@ @@@  @@@ @@@@@@ >" + "\n" +
        "<                          @@@@                                                <" + "\n" +
        "^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^v^" + "\n\n\n\n\n"
                , new TextColor.RGB(45, 255, 49), TextColor.ANSI.BLACK, terminal);

        ViewUtils.putString(MIDDLE_OFFSET, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("╔═════════════════════════╗\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString(MIDDLE_OFFSET, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("║      ", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("   NEW GAME   ", TextColor.ANSI.BLACK, TextColor.ANSI.WHITE, terminal);
        ViewUtils.putString("     ║\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString(MIDDLE_OFFSET, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("║                         ║\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString(MIDDLE_OFFSET, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("║           QUIT          ║\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString(MIDDLE_OFFSET, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("╚═════════════════════════╝\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
    }

    public void setNewGame() throws IOException {
        terminal.setCursorPosition(MIDDLE_OFFSET.length(), 14);
        ViewUtils.putString("║      ", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("   NEW GAME   ", TextColor.ANSI.BLACK, TextColor.ANSI.WHITE, terminal);
        ViewUtils.putString("     ║\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString(MIDDLE_OFFSET, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("║                         ║\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString(MIDDLE_OFFSET, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("║           QUIT          ║\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
    }

    public void setQuit() throws IOException {
        terminal.setCursorPosition(MIDDLE_OFFSET.length(), 14);
        ViewUtils.putString("║         NEW GAME        ║\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString(MIDDLE_OFFSET, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("║                         ║\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString(MIDDLE_OFFSET, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("║       ", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        ViewUtils.putString("    QUIT     ", TextColor.ANSI.BLACK, TextColor.ANSI.WHITE, terminal);
        ViewUtils.putString("     ║\n", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
    }

    public void showWaitMessage() throws IOException {
        terminal.setCursorPosition(MIDDLE_OFFSET.length() + 2, 21);
        ViewUtils.putString("Waiting for player 2...", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
    }

    public int getCurrentChoice() {
        return currentChoice;
    }

    public int nextChoice() {
        currentChoice = (currentChoice + 1) % NBCHOICES;
        return currentChoice;
    }

    public int previousChoice() {
        if (currentChoice == 0)
            currentChoice = NBCHOICES-1;
        else
            currentChoice--;
        return currentChoice;
    }

    public KeyStroke getInput() throws IOException {
        KeyStroke key = terminal.readInput();
        while(key == null) {
            key = terminal.readInput();
        }
        return key;
    }

    public void close() throws IOException {
        terminal.close();
    }
}
