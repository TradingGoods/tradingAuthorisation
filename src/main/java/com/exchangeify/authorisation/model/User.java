package com.exchangeify.authorisation.model;

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
        
        @Override
        public String toString() {
            return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
                    + ", profilePicUrl=" + profilePicUrl + ", phoneNumber=" + phoneNumber + ", createdDate="
                    + createdDate + ", privateKey=" + privateKey + "]";
        }
        public User(String id, String firstName, String lastName, String emailId, String profilePicUrl,
                String phoneNumber, String createdDate, String privateKey) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.emailId = emailId;
            this.profilePicUrl = profilePicUrl;
            this.phoneNumber = phoneNumber;
            this.createdDate = createdDate;
            this.privateKey = privateKey;
        }
        
		
        
}
