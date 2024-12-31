import accountInfo.Account;
import accountInfo.Credentials;
import exceptions.AccountNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Test {
    private static Game game;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Test::createStartMenu);
    }

    /**
     * Creates the start menu.
     */
    private static void createStartMenu() {
        /* Main frame */
        JFrame frame = new JFrame("Game - Start Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 600);
        frame.setLocationRelativeTo(null);

        /* Panel for the image */
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/images/background.jpeg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        /* Center the panel */
        panel.setLayout(new GridBagLayout());

        JPanel questionPanel = new JPanel();
        questionPanel.setBackground(new Color(255, 255, 255, 150));
        questionPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200, 150), 2));
        questionPanel.setLayout(new GridBagLayout());

        JLabel questionLabel = new JLabel("Choose interface");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionLabel.setForeground(Color.BLACK);
        questionPanel.add(questionLabel);

        /* Two buttons for the interface */
        JButton textInterfaceButton = createStylishButton("Text interface");
        JButton guiInterfaceButton = createStylishButton("GUI");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        /* The text is on 2 columns */
        gbc.gridwidth = 2;
        panel.add(questionPanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(textInterfaceButton, gbc);
        gbc.gridx = 1;
        panel.add(guiInterfaceButton, gbc);

        /* Adds panel to the frame */
        frame.setContentPane(panel);
        frame.setVisible(true);

        textInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "You chose the text interface.");
                frame.dispose();
                startTextInterface();
            }
        });

        guiInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "You chose the GUI.");
                frame.dispose();
                startGUIInterface();
            }
        });
    }

    /**
     * Creates transparent buttons.
     * @param text, the text on the button
     */
    public static JButton createStylishButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));

        Color borderColor = new Color(160, 60, 35, 255);
        Color backgroundColorDefault = new Color(142, 144, 205);
        Color backgroundColorHover = new Color(160, 60, 35);
        Color textColorDefault = new Color(10, 10, 10);
        Color textColorHover = Color.WHITE;

        button.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        button.setForeground(textColorDefault);
        button.setBackground(backgroundColorDefault);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        /* Mouse listener for hover and other effects */
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setForeground(textColorHover);
                button.setBackground(backgroundColorHover);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setForeground(textColorDefault);
                button.setBackground(backgroundColorDefault);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                button.setForeground(textColorHover);
                button.setBackground(backgroundColorHover.darker());
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                /* If mouse is still on the button */
                if (button.getMousePosition() != null) {
                    button.setForeground(textColorHover);
                    button.setBackground(backgroundColorHover);
                } else {
                    button.setForeground(textColorDefault);
                    button.setBackground(backgroundColorDefault);
                }
            }
        });

        return button;
    }

    /**
     *  Starts the game in CLI mode.
     */
    public static void startTextInterface() {
        System.out.println("CLI was launched.");
        game = Game.getInstance();
        try {
            game.run();
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the game in GUI mode.
     */
    public static void startGUIInterface() {
        System.out.println("GUI was launched.");
        game = Game.getInstance();
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 600);
        frame.setLocationRelativeTo(null);

        /* Panel for the background image */
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/images/background.jpeg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        /* Center it */
        panel.setLayout(new GridBagLayout());

        /* Login panel */
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(255, 255, 255, 200));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        loginPanel.setLayout(new GridBagLayout());

        /* Email and password fields */
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        /* Login button */
        JButton loginButton = createStylishButton("Login");

        /* Error message, initially hidden */
        JLabel errorLabel = new JLabel("Email or password is invalid!");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setVisible(false);

        /* Adds components to the login panel */
        GridBagConstraints gbc = new GridBagConstraints();
        /* 10 pixels in between components */
        gbc.insets = new Insets(10, 10, 10, 10);

        /* 1 column */
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        loginPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(emailField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 4;
        loginPanel.add(errorLabel, gbc);

        /* Adds login panel to the main frame */
        panel.add(loginPanel);
        frame.setContentPane(panel);
        frame.setVisible(true);

        loginButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String email = emailField.getText();
                        String password = new String(passwordField.getPassword());
                        ArrayList<Account> accounts = game.getAccounts();
                        Account selectedAccount = null;
                        for (Account account : accounts) {
                            Credentials creds = account.getPlayerInfo().getCreds();
                            if (email.equals(creds.getEmail())
                                    && password.equals(creds.getPassword())) {
                                selectedAccount = account;
                                break;
                            }
                        }

                        if (selectedAccount == null) {
                            errorLabel.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Login successful!");
                            frame.dispose();
                            game.setSelectedAccount(selectedAccount);
                            SwingUtilities.invokeLater(() -> new GameGUI(game));
                        }
                    }
                });
    }
}
