// this code is generated and should not be modified
package org.hyperledger.composer.system;
import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "$class")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "$id")
public abstract class Resource
{
    public abstract String getID();
    private String $id;
    
    @JsonProperty("$id")
    public String get$id() {
        return $id; 
    }
    @JsonProperty("$id")
    public void set$id(String i) {
        $id = i; 
    }

}
        
