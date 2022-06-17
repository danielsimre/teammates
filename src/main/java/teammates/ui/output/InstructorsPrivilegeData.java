package teammates.ui.output;

import java.util.List;

import teammates.common.datatransfer.InstructorPrivileges;

/**
 * The output format for a list of instructor privileges.
 */
public class InstructorsPrivilegeData extends ApiOutput {

    private final List<InstructorPrivileges> privilegesList;

    public InstructorsPrivilegeData(List<InstructorPrivileges> privilegesList) {
        this.privilegesList = privilegesList;
    }

    public List<InstructorPrivileges> getPrivilegesData() {
        return privilegesList;
    }

}
