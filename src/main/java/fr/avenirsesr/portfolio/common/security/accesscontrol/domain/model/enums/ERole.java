package fr.avenirsesr.portfolio.common.security.accesscontrol.domain.model.enums;

import java.util.HashSet;
import java.util.Set;

public enum ERole {
  // ---- Common ----
  ROLE_USER_BASE(
      "Base account capabilities: own profile and own notifications",
      Set.of(),
      Set.of(),
      Set.of(
          EPermissionGroup.PROFILE_MANAGEMENT_OWN, EPermissionGroup.NOTIFICATION_MANAGEMENT_OWN)),

  // ---- Student space ----
  ROLE_STUDENT_PORTFOLIO(
      "Personal portfolio content: traces, declared activities, skills, experiences, programs,"
          + " self-knowledge and their associations",
      Set.of(),
      Set.of(EPermission.PERM_ASSOCIATION_MANAGE),
      Set.of(
          EPermissionGroup.TRACE_MANAGEMENT_OWN,
          EPermissionGroup.DECLARED_ACTIVITY_MANAGEMENT_OWN,
          EPermissionGroup.DECLARED_SKILL_MANAGEMENT_OWN,
          EPermissionGroup.DECLARED_EXPERIENCE_MANAGEMENT_OWN,
          EPermissionGroup.DECLARED_PROGRAM_MANAGEMENT_OWN,
          EPermissionGroup.SELF_KNOWLEDGE_MANAGEMENT_OWN)),

  ROLE_STUDENT_ACTIVITY(
      "Student access to the activity catalog, registration and activity documents",
      Set.of(),
      Set.of(),
      Set.of(EPermissionGroup.STUDENT_ACTIVITY_ACCESS)),

  ROLE_STUDENT_FEEDBACK(
      "Student feedback: request feedback and view the feedback received",
      Set.of(),
      Set.of(),
      Set.of(EPermissionGroup.STUDENT_FEEDBACK_ACCESS)),

  ROLE_STUDENT_EMPLOYMENT_KIT(
      "Own employment kit page and download of its multimedia content",
      Set.of(),
      Set.of(EPermission.PERM_EMPLOYMENT_KIT_READ_OWN, EPermission.PERM_KIT_DOWNLOAD_MEDIA_OWN),
      Set.of()),

  ROLE_STUDENT_REFERENCE_DATA(
      "Read access to the reference data used by the student space: competencies, external skills"
          + " and trace/additional-skill settings",
      Set.of(),
      Set.of(
          EPermission.PERM_COMPETENCY_READ,
          EPermission.PERM_EXTERNAL_SKILL_READ,
          EPermission.PERM_ADDITIONAL_SKILL_CONFIG_READ,
          EPermission.PERM_TRACE_CONFIG_READ),
      Set.of()),

  // ---- Staff space ----
  ROLE_STAFF_TRACE_REVIEWER(
      "View and download student traces made accessible in an authorized context",
      Set.of(),
      Set.of(EPermission.PERM_TRACE_READ_CONTEXTUAL, EPermission.PERM_TRACE_DOWNLOAD_CONTEXTUAL),
      Set.of()),

  ROLE_STAFF_ACTIVITY_VIEWER(
      "Read-only access to the activity catalog, activity documents and the staff library",
      Set.of(),
      Set.of(),
      Set.of(EPermissionGroup.STAFF_ACTIVITY_ACCESS)),

  ROLE_ACTIVITY_EDITOR(
      "Create, update and delete unpublished activities",
      Set.of(),
      Set.of(),
      Set.of(EPermissionGroup.ACTIVITY_MANAGEMENT)),

  ROLE_STAFF_ACTIVITY_AUTHOR(
      "Activity authoring: edition of activities plus duplication and update of published ones",
      Set.of(ROLE_ACTIVITY_EDITOR),
      Set.of(
          EPermission.PERM_ACTIVITY_DUPLICATE,
          EPermission.PERM_ACTIVITY_PUBLISHED_UPDATE_CONTEXTUAL),
      Set.of()),

