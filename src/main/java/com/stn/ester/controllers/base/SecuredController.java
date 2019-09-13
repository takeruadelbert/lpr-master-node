package com.stn.ester.controllers.base;

import com.stn.ester.helpers.SessionHelper;
import com.stn.ester.services.crud.AccessGroupService;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.stn.ester.etc.security.SecurityConstants.ROLE_PREFIX;
import static com.stn.ester.etc.security.SecurityConstants.ROLE_SUPERADMIN;

public abstract class SecuredController implements BeanNameAware {

    String beanName;

    @Autowired
    private AccessGroupService accessGroupService;

    @Override
    public void setBeanName(final String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public String readCurrentUserRole(String moduleName) {
        String role = "NOACCESS";
        if (!SessionHelper.isSuperAdmin()) {
            Boolean isCrud = false;
            if (moduleName=="") {
                moduleName = getBeanName().replace("Controller", "");
                isCrud = true;
            }
            final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            final HttpServletRequest request = attr.getRequest();
            role = this.accessGroupService.findAccessRole(com.stn.ester.entities.enumerate.RequestMethod.valueOf(request.getMethod().toUpperCase()), moduleName, isCrud);
        } else {
            role = ROLE_PREFIX + "_" + ROLE_SUPERADMIN;
        }
        return role;
    }

    public String readCurrentUserRole() {
        return readCurrentUserRole("");
    }

    public String superAdminRole() {
        return ROLE_PREFIX + "_" + ROLE_SUPERADMIN;
    }
}
