package dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
public class ConversionDTO implements Serializable {

    private static final long serialVersionUID = 1234L;
    private Double value;
    private LocalTime requestTime;
}
