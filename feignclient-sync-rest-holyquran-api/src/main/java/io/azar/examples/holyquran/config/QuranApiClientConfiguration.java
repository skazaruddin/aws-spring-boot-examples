package io.azar.examples.holyquran.config;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import io.azar.examples.holyquran.util.CertUtils;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.httpcomponents.PoolingHttpClientConnectionManagerMetricsBinder;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuranApiClientConfiguration {

    private final ApiConfigurationProperties apiConfigurationProperties;

    public QuranApiClientConfiguration(ApiConfigurationProperties apiConfigurationProperties) {
        this.apiConfigurationProperties = apiConfigurationProperties;
    }

    @Bean
    public Client feignClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) throws Exception {
        List<X509Certificate> x509Certificates = CertUtils.loadCertificates(apiConfigurationProperties.getQuranapi().getTruststore().getCertificates());

        HttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .disableRedirectHandling()
                .disableAutomaticRetries()
//                .setConnectTimeout((int) TimeUnit.SECONDS.toMillis(apiConfigurationProperties.getQuranapi().getConnection().getConnectTimeoutSeconds()))
//                .setS((int) TimeUnit.SECONDS.toMillis(apiConfigurationProperties.getQuranapi().getConnection().getReadTimeoutSeconds()))
                .setKeepAliveStrategy((httpResponse, httpContext)-> 30000L)
                .setSSLContext(sslContext(x509Certificates))
                .setSSLHostnameVerifier(new DefaultHostnameVerifier())
                .build();

        return new ApacheHttpClient(httpClient);
    }

    @Bean
    public PoolingHttpClientConnectionManager connectionManager(MeterRegistry meterRegistry){

//        Registry<?> registry = RegistryBuilder.create()
//                .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                .register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
//
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(10000, TimeUnit.MILLISECONDS);
        connectionManager.setMaxTotal(100); // Maximum total connection
        connectionManager.setDefaultMaxPerRoute(20); // Maximum connections per route
        connectionManager.closeIdleConnections(30, TimeUnit.SECONDS); // Close idle connections after 30 seconds
        new PoolingHttpClientConnectionManagerMetricsBinder(connectionManager, "quran-http-conn-pool").bindTo(meterRegistry);
        return connectionManager;
    }


    private SSLContext sslContext(List<X509Certificate> certificates) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
        sslContext.init(null, trustManager(certificates), null);
        return sslContext;
    }

    private TrustManager[] trustManager(List<X509Certificate> certificates) throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        for (int i = 0; i < certificates.size(); i++) {
            keyStore.setCertificateEntry("ca" + i, certificates.get(i));
        }
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + java.util.Arrays.toString(trustManagers));
        }
        return trustManagers;
    }
}
