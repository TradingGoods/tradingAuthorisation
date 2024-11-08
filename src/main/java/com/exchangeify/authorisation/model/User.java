package com.exchangeify.authorisation.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user")
public class User {

		private String id;
		private String firstName;
		private String lastName;

        @Id
		private String emailId;

        private String profilePicUrl;
        private String phoneNumber;
        private String createdDate;
        private String privateKey;
        private String password;
        private Set<String> roles = new HashSet<>();
        private String authProvider; // 'GOOGLE' or 'BASIC'
        
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public String getEmailId() {
            return emailId;
        }
        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }
        public String getProfilePicUrl() {
            return profilePicUrl;
        }
        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }
        public String getPhoneNumber() {
            return phoneNumber;
        }
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
        public String getCreatedDate() {
            return createdDate;
        }
        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }
        public String getPrivateKey() {
            return privateKey;
        }
        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public Set<String> getRoles() {
            return roles;
        }
        public void setRoles(Set<String> roles) {
            this.roles = roles;
        }
        public String getAuthProvider() {
            return authProvider;
        }
        public void setAuthProvider(String authProvider) {
            this.authProvider = authProvider;
        }
        
        @Override
        public String toString() {
            return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
                    + ", profilePicUrl=" + profilePicUrl + ", phoneNumber=" + phoneNumber + ", createdDate="
                    + createdDate + ", privateKey=" + privateKey + ", password=" + password + ", roles=" + roles
                    + ", authProvider=" + authProvider + "]";
        }
        public User(String id, String firstName, String lastName, String emailId, String profilePicUrl,
                String phoneNumber, String createdDate, String privateKey, String password, Set<String> roles,
                String authProvider) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.emailId = emailId;
            this.profilePicUrl = profilePicUrl;
            this.phoneNumber = phoneNumber;
            this.createdDate = createdDate;
            this.privateKey = privateKey;
            this.password = password;
            this.roles = roles;
            this.authProvider = authProvider;
        }
        
		
        
}
