package cn.cnic.component.system.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "Statistics")
public class Statistics {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 40)
    private String id;
    private String loginIp;
    private String loginUser;
    private Date loginTime;
}
