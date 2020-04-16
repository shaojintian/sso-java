package com.sjt.sso.core.login;

import com.sjt.sso.core.conf.Conf;
import com.sjt.sso.core.store.SsoLoginStore;
import com.sjt.sso.core.user.SsoUser;
import com.sjt.sso.core.store.SsoSessionIdHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xuxueli 2018-11-15 15:54:40
 */
public class SsoTokenLoginHelper {

    /**
     * client login
     *
     * @param sessionId
     * @param  User
     */
    public static void login(String sessionId, SsoUser  User) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        SsoLoginStore.put(storeKey, User);
    }

    /**
     * client logout
     *
     * @param sessionId
     */
    public static void logout(String sessionId) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return;
        }

        SsoLoginStore.remove(storeKey);
    }
    /**
     * client logout
     *
     * @param request
     */
    public static void logout(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        logout(headerSessionId);
    }


    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    public static  SsoUser loginCheck(String  sessionId){

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return null;
        }

         SsoUser  User = SsoLoginStore.get(storeKey);
        if ( User != null) {
            String version = SsoSessionIdHelper.parseVersion(sessionId);
            if ( User.getVersion().equals(version)) {

                // After the expiration time has passed half, Auto refresh
                if ((System.currentTimeMillis() -  User.getExpireFreshTime()) >  User.getExpireMinute()/2) {
                     User.setExpireFreshTime(System.currentTimeMillis());
                    SsoLoginStore.put(storeKey,  User);
                }

                return  User;
            }
        }
        return null;
    }


    /**
     * login check
     *
     * @param request
     * @return
     */
    public static  SsoUser loginCheck(HttpServletRequest request){
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        return loginCheck(headerSessionId);
    }


}
