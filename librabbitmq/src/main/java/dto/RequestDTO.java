package dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestDTO implements Serializable{


    private static final long serialVersionUID = 4567L;
    private String quotationDate;

    private String originCurrency;

    private String finalCurrency;

    private Double valueConversion;

    private String correlation;

    public RequestDTO() {}

    public RequestDTO(String quotationDate, String originCurrency,
                           String finalCurrency, Double valueConversion) {
        this.quotationDate = quotationDate;
        this.originCurrency = originCurrency;
        this.finalCurrency = finalCurrency;
        this.valueConversion = valueConversion;
    }
}
