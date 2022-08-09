package com.cortex.consumer.converter.service;

import dto.ConversionDTO;
import dto.RequestDTO;
import dto.ResponseBcbDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalTime;
import java.util.Objects;


@Slf4j
@Service
public class ConsumerService {
    @Value("${bcb.quote-url}")
    private String bcbQuoteURL;

    @Value("${bcb.format}")
    private String bcbFormatURL;

    private final WebClient webClient;

    private ConversionDTO conversionDTO;

    public ConsumerService() {
        this.conversionDTO = new ConversionDTO();
        this.webClient = WebClient.builder()
                .baseUrl("https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ConversionDTO convertCurrency(RequestDTO requestDTO) throws Exception {
        Double finalCurrencyQuote   = getQuoteOfDay(requestDTO.getFinalCurrency(), requestDTO.getQuotationDate(), "Final");
        Double sourceCurrencyQuote  = getQuoteOfDay(requestDTO.getOriginCurrency(), requestDTO.getQuotationDate(), "Origem");

        this.conversionDTO.setRequestTime(LocalTime.now());
        this.conversionDTO.setValue(requestDTO.getValueConversion() * (finalCurrencyQuote / sourceCurrencyQuote));

        return this.conversionDTO;
    }


    private Double getQuoteOfDay(String typeCurrency, String quotationDate, String tipoMoeda) throws Exception {
        String url = builderStringApiBCBURL(typeCurrency, quotationDate);

        var quotesOfDay = Objects.requireNonNull(webClient
                .get()
                .uri("/" + url)
                .retrieve()
                .bodyToFlux(ResponseBcbDTO.class).blockFirst()).getValue();

        if (quotesOfDay.size() == 0) {
            throw new Exception("Dado Inválido");
        }

        log.info("Informações recuperadas da API! Moeda: " + tipoMoeda);
        return (Double) quotesOfDay.get(quotesOfDay.size()-1).get("cotacaoCompra"); //retornando o valor da moeda mais recente

    }

    private String builderStringApiBCBURL(String sourceCurrency, String quotationDate) {
        var currencyCodeFilter  = String.format("?@moeda='%s'", sourceCurrency);
        var quotationDateFilter = String.format("&@dataInicial='%s'&@dataFinalCotacao='%s'", quotationDate, quotationDate);
        var formatURL           = String.format("&$format=%s", bcbFormatURL);
        return bcbQuoteURL + currencyCodeFilter + quotationDateFilter + formatURL;
    }
}
