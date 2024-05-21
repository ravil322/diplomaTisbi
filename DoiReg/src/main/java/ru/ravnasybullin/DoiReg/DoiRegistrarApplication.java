package ru.ravnasybullin.DoiReg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ravnasybullin.DoiReg.models.User;
import ru.ravnasybullin.DoiReg.repositories.UserRepository;

import java.util.List;

@SpringBootApplication
public class DoiRegistrarApplication {

    @Autowired
    public DoiRegistrarApplication(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(DoiRegistrarApplication.class, args);
    }

    private final ApplicationContext applicationContext;


    @EventListener(ApplicationReadyEvent.class)
    public void eventListenerExecute() {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        UserRepository userRepository = applicationContext.getBean(UserRepository.class);

        // drop all active users on app restart
        if (((List<User>) userRepository.findAll()).size() == 0) {



            User admin = new User();
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("Rawnb123"));
            admin = userRepository.save(admin);
        }
    }
}