  ROLE_STAFF_FEEDBACK_RESPONDER(
      "View and respond to the feedback requests assigned to the staff member",
      Set.of(),
      Set.of(
          EPermission.PERM_FEEDBACK_REQUEST_READ_ASSIGNED_CONTEXTUAL,
          EPermission.PERM_FEEDBACK_REQUEST_RESPOND_ASSIGNED_CONTEXTUAL),
      Set.of()),

  ROLE_STAFF_FEEDBACK_SUPERVISOR(
      "Monitor the feedback history and dashboard of the institution",
      Set.of(),
      Set.of(
          EPermission.PERM_FEEDBACK_HISTORY_READ_CONTEXTUAL,
          EPermission.PERM_FEEDBACK_DASHBOARD_READ_CONTEXTUAL),
      Set.of()),

  // ---- Administration: one viewer / manager pair per domain ----
  ROLE_ESTABLISHMENT_VIEWER(
      "Read-only access to primary and secondary institutions",
      Set.of(),
      Set.of(
          EPermission.PERM_PRIMARY_ESTABLISHMENT_READ,
          EPermission.PERM_SECONDARY_ESTABLISHMENT_READ),
      Set.of()),

  ROLE_ESTABLISHMENT_MANAGER(
      "Full management of primary and secondary institutions",
      Set.of(),
      Set.of(),
      Set.of(EPermissionGroup.ESTABLISHMENT_MANAGEMENT)),

  ROLE_GROUP_VIEWER(
      "Read-only access to groups", Set.of(), Set.of(EPermission.PERM_GROUP_READ), Set.of()),

  ROLE_GROUP_MANAGER(
      "Full management of groups, including import",
      Set.of(),
      Set.of(),
      Set.of(EPermissionGroup.GROUP_MANAGEMENT)),

  ROLE_EXTERNAL_USER_VIEWER(
      "Read-only access to external users",
      Set.of(),
      Set.of(EPermission.PERM_EXTERNAL_USER_READ),
      Set.of()),

  ROLE_EXTERNAL_USER_MANAGER(
      "Full management of external users and their affiliations",
      Set.of(),
      Set.of(),
      Set.of(EPermissionGroup.EXTERNAL_USER_MANAGEMENT)),

  ROLE_CONFIG_VIEWER(
      "Read-only access to the back-office configuration",
      Set.of(),
      Set.of(
          EPermission.PERM_ADDITIONAL_SKILL_CONFIG_READ,
          EPermission.PERM_INSTITUTION_CONFIG_READ,
          EPermission.PERM_TRACE_CONFIG_READ,
          EPermission.PERM_WEBSITE_CONTENT_CONFIG_READ),
      Set.of()),

  ROLE_CONFIG_MANAGER(
      "Full management of the back-office configuration and terms of use",
      Set.of(),
      Set.of(),
      Set.of(EPermissionGroup.BACKOFFICE_CONFIG_MANAGEMENT)),

  ROLE_RBAC_AUDITOR(
      "Read-only access to RBAC administration data",
      Set.of(),
      Set.of(EPermission.PERM_RBAC_READ),
      Set.of()),

  ROLE_RBAC_ADMIN(
      "Full RBAC administration: roles, assignments, scopes and contexts",
      Set.of(),
      Set.of(),
      Set.of(EPermissionGroup.RBAC_MANAGEMENT)),

  ROLE_ACTIVITY_ADMIN(
      "Administer any activity of the platform, including definitive deletion",
      Set.of(ROLE_ACTIVITY_EDITOR),
      Set.of(EPermission.PERM_ACTIVITY_ADMIN_MANAGEMENT),
      Set.of()),

