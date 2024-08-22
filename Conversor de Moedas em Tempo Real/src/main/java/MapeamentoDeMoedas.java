import java.util.HashMap;
import java.util.Map;

public class MapeamentoDeMoedas {
    private static final Map<String, String> mapaDeMoedas = new HashMap<>();

    static {
        // Mapeamento de nomes de moedas para códigos ISO
        mapaDeMoedas.put("usd", "USD");
        mapaDeMoedas.put("dólar", "USD");
        mapaDeMoedas.put("dolar", "USD");
        mapaDeMoedas.put("eur", "EUR");
        mapaDeMoedas.put("euro", "EUR");
        mapaDeMoedas.put("brl", "BRL");
        mapaDeMoedas.put("real", "BRL");
        mapaDeMoedas.put("gbp", "GBP");
        mapaDeMoedas.put("libra", "GBP");
        mapaDeMoedas.put("jpy", "JPY");
        mapaDeMoedas.put("iene", "JPY");
    }

    public static String getCodigoMoeda(String nomeDaMoeda) {
        return mapaDeMoedas.getOrDefault(nomeDaMoeda.toLowerCase(), null);
    }
}
