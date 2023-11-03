package com.everett.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.everett.models.type.MacRecordStatus;
import com.everett.utils.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Mac_record")
@Table(name = "Mac_record", schema = "PUBLIC")
public class MacRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "macRecId", nullable = false)
    private Long macRecId;

    @Column(name = "createdAt")
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MacRecordStatus status;

    @Column(name = "statusMessage")
    private String statusMessage;

    @Column(name = "stackId")
    private String stackId;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "userTempPass")
    private String userTempPass;
}
