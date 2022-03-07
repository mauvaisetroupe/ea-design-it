package com.mauvaisetroupe.eadesignit.web.util;

import com.mauvaisetroupe.eadesignit.domain.Owner;
import com.mauvaisetroupe.eadesignit.domain.User;
import com.mauvaisetroupe.eadesignit.domain.util.Ownershipable;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
import com.mauvaisetroupe.eadesignit.security.AuthoritiesConstants;
import com.mauvaisetroupe.eadesignit.security.SecurityUtils;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Transactional
public class OwnershipChecker {

    @Autowired
    private OwnerRepository ownerRepository;

    public boolean check(Ownershipable ownershipable) {
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.WRITE)) return true;

        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.CONTRIBUTOR)) {
            if (StringUtils.hasText(userLogin.get()) && ownershipable.getOwner() != null && ownershipable.getOwner().getUsers() != null) {
                Optional<Owner> ownerOption = ownerRepository.findById(ownershipable.getOwner().getId());
                if (ownerOption.isPresent()) {
                    for (User user : ownerOption.get().getUsers()) {
                        if (user.getLogin().equals(userLogin.get())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
