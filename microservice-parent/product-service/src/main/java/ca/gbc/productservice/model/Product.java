package ca.gbc.productservice.model;
import lombok.Data;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Product {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
