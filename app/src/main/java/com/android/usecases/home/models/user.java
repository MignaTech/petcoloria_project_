package com.android.usecases.home.models;

    public class user {
        private String name;
        private String username;
        private String email;
        private String password;
        private String typeuser;
        private String address;
        private String phone;
        private String cedula;
        private String institucion;


        public void setPhone(String phone) {
            this.phone = phone;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public void setAddress(String address){
            this.address = address;
        }
        public void setTypeUser(String typeuser) {
            this.typeuser = typeuser;
        }
        public void setCedula(String cedula) {
            this.cedula = cedula;
        }
        public void setInstitucion(String institucion) {
            this.institucion = institucion;
        }

        public String getPassword() {
            return password;
        }
        public String getUsername() {
            return username;
        }
        public String getPhone() {
            return phone;
        }
        public String getName() {
            return name;
        }
        public String getEmail() {
            return email;
        }
        public String getAddress() {
            return address;
        }
        public String getTypeUser() {
            return typeuser;
        }
        public String getCedula() {
            return cedula;
        }
        public String getInstitucion() {
            return institucion;
        }
    }
