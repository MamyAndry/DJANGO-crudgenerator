package django.sourcegenerator.generator.service.view;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TypeProperties {
    HashMap<String, String> mapping;

    public TypeProperties(){
        this.initMapping();
    }

    public void initMapping(){
        HashMap<String, String> map = new HashMap<>();
        map.put("Timestamp", "date-time local");
        map.put("Time", "time");
        map.put("Date", "Date");
        map.put("Integer", "number");
        map.put("Double", "number");
        map.put("Float", "number");
        map.put("Long", "number");
        map.put("Boolean", "text");
        map.put("String", "text");
        map.put("Char", "text");
        map.put("BigDecimal", "number");
        map.put("BigInt", "number");
        map.put("[B", "file");
        this.setMapping(map);
    }
}
