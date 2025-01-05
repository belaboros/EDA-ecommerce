package org.msffp.product.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@RestController
class InfoController {

    @GetMapping("/info")
    private String info() {
        return new JSONObject()
                .put("SystemProperties", System.getProperties())
                .put("Env", System.getenv())
                .put("IP addresses", getIPAdresses())
                .toString(4);
    }

    private static JSONArray getIPAdresses() {
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            JSONArray ni = new JSONArray();
            while(e.hasMoreElements())
            {
                NetworkInterface n = e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                JSONArray ia = new JSONArray();
                while (ee.hasMoreElements()) {
                    InetAddress i = ee.nextElement();
                    ia.put(i.getHostAddress());
                }

                ni.put(new JSONObject().put(n.getName(), ia));
            }

            return ni;
        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        }
    }
}
