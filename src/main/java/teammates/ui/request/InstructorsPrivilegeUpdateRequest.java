package teammates.ui.request;

import teammates.common.datatransfer.InstructorPrivileges;

/**
 * The update request for multiple instructors' privilege.
 */
public class InstructorsPrivilegeUpdateRequest extends BasicRequest {

    private InstructorPrivileges privileges;

    @Override
    public void validate() throws InvalidHttpRequestBodyException {
        assertTrue(privileges != null, "Privileges cannot be null");
    }

    public InstructorPrivileges getPrivileges() {
        return privileges;
    }

    public void setPrivileges(InstructorPrivileges privileges) {
        this.privileges = privileges;
    }

}
