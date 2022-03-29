package com.maxlength.aggregate.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.maxlength.spec.enums.Yesno;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Table(name = "user_mst")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name= "name", length = 50)   // 이름
    private String name;

    @Column(name= "sexdstn", length = 1, columnDefinition = "char")   // 성별
    private String sexdstn;

    @Column(name= "birthday", length = 10)   // 생년월일
    private String birthday;

    @Column(name= "mbtlnum", length = 20)   // 휴대폰번호
    private String mbtlnum;

    @Column(name= "email", length = 50)   // 이메일
    private String email;

    @Column(name= "ci", length = 100)   // CI
    private String ci;

    @Column(name= "del_yn", length = 1, columnDefinition = "char")
    @Enumerated(EnumType.STRING)
    private Yesno delYn;

    @Builder.Default
    @OneToMany(mappedBy= "userEntity", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AccountEntity> accountEntityList = new ArrayList<>();

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private WalletEntity walletEntity;
}