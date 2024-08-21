package io.azar.examples.holyquran.util;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMParser;

import java.io.StringReader;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class CertUtils {

    /**
     * Creates a KeyStore (truststore) from a concatenated PEM certificate string.
     * <p>
     * <b>Steps:</b>
     * <ol>
     *   <li><b>Initialize Truststore:</b> Create a new instance of KeyStore using the PKCS12 type.</li>
     *   <li><b>Load Truststore:</b> Load the truststore with a default keystore password (null values).</li>
     *   <li><b>Read PEM Certificates:</b> Use a PEMParser to parse the concatenated PEM certificates.</li>
     *   <li><b>Process PEM Objects:</b> Iterate through the PEM objects read by the parser.
     *     <ul>
     *       <li>a. If the PEM object is an X509CertificateHolder, convert it to an X509Certificate.</li>
     *       <li>b. Assign a unique alias for each certificate in the truststore.</li>
     *       <li>c. Set the certificate entry in the truststore using the alias.</li>
     *     </ul>
     *   </li>
     * </ol>
     *
     * @param concatenatedPemCertificates Concatenated PEM certificates as a String.
     * @return The created KeyStore containing X.509 certificates.
     * @throws Exception If there is an issue during the truststore creation or certificate conversion.
     */
    public static KeyStore createTruststore(String concatenatedPemCertificates) throws Exception {
        KeyStore truststore = KeyStore.getInstance("PKCS12");
        truststore.load(null, null);

        try (StringReader stringReader = new StringReader(concatenatedPemCertificates); PEMParser pemParser = new PEMParser(stringReader)) {
            List<X509Certificate> certificates = new ArrayList<>();
            int aliasCounter = 1;
            Object pemObject;
            while ((pemObject = pemParser.readObject()) != null) {
                if (pemObject instanceof X509CertificateHolder) {
                    X509CertificateHolder certificateHolder = (X509CertificateHolder) pemObject;
                    X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(certificateHolder);
                    String alias = "cert-" + aliasCounter++;
                    truststore.setCertificateEntry(alias, certificate);
                }
                // You can handle other types of PEM objects if needed
            }
        }
        return truststore;
    }


    /**
     * Loads X.509 certificates from a concatenated PEM certificate string.
     * <p>
     * <b>Steps:</b>
     * <ol>
     *   <li><b>Initialize Certificates List:</b> Create an empty list to store X.509 certificates.</li>
     *   <li><b>Read PEM Certificates:</b> Use a PEMParser to parse the concatenated PEM certificates.</li>
     *   <li><b>Process PEM Objects:</b> Iterate through the PEM objects read by the parser.
     *     <ul>
     *       <li>a. If the PEM object is an X509CertificateHolder, convert it to an X509Certificate.</li>
     *       <li>b. Add the converted certificate to the list.</li>
     *     </ul>
     *   </li>
     *   <li><b>Return Certificates List:</b> Return the list of X.509 certificates.</li>
     * </ol>
     *
     * @param concatenatedPemCertificates Concatenated PEM certificates as a String.
     * @return List of X.509 certificates loaded from the PEM string.
     * @throws Exception If there is an issue during the certificate loading or conversion.
     */
    public static List<X509Certificate> loadCertificates(String concatenatedPemCertificates) throws Exception {
        List<X509Certificate> certificates = new ArrayList<>();

        try (PEMParser pemParser = new PEMParser(new StringReader(concatenatedPemCertificates))) {
            Object pemObject;
            while ((pemObject = pemParser.readObject()) != null) {
                if (pemObject instanceof X509CertificateHolder) {
                    X509CertificateHolder certificateHolder = (X509CertificateHolder) pemObject;
                    X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(certificateHolder);
                    certificates.add(certificate);
                }
                // You can handle other types of PEM objects if needed
            }
        }

        return certificates;
    }


}
