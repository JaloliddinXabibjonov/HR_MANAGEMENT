package uz.pdp.hr_management1.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.hr_management1.entity.template.BaseEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "users")
public class User extends BaseEntity implements UserDetails {

//    @Column(nullable = false , length = 50)
    private String firstName; //ismi

//    @Column(nullable = false,length = 50)
    private String lastName; //familiyasi

    @Column(unique = true, nullable = false)
    private String email; //userning email pochtasi. Mas: Example@gmail.com

    @Column(nullable = false)
    private String password; //userning paroli

    @ManyToMany(fetch= FetchType.EAGER)
    private Set<Role> roles;


    private boolean accountNonExpired=true;    // bu userning amal qilish muddati o`tmaganligi
    private boolean accountNonLocked=true;      // bu user bloklanmagan
    private boolean credentialsNonExpired=true;
    private boolean enabled;

    private String emailCode;

    public User(String email, String password, Set<Role> roles, boolean enabled) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
    }

    // ----------------BU USERDETAILSNING METODLARI-------------------
    //BU USERNING HUQUQLARI RO`YHATI
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    //BU USERNING USERNAME NI QAYTARUVCHI METOD
    @Override
    public String getUsername() {
        return this.email;
    }

    //ACCOUNTNING AMAL QILISH MUDDATI QAYTARADI
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    //ACCOUNTNI BLOKLANGANLIGI HOLATI
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    //ACCOUNTNING ISHONCHLILIK MUDDATI TUGAGAN YOKI TUGAMAGANLIGINI QAYTARADI
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    //ACCOUNTNING AKTIVLIGINI QAYTARADI
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
