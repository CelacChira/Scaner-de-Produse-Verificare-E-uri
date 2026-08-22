import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

class Produs {
    protected String nume;
    protected String euri;
    public Produs(String nume, String euri) {
        this.nume = nume;
        this.euri = euri;
    }
    public void afiseaza() {
        System.out.println("Produs: " + nume + "\nE-uri: " + euri);
    }
}

class ProdusEvaluat extends Produs {
    private final String evaluare;
    public ProdusEvaluat(String nume, String euri, String evaluare) {
        super(nume, euri);
        this.evaluare = evaluare;
    }

    @Override
    public void afiseaza() {
        super.afiseaza();
        System.out.println("Evaluare: " + evaluare);
    }
}

public class ScanerProduseEuri {
    private final JFrame frame;
    private final JTextField numeProdusField;
    private final JTextField euriField;
    private final JTextArea outputArea;
    private String[] coduriEuri;
    private String[] categoriiEuri;
    private int nrEuri;
    private final JButton adaugaButton;

    public ScanerProduseEuri() {
        incarcaDateEuri();

        frame = new JFrame("Scaner de Produse - Verificare E-uri");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 500);
        frame.setLayout(new BorderLayout(40, 40));
        frame.getContentPane().setBackground(new Color(114, 114, 131));

        JPanel inputPanel = new JPanel(new BorderLayout(20, 20));
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 12));
        inputPanel.setBackground(new Color(114, 114, 131));

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 1, 30, 40));
        fieldsPanel.setBackground(new Color(114, 114, 131, 255));

        JLabel lblNume = new JLabel("Nume produs:");
        lblNume.setFont(new Font("Arial", Font.BOLD, 20));
        lblNume.setForeground(Color.WHITE);
        fieldsPanel.add(lblNume);

        numeProdusField = new JTextField();
        numeProdusField.setFont(new Font("Arial", Font.PLAIN, 33));
        numeProdusField.setBackground(new Color(213, 213, 239));
        fieldsPanel.add(numeProdusField);

        JLabel lblEuri = new JLabel("E-uri:");
        lblEuri.setFont(new Font("Arial", Font.BOLD, 20));
        lblEuri.setForeground(Color.WHITE);
        fieldsPanel.add(lblEuri);

        euriField = new JTextField();
        euriField.setFont(new Font("Arial", Font.PLAIN, 33));
        euriField.setBackground(new Color(213, 213, 239));
        fieldsPanel.add(euriField);
        inputPanel.add(fieldsPanel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setBackground(new Color(114, 114, 131));

        JButton verificaButton = createImageButton("verify.png", "Verifica Produs");
        verificaButton.setBackground(new Color(114, 114, 131));
        verificaButton.setBorder(new EmptyBorder(15, 20, 15, 110));

        buttonsPanel.add(verificaButton);

        // Add button
        adaugaButton = createImageButton("add.png", "Adauga Produs");
        adaugaButton.setBackground(new Color(114, 114, 131));
        adaugaButton.setBorder(new EmptyBorder(15, 20, 15, 20));
        adaugaButton.setEnabled(false);
        buttonsPanel.add(adaugaButton);

        JButton diagramaButton = createImageButton("chart.png", "Diagrama Produse");
        diagramaButton.setBackground(new Color(114, 114, 131));
        diagramaButton.setBorder(new EmptyBorder(15, 110, 15, 10));
        buttonsPanel.add(diagramaButton);

        inputPanel.add(buttonsPanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setBackground(Color.WHITE);
        outputArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        verificaButton.addActionListener(e -> verificaProdus());
        adaugaButton.addActionListener(e -> {
            String nume = numeProdusField.getText().trim();
            String euri = euriField.getText().trim();
            String evaluare = extrageEvaluareDinOutput();
            salveazaProdus(nume, euri, evaluare);
            JOptionPane.showMessageDialog(frame, "Produsul a fost salvat cu succes.");
            adaugaButton.setEnabled(false);
        });
        diagramaButton.addActionListener(e -> afiseazaDiagrama());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton createImageButton(String imagePath, String tooltipText) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            JButton button = new JButton(new ImageIcon(img));
            button.setToolTipText(tooltipText);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            return button;
        } catch (Exception e) {
            JButton button = new JButton(tooltipText);
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setFocusPainted(false);
            return button;
        }
    }

    private void afiseazaDiagrama() {
        int extrem = 0, cancerigen = 0, riscant = 0, sigur = 0, necunoscut = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("produse.txt"))) {
            String linie;
            while ((linie = reader.readLine()) != null) {
                if (linie.startsWith("Evaluare: ")) {
                    String evaluare = linie.substring(10).trim().toLowerCase();
                    if (evaluare.contains("extrem")) extrem++;
                    else if (evaluare.contains("cancerigen")) cancerigen++;
                    else if (evaluare.contains("riscant")) riscant++;
                    else if (evaluare.contains("sigur")) sigur++;
                    else necunoscut++;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Eroare la citirea fisierului produse.txt", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (extrem > 0) dataset.setValue("Extrem de periculos", extrem);
        if (cancerigen > 0) dataset.setValue("Cancerigen", cancerigen);
        if (riscant > 0) dataset.setValue("Riscant", riscant);
        if (sigur > 0) dataset.setValue("Sigur", sigur);
        if (necunoscut > 0) dataset.setValue("Necunoscut", necunoscut);

        JFreeChart chart = ChartFactory.createPieChart("Distribuția produselor după evaluare", dataset, true, true, false);

        org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();

        // Setare culori pentru categorii
        plot.setSectionPaint("Extrem de periculos", new Color(197, 99, 99, 174));
        plot.setSectionPaint("Cancerigen", new Color(225, 187, 187));
        plot.setSectionPaint("Riscant", new Color(255, 242, 191));
        plot.setSectionPaint("Sigur", new Color(183, 227, 183));
        plot.setSectionPaint("Necunoscut", Color.GRAY);

        // Setare fonturi și legendă
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 20));
        chart.setBackgroundPaint(new Color(114, 114, 131));
        plot.setBackgroundPaint(new Color(151, 151, 177));
        chart.removeLegend();
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 40));
        plot.setLabelBackgroundPaint(new Color(151, 151, 177));
        plot.setLabelShadowPaint(null);
        plot.setLabelOutlinePaint(null);

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame chartFrame = new JFrame("Diagrama Produse");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setSize(600, 400);
        chartFrame.setLocationRelativeTo(null);
        chartFrame.setContentPane(chartPanel);
        chartFrame.setVisible(true);
    }

    private void incarcaDateEuri() {
        coduriEuri = new String[500];
        categoriiEuri = new String[500];
        nrEuri = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("date.txt"))) {
            String linie;
            while ((linie = reader.readLine()) != null) {
                String[] parts = linie.split("\\s+");
                if (parts.length >= 2) {
                    coduriEuri[nrEuri] = parts[0].trim().toUpperCase();
                    categoriiEuri[nrEuri] = parts[1].trim().toLowerCase();
                    nrEuri++;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, " Eroare la citirea fisierului date.txt!", "Eroare", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private String cautaCategorie(String cod) {
        for (int i = 0; i < nrEuri; i++) {
            if (coduriEuri[i].equalsIgnoreCase(cod)) {
                return categoriiEuri[i];
            }
        }
        return "necunoscut";
    }

    private void verificaProdus() {
        outputArea.setText("");
        outputArea.setBackground(Color.WHITE);

        String nume = numeProdusField.getText().trim();
        String euriText = euriField.getText().trim();

        if (nume.isEmpty() || euriText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Completeaza datele", "Eroare", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] listaEuri = euriText.split(",");
        boolean produsFoarteCancerigen = false;
        boolean produsCancerigen = false;
        boolean produsPericulos = false;
        boolean produsModerat = false;
        boolean faraRisc = false;

        outputArea.append("Verificare pentru produsul: " + nume + "\n");
        outputArea.append("--------------------------------------------\n");

        for (String e : listaEuri) {
            e = e.trim().toUpperCase();
            String categorie = cautaCategorie(e);

            switch (categorie) {
                case "foarte_cancerigen":
                    outputArea.append(e + " - Foarte cancerigen!\n");
                    produsFoarteCancerigen = true;
                    break;
                case "cancerigen":
                    outputArea.append(e + " - Cancerigen!\n");
                    produsCancerigen = true;
                    break;
                case "periculos":
                    outputArea.append(e + " - Periculos!\n");
                    produsPericulos = true;
                    break;
                case "moderate":
                    outputArea.append(e + " - Produse moderate!\n");
                    produsModerat = true;
                    break;
                case "nepericulos":
                    outputArea.append(e + " - Nepericulos\n");
                    faraRisc = true;
                    break;
                default:
                    outputArea.append(e + " - Necunoscut\n");
                    break;
            }
        }

        outputArea.append("\nEvaluare Finala:\n");
        String evaluare = "";

        if (produsFoarteCancerigen) {
            evaluare = "PRODUSUL ESTE EXTREM DE PERICULOS! Evita-l complet!";
            outputArea.setBackground(new Color(228, 120, 120, 255));
        } else if (produsCancerigen) {
            evaluare = "PRODUSUL ESTE CANCERIGEN! Consuma doar daca e nevoie!";
            outputArea.setBackground(new Color(246, 182, 171));
        } else if (produsPericulos || produsModerat) {
            evaluare = "PRODUSUL ESTE RISCANT.";
            outputArea.setBackground(new Color(253, 229, 128));
        } else if (faraRisc) {
            evaluare = "PRODUSUL ESTE SIGUR.";
            outputArea.setBackground(new Color(192, 220, 182));
        } else {
            evaluare = "NU SE CUNOASTE.";
        }

        outputArea.append(evaluare + "\n");

        // Activează butonul de adăugare
        adaugaButton.setEnabled(true);

        // Demonstrație moștenire și suprascriere
        ProdusEvaluat pe = new ProdusEvaluat(nume, euriText, evaluare);
        pe.afiseaza(); // Console log
    }

    private String extrageEvaluareDinOutput() {
        String[] linii = outputArea.getText().split("\n");
        for (int i = linii.length - 1; i >= 0; i--) {
            if (linii[i].startsWith("PRODUSUL") || linii[i].startsWith("NU SE CUNOASTE")) {
                return linii[i];
            }
        }
        return "NECUNOSCUT";
    }

    private void salveazaProdus(String numeProdus, String euri, String evaluareFinala) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("produse.txt", true))) {
            writer.write("Produs: " + numeProdus + "\n");
            writer.write("E-uri: " + euri + "\n");
            writer.write("Evaluare: " + evaluareFinala + "\n");
            writer.write("----------------------------------------\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Eroare (salvarea in fisier)", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScanerProduseEuri::new);
    }
}