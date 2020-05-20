package com.nature.component.mxGraph.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "MX_NODE_IMAGE")
public class MxNodeImage extends BaseHibernateModelUUIDNoCorpAgentId {

    private String imageName;
    private String imagePath;
    private String imageUrl;
    private String imageType;

}
