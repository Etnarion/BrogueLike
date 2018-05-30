package client.view;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import model.Dungeon;
import model.elements.entities.Hero;

import java.awt.*;
import java.io.IOException;

public class HeroView {
    private final static int OFFSET_MENU = 2;
    Terminal terminal;
    Hero hero;

    private static HeroView heroView = new HeroView();

    HeroView() {
        terminal = DungeonView.getDungeonView().getTerminal();
        hero = Dungeon.getDungeon().getHero();
    }

    public void showHealth() throws IOException {
        terminal.setCursorPosition(Dungeon.DUNGEON_SIZE*2 + OFFSET_MENU, OFFSET_MENU);
        String currentHealth = String.valueOf(hero.getHealth());
        String maxHealth = String.valueOf(hero.getMaxHealth());
        formattedString(3, currentHealth);
        terminal.setBackgroundColor(new TextColor.RGB(0,0,0));
        terminal.putCharacter('/');
        formattedString(3, maxHealth);
        terminal.flush();
    }

    private void formattedString(int size, String string) throws IOException {
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (i < size - string.length()) {
                terminal.setBackgroundColor(new TextColor.RGB(0,0,0));
                terminal.putCharacter('0');
                j++;
            } else {
                terminal.setBackgroundColor(new TextColor.RGB(0,0,0));
                terminal.putCharacter(string.charAt(i-j));
            }
        }
    }

    public void showWeapon() {

    }

    public void showScore() {

    }

    public static HeroView getHeroView() {
        return heroView;
    }
}
