package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * User : Manish K. Gupta
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ItemHsnKey {
    private String hsn;
    private int rate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemHsnKey that = (ItemHsnKey) o;
        return rate == that.rate && Objects.equals(hsn, that.hsn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hsn, rate);
    }
}
