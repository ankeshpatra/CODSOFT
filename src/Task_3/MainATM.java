package Task_3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainATM extends JFrame {
    private ATM atm;
    private JLabel balanceLabel;

    public MainATM() {
        BankAccount account = new BankAccount(0.0);
        atm = new ATM(account);

        setTitle("ATM Machine");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        balanceLabel = new JLabel("Balance: $0.00");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton exitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(checkBalanceButton);
        buttonPanel.add(exitButton);

        add(balanceLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        depositButton.addActionListener(e -> deposit());
        withdrawButton.addActionListener(e -> withdraw());
        checkBalanceButton.addActionListener(e -> checkBalance());
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void deposit() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (input == null || input.trim().isEmpty()) {
            return;
        }
        try {
            double amount = Double.parseDouble(input.trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a positive amount.");
                return;
            }
            if (atm.deposit(amount)) {
                JOptionPane.showMessageDialog(this, "Deposit successful.");
                updateBalanceLabel();
            } else {
                JOptionPane.showMessageDialog(this, "Deposit failed.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format.");
        }
    }

    private void withdraw() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (input == null || input.trim().isEmpty()) {
            return;
        }
        try {
            double amount = Double.parseDouble(input.trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a positive amount.");
                return;
            }
            if (atm.withdraw(amount)) {
                JOptionPane.showMessageDialog(this, "Withdrawal successful.");
                updateBalanceLabel();
            } else {
                JOptionPane.showMessageDialog(this, "Withdrawal failed. Insufficient balance or invalid amount.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format.");
        }
    }

    private void checkBalance() {
        double balance = atm.checkBalance();
        JOptionPane.showMessageDialog(this, String.format("Current balance: $%.2f", balance));
        updateBalanceLabel();
    }

    private void updateBalanceLabel() {
        balanceLabel.setText(String.format("Balance: $%.2f", atm.checkBalance()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainATM atmFrame = new MainATM();
            atmFrame.setVisible(true);
        });
    }
}
