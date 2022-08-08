package com.cortex.producer.converter.controller;

import com.cortex.producer.converter.service.ProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import dto.ConversionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController("CurrencyConverterController")
@RequestMapping(value = "cotacaomoedacortex")
public class ProducerController {

    private ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }


    @GetMapping("/conversao")
    public ResponseEntity<Object>currencyConversion(@RequestParam @Valid String dataCotacao,
                                                            @RequestParam @Valid String moedaOrigem,
                                                            @RequestParam @Valid String moedaFinal,
                                                            @RequestParam @Valid Double valorDesejado) {
        try {
            var conversionDTO = this.producerService.sendMessageConversion(dataCotacao, moedaOrigem, moedaFinal, valorDesejado);
            return ResponseEntity.status(HttpStatus.OK).body(conversionDTO);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
