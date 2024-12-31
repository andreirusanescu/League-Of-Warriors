import api.CellEntityType;
import entities.abilities.Spell;
import entities.characters.Character;
import entities.characters.Enemy;
import entities.characters.Entity;
import grid.Cell;
import grid.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class GameGUI {
    /* Main frame */
    private JFrame frame;
    private JPanel gridPanel;
    private JPanel controlsPanel;
    private JLabel statsLabel;
    private JLabel messageLabel;
    private Character selectedCharacter;
    private final Game game;
    private JButton[][] cellButtons;
    private JLabel enemyInfoLabel;
    private JLabel playerInfoLabel;
    private Enemy currentEnemy;

    public GameGUI(Game game) {
        this.game = game;
        setupUI();
    }

    /**
     * This function sets up the main frame and
     * asks the user to choose a character from
     * the ones in their account
     */
    private void setupUI() {
        /* Sets up the main frame */
        frame = new JFrame("Game GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        /* Panel for character selection */
        JPanel characterSelectionPanel = new JPanel();

        /* Grid Layout for stats */
        characterSelectionPanel.setLayout(new GridLayout(1, 0, 10, 10));

        ArrayList<Character> characters = game.getSelectedAccount().getCharacters();

        /* For each character, add a panel with their image and stats */
        for (Character character : characters) {
            JPanel characterPanel = new JPanel();
            characterPanel.setLayout(new BorderLayout());

            /* Add an image for each character */
            JLabel characterImageLabel = new JLabel();
            ImageIcon characterImage = new ImageIcon(character.getImagePath());
            Image scaledImage = characterImage.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            characterImageLabel.setIcon(new ImageIcon(scaledImage));
            /* Image at the top */
            characterPanel.add(characterImageLabel, BorderLayout.NORTH);

            /* Panel with character's stats (name, health, attack, defense)
               6 rows, 2 columns, 5px horizontal/vertical gaps
             */
            JPanel statsPanel = new JPanel(new GridLayout(7, 2, 5, 5));

            /* Add stats with label and text field side by side */
            statsPanel.add(new JLabel("Name: "));
            statsPanel.add(createStatTextField(character.getName()));
            statsPanel.add(new JLabel("Role: "));
            statsPanel.add(createStatTextField(character.getRole().toString()));
            statsPanel.add(new JLabel("Strength: "));
            statsPanel.add(createStatTextField(String.valueOf(character.getStrength())));
            statsPanel.add(new JLabel("Dexterity: "));
            statsPanel.add(createStatTextField(String.valueOf(character.getDexterity())));
            statsPanel.add(new JLabel("Charisma: "));
            statsPanel.add(createStatTextField(String.valueOf(character.getCharisma())));
            statsPanel.add(new JLabel("Experience: "));
            statsPanel.add(createStatTextField(String.valueOf(character.getExperience())));
            statsPanel.add(new JLabel("Level: "));
            statsPanel.add(createStatTextField(String.valueOf(character.getLevel())));

            /* Stats panel under the image */
            characterPanel.add(statsPanel, BorderLayout.CENTER);

            /* Add a select button for each character */
            JButton selectButton = new JButton("Select");
            selectButton.addActionListener( new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCharacter = character;
                    game.setCurrentCharacter(selectedCharacter);
                    JOptionPane.showMessageDialog(frame, "You chose: " + selectedCharacter.getName());
                    frame.getContentPane().removeAll();
                    setupGameUI();
                }
            });

            characterPanel.add(selectButton, BorderLayout.SOUTH);

            /* Add each character panel to the main selection panel */
            characterSelectionPanel.add(characterPanel);
        }

        /* Add the character selection panel to the frame */
        frame.add(characterSelectionPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     *  Helper method to create non-editable text fields.
     * @param text, the text of the text-field
     */
    private JTextField createStatTextField(String text) {
        JTextField textField = new JTextField(text);
        /* Make the text field non-editable */
        textField.setEditable(false);
        /* Resize textfield */
        textField.setPreferredSize(new Dimension(150, 25));
        /* Remove border */
        textField.setBorder(BorderFactory.createEmptyBorder());

        textField.setBackground(Color.WHITE);
        /* Set the cursor to the color of the background, so it cannot be seen */
        textField.setCaretColor(Color.WHITE);
        /* Prevent focus to hide caret entirely */
        textField.setFocusable(false);
        return textField;
    }

    /**
     * Grid page with the stats and the control buttons
     */
    private void setupGameUI() {
        /* The control panel (NORTH, SOUTH etc.) */
        controlsPanel = new JPanel();

        /* 6 statistics */
        controlsPanel.setLayout(new GridLayout(6, 1));

        /* The statistics: */
        String text = "<html>Health: " + selectedCharacter.getHealthBar()
                + "<br>Mana: " + selectedCharacter.getBlessing() + "<br>Level: "
                + selectedCharacter.getLevel() + "<br>Xp: " + selectedCharacter.getExperience()
                + "</html>";

        /* The stats label */
        statsLabel = new JLabel(text);
        controlsPanel.add(statsLabel);

        JButton upButton = new JButton("North");
        JButton downButton = new JButton("South");
        JButton leftButton = new JButton("West");
        JButton rightButton = new JButton("East");

        /* Add action listeners for the buttons */
        upButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        moveCharacter(-1, 0);
                    }
                }
        );
        downButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        moveCharacter(1, 0);
                    }
                }
        );
        leftButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        moveCharacter(0, -1);
                    }
                }
        );
        rightButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        moveCharacter(0, 1);
                    }
                }
        );

        /* Add the buttons to the panel */
        controlsPanel.add(upButton);
        controlsPanel.add(downButton);
        controlsPanel.add(leftButton);
        controlsPanel.add(rightButton);

        Random random = new Random();

        /* Grid configuration */
        gridPanel = new JPanel();
        game.setGrid(Grid.generateGrid(random.nextInt(10),
                random.nextInt(10), false));
        game.getGrid().setCurrentCharacter(selectedCharacter);
        gridPanel.setLayout(new GridLayout(game.getGrid().getRows(), game.getGrid().getCols()));

        /* The grid is held as a matrix of buttons */
        cellButtons = new JButton[game.getGrid().getRows()][game.getGrid().getCols()];

        for (int row = 0; row < game.getGrid().getRows(); row++) {
            for (int col = 0; col < game.getGrid().getCols(); col++) {
                JButton cellButton = new JButton();

                /* Gray border for the buttons */
                cellButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                cellButton.setFocusPainted(false);

                /*
                   Leaves the content of the button transparent,
                   so it is filled with the icon
                 */
                cellButton.setContentAreaFilled(false);
                cellButton.setOpaque(false);

                Cell currentCell = game.getGrid().get(row).get(col);
                ImageIcon icon;

                /* Gets the icon of the cell */
                if (currentCell == game.getGrid().getCurrentCell())
                    icon = new ImageIcon("src/images/stickman.png");
                else
                    icon = new ImageIcon("src/images/question.png");
                Image image = icon.getImage();

                /* Scale the image */
                Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                cellButton.setIcon(new ImageIcon(resizedImage));

                /* Adds the button to the matrix */
                cellButtons[row][col] = cellButton;
                gridPanel.add(cellButton);
            }
        }

        /* Adds the control panel to the left and
           the grid panel to the center / right
         */
        frame.add(controlsPanel, BorderLayout.WEST);
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Updates the stats in the control panel
     * after a fight or a sanctuary cell.
     */
    private void updateStatsLabel() {
        String text = "<html>Health: " + selectedCharacter.getHealthBar()
                + "<br>Mana: " + selectedCharacter.getBlessing()
                + "<br>Level: " + selectedCharacter.getLevel()
                + "<br>Xp: " + selectedCharacter.getExperience()
                + "</html>";
        statsLabel.setText(text);
        statsLabel.revalidate();
        statsLabel.repaint();
    }

    /**
     * Moves the character on the grid.
     * @param deltaX - moves the character on the rows (-1, 0, 1)
     * @param deltaY - moves the character on the columns (-1, 0, 1)
     */
    private void moveCharacter(int deltaX, int deltaY) {
        Grid grid = game.getGrid();
        Cell currentCell = grid.getCurrentCell();
        int newRow = currentCell.getX() + deltaX;
        int newCol = currentCell.getY() + deltaY;

        /* Checks to be within bounds */
        if (newRow < 0 || newRow >= grid.getRows() || newCol < 0 || newCol >= grid.getCols()) {
            System.out.println("Move not allowed. Try again!");
            return;
        }

        /* Sets the current cell as visited */
        currentCell.setType(CellEntityType.VISITED);
        updateCellImage(currentCell, "src/images/visit.png");

        /* Moves the character on the current cell */
        Cell newCell = grid.get(newRow).get(newCol);
        grid.setCurrentCell(newCell);

        /* Process the type of this cell */
        processCell(newCell);

        /* Updates the image of the cell */
        updateCellImage(newCell, "src/images/stickman.png");

        /* Refresh the UI */
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Processes the logic for the current cell.
     * @param cell - current cell
     */
    private void processCell(Cell cell) {
        switch (cell.getType()) {
            case ENEMY -> startFightUI();
            case SANCTUARY -> sanctuaryCell();
            case PORTAL -> portalCell();
        }
        /* Sets the type of the cell */
        cell.setType(CellEntityType.PLAYER);
    }

    /**
     * Called when character gets to
     * a SANCTUARY CELL.
     */
    private void sanctuaryCell() {
        Random rand = new Random();

        /* Regenerate life */
        int regeneratedHealth = rand.nextInt(selectedCharacter.getMaxHealth());
        selectedCharacter.regenerateHealth(regeneratedHealth);

        /* Regenerate mana */
        int regeneratedBlessing = rand.nextInt(selectedCharacter.getMaxBlessing());
        selectedCharacter.regenerateBlessing(regeneratedBlessing);

        /* Sets the type of the cell */
        game.getGrid().getCurrentCell().setType(CellEntityType.PLAYER);
        String message = "<html>Sanctuary... Regenerating!<br>" +
                "Regenerated health: +" + regeneratedHealth + "<br>" +
                "Regenerated blessing: +" + regeneratedBlessing + "</html>";

        /* Notifies the user */
        JOptionPane.showMessageDialog(
                frame,
                message,
                "Sanctuary Cell",
                JOptionPane.INFORMATION_MESSAGE
        );

        /* Refresh the UI */
        refreshGridUI();
        updateStatsLabel();
    }

    /**
     * Called when a character gets
     * to a PORTAL CELL.
     */
    private void portalCell() {
        /* Increase experience */
        selectedCharacter.increaseExperience(selectedCharacter.getLevel() * 5);
        /* Increase level and number of games */
        selectedCharacter.setLevel(selectedCharacter.getLevel() + 1);
        game.getSelectedAccount().setNumberOfGames(game.getSelectedAccount().getNumberOfGames() + 1);

        JOptionPane.showMessageDialog(
                frame,
                "<html>Portal...<br>" +
                        "Level up! Current level: " + selectedCharacter.getLevel() + "<br>" +
                        "New stats: Damage - " + selectedCharacter.getNormalDamage() + ", Strength - " +
                        selectedCharacter.getStrength() + ", Charisma - " + selectedCharacter.getCharisma() +
                        ", Dexterity - " + selectedCharacter.getDexterity() + "</html>",
                "Portal Notification",
                JOptionPane.INFORMATION_MESSAGE
        );

        selectedCharacter.regenerateHealth(selectedCharacter.getMaxHealth());
        selectedCharacter.regenerateBlessing(selectedCharacter.getMaxBlessing());
        selectedCharacter.getAbilities().removeAll(selectedCharacter.getAbilities());
        selectedCharacter.addAbilities();
        frame.dispose();

        /* Return to the start */
        setupUI();
    }

    /**
     *  Refreshes the grid.
     */
    private void refreshGridUI() {
        frame.getContentPane().removeAll();
        frame.add(controlsPanel, BorderLayout.WEST);
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Updates the image of a cell.
     * @param cell - the cell to be updated
     * @param imagePath - path to the new image
     */
    private void updateCellImage(Cell cell, String imagePath) {
        JButton button = cellButtons[cell.getX()][cell.getY()];
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(resizedImage));
    }

    /**
     * Handler for each round of the fight.
     */
    private void startFightUI() {
        if (currentEnemy == null) {
            currentEnemy = new Enemy(selectedCharacter.getNormalDamage(), selectedCharacter.getMaxHealth());
        }

        /* Create the panel for the fight */
        JPanel fightPanel = new JPanel(new BorderLayout());

        /* Panels for player and enemy */
        JPanel playerPanel = createCharacterPanel();
        JPanel enemyPanel = createEnemyPanel();

        /* Panel for the actions */
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

        /* Default attack button */
        JButton attackButton = new JButton("Normal Attack");
        attackButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        executePlayerAction("attack", currentEnemy, 0);
                    }
                }
        );
        actionPanel.add(attackButton);

        /* Ability button */
        JButton abilityButton = new JButton("Use Ability");
        abilityButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showAbilityOptions();
                    }
                }
        );
        actionPanel.add(abilityButton);

        /* Message panel for the fight */
        JPanel messagePanel = new JPanel();
        messageLabel = new JLabel("The fight begins!");
        messagePanel.add(messageLabel);

        fightPanel.add(playerPanel, BorderLayout.WEST);
        fightPanel.add(actionPanel, BorderLayout.CENTER);
        fightPanel.add(enemyPanel, BorderLayout.EAST);
        fightPanel.add(messagePanel, BorderLayout.SOUTH);

        /* Sets the new frame */
        frame.getContentPane().removeAll();
        frame.add(fightPanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Creates the panel for the character.
     */
    private JPanel createCharacterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Player");
        /* Saves the label globally to update it later */
        playerInfoLabel = new JLabel(selectedCharacter.getInformation());

        ImageIcon originalIcon = new ImageIcon(selectedCharacter.getImagePath());

        /* 35% width */
        int imageWidth = (int) (frame.getWidth() * 0.35);
        int imageHeight = (int) (frame.getHeight() * 0.6);
        Image resizedImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));

        panel.add(titleLabel);
        panel.add(imageLabel);
        panel.add(playerInfoLabel);
        return panel;
    }


    /**
     * Creates the panel for the enemy.
     */
    private JPanel createEnemyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Enemy");

        /* Saves the label globally to update it later */
        enemyInfoLabel = new JLabel(currentEnemy.getInformation());

        int imageWidth = (int) (frame.getWidth() * 0.35);
        int imageHeight = (int) (frame.getHeight() * 0.6);

        ImageIcon originalIcon = new ImageIcon("src/images/villain.png");
        Image resizedImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));

        panel.add(titleLabel);
        panel.add(imageLabel);
        panel.add(enemyInfoLabel);
        return panel;
    }

    /**
     * Handler for using the ability.
     * @param source, the attacker
     * @param target, the attacked
     * @param ability, the ability of the attacker
     */
    private void useAbilityGUI(Entity source, Entity target, Spell ability) {
        if (ability != null && ability.getCost() <= source.getBlessing()) {
            int oldHealth = target.getHealthBar();
            source.accept(ability);
            source.setBlessing(source.getBlessing() - ability.getCost());
            /* Remove ability after using it */
            source.getAbilities().remove(ability);
            target.accept(ability);
            if (source == selectedCharacter) {
                if (oldHealth != target.getHealthBar())
                    messageLabel.setText("Enemy received " + ability.getDamage() + " damage!");
                else
                    messageLabel.setText("Enemy avoided the attack!");
            }
        }
    }

    /**
     * Handler for using the default attack.
     * @param source, the attacker
     * @param target, the attacked
     */
    private void attackGUI(Entity source, Entity target) {
        int oldHealth = target.getHealthBar();
        source.attackEnemy(target);
        if (source == selectedCharacter) {
            if (oldHealth != target.getHealthBar())
                messageLabel.setText("Enemy received " + source.getNormalDamage() + " damage!");
            else
                messageLabel.setText("Enemy avoided the attack!");
        }
    }

    /**
     * Executes the action of the player.
     * @param action, the type of action
     * @param enemy, the target of the action
     * @param index, the index of the ability
     *               if action is "ability"
     */
    private void executePlayerAction(String action, Enemy enemy, int index) {
        if (action.equals("attack")) {
            attackGUI(selectedCharacter, currentEnemy);
        } else if (action.equals("ability")) {
            useAbilityGUI(selectedCharacter, currentEnemy, selectedCharacter.getAbilities().get(index));
            showAbilityOptions();
        }
        messageLabel.revalidate();
        messageLabel.repaint();

        if (!enemy.isAlive()) {
            JOptionPane.showMessageDialog(frame, "You defeated the enemy!");
            endFight(true);
            return;
        } else {
            /* Enemy's turn */
            if (!enemy.getAbilities().isEmpty())
                useAbilityGUI(currentEnemy, selectedCharacter,
                        currentEnemy.getAbilities().get(currentEnemy.getAbilities().size() - 1));
            else
                attackGUI(currentEnemy, selectedCharacter);

            if (!selectedCharacter.isAlive()) {
                JOptionPane.showMessageDialog(frame, "Game Over! You died.");
                endFight(false);
                return;
            }
        }
        updateStatsLabel();
        enemyInfoLabel.setText(currentEnemy.getInformation());
        playerInfoLabel.setText(selectedCharacter.getInformation());
        refreshFightUI();
    }

    /**
     * Shows the buttons for each ability.
     */
    private void showAbilityOptions() {
        /* Creates a frame for the abilities */
        JFrame abilitiesFrame = new JFrame("Select Ability");
        abilitiesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        abilitiesFrame.setSize(600, 400);
        abilitiesFrame.setLayout(new BorderLayout());

        /* Panel with GridLayout for abilities, dynamic rows and 3 columns max */
        JPanel abilityGridPanel = new JPanel(new GridLayout(0, 3, 10, 10));

        /* Add each ability as a card with an icon, details, and a select button */
        for (Spell ability : selectedCharacter.getAbilities()) {
            JPanel abilityCard = new JPanel(new BorderLayout());
            abilityCard.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            /* Icon for the ability */
            JLabel abilityIcon = new JLabel();
            ImageIcon icon = new ImageIcon(ability.getIcon());
            Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            abilityIcon.setIcon(new ImageIcon(scaledImage));
            abilityIcon.setHorizontalAlignment(SwingConstants.CENTER);
            abilityCard.add(abilityIcon, BorderLayout.CENTER);

            JLabel abilityInfo = new JLabel(
                    "<html>Name: " + ability.getClass().getSimpleName()
                       + "<br>Mana cost: " + ability.getCost()
                       + "<br>Damage: " + ability.getDamage() + "</html>"
            );
            abilityInfo.setHorizontalAlignment(SwingConstants.CENTER);
            abilityCard.add(abilityInfo, BorderLayout.SOUTH);

            /* Select button for the ability */
            JButton selectButton = new JButton("SELECT");
            selectButton.setHorizontalAlignment(SwingConstants.CENTER);
            selectButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (ability.getCost() <= selectedCharacter.getBlessing()) {
                                /* Execute the selected ability */
                                executePlayerAction("ability", currentEnemy,
                                        selectedCharacter.getAbilities().indexOf(ability));

                                /* Close all frames displaying abilities */
                                abilitiesFrame.dispose();

                                /* Refocus on the main frame */
                                frame.toFront();
                                frame.repaint();
                            } else {
                                JOptionPane.showMessageDialog(
                                        abilitiesFrame,
                                        "Not enough mana for "
                                                + ability.getClass().getSimpleName()
                                                + "!\nPlease try a normal attack.",
                                        "Insufficient Mana",
                                        JOptionPane.WARNING_MESSAGE
                                );
                            }
                        }
                    });
            abilityCard.add(selectButton, BorderLayout.NORTH);

            /* Add the ability card to the grid panel */
            abilityGridPanel.add(abilityCard);
        }

        /* Add the grid panel to the frame and make it scrollable */
        abilitiesFrame.add(new JScrollPane(abilityGridPanel), BorderLayout.CENTER);
        abilitiesFrame.setVisible(true);
    }

    /**
     * Updates the UI after every round.
     */
    private void refreshFightUI() {
        playerInfoLabel.setText(selectedCharacter.getInformation());
        enemyInfoLabel.setText(currentEnemy.getInformation());
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Finishes the fight and returns to the grid.
     * @param playerWon, whether the player won or not
     */
    private void endFight(boolean playerWon) {
        if (playerWon) {
            game.getGrid().getCurrentCell().setType(CellEntityType.PLAYER);

            /* Doubles the health and mana */
            selectedCharacter.regenerateHealth(selectedCharacter.getHealthBar());
            selectedCharacter.regenerateBlessing(selectedCharacter.getBlessing());

            /* Increases experience */
            selectedCharacter.increaseExperience(selectedCharacter.getExperienceBound());
            /* Increases the enemies killed */
            selectedCharacter.setEnemiesKilled(selectedCharacter.getEnemiesKilled() + 1);
        } else {
            /* Character lost */
            selectedCharacter.regenerateHealth(selectedCharacter.getMaxHealth());
            selectedCharacter.regenerateBlessing(selectedCharacter.getMaxBlessing());
            selectedCharacter.getAbilities().removeAll(selectedCharacter.getAbilities());
            selectedCharacter.addAbilities();
            selectedCharacter.setAlive(true);
            finalPage();
            return;
        }

        /* Reset enemy */
        currentEnemy = null;

        /* Resets UI to the grid */
        frame.getContentPane().removeAll();
        frame.add(controlsPanel, BorderLayout.WEST);
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        updateStatsLabel();
    }

    /* The final page with the stats of the character */
    private void finalPage() {
        frame.getContentPane().removeAll();

        /* Main panel for the frame */
        JPanel finalPagePanel = new JPanel(new BorderLayout());

        /* Character stats panel */
        JPanel characterPanel = new JPanel(new BorderLayout());
        characterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /* Add character image */
        JLabel characterImageLabel = new JLabel();
        ImageIcon characterImage = new ImageIcon(selectedCharacter.getImagePath());
        Image scaledImage = characterImage.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        characterImageLabel.setIcon(new ImageIcon(scaledImage));
        characterPanel.add(characterImageLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Character Stats"));

        /* Add stats with labels and non-editable text fields */
        statsPanel.add(new JLabel("Name: "));
        statsPanel.add(createStatTextField(selectedCharacter.getName()));
        statsPanel.add(new JLabel("Role: "));
        statsPanel.add(createStatTextField(selectedCharacter.getRole().toString()));
        statsPanel.add(new JLabel("Strength: "));
        statsPanel.add(createStatTextField(String.valueOf(selectedCharacter.getStrength())));
        statsPanel.add(new JLabel("Dexterity: "));
        statsPanel.add(createStatTextField(String.valueOf(selectedCharacter.getDexterity())));
        statsPanel.add(new JLabel("Charisma: "));
        statsPanel.add(createStatTextField(String.valueOf(selectedCharacter.getCharisma())));
        statsPanel.add(new JLabel("Experience: "));
        statsPanel.add(createStatTextField(String.valueOf(selectedCharacter.getExperience())));
        statsPanel.add(new JLabel("Level: "));
        statsPanel.add(createStatTextField(String.valueOf(selectedCharacter.getLevel())));

        characterPanel.add(statsPanel, BorderLayout.CENTER);

        /* Add buttons panel */
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(e -> {
            /* Close the current frame */
            frame.dispose();
            /* Restart the game */
            setupUI();
        });

        buttonPanel.add(exitButton);
        buttonPanel.add(continueButton);

        finalPagePanel.add(characterPanel, BorderLayout.CENTER);
        finalPagePanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(finalPagePanel, BorderLayout.CENTER);

        /* Refresh the frame */
        frame.revalidate();
        frame.repaint();
    }
}
