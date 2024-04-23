package cn.cnic.component.visual.mapper.piflowdb;


import cn.cnic.component.visual.entity.GraphConf;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * TODO
 * author:hmh
 * date:2023-09-22
 */
@Mapper
public interface GraphConfMapper extends BaseMapper<GraphConf> {
    @Insert("insert into graph_conf (name, type, data_base_id, data_x_axis, data_y_axis, p_coordinate_up, p_coordinate_down, p_coordinate_left," +
            " p_coordinate_right, p_title_display, p_title_x_align, p_title_y_align, p_title_content, p_title_font_size, " +
            "p_title_font_weight, p_title_font_color, p_box_display, p_key_display, p_key_x_align, p_key_y_align, p_key_place, " +
            "p_key_font_size, p_key_font_color, p_x_display, p_x_type, p_x_name, p_x_name_place, p_x_font_size, p_x_line_color, " +
            "p_x_line_weight, p_x_line_type, p_x_scale_display, p_x_scale_space, p_x_tag_display, p_x_tag_space, p_x_tag_angle, " +
            "p_x_tag_font, p_x_split_display, p_x_split_weight, p_x_split_type, p_y_display, p_y_type, p_y_max, p_y_min, p_y_part," +
            " p_y_name, p_y_font_size, p_y_line_display, p_y_line_color, p_y_line_weight, p_y_line_type, p_y_tag_display, " +
            "p_y_tag_font, p_y_split_display, p_y_split_weight, p_y_split_type,create_time,update_time) " +
            "values (#{name},#{type}, #{data_base_id} #{dataXAxis}, #{dataYAxis}, #{proCoordinateUp}, #{proCoordinateDown}, #{proCoordinateLeft}, #{proCoordinateRight}, #{proTitleDisplay}, #{proTitleXAlign}, #{proTitleYAlign}, #{proTitleContent}, #{proTitleFontSize}, #{proTitleFontWeight}, #{proTitleFontColor}, #{proBoxDisplay}, #{proKeyDisplay}, #{proKeyXAlign}, #{proKeyYAlign}, #{proKeyPlace}, #{proKeyFontSize}, #{proKeyFontColor}, #{proXDisplay}, #{proXType}, #{proXName}, #{proXNamePlace}, #{proXFontSize}, #{proXLineColor}, #{proXLineWeight}, #{proXLineType}, #{proXScaleDisplay}, #{proXScaleSpace}, #{proXTagDisplay}, #{proXTagSpace}, #{proXTagAngle}, #{proXTagFont}, #{proXSplitDisplay}, #{proXSplitWeight}, #{proXSplitType}, #{proYDisplay}, #{proYType}, #{proYMax}, #{proYMin}, #{proYPart}, #{proYName}, #{proYFontSize}, #{proYLineDisplay}, #{proYLineColor}, #{proYLineWeight}, #{proYLineType}, #{proYTagDisplay}, #{proYTagFont}, #{proYSplitDisplay}, #{proYSplitWeight}, #{proYSplitType}, #{createTime}, #{updateTime})")
    int insertSelf(GraphConf graphConf);
    @Delete("delete from graph_conf where id = #{id}")
    int deleteByIdSelf(int id);
    @Delete("delete from graph_conf where name = #{name}")
    int deleteByName(String name);
//    @Update("update graph_conf set type=#{type}, data_x_axis=#{dataXAxis}, data_y_axis=#{dataYAxis}, p_coordinate_up=#{proCoordinateUp}, p_coordinate_down=#{proCoordinateDown}, p_coordinate_left=#{proCoordinateLeft}, p_coordinate_right=#{proCoordinateRight}, p_title_display=#{proTitleDisplay}, p_title_x_align=#{proTitleXAlign}, p_title_y_align=#{proTitleYAlign}, p_title_content=#{proTitleContent}, p_title_font_size=#{proTitleFontSize}, p_title_font_weight=#{proTitleFontWeight}, p_title_font_color=#{proTitleFontColor}, p_box_display=#{proBoxDisplay}, p_key_display=#{proKeyDisplay}, p_key_x_align=#{proKeyXAlign}, p_key_y_align=#{proKeyYAlign}, p_key_place=#{proKeyPlace}, p_key_font_size=#{proKeyFontSize}, p_key_font_color=#{proKeyFontColor}, p_x_display=#{proXDisplay}, p_x_type=#{proXType}, p_x_name=#{proXName}, p_x_name_place=#{proXNamePlace}, p_x_font_size=#{proXFontSize}, p_x_line_color=#{proXLineColor}, p_x_line_weight=#{proXLineWeight}, p_x_line_type=#{proXLineType}, p_x_scale_display=#{proXScaleDisplay}, p_x_scale_space=#{proXScaleSpace}, p_x_tag_display=#{proXTagDisplay}, p_x_tag_space=#{proXTagSpace}, p_x_tag_angle=#{proXTagAngle}, p_x_tag_font=#{proXTagFont}, p_x_split_display=#{proXSplitDisplay}, p_x_split_weight=#{proXSplitWeight}, p_x_split_type=#{proXSplitType}, p_y_display=#{proYDisplay}, p_y_type=#{proYType}, p_y_max=#{proYMax}, p_y_min=#{proYMin}, p_y_part=#{proYPart}, p_y_name=#{proYName}, p_y_font_size=#{proYFontSize}, p_y_line_display=#{proYLineDisplay}, p_y_line_color=#{proYLineColor}, p_y_line_weight=#{proYLineWeight}, p_y_line_type=#{proYLineType}, p_y_tag_display=#{proYTagDisplay}, p_y_tag_font=#{proYTagFont}, p_y_split_display=#{proYSplitDisplay}, p_y_split_weight=#{proYSplitWeight}, p_y_split_type=#{proYSplitType},update_time=#{updateTime}" +
//            " where id = #{id}")
//    int updateByIdSelf(GraphConf graphConf);
    @Select("select * from graph_conf where name = #{name}")
    List<GraphConf> getGraphConfByNameSelf(String name);

}
