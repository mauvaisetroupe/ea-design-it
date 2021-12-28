export enum Authority {
  ADMIN = 'ROLE_ADMIN', // User managemtn, API, & Actuator
  USER = 'ROLE_USER', // for READ ACCESS if AccountService.anonymousReadAllowed = false
  ANONYMOUS_ALLOWED = 'ANONYMOUS_ALLOWED',
  CONTRIBUTOR = 'ROLE_CONTRIBUTOR', // Write Access only if is Owner
  WRITE = 'ROLE_WRITE', // Write access on all object except hard delete
  HARD_DELETE = 'ROLE_HARD_DELETE', // Delete Access
}
