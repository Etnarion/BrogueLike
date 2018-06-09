package client.view;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import model.Dungeon;
import model.elements.entities.Hero;

import java.io.IOException;

public class HeroView {
    private enum Positions {TOP, HEALTH, DAMAGE, RANGE, GOLD, WEAPON, BOTTOM}
    private final static int OFFSET_MENU = 2;
    private final static int OFFSET_LABELS = 10;
    private final static int OFFSET_RIGHT = 8;
    Terminal terminal;
    Hero hero;

    private static HeroView heroView = new HeroView();

    HeroView() {
        terminal = DungeonView.getDungeonView().getTerminal();
        hero = Dungeon.getDungeon().getHero();
    }

    public void showTopBorder() throws IOException {
        terminal.setCursorPosition(Dungeon.DUNGEON_SIZE*2 + OFFSET_MENU, OFFSET_MENU);
        ViewUtils.putString("╔═════════════════════════╗", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
    }

    public void showBottomBorder() throws IOException {
        terminal.setCursorPosition(Dungeon.DUNGEON_SIZE*2 + OFFSET_MENU, OFFSET_MENU + Positions.BOTTOM.ordinal());
        ViewUtils.putString("╚═════════════════════════╝", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
    }

    public void showHealth() throws IOException {
        synchronized (DungeonView.getDungeonView()) {
            terminal.setCursorPosition(Dungeon.DUNGEON_SIZE * 2 + OFFSET_MENU, OFFSET_MENU + Positions.HEALTH.ordinal());
            ViewUtils.putString("║ ", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString("Health : ", OFFSET_LABELS, new TextColor.RGB(255, 255, 255), TextColor.ANSI.BLACK, terminal);
            String currentHealth = String.valueOf(hero.getHealth());
            String maxHealth = String.valueOf(hero.getMaxHealth());
            TextColor healthColor = new TextColor.RGB(188, 1, 1);
            ViewUtils.putFormattedString(currentHealth, 3, healthColor, TextColor.ANSI.BLACK, terminal);
            ViewUtils.putString("/", healthColor, TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString(maxHealth, 3, healthColor, TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString("║", OFFSET_RIGHT, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        }
    }

    public void showDamage() throws IOException {
        synchronized (DungeonView.getDungeonView()) {
            terminal.setCursorPosition(Dungeon.DUNGEON_SIZE * 2 + OFFSET_MENU, OFFSET_MENU + Positions.DAMAGE.ordinal());
            ViewUtils.putString("║ ", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString("Damage : ", OFFSET_LABELS, new TextColor.RGB(255, 255, 255), TextColor.ANSI.BLACK, terminal);
            String damage = String.valueOf(hero.getDamage());
            ViewUtils.putFormattedString(damage, 7, new TextColor.RGB(255, 255, 255), TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString("║", OFFSET_RIGHT, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        }
    }

    public void showRange() throws IOException {
        synchronized (DungeonView.getDungeonView()) {
            terminal.setCursorPosition(Dungeon.DUNGEON_SIZE * 2 + OFFSET_MENU, OFFSET_MENU + Positions.RANGE.ordinal());
            ViewUtils.putString("║ ", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString("Range : ", OFFSET_LABELS, new TextColor.RGB(255, 255, 255), TextColor.ANSI.BLACK, terminal);
            String range = String.valueOf(hero.getRange());
            ViewUtils.putFormattedString(range, 7, new TextColor.RGB(255, 255, 255), TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString("║", OFFSET_RIGHT, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        }
    }

    public void showGold() throws IOException {
        synchronized (DungeonView.getDungeonView()) {
            terminal.setCursorPosition(Dungeon.DUNGEON_SIZE * 2 + OFFSET_MENU, OFFSET_MENU + Positions.GOLD.ordinal());
            ViewUtils.putString("║ ", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString("Gold : ", OFFSET_LABELS, new TextColor.RGB(255, 255, 255), TextColor.ANSI.BLACK, terminal);
            String gold = String.valueOf(hero.getGold());
            ViewUtils.putFormattedString(gold, 7, new TextColor.RGB(255, 200, 2), TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString("║", OFFSET_RIGHT, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        }
    }

    public void showWeapon() throws IOException {
        synchronized (DungeonView.getDungeonView()) {
            terminal.setCursorPosition(Dungeon.DUNGEON_SIZE * 2 + OFFSET_MENU, OFFSET_MENU + Positions.WEAPON.ordinal());
            ViewUtils.putString("║ ", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString("Weapon : ", OFFSET_LABELS, new TextColor.RGB(255, 255, 255), TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString(hero.getWeapon().getName(), 7, new TextColor.RGB(255, 255, 255), TextColor.ANSI.BLACK, terminal);
            ViewUtils.putFormattedString("║", OFFSET_RIGHT, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, terminal);
        }
    }

    public static HeroView getHeroView() {
        return heroView;
    }

    public synchronized void showStatus() throws IOException {
        HeroView heroView = HeroView.getHeroView();
        heroView.showTopBorder();
        heroView.showHealth();
        heroView.showDamage();
        heroView.showRange();
        heroView.showGold();
        heroView.showWeapon();
        heroView.showBottomBorder();
    }
}
