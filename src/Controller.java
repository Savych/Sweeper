import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import resources.Box;
import resources.Coord;
import resources.GameController;
import resources.Ranges;

class Controller extends JFrame {
    private GameController game;

    private JPanel panel;
    private JLabel label;
    private JMenuBar menuBar;
    private JMenu options;
    private JMenuItem modeNoob;
    private JMenuItem modeNormal;
    private JMenuItem modePro;
    private JMenuItem restart;

    private int COLUMS = 9;
    private int ROWS = 9;
    private int BOMBS = 15;
    private final int IMAGE_SIZE = 50;

    Controller() {
        gameStart();
        setImages();
        initPanel();
        initLabel();
        initMenuBar();
        initFrame();
    }

    private void gameStart() {
        game = new GameController(COLUMS, ROWS, BOMBS);
        game.start();
    }

    private void initLabel() {
        label = new JLabel("Добро пожаловать! Кликните, чтобы начать!");
        add(label, BorderLayout.SOUTH);
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        options = new JMenu("Настройки");
        modeNoob = new JMenuItem("Режим игры: новичок");
        modeNormal = new JMenuItem("Режим игры: нормальный");
        modePro = new JMenuItem("Режим игры: профессионал");
        restart = new JMenuItem("Начать сначала");
        modeNoob.addActionListener(e -> {
            BOMBS = 15;
            ROWS = 9;
            COLUMS = 9;
            gameStart();
            label.setText(getMessage());
            panelSetSize();
        });
        modeNormal.addActionListener(e -> {
            BOMBS = 40;
            ROWS = 17;
            COLUMS = 17;
            gameStart();
            label.setText(getMessage());
            panelSetSize();
        });
        modePro.addActionListener(e -> {
            BOMBS = 55;
            ROWS = 20;
            COLUMS = 20;
            gameStart();
            label.setText(getMessage());
            panelSetSize();
        });
        restart.addActionListener(e -> {
            gameStart();
            label.setText(getMessage());
            panel.repaint();
        });
        options.add(restart);
        options.add(modeNoob);
        options.add(modeNormal);
        options.add(modePro);
        menuBar.add(options);
        add(menuBar, BorderLayout.NORTH);
    }

    private void panelSetSize() {
        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGE_SIZE,
                Ranges.getSize().y * IMAGE_SIZE));
        panel.repaint();
        pack();
        setLocationRelativeTo(null);
    }

    private void initPanel() {

        panel = new JPanel() {
            @Override
            protected void paintComponent (Graphics g) {
                super.paintComponent(g);
                for (Coord coord : Ranges.getAllCoords()) {
                    g.drawImage((Image) game.getBox(coord).image,
                            coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, this);
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x, y);
                if (e.getButton() == MouseEvent.BUTTON1)
                    game.pressLB(coord);
                if (e.getButton() == MouseEvent.BUTTON2)
                    game.start();
                if (e.getButton() == MouseEvent.BUTTON3)
                    game.pressRB(coord);
                label.setText(getMessage());
                panel.repaint();
            }
        });

        panelSetSize();
        add(panel);
    }

    private String getMessage() {
        switch (game.getState()) {
            case PLAYED: return "Вы нашли " + game.getCountOfFlags() + " бомб из " + BOMBS;
            case BOMBED: return "Вы проиграли, попробуйте снова!";
            case WINNER: return "Вы победили!";
            default: return "";
        }
    }

    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Сапёр");
        setResizable(false);
        setIconImage(getImage("icon"));
        pack();
        setLocationRelativeTo(null);
    }

    private void setImages() {
        for (Box box : Box.values())
            box.image = getImage(box.name().toLowerCase());
    }

    private Image getImage(String name) {
        String fileName = name + ".png";
        ImageIcon icon =
                new ImageIcon(getClass().getResource(fileName));
        return icon.getImage();
    }
}
