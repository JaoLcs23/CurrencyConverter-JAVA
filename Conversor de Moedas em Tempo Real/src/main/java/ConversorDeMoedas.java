import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ConversorDeMoedas {

    private static final String API_KEY = "73cb4c86e6cec9ecd48aef37";  // Substitua pela sua chave de API
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static double convertCurrency(String moedaEntrada, String moedaSaida, double quantia) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + moedaEntrada;
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                System.out.println("Erro na solicitação HTTP. Status Code: " + statusCode);
                return -1;
            }

            String json = EntityUtils.toString(response.getEntity());
            System.out.println("Resposta da API: " + json);

            JSONObject taxasDeCambio = new JSONObject(json);
            double taxaDeCambio = taxasDeCambio.getJSONObject("conversion_rates").getDouble(moedaSaida);

            return quantia * taxaDeCambio;

        } catch (Exception e) {
            System.out.println("Erro ao obter a taxa de câmbio: " + e.getMessage());
            return -1;
        }
    }
}