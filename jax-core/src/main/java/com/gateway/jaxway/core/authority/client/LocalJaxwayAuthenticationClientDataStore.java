package com.gateway.jaxway.core.authority.client;

import com.gateway.jaxway.core.authority.JaxwayClientAuthenticationDataStore;
import com.gateway.jaxway.core.authority.JaxwayCoder;
import com.gateway.jaxway.core.authority.impl.Base64JaxwayCoder;
import com.gateway.jaxway.core.vo.JaxClientAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author huaili
 * @Date 2019/4/17 20:25
 * @Description LocalJaxwayAuthenticationClientDataStore
 **/
public class LocalJaxwayAuthenticationClientDataStore implements JaxwayClientAuthenticationDataStore {
    private Logger logger = LoggerFactory.getLogger(LocalJaxwayAuthenticationClientDataStore.class);

    private static JaxwayClientAuthenticationDataStore INSTANCE = new LocalJaxwayAuthenticationClientDataStore();

    private JaxwayCoder jaxwayCoder;

    private LocalJaxwayAuthenticationClientDataStore(){
        this(new Base64JaxwayCoder());
    }

    private LocalJaxwayAuthenticationClientDataStore(JaxwayCoder jaxwayCoder){
        this.jaxwayCoder = jaxwayCoder;
    }

    public static JaxwayClientAuthenticationDataStore instance(){
        return INSTANCE;
    }

    private static volatile Map<String, Set<String>> whiteAppSets = new ConcurrentHashMap<>();

    @Override
    public void updateAppAuthentications(Map<String, Set<String>> newAppAppAuthenticationMap) {
        whiteAppSets.putAll(newAppAppAuthenticationMap);
    }

    @Override
    public void updateAppAuthentications(JaxClientAuthentication jaxAuthentication) {
        String token = null;
        String appId = null;
        try {
            token = jaxwayCoder.decode(jaxAuthentication.getToken());
            appId = jaxwayCoder.decode(jaxAuthentication.getAppId());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("decode token error from portal,{}",e.getMessage());
            return ;
        }

        switch (jaxAuthentication.getOpType()){
            case ADD:
                Set<String> toAddTokenSet = whiteAppSets.get(appId);
                if(CollectionUtils.isEmpty(toAddTokenSet)){
                    toAddTokenSet = new HashSet<>();
                    whiteAppSets.put(appId,toAddTokenSet);
                }
                toAddTokenSet.add(token);
                logger.trace("add info appId={} token={} ",appId,token);
                break;
            case DELETE:
                Set<String> toDeleTokenSet = whiteAppSets.get(appId);
                if(!CollectionUtils.isEmpty(toDeleTokenSet)){
                    toDeleTokenSet.remove(token);
                }
                logger.trace("dele info appId={} token={} ",appId,token);
                break;
        }
    }

    @Override
    public Map<String, Set<String>> getAllAppAuthentications() {
        return whiteAppSets;
    }


}