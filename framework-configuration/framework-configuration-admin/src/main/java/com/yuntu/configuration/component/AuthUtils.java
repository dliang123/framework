package com.yuntu.configuration.component;

import com.yuntu.web.AuthorityProcess;
import com.yuntu.web.exception.NoAuthorityException;
import com.yuntu.web.exception.NoLoginException;
import com.yuntu.web.util.WebUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Stream;

/**
 *  16/1/14.
 */
@Component
@SuppressWarnings({"rawtypes","unchecked"})
public class AuthUtils implements AuthorityProcess {

    public static final String PERMISSION_KEY="PERMISSION_KEY";
    public static final String PERMISSION_LOGIN="LOGIN";


	public boolean validationByPermissions(String... permissions) throws NoAuthorityException {

        Object object = WebUtils.getSession().getAttribute(PERMISSION_KEY);
        if( object instanceof Collection){
			Collection<String> permissionsCollection = (Collection)object;
            for(String permission : permissions){
                if( permissionsCollection.contains(permission) ){
                    return true;
                }
            }

        }
        if(Stream.of(permissions).filter( e -> PERMISSION_LOGIN.equals(e) ).findFirst().isPresent()){
            throw new NoLoginException();
        }


        return false;

    }

    public boolean validationByResources(String resources) throws NoAuthorityException {
        return false;
    }
}
