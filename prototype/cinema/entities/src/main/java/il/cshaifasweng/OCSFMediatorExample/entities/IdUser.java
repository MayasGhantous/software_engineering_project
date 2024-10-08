package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "id_users")
public class IdUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auto_number_id_user;

    private String user_id;
    private String user_name;
    private String phone_number;
    private String email;
    private boolean isLoggedIn;

    @OneToMany(mappedBy = "id_user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserPurchases> user_purchases;

    @OneToMany(mappedBy = "id_user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MultiEntryTicket> multi_entry_ticket;

    @OneToMany(mappedBy = "id_user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Complains> complains;

    // Default constructor
    public IdUser() {}



    // constructor
    public IdUser(String user_id, String user_name, String phone_number, String email) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.phone_number = phone_number;
        this.email = email;
        this.isLoggedIn = false;
    }

    // getter and setter
    public int getAuto_number_id_users() {
        return auto_number_id_user;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getName() {
        return user_name;
    }

    public void setName(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public boolean getIsLoggedIn() {return isLoggedIn;}
    
    public void setIsLoggedIn(boolean isLoggedIn) {this.isLoggedIn = isLoggedIn;}

    public List<UserPurchases> getUser_purchases() {
        return user_purchases;
    }

    public void setUser_purchases(List<UserPurchases> user_purchases) {
        this.user_purchases = user_purchases;
    }

    public List<MultiEntryTicket> getMulti_entry_ticket() {
        return multi_entry_ticket;
    }

    public void setMulti_entry_ticket(List<MultiEntryTicket> multi_entry_ticket) {
        this.multi_entry_ticket = multi_entry_ticket;
    }

    public List<Complains> getComplains() {
        return complains;
    }

    public void setComplains(List<Complains> complains) {
        this.complains = complains;
    }

    public void setAuto_number_id_user(int id){
        this.auto_number_id_user = id;
    }
}

