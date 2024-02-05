package com.hopeback.entity.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "medicine")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Medicine {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // es의 id 값
    @Column(nullable = false)
    private String documentId;

    // es의 name 값
    @Column(nullable = false)
    private String name;
}
