package user;

public class AuthenticationService {
    private UserService userService;
    private User currentUser;
    
    public AuthenticationService(UserService userService) {
        this.userService = userService;
        this.currentUser = null;
    }
    
    public boolean login(String username, String password) {
        User user = userService.authenticateUser(username, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logout() {
        this.currentUser = null;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isCurrentUserAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
    
    public boolean isCurrentUserRegularUser() {
        return currentUser != null && currentUser.isUser();
    }
    
    public String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }
    
    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }
    
    public boolean requiresAuthentication() {
        return !isLoggedIn();
    }
    
    public boolean hasAdminAccess() {
        return isLoggedIn() && isCurrentUserAdmin();
    }
    
    public boolean hasUserAccess() {
        return isLoggedIn() && (isCurrentUserAdmin() || isCurrentUserRegularUser());
    }
}