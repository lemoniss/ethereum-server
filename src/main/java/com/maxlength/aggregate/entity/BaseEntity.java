package com.maxlength.aggregate.entity;


import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name= "reg_id")
    private Long regId;

    @Column(name= "reg_dt")
    private LocalDateTime regDt;

    @Column(name= "mod_id")
    private Long modId;

    @Column(name= "mod_dt")
    private LocalDateTime modDt;

    public BaseEntity() {
        if(regId == null) this.regId = 1L;
        if(regDt == null) this.regDt = LocalDateTime.now();
    }

}
