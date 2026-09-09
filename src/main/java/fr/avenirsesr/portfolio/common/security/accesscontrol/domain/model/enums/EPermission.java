package fr.avenirsesr.portfolio.common.security.accesscontrol.domain.model.enums;

public enum EPermission {
  PERM_PROFILE_READ_OWN("profile:read:own", "View own profile"),
  PERM_PROFILE_UPDATE_OWN("profile:update:own", "Update own profile or photo"),

  PERM_TRACE_CREATE_OWN("trace:create:own", "Add a trace to own personal space"),
  PERM_TRACE_LIST_OWN("trace:list:own", "View the list of own traces"),
  PERM_TRACE_READ_CONTEXTUAL(
      "trace:read:contextual", "View a trace made accessible in an authorized context"),
  PERM_TRACE_DOWNLOAD_CONTEXTUAL(
      "trace:download:contextual", "Download a trace made accessible in an authorized context"),
  PERM_TRACE_ASSOCIATION_MANAGE_OWN(
      "trace:association:manage:own",
      "Associate a trace with a competency, an activity or an experience"),
  PERM_TRACE_UPDATE_OWN("trace:update:own", "Update own trace or add an attachment"),
  PERM_TRACE_DELETE_OWN("trace:delete:own", "Delete own traces"),

  PERM_COMPETENCY_READ("competency:read", "View the target competencies of the framework"),

  PERM_EXTERNAL_SKILL_READ("external-skill:read", "View the external skill catalog"),

  PERM_DECLARED_ACTIVITY_LIST_OWN(
      "declared-activity:list:own",
      "View the list of own declared activities and their associations"),
  PERM_DECLARED_ACTIVITY_UPDATE_OWN(
      "declared-activity:update:own", "Update, finish or add a reflection to a declared activity"),
  PERM_DECLARED_ACTIVITY_ASSOCIATION_MANAGE_OWN(
      "declared-activity:association:manage:own", "Remove associations of a declared activity"),

  PERM_DECLARED_SKILL_LIST_OWN("declared-skill:list:own", "View the list of own declared skills"),
  PERM_DECLARED_SKILL_CREATE_OWN("declared-skill:create:own", "Add a declared skill"),
  PERM_DECLARED_SKILL_UPDATE_OWN("declared-skill:update:own", "Update a declared skill"),
  PERM_DECLARED_SKILL_DELETE_OWN("declared-skill:delete:own", "Delete a declared skill"),
  PERM_DECLARED_SKILL_ASSOCIATION_MANAGE_OWN(
      "declared-skill:association:manage:own", "Add or remove associations of a declared skill"),

  PERM_DECLARED_EXPERIENCE_LIST_OWN(
      "declared-experience:list:own", "View the list of own declared experiences"),
  PERM_DECLARED_EXPERIENCE_CREATE_OWN(
      "declared-experience:create:own", "Add a declared experience"),
  PERM_DECLARED_EXPERIENCE_UPDATE_OWN(
      "declared-experience:update:own", "Update a declared experience"),
  PERM_DECLARED_EXPERIENCE_DELETE_OWN(
      "declared-experience:delete:own", "Delete a declared experience"),
  PERM_DECLARED_EXPERIENCE_ASSOCIATION_MANAGE_OWN(
      "declared-experience:association:manage:own",
      "Associate a trace or a competency with a declared experience"),

  PERM_DECLARED_PROGRAM_LIST_OWN(
      "declared-program:list:own", "View the list of own declared programs"),
  PERM_DECLARED_PROGRAM_CREATE_OWN("declared-program:create:own", "Add a declared program"),
  PERM_DECLARED_PROGRAM_UPDATE_OWN("declared-program:update:own", "Update a declared program"),
  PERM_DECLARED_PROGRAM_DELETE_OWN("declared-program:delete:own", "Delete a declared program"),

  PERM_SELF_KNOWLEDGE_LIST_OWN(
      "self-knowledge:list:own", "View own self-knowledge elements and categories"),
  PERM_SELF_KNOWLEDGE_CREATE_OWN(
      "self-knowledge:create:own", "Add a self-knowledge element or category"),
  PERM_SELF_KNOWLEDGE_UPDATE_OWN("self-knowledge:update:own", "Update a self-knowledge element"),
  PERM_SELF_KNOWLEDGE_DELETE_OWN(
      "self-knowledge:delete:own", "Delete a self-knowledge element or category"),

  PERM_NOTIFICATION_READ_OWN("notification:read:own", "View own notifications"),
  PERM_NOTIFICATION_UPDATE_OWN("notification:update:own", "Mark own notification as seen"),

