package Task_4;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class CurrencyConverter extends JFrame {

    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JTextField amountField;
    private JButton convertButton;
    private JLabel resultLabel;

    private final String[] currencies = {"USD", "EUR", "GBP", "INR", "JPY", "CAD", "AUD", "CNY", "AED", "AMD", "ALL", "AFN"};
    private final String apiKey = "11DPZXwA6rKlMDo9SEGb6qNBaHbYQhfH";

    private final HttpClient client = HttpClient.newHttpClient();

    public CurrencyConverter() {
        setTitle("Currency Converter");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("From Currency:"));
        fromCurrency = new JComboBox<>(currencies);
        add(fromCurrency);

        add(new JLabel("To Currency:"));
        toCurrency = new JComboBox<>(currencies);
        toCurrency.setSelectedIndex(1);
        add(toCurrency);

        add(new JLabel("Amount:"));
        amountField = new JTextField();
        add(amountField);

        convertButton = new JButton("Convert");
        add(convertButton);

        resultLabel = new JLabel("Converted Amount: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(resultLabel);

        convertButton.addActionListener(_-> convertCurrency());
    }

    private void convertCurrency() {
        String from = (String) fromCurrency.getSelectedItem();
        String to = (String) toCurrency.getSelectedItem();
        String amountText = amountField.getText();

        if (from == null || to == null) {
            JOptionPane.showMessageDialog(this, "Please select both currencies.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a positive number.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            return;
        }

        convertButton.setEnabled(false);
        resultLabel.setText("Converting...");

        String urlStr = "https://api.apilayer.com/exchangerates_data/convert?from=" + from + "&to=" + to + "&amount=" + amount + "&apikey=" + apiKey;
        URI uri = URI.create(urlStr);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        responseFuture.thenAccept(response -> {
            String responseBody = response.body();
            double result = extractConversionResult(responseBody);
            if (result > 0) {
                SwingUtilities.invokeLater(() -> {
                    resultLabel.setText("Converted Amount: " + String.format("%.2f", result) + " " + to);
                    convertButton.setEnabled(true);
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Failed to get conversion result.");
                    resultLabel.setText("Converted Amount: ");
                    convertButton.setEnabled(true);
                });
            }
        }).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                resultLabel.setText("Converted Amount: ");
                convertButton.setEnabled(true);
            });
            return null;
        });
    }

    private double extractConversionResult(String jsonResponse) {
        try {
            String resultKey = "\"result\":";
            int resultPos = jsonResponse.indexOf(resultKey);
            if (resultPos != -1) {
                String resultString = jsonResponse.substring(resultPos + resultKey.length()).trim();
                StringBuilder sb = new StringBuilder();
                for (char c : resultString.toCharArray()) {
                    if ((c >= '0' && c <= '9') || c == '.') {
                        sb.append(c);
                    } else {
                        break;
                    }
                }
                return Double.parseDouble(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CurrencyConverter app = new CurrencyConverter();
            app.setVisible(true);
        });
    }
}
