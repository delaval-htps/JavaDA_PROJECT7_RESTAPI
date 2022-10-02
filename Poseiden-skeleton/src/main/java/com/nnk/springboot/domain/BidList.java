package com.nnk.springboot.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bid_list")
public class BidList {
    // TODO: Map columns in data table BIDLIST with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bidListId;

    @NotBlank(message = "Account is mandatory")
    private String account;

    @NotBlank(message = "Type is mandatory")
    private String type;

    private Double bidQuantity;
    
    private Double askQuantity;
    
    private Double bid;
    
    private Double ask;
    
    private String benchmark;
    
    private Timestamp bidListDate;
    
    private String commentary;
    
    private String security;
    
    private String status;
    
    private String trader;
    
    private String book;
    
    private String creationName;
    
    private Timestamp creationDate;
    
    private String revisionName;
    
    private Timestamp revisionDate;
    
    private String dealName;
    
    private String dealType;
    
    private String sourceListId;
    
    private String side;

    public BidList(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}
