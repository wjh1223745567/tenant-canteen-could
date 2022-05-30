package com.iotinall.canteen.common.security;

import com.iotinall.canteen.common.tenant.TenantUserService;
import com.iotinall.canteen.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 业务过滤器
 *
 * @author xin-bing
 * @date 10/17/2019 15:32
 */
@Slf4j
@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {
    @Resource
    private RedisTemplate<String, SecurityUserDetails> redisTemplate;

    @Value("${login-timeout:30}")
    private long loginTimeout;

    public static String TOKEN_KEY = "X-Authorization";

    /**
     * 小程序TOKEN
     */
    public static String APP_TOKEN_KEY = "token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        //木有用户，当configuration中设置SessionCreationPolicy.STATELESS，不创建会话时，这里每一次都是空
        if (StringUtils.isNotBlank(token)) {
            SecurityUserDetails userDetails = redisTemplate.opsForValue().get(token);
            TenantUserService tenantUserService = SpringContextUtil.getBean(TenantUserService.class);
            if (userDetails != null) {
                tenantUserService.changeDataSource(userDetails.getSourceInfo());
                redisTemplate.expire(token, loginTimeout, TimeUnit.MINUTES);
                userDetails.setAuthorities(userDetails.getPermissions().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                String[] openids = token.split(IUserService.openIdKey);
                if(openids.length == 2 && StringUtils.isNotBlank(openids[1])){
                    IUserService userService = SpringContextUtil.getBean(IUserService.class);
                    if (userService != null){
                        SecurityUserDetails openidUserDetails = userService.loadDataByOpenId(openids[1]);
                        if(openidUserDetails != null){
                            tenantUserService.changeDataSource(openidUserDetails.getSourceInfo());
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(openidUserDetails, null, openidUserDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    // 获取token
    public static String getToken(HttpServletRequest request) {
        String apptoken = request.getHeader(TOKEN_KEY);
        String systoken = request.getHeader(APP_TOKEN_KEY);
        return StringUtils.isNotBlank(apptoken) ? apptoken : StringUtils.isNotBlank(systoken) ? systoken : null;
    }
}
