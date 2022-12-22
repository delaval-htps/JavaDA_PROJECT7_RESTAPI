package com.nnk.springboot.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class Rating
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    String moodysRating;

    @NotBlank
    String sandPRating;

    @NotBlank
    String fitchRating;

    @NotNull
    @Digits(integer = 5, fraction = 0, message = "OrderNumber must not have more than 5 digits")
    @Min(value = 1, message = "Order number must be strictly ")
    Integer orderNumber;

    public Rating(String moodysRating, String sandPRating, String fitchRating, int orderNumber) {
        this.orderNumber = orderNumber;
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        return "Rating [id=" + id + ", moodysRating=" + moodysRating + ", sandPRating=" + sandPRating + ", fitchRating="
                + fitchRating + ", orderNumber=" + orderNumber + "]";
    }

}
