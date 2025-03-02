package com.nnk.springboot.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.nnk.springboot.validation.QuantityConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class CurvePoint
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "curve_point")
public class CurvePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "must not be null")
    @Range(min = 1)
    @Column(name = "curve_id", nullable = false)
    private Integer curveId;

    private Timestamp asOfDate;

    @QuantityConstraint
    private Double term;
    
    @QuantityConstraint
    @Column(name = "`value`")
    private Double value;

    private Timestamp creationDate;

    public CurvePoint(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        return "CurvePoint [id=" + id + ", curveId=" + curveId + ", term=" + term + ", value=" + value
                + ", creationDate=" + creationDate + "]";
    }

}
