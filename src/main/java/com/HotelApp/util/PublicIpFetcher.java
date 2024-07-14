package com.HotelApp.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class PublicIpFetcher {

     public static String getPublicIpAddress() throws IOException {
         String ipServiceUrl = "https://api.ipify.org";
         try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
             HttpGet request = new HttpGet(ipServiceUrl);
             try (CloseableHttpResponse response = httpClient.execute(request)) {
                 return EntityUtils.toString(response.getEntity()).trim();
             }
         }
     }
}