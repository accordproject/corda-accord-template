package net.corda.accord;

import net.corda.core.node.ServiceHub;

public class BraidService {

    ServiceHub serviceHub;

    BraidService(ServiceHub serviceHub) {
        this.serviceHub = serviceHub;
    }

    public String whoAmI() {
        return serviceHub.getMyInfo().getLegalIdentities().get(0).getName().getOrganisation();
    }

}
