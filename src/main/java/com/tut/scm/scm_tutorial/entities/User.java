package com.tut.scm.scm_tutorial.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="users_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements UserDetails {


        @Id
        private String userId;

        @Column(name="user_name",nullable = false)
        private String name;

        @Column(unique=true,nullable=false)
        private String email;

        @Getter(value=AccessLevel.NONE)
        private String password;

        @Column(length=1000)
        private String about;

        private String gender;

        @Column(length=1000)
        private String profilePic;

        private String phoneNumber;

        private boolean enabled=false;

        private boolean emailVerified=false;

        private boolean phoneVerified=false;

        //user signup with SELF Goggle GitHub

        @Enumerated(value=EnumType.STRING)
        private Providers provider=Providers.SELF;
        private String providersUserId;

        // add more field if needed
        @OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval=true)
        private List<Contact>contacts=new ArrayList<>();

        @ElementCollection(fetch=FetchType.EAGER)
        private List<String>roleList=new ArrayList<>();


        private String emailToken;
        

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                //list of roles[USER,ADMIN]
                //Collection of SimpleGrantedAuthority[roles{ADMIN,USER}]
             Collection<SimpleGrantedAuthority>roles= roleList.stream()
                .map(role-> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
             return  roles;
        }

        @Override
        public String getUsername() {
                return this.email;
        }

        @Override
        public String getPassword() {
               
                return this.password;
        }

        @Override
        public boolean isEnabled(){
            return this.enabled;    
        }


}
