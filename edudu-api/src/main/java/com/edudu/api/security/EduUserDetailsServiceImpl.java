package com.edudu.api.security;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.edudu.common.exception.ServiceException;
import com.edudu.common.utils.MessageUtils;
import com.edudu.common.utils.StringUtils;
import com.edudu.edu.domain.EduUser;
import com.edudu.edu.service.IEduUserService;

import java.util.Collections;

/**
 * API用户验证处理
 *
 * @author edudu
 */
@Service
public class EduUserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(EduUserDetailsServiceImpl.class);

    @Autowired
    private IEduUserService userService;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String phone) throws UsernameNotFoundException
    {
        EduUser user = userService.selectEduUserByPhone(phone);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", phone);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }
        else if ("2".equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", phone);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        else if ("1".equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", phone);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(EduUser user)
    {
        // API用户默认权限集合
        return new EduLoginUser(user, Collections.singleton("app:user"));
    }
}
