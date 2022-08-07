package dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
public class ResponseBcbDTO implements Serializable {

    private static final long serialVersionUID = 8901L;
    private List<HashMap<Object, Object>> value;
}
