package com.nnk.springboot.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rule_name")
public class RuleName {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    String name;
    String description;
    String json;
    String template;
    String sqlStr;
    String sqlPart;

    public RuleName(String ruleName, String description, String json, String template, String sqlString,
            String sqlPart) {

        this.name = ruleName;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlString;
        this.sqlPart = sqlPart;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        return "RuleName [id=" + id + ", name=" + name + ", description=" + description + ", json=" + json + ", template=" + template + ", sqlStr=" + sqlStr + ", sqlPart=" + sqlPart + "]";
    }
    
}
