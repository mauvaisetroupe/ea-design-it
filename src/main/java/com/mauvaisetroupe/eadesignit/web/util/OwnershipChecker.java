package com.mauvaisetroupe.eadesignit.web.util;

import com.mauvaisetroupe.eadesignit.domain.Owner;
import com.mauvaisetroupe.eadesignit.domain.User;
import com.mauvaisetroupe.eadesignit.domain.util.Ownershipable;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
import com.mauvaisetroupe.eadesignit.security.AuthoritiesConstants;
import com.mauvaisetroupe.eadesignit.security.SecurityUtils;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Transactional
public class OwnershipChecker {

    @Autowired
    private OwnerRepository ownerRepository;

    public boolean check(Ownershipable ownershipable) {
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.WRITE)) return true;
        String userLogin = SecurityUtils.getCurrentUserLogin().orElse(null);
        if (!StringUtils.hasText(userLogin)) return false;
        if (ownershipable == null) return false;
        if (ownershipable.getOwner() == null) return false;
        if (ownershipable.getOwner().getUsers() == null) return false;
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.CONTRIBUTOR)) {
            Owner owner = ownerRepository.findById(ownershipable.getOwner().getId()).orElse(null);
            if (owner != null) {
                for (User user : owner.getUsers()) {
                    if (user.getLogin().equals(userLogin)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
