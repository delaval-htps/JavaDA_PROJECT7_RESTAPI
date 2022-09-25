package com.nnk.springboot.domain;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

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
    @Column(name="curve_id", nullable=false)
    private Integer curveId;

    private Timestamp asOfDate;

    private Double term;
    
    private Double value;
    
    private Timestamp creationDate;

    public CurvePoint(Integer id, Double term, Double value) {
        this.curveId = id;
        this.term = term;
        this.value = value;
    }

}
