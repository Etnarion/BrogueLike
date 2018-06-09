package client.view;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class ViewUtils {

    public static final TextColor DEFAULT_COLOR = new TextColor.RGB(48, 48, 48);

    public static void putString(String string, TextColor textColor, TextColor backgroundColor, Terminal terminal) throws IOException {
        for (char c : string.toCharArray()) {
            terminal.setBackgroundColor(backgroundColor);
            terminal.setForegroundColor(textColor);
            terminal.putCharacter(c);
            terminal.flush();
        }
    }

    public static void putFormattedString(String string, int size, TextColor textColor, TextColor backgroundColor, Terminal terminal) throws IOException {
        int j = 0;
        terminal.setBackgroundColor(backgroundColor);
        for (int i = 0; i < size; i++) {
            if (i < size - string.length()) {
                terminal.setForegroundColor(textColor);
                terminal.putCharacter(' ');
                j++;
            } else {
                terminal.setForegroundColor(textColor);
                terminal.putCharacter(string.charAt(i-j));
            }
        }
        terminal.flush();
    }
}
