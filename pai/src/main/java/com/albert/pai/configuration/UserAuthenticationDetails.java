package com.albert.pai.configuration;
import com.albert.pai.dao.userDao;
import com.albert.pai.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
@Component
public class UserAuthenticationDetails implements UserDetailsService {

    /*
    Działanie tej klasy umożliwia logowanie do środowiska Spring Security. Klasa posiada jedną
    metodę loadUserByUsername, której parametrem jest ciąg znaków. Metoda wywoływana
    jest w trakcie logowania użytkownika, po wpisaniu i zaakceptowaniu przez niego danych
    w formularzu logowania. Naszym zadaniem w tym przypadku jest pobranie z bazy danych
    użytkownika o wskazanej przez parametr nazwie. W przypadku, gdy nie ma takiego
    użytkownika - wyrzucany jest wyjątek ze stosowną informacją. W ten sposób na podstawie
    połączenia do bazy danych realizowane jest logowanie do systemu przy pomocy klas
    zawartych w pakiecie *.configuration.
    */

    @Autowired
    private userDao dao;
    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {
        User user = dao.findByLogin(login);
        if (user != null) {
            List <GrantedAuthority> grupa = new ArrayList<>();
            grupa.add(new SimpleGrantedAuthority("normalUser"));
            return new
                    org.springframework.security.core.userdetails.User(user.getLogin(),
                    user.getPassword(), true,
                    true, true, true, grupa);
        } else {
            throw new UsernameNotFoundException("Zły login lub hasło...");
        }
    }
}