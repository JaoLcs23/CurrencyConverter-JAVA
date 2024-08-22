import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDeConversao {
    private static final List<String> historico = new ArrayList<>();
    private static final String FILE_PATH = "histórico_de_conversões.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void addEntry(String moedaSaida, String moedaEntrada, double quantia, double valorConvertido) {
        String timestamp = LocalDateTime.now().format(formatter);
        String entry = String.format("[%s] %.2f %s => %.2f %s", timestamp, quantia, moedaSaida, valorConvertido, moedaEntrada);
        historico.add(entry);
        saveToFile();
    }

    public static List<String> getHistorico() {
        loadFromFile();
        return new ArrayList<>(historico);
    }

    private static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Adiciona a última entrada ao arquivo
            writer.write(historico.get(historico.size() - 1));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao salvar o histórico em arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadFromFile() {
        historico.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historico.add(line);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar o histórico do arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
