package com.marketplace.servicecatalog.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.marketplace.servicecatalog.dto.CountryInfoDTO;

import reactor.core.publisher.Mono;

@Service
public class RestCountriesClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://restcountries.com")
            .build();

    public Mono<CountryInfoDTO> getCountry(String countryCode) {

        return webClient.get()
                .uri("/v3.1/alpha/{code}", countryCode)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.empty();
                    }

                    Map<String, Object> data = list.get(0);

                    // name
                    String name = null;
                    Object nameObj = data.get("name");
                    if (nameObj instanceof Map<?,?> nameMap) {
                        Object common = nameMap.get("common");
                        if (common != null) name = common.toString();
                    }

                    // capital
                    String capital = null;
                    Object capitalsObj = data.get("capital");
                    if (capitalsObj instanceof List<?> capitals && !capitals.isEmpty()) {
                        capital = capitals.get(0).toString();
                    }

                    // region
                    String region = data.get("region") != null ? data.get("region").toString() : null;

                    // currency
                    String currency = null;
                    Object currenciesObj = data.get("currencies");
                    if (currenciesObj instanceof Map<?,?> currencies && !currencies.isEmpty()) {
                        Map.Entry<?,?> entry = currencies.entrySet().iterator().next();
                        Object curDetailsObj = entry.getValue();
                        if (curDetailsObj instanceof Map<?,?> curDetails) {
                            Object cname = curDetails.get("name");
                            if (cname != null) currency = cname.toString();
                        }
                    }

                    // flag
                    String flag = data.get("flag") != null ? data.get("flag").toString() : null;

                    return Mono.just(new CountryInfoDTO(name, capital, region, currency, flag));
                });
    }
}
