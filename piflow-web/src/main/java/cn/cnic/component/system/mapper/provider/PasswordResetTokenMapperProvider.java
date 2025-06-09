package cn.cnic.component.system.mapper.provider;

import cn.cnic.component.system.entity.PasswordResetToken;
import org.apache.ibatis.jdbc.SQL;

/**
 * @ClassName PasswordResetTokenMapperProvider
 * @Description TODO
 * @Author ty
 * @Date 2025/5/30 16:33
 * @Version 1.0
 **/
public class PasswordResetTokenMapperProvider {

//    public String save(PasswordResetToken token) {
//        SQL sql = new SQL();
//        sql.INSERT_INTO("password_reset_token");
//        sql.VALUES("id", "#{id}");
//        sql.VALUES("user_id", "#{userId}");
//        sql.VALUES("token", "#{token}");
//        sql.VALUES("expiry_date", "#{expiryDate}");
//        sql.VALUES("used", "#{used}");
//        return sql.toString();
//    }

//    public String update(PasswordResetToken token) {
//        SQL sql = new SQL();
//        sql.UPDATE("password_reset_token");
//        sql.SET("used = #{used}");
//        sql.WHERE("token = #{token}");
//        return sql.toString();
//    }
}
