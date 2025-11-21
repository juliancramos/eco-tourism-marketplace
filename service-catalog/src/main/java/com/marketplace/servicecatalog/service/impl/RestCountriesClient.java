package com.marketplace.servicecatalog.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.marketplace.servicecatalog.dto.CountryInfoDTO;

@Service
public class RestCountriesClient {

    private final WebClient webClient = WebClient.create();

    public CountryInfoDTO getCountry(String countryCode) {

        String url = "https://restcountries.com/v3.1/alpha/" + countryCode;

        List response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(List.class)
                .block();

        if (response == null || response.isEmpty()) return null;

        Map data = (Map) response.get(0);

        // name
        Map nameMap = (Map) data.get("name");
        String name = nameMap != null ? (String) nameMap.get("common") : null;

        // capital
        List capitals = (List) data.get("capital");
        String capital = (capitals != null && !capitals.isEmpty()) ? capitals.get(0).toString() : null;

        // region / continent
        String region = data.get("region") != null ? data.get("region").toString() : null;

        // currency
        String currency = null;
        if (data.get("currencies") instanceof Map currencies) {
            Map.Entry entry = (Map.Entry) currencies.entrySet().iterator().next();
            Map curDetails = (Map) entry.getValue();
            currency = curDetails.get("name").toString();
        }

        // flag
        String flag = data.get("flag") != null ? data.get("flag").toString() : null;

        return new CountryInfoDTO(name, capital, region, currency, flag);
    }
}
