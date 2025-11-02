package se331.metricbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories") // ระบุชื่อ Collection (เทียบเท่า Table)
public class Category {

    @Id
    private String id; // ID ของ MongoDB เป็น String

    private String name;
    private String categoryImage;
}