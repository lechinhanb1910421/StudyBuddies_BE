package com.everett.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.everett.utils.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Mac_info")
@Table(name = "Mac_info", schema = "PUBLIC")
public class MacInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "macInfoId", nullable = false)
    private Long macInfoId;

    @Column(name = "createdAt")
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp createdAt;

    @Column(name = "triggerUserEmail")
    private String triggerUserEmail;

    @Column(name = "stackId")
    private String stackId;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "fileFullData")
    private String fileFullData;
}
