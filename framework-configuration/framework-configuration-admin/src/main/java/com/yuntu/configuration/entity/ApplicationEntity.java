package com.yuntu.configuration.entity;

import com.yuntu.jpa.AuditEntity;

import javax.persistence.*;

/**
 *  16/1/14.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "application")
public class ApplicationEntity extends AuditEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @Column(name = "application_key")
    private String key;

    @Column(name = "application_name")
    private String name;

    @Column(name = "application_describe")
    private String describe;

    @Column(name = "application_cluster")
    private String cluster;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
}
