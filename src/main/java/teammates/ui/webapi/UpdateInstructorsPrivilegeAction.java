package teammates.ui.webapi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import teammates.common.datatransfer.InstructorPrivileges;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InstructorUpdateException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Const;
import teammates.common.util.Logger;
import teammates.ui.output.InstructorsPrivilegeData;
import teammates.ui.request.InstructorsPrivilegeUpdateRequest;
import teammates.ui.request.InvalidHttpRequestBodyException;

/**
 * Update all instructors privilege by instructors with instructor modify permission.
 */
class UpdateInstructorsPrivilegeAction extends Action {

    private static final Logger log = Logger.getLogger();

    @Override
    AuthType getMinAuthLevel() {
        return AuthType.LOGGED_IN;
    }

    @Override
    void checkSpecificAccessControl() throws UnauthorizedAccessException {
        String courseId = getNonNullRequestParamValue(Const.ParamsNames.COURSE_ID);
        InstructorAttributes instructor = logic.getInstructorForGoogleId(courseId, userInfo.getId());

        gateKeeper.verifyAccessible(
                instructor, logic.getCourse(courseId), Const.InstructorPermissions.CAN_MODIFY_INSTRUCTOR);
    }

    @Override
    public JsonResult execute() throws InvalidHttpRequestBodyException {
        String courseId = getNonNullRequestParamValue(Const.ParamsNames.COURSE_ID);

        String idOfUpdatingInstructor = getNonNullRequestParamValue(Const.ParamsNames.INSTRUCTOR_ID);
        List<InstructorAttributes> courseInstructors = logic.getInstructorsForCourse(courseId);

        InstructorsPrivilegeUpdateRequest request = getAndValidateRequestBody(InstructorsPrivilegeUpdateRequest.class);
        InstructorPrivileges newPrivileges = request.getPrivileges();
        newPrivileges.validatePrivileges();

        List<InstructorPrivileges> instructorPrivilegesList = new ArrayList<>();
        for (InstructorAttributes instructor : courseInstructors) {
            // Do not change the privilege of the instructor that made the request
            // If the instructor has not joined the course, skip the google id check
            if (instructor.getGoogleId() != null
                    && instructor.getGoogleId().equals(idOfUpdatingInstructor)) {
                continue;
            }

            // Since the request can contain any combination of privileges, set the role to custom
            instructor.setRole(Const.InstructorPermissionRoleNames.INSTRUCTOR_PERMISSION_ROLE_CUSTOM);
            instructor.setPrivileges(newPrivileges);
            logic.updateToEnsureValidityOfInstructorsForTheCourse(courseId, instructor);

            try {
                logic.updateInstructor(
                        InstructorAttributes
                                .updateOptionsWithEmailBuilder(instructor.getCourseId(), instructor.getEmail())
                                .withRole(instructor.getRole())
                                .withPrivileges(instructor.getPrivileges())
                                .build());
            } catch (InstructorUpdateException | InvalidParametersException e) {
                // Should not happen as only privilege is updated
                log.severe("Unexpected error", e);
                return new JsonResult(e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
            } catch (EntityDoesNotExistException ednee) {
                throw new EntityNotFoundException(ednee);
            }

            instructorPrivilegesList.add(instructor.getPrivileges());
        }

        InstructorsPrivilegeData response = new InstructorsPrivilegeData(instructorPrivilegesList);
        return new JsonResult(response);
    }

}
