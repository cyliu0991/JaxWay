package com.gateway.jaxway.admin.service;

import com.gateway.jaxway.admin.dao.model.JaxwayServerModel;
import com.gateway.jaxway.admin.dao.model.UserModel;
import com.gateway.jaxway.admin.vo.UserInfoVO;

import java.util.List;

/**
 * @Author huaili
 * @Date 2019/5/15 16:42
 * @Description UserService
 **/
public interface UserService {

    UserModel checkUser(String username, String pwd);

    boolean checkUserAuthority(String username,String path);

    void insertUser(UserInfoVO userInfoVO);

    boolean checkJaxwayServerAuthority(Integer jaxwayServerId,Integer userId);

    List<JaxwayServerModel> getJaxwayServersByUserId(Integer userId);

}