  ROLE_SYSTEM_OPERATOR(
      "Destructive system operations such as resetting and reseeding the database",
      Set.of(),
      Set.of(),
      Set.of(EPermissionGroup.SYSTEM_ADMINISTRATION)),

  // ---- Profiles: compositions of the roles above ----
  ROLE_STUDENT(
      "Student with a personal COFOLIO space for their life project and skills-based approach",
      Set.of(
          ROLE_USER_BASE,
          ROLE_STUDENT_PORTFOLIO,
          ROLE_STUDENT_ACTIVITY,
          ROLE_STUDENT_FEEDBACK,
          ROLE_STUDENT_EMPLOYMENT_KIT,
          ROLE_STUDENT_REFERENCE_DATA),
      Set.of(),
      Set.of()),

  ROLE_STAFF(
      "Staff member providing educational guidance and handling feedback",
      Set.of(
          ROLE_USER_BASE,
          ROLE_STAFF_TRACE_REVIEWER,
          ROLE_STAFF_ACTIVITY_VIEWER,
          ROLE_STAFF_ACTIVITY_AUTHOR,
          ROLE_STAFF_FEEDBACK_RESPONDER,
          ROLE_STAFF_FEEDBACK_SUPERVISOR),
      Set.of(),
      Set.of()),

  ROLE_ADMIN(
      "Administrator of an institution, in charge of its secondary institutions, its groups and"
          + " its external users",
      Set.of(ROLE_ESTABLISHMENT_MANAGER, ROLE_GROUP_MANAGER, ROLE_EXTERNAL_USER_MANAGER),
      Set.of(),
      Set.of()),

  ROLE_SUPER_ADMIN(
      "Global administrator of the COFOLIO platform",
      Set.of(
          ROLE_ADMIN,
          ROLE_ACTIVITY_ADMIN,
          ROLE_RBAC_ADMIN,
          ROLE_CONFIG_MANAGER,
          ROLE_SYSTEM_OPERATOR),
      Set.of(),
      Set.of());

  private final String description;
  private final Set<ERole> includedRoles;
  private final Set<EPermission> directPermissions;
  private final Set<EPermissionGroup> permissionGroups;
  private final Set<EPermission> permissions;

  ERole(
      String description,
      Set<ERole> includedRoles,
      Set<EPermission> directPermissions,
      Set<EPermissionGroup> permissionGroups) {
    this.description = description;
    this.includedRoles = Set.copyOf(includedRoles);
    this.directPermissions = Set.copyOf(directPermissions);
    this.permissionGroups = Set.copyOf(permissionGroups);
    this.permissions = flatten(this.includedRoles, this.directPermissions, this.permissionGroups);
  }

  public String description() {
    return description;
  }

  public Set<EPermission> permissions() {
    return permissions;
  }

  public Set<ERole> includedRoles() {
    return includedRoles;
  }

  public Set<EPermission> directPermissions() {
    return directPermissions;
  }

  public Set<EPermissionGroup> permissionGroups() {
    return permissionGroups;
  }

  private static Set<EPermission> flatten(
      Set<ERole> includedRoles,
      Set<EPermission> directPermissions,
      Set<EPermissionGroup> permissionGroups) {
    Set<EPermission> flattened = new HashSet<>(directPermissions);

    for (ERole included : includedRoles) {
      for (EPermission permission : included.permissions()) {
        if (!flattened.add(permission)) {
          throw new IllegalStateException(
              "Permission "
                  + permission
                  + " is declared twice, including via role "
                  + included
                  + "; remove the redundant declaration.");
        }
      }
    }

    for (EPermissionGroup group : permissionGroups) {
      for (EPermission permission : group.permissions()) {
        if (!flattened.add(permission)) {
          throw new IllegalStateException(
              "Permission "
                  + permission
                  + " is declared both directly and via group "
                  + group
                  + "; remove the redundant declaration.");
        }
      }
    }

    return Set.copyOf(flattened);
  }
}
