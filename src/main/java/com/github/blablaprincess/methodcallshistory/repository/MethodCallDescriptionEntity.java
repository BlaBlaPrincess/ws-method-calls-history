package com.github.blablaprincess.methodcallshistory.repository;

import com.github.blablaprincess.methodcallshistory.common.persistance.UuidEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MethodCallDescriptionEntity extends UuidEntity {

    @Transient
    public static final int MAX_ARGS_LENGTH = 512;

    @Transient
    public static final int MAX_RESPONSE_LENGTH = 255;

    private String method;

    @Column(length = MAX_ARGS_LENGTH)
    private String args;

    private boolean successful;

    @Column(length = MAX_RESPONSE_LENGTH)
    private String response;

    private LocalDateTime timestamp;

}
