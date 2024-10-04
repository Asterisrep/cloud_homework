package com.example.demo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.io.File;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;

import java.nio.file.Paths;
import java.security.KeyStore;
import java.io.FileInputStream;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;

import org.apache.http.auth.AuthScope;


@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() throws Exception {
        // Load the CA certificate
        // File caFile = new ClassPathResource("ca.crt").getFile();
        KeyStore keyStore = KeyStore.getInstance("jks");
        File keyStoreFile = Paths.get("/home/repopolidis/Downloads/docker-elk-tls/my_app/elasticsearch.jks").toFile();
        try (FileInputStream is = new FileInputStream(keyStoreFile)) {
            keyStore.load(is, "123988".toCharArray());
        }
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(keyStore, null)
                .build();
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials("elastic", "8_1*gTOtDh62_E18zZpk"));

        // Create the RestClientBuilder with HTTPS and the SSL context
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "https"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        httpClientBuilder.setSSLContext(sslContext);
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        return httpClientBuilder;
                    }
                });

        RestClient restClient = builder.build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}
