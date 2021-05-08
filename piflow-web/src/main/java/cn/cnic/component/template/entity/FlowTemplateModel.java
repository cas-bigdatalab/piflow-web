package cn.cnic.component.template.entity;
//
//import org.hibernate.annotations.GenericGenerator;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "flow_template")
//public class FlowTemplateModel implements Serializable {
//    /**
//     * flow template
//     */
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GenericGenerator(name = "idGenerator", strategy = "uuid")
//    @GeneratedValue(generator = "idGenerator")
//    @Column(length = 40)
//    private String id;
//
//    private String name;
//
//    @Column(name = "description",columnDefinition="varchar(1024) COMMENT 'description'")
//    private String description;
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//}
