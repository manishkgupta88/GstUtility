package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * User : Manish K. Gupta
 */

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ExempRecord {
    private String description;
    private String nilSupply;
    private String exemptedSupply;
    private String nonGstSupply;
}
