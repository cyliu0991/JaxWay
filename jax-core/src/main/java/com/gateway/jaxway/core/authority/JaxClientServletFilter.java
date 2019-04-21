package com.gateway.jaxway.core.authority;

import com.alibaba.fastjson.JSON;
import com.gateway.jaxway.core.authority.bean.JaxRequest;
import com.gateway.jaxway.core.authority.impl.Base64JaxwayTokenCoder;
import com.gateway.jaxway.core.authority.impl.DefaultJaxwayAuthenticationDataStore;
import com.gateway.jaxway.core.authority.impl.DefaultJaxwayClientValidator;
import com.gateway.jaxway.core.vo.ResultVO;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.gateway.jaxway.core.common.JaxwayConstant.JAXWAY_REQUEST_TOKEN_HEADER_KEY;

/**
 * @Author huaili
 * @Date 2019/4/21 17:43
 * @Description JaxWay 的客户端的 Servlet 权限过滤器
 **/
public class JaxClientServletFilter implements Filter {

    private JaxwayClientValidator jaxwayClientValidator;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if(jaxwayClientValidator == null){
            jaxwayClientValidator = new DefaultJaxwayClientValidator(new Base64JaxwayTokenCoder(),new DefaultJaxwayAuthenticationDataStore());
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        JaxRequest jaxRequest = JaxRequest.newBuilder().token( req.getHeader(JAXWAY_REQUEST_TOKEN_HEADER_KEY)).url(req.getRequestURI()).build();
        if(jaxwayClientValidator.validate(jaxRequest)){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        servletResponse.getOutputStream().write(JSON.toJSONBytes(ResultVO.NOT_AUTHORIZED_REQUEST));

    }

    @Override
    public void destroy() {

    }
}
