import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.util.List;

public class InterfaceGraficaDoConversor {

    private static final String API_KEY = "73cb4c86e6cec9ecd48aef37";  // Substitua pela sua chave de API
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Conversor de Moedas");
            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(null);

            JLabel fromLabel = new JLabel("De:");
            fromLabel.setBounds(20, 20, 50, 30);
            frame.add(fromLabel);

            JTextField campoMoedaSaida = new JTextField();
            campoMoedaSaida.setBounds(80, 20, 100, 30);
            frame.add(campoMoedaSaida);

            JLabel toLabel = new JLabel("Para:");
            toLabel.setBounds(20, 60, 50, 30);
            frame.add(toLabel);

            JTextField campoMoedaEntrada = new JTextField();
            campoMoedaEntrada.setBounds(80, 60, 100, 30);
            frame.add(campoMoedaEntrada);

            JLabel amountLabel = new JLabel("Valor:");
            amountLabel.setBounds(20, 100, 50, 30);
            frame.add(amountLabel);

            JTextField campoValor = new JTextField();
            campoValor.setBounds(80, 100, 100, 30);
            frame.add(campoValor);

            JButton convertButton = new JButton("Converter");
            convertButton.setBounds(80, 140, 120, 30);
            frame.add(convertButton);

            JButton historyButton = new JButton("Histórico");
            historyButton.setBounds(220, 140, 120, 30);
            frame.add(historyButton);

            JLabel rotuloResultado = new JLabel("Resultado:");
            rotuloResultado.setBounds(20, 180, 450, 30);
            frame.add(rotuloResultado);

            JTextArea AreaHistorico = new JTextArea();
            AreaHistorico.setBounds(20, 220, 450, 120);
            AreaHistorico.setEditable(false);
            frame.add(AreaHistorico);

            convertButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nomeMoedaSaida = campoMoedaSaida.getText().toLowerCase();
                    String nomeMoedaEntrada = campoMoedaEntrada.getText().toLowerCase();
                    String moedaSaida = MapeamentoDeMoedas.getCodigoMoeda(nomeMoedaSaida);
                    String moedaEntrada = MapeamentoDeMoedas.getCodigoMoeda(nomeMoedaEntrada);
                    double valor = Double.parseDouble(campoValor.getText());

                    if (moedaSaida == null || moedaEntrada == null) {
                        rotuloResultado.setText("Moeda inválida.");
                        return;
                    }

                    double valorConvertido = moedaConvertida(moedaSaida, moedaEntrada, valor);
                    if (valorConvertido != -1) {
                        String textoResultante = String.format("%.2f %s é equivalente a %.2f %s", valor, nomeMoedaSaida, valorConvertido, nomeMoedaEntrada);
                        rotuloResultado.setText(textoResultante);
                        HistoricoDeConversao.addEntry(nomeMoedaSaida, nomeMoedaEntrada, valor, valorConvertido);
                    } else {
                        rotuloResultado.setText("Erro ao obter a taxa de câmbio.");
                    }
                }
            });

            historyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<String> historico = HistoricoDeConversao.getHistorico();
                    AreaHistorico.setText(String.join("\n", historico));
                }
            });

            frame.setVisible(true);
        });
    }

    public static double moedaConvertida(String moedaSaida, String moedaEntrada, double valor) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + moedaSaida;
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                System.out.println("Erro na solicitação HTTP. Status Code: " + statusCode);
                return -1;
            }

            String json = EntityUtils.toString(response.getEntity());
            System.out.println("Resposta da API: " + json);

            JSONObject exchangeRates = new JSONObject(json);
            double taxaCambio = exchangeRates.getJSONObject("conversion_rates").getDouble(moedaEntrada);

            return valor * taxaCambio;

        } catch (Exception e) {
            System.out.println("Erro ao obter a taxa de câmbio: " + e.getMessage());
            return -1;
        }
    }
}
