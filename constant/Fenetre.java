package com.devoir_6_java.constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Fenetre extends JFrame {
    private List<Compte> compteList;
    private List<Transaction> transactionList;

    private JTabbedPane tabbedPane;
    private JPanel comptePanel;
    private JPanel transactionPanel;

    private Connection connection;

    public Fenetre() {
        compteList = new ArrayList<>();
        transactionList = new ArrayList<>();

        setTitle("Bank Management System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a connection to the SQLite database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:bank_Account.db");
            createTables(); // Create the necessary tables if they don't exist
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tabbedPane = new JTabbedPane();

        // Create the Compte panel
        comptePanel = createComptePanel();
        tabbedPane.addTab("Compte", comptePanel);

        // Create the Transaction panel
        transactionPanel = createTransactionPanel();
        tabbedPane.addTab("Transaction", transactionPanel);

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void createTables() throws SQLException {
        // Create a table for Compte if it doesn't exist
        String createCompteTableQuery = "CREATE TABLE IF NOT EXISTS compte (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "numero TEXT NOT NULL," +
                "solde REAL NOT NULL," +
                "nom TEXT NOT NULL" +
                ");";
        executeUpdateQuery(createCompteTableQuery);

        // Create a table for Transactions if it doesn't exist
        String createTransactionTableQuery = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "montant REAL NOT NULL," +
                "type TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "compte_id INTEGER NOT NULL" +
                ");";
        executeUpdateQuery(createTransactionTableQuery);
    }

    private void executeUpdateQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    private void executeInsertQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(query, Statement.RETURN_GENERATED_KEYS);
        statement.close();
    }

    private boolean checkAccountExists(String accountNumber) {
        try {
            String query = "SELECT * FROM compte WHERE numero = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getAccountId(String accountNumber) {
        try {
            String query = "SELECT id FROM compte WHERE numero = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private JPanel createComptePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel compteNumeroLabel = new JLabel("Numero de Compte: ");
        JTextField compteNumeroTextField = new JTextField(20);

        JLabel soldeLabel = new JLabel("Balance: ");
        JTextField soldeTextField = new JTextField(20);

        JLabel nomLabel = new JLabel("Nom du Compte: ");
        JTextField nomTextField = new JTextField(20);

        JButton enregistrerButton = new JButton("Save");
        enregistrerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String compteNumero = compteNumeroTextField.getText();
                double solde = Double.parseDouble(soldeTextField.getText());
                String nom = nomTextField.getText();

                Compte compte = new Compte(compteNumero, solde, nom);
                compteList.add(compte);

                // Insert compte into the database
                String insertCompteQuery = "INSERT INTO compte (numero, solde, nom) VALUES ('" +
                        compte.getNumero_de_compte() + "', " +
                        compte.getSolde() + ", '" +
                        compte.getNom() + "');";
                try {
                    executeInsertQuery(insertCompteQuery);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


                JOptionPane.showMessageDialog(panel, compte.toString());
                compteNumeroTextField.setText("");
                soldeTextField.setText("");
                nomTextField.setText("");
            }
        });

        JButton annulerButton = new JButton("Annuler");
        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int val = JOptionPane.showConfirmDialog(comptePanel, "etes-vous sur?", null, JOptionPane.YES_NO_CANCEL_OPTION);
            	
            	if (val == 0) {
            		compteNumeroTextField.setText("");
                    soldeTextField.setText("");
                    nomTextField.setText("");
				}
                
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(compteNumeroLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(compteNumeroTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(soldeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(soldeTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(nomLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(nomTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(enregistrerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(annulerButton, gbc);

        return panel;
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel compteNumeroLabel = new JLabel("Numero de compte: ");
        JTextField compteNumeroTextField = new JTextField(20);

        JLabel montantLabel = new JLabel("Montant: ");
        JTextField montantTextField = new JTextField(20);

        JLabel typeLabel = new JLabel("Type de Transaction: ");
        String[] transactionTypes = {"Depôt", "Retrait"};
        JComboBox<String> transactionTypeComboBox = new JComboBox<>(transactionTypes);

        JButton enregistrerButton = new JButton("Enregistrer");
        enregistrerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String compteNumero = compteNumeroTextField.getText();
                double montant = Double.parseDouble(montantTextField.getText());
                String type = transactionTypeComboBox.getSelectedItem().toString();
                LocalDateTime date = LocalDateTime.now();

                // Check if the account number exists in the database
                boolean accountExists = checkAccountExists(compteNumero);
                if (!accountExists) {
                    JOptionPane.showMessageDialog(panel, "Numéro de compte invalide. Veuillez fournir un numéro de compte valide.");
                    return;
                }

                int compteId = getAccountId(compteNumero);

                Transaction transaction = new Transaction(montant, type, date, compteId);
                transactionList.add(transaction);

                // Insert transaction into the database
                String insertTransactionQuery = "INSERT INTO transactions (montant, type, date, compte_id) VALUES (" +
                        transaction.getMontant() + ", '" +
                        transaction.getType() + "', '" +
                        transaction.getDate() + "', " +
                        transaction.getId() + ");";
                try {
                    executeInsertQuery(insertTransactionQuery);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                compteNumeroTextField.setText("");
                montantTextField.setText("");
                JOptionPane.showMessageDialog(transactionPanel, "Transaction réussite!");

            }
        });

        JButton annulerButton = new JButton("Annuler");
        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	int val = JOptionPane.showConfirmDialog(transactionPanel, "etes-vous sur?", null, JOptionPane.YES_NO_CANCEL_OPTION);
            	
            	if (val == 0) {
            		compteNumeroTextField.setText("");
            		compteNumeroTextField.setText("");
                    montantTextField.setText("");
				}
                
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(compteNumeroLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(compteNumeroTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(montantLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(montantTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(transactionTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(enregistrerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(annulerButton, gbc);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Fenetre();
            }
        });
    }
}










































