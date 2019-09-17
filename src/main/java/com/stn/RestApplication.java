package com.stn;

import com.stn.ester.core.configurations.DatabaseConfig;
import com.stn.ester.entities.Biodata;
import com.stn.ester.entities.User;
import com.stn.ester.entities.UserGroup;
import com.stn.ester.services.crud.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import static com.stn.ester.core.security.SecurityConstants.ROLE_SUPERADMIN;

@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
@EnableRabbit
public class RestApplication extends SpringBootServletInitializer {

    @Value("${ester.server.timezone}")
    private String timezone;

    @Value("${queue.access-log.name}")
    private String queueName;

    @Autowired
    private AssetFileService assetFileService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private MenuService menuService;

    public static void main(String[] args) {
        if (args.length > 0) {
            String extJsonFile = args[0];
            if (!extJsonFile.isEmpty()) {
                DatabaseConfig.extJsonFileConfigPath = extJsonFile;
            }
        }
        SpringApplication.run(RestApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        this.addDefaultProfilePicture();
        this.addSuperAdmin();
        this.addDefaultSystemProfile();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RestApplication.class);
    }

    private void addDefaultProfilePicture() {
        this.assetFileService.addDefaultProfilePicture();
    }

    private void addDefaultSystemProfile() {
        this.assetFileService.addDefaultSystemProfileIfDoesntExist();
    }

    public void addSuperAdmin() {
        UserGroup userGroup = new UserGroup();
        userGroup.setName(ROLE_SUPERADMIN);
        userGroup.setLabel("Super Admin");
        userGroup = (UserGroup) userGroupService.createIfNameNotExist(userGroup, ROLE_SUPERADMIN);
        User user = new User();
        String username = "admin";
        user.setUsername(username);
        user.setPassword("adminstn");
        user.setEmail("info@suryateknologi.co.id");
        user.setUserGroupId(userGroup.getId());
        Biodata biodata = new Biodata();
        biodata.setFirstName("STN");
        biodata.setLastName("Ester");
        biodata.setUser(user);
        user.setBiodata(biodata);
        userService.createIfUsernameNotExist(user, username);
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
