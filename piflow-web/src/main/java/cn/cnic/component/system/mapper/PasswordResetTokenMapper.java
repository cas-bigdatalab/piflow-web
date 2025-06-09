package cn.cnic.component.system.mapper;

import cn.cnic.component.system.entity.PasswordResetToken;
import cn.cnic.component.system.mapper.provider.PasswordResetTokenMapperProvider;
import org.apache.ibatis.annotations.*;

/**
 * @InterfaceName PasswordResetTokenMapper
 * @Description TODO
 * @Author ty
 * @Date 2025/5/30 16:31
 * @Version 1.0
 **/
@Mapper
public interface PasswordResetTokenMapper {

    @Insert({
            "INSERT INTO password_reset_token (id, user_id, token, expiry_date, used)",
            "VALUES (#{id}, #{userId}, #{token}, #{expiryDate}, #{used})"
    })
    int save(PasswordResetToken token);

    @Select("SELECT * FROM password_reset_token WHERE token = #{token}")
    PasswordResetToken findByToken(String token);

    @Update("UPDATE password_reset_token SET used = true WHERE token = #{token}")
    int markAsUsed(String token);

}
