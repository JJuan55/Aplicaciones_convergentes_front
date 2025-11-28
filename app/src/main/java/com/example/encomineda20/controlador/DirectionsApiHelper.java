package com.example.encomineda20.controlador;


import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DirectionsApiHelper {

    public interface RutaCallback {
        void onRutaCalculada(List<LatLng> puntos, double distanciaTotal);
        void onError(String error);
    }

    public static void obtenerRuta(Context context, LatLng origen, LatLng destino, String apiKey, RutaCallback callback) {

        String urlStr =
                "https://maps.googleapis.com/maps/api/directions/json?"
                        + "origin=" + origen.latitude + "," + origen.longitude
                        + "&destination=" + destino.latitude + "," + destino.longitude
                        + "&mode=driving"
                        + "&alternatives=false"
                        + "&key=" + apiKey;

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    return sb.toString();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String response) {
                if (response == null) {
                    callback.onError("No hay respuesta del servidor Directions API");
                    return;
                }

                try {
                    JSONObject json = new JSONObject(response);

                    JSONArray routes = json.getJSONArray("routes");
                    if (routes.length() == 0) {
                        callback.onError("No se encontró ruta");
                        return;
                    }

                    JSONObject route = routes.getJSONObject(0);
                    JSONObject overview = route.getJSONObject("overview_polyline");
                    String polyline = overview.getString("points");

                    // Decodifica la polilínea de Google
                    List<LatLng> puntos = decodePolyline(polyline);

                    // Distancia real
                    double distanciaTotal = 0;
                    JSONArray legs = route.getJSONArray("legs");
                    for (int i = 0; i < legs.length(); i++) {
                        distanciaTotal += legs.getJSONObject(i)
                                .getJSONObject("distance")
                                .getDouble("value"); // en metros
                    }

                    callback.onRutaCalculada(puntos, distanciaTotal);

                } catch (Exception e) {
                    callback.onError("Error procesando JSON: " + e.getMessage());
                }
            }

        }.execute(urlStr);
    }



    public static List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();

        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng(
                    lat / 1E5,
                    lng / 1E5
            );
            poly.add(p);
        }

        return poly;
    }
}