  PERM_ACTIVITY_CATALOG_READ_CONTEXTUAL(
      "activity:catalog:read:contextual", "View the activity catalog"),
  PERM_ACTIVITY_REGISTER_OWN("activity:register:own", "Register for an activity"),
  PERM_ACTIVITY_READ_CONTEXTUAL("activity:read:contextual", "View activity details"),
  PERM_ACTIVITY_DOCUMENT_DOWNLOAD_CONTEXTUAL(
      "activity:document:download:contextual", "Download an activity's documents and links"),
  PERM_ACTIVITY_DOCUMENT_READ_CONTEXTUAL(
      "activity:document:read:contextual", "View an activity's documents and links"),
  PERM_EMPLOYMENT_KIT_READ_OWN("employment-kit:read:own", "View the My employment kit page"),
  PERM_ACTIVITY_LIBRARY_STAFF_READ(
      "activity:library:staff:read", "View the staff activity library"),
  PERM_ACTIVITY_CREATE("activity:create", "Create an activity"),
  PERM_ACTIVITY_DUPLICATE("activity:duplicate", "Duplicate an activity"),
  PERM_ACTIVITY_UPDATE("activity:update", "Update the content of an unpublished activity"),
  PERM_ACTIVITY_PUBLISHED_UPDATE_CONTEXTUAL(
      "activity:published:update:contextual", "Update a published activity"),
  PERM_ACTIVITY_DELETE("activity:delete", "Delete an unpublished activity"),
  PERM_ACTIVITY_FEEDBACK_SETTINGS_UPDATE(
      "activity:feedback-settings:update",
      "Configure the feedback request settings of an activity"),

  PERM_FEEDBACK_REQUEST_CREATE_OWN("feedback:request:create:own", "Request feedback"),
  PERM_FEEDBACK_RECEIVED_READ_OWN("feedback:received:read:own", "View received feedback"),
  PERM_FEEDBACK_REQUEST_READ_ASSIGNED_CONTEXTUAL(
      "feedback:request:read:assigned", "View feedback requests assigned to them"),
  PERM_FEEDBACK_REQUEST_RESPOND_ASSIGNED_CONTEXTUAL(
      "feedback:request:respond:assigned", "Respond to an assigned feedback request"),
  PERM_FEEDBACK_HISTORY_READ_CONTEXTUAL(
      "feedback:history:read:contextual", "View the feedback history of their institution"),
  PERM_FEEDBACK_DASHBOARD_READ_CONTEXTUAL(
      "feedback:dashboard:read:contextual", "View the feedback dashboard of their institution"),

  PERM_PRIMARY_ESTABLISHMENT_READ("primary-establishment:read", "View a primary institution"),
  PERM_PRIMARY_ESTABLISHMENT_CREATE("primary-establishment:create", "Create a primary institution"),
  PERM_PRIMARY_ESTABLISHMENT_UPDATE("primary-establishment:update", "Update a primary institution"),
  PERM_PRIMARY_ESTABLISHMENT_DELETE("primary-establishment:delete", "Delete a primary institution"),
  PERM_SECONDARY_ESTABLISHMENT_READ("secondary-establishment:read", "View a secondary institution"),
  PERM_SECONDARY_ESTABLISHMENT_CREATE(
      "secondary-establishment:create", "Create a secondary institution"),
  PERM_SECONDARY_ESTABLISHMENT_UPDATE(
      "secondary-establishment:update", "Update a secondary institution"),
  PERM_SECONDARY_ESTABLISHMENT_DELETE(
      "secondary-establishment:delete", "Delete a secondary institution"),
  PERM_GROUP_READ("group:read", "View a group"),
  PERM_GROUP_IMPORT("group:import", "Populate a group via import"),
  PERM_GROUP_CREATE("group:create", "Create a group"),
  PERM_GROUP_UPDATE("group:update", "Update a group"),
  PERM_GROUP_DELETE("group:delete", "Delete a group"),

  PERM_RBAC_READ(
      "rbac:read",
      "Read RBAC administration data: roles, permissions, assignments, scopes and contexts"),
  PERM_RBAC_ASSIGN("rbac:assign", "Create or grant an RBAC assignment to a principal"),
  PERM_RBAC_REVOKE("rbac:revoke", "Revoke an RBAC assignment"),
  PERM_RBAC_MANAGE(
      "rbac:manage", "Create, update or delete structuring RBAC elements such as roles"),

  PERM_ADDITIONAL_SKILL_CONFIG_READ(
      "additional-skill-config:read", "View the additional skill configuration"),
  PERM_ADDITIONAL_SKILL_CONFIG_UPDATE(
      "additional-skill-config:update", "Update the additional skill configuration"),
  PERM_INSTITUTION_CONFIG_READ("institution-config:read", "View an institution's configuration"),
  PERM_TRACE_CONFIG_READ("trace-config:read", "View the trace retention configuration"),
  PERM_TRACE_CONFIG_UPDATE("trace-config:update", "Update the trace retention configuration"),
  PERM_WEBSITE_CONTENT_CONFIG_READ(
      "website-content-config:read", "View the configurable website content"),
  PERM_WEBSITE_CONTENT_CONFIG_UPDATE(
      "website-content-config:update", "Update the configurable website content"),

  PERM_EXTERNAL_USER_READ("external-user:read", "View external users"),
  PERM_EXTERNAL_USER_IMPORT("external-user:import", "Import external users in bulk"),
  PERM_EXTERNAL_USER_UPDATE("external-user:update", "Update or activate an external user in bulk"),
  PERM_EXTERNAL_USER_DELETE("external-user:delete", "Delete an external user"),

  PERM_SEEDER_RESET(
      "seeder:reset", "Reset and reseed the database (destructive administration operation)"),

  PERM_KIT_DOWNLOAD_MEDIA_OWN(
      "kit:download-media:own", "Download all the featured multimedia content from your own kit");

  private final String authority;
  private final String description;

  EPermission(String authority, String description) {
    this.authority = authority;
    this.description = description;
  }

  public String authority() {
    return authority;
  }

  public String description() {
    return description;
  }

  public String legacyAuthority() {
    return name();
  }
}
