package biz.mercue.impactweb.configuration;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import biz.mercue.impactweb.util.Constants;



@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = "biz.mercue.impactweb")
public class MercueConfiguration implements WebMvcConfigurer{
	
	private Logger log = Logger.getLogger(MercueConfiguration.class);
	
//    @Autowired
//    QuartzService quartzService;
    
    @Autowired
    YamlConfiguration yamlConfig;
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		registry.viewResolver(viewResolver);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		
		Constants.IMAGE_UPLOAD_PATH = yamlConfig.getConfig().getPath().get("image");
		Constants.IMAGE_LOAD_URL = yamlConfig.getConfig().getUrl().get("image");
		Constants.VIDEO_UPLOAD_PATH = yamlConfig.getConfig().getPath().get("video");
		Constants.VIDEO_LOAD_URL = yamlConfig.getConfig().getUrl().get("video");
		Constants.FILE_UPLOAD_PATH = yamlConfig.getConfig().getPath().get("file");
		Constants.FILE_LOAD_URL = yamlConfig.getConfig().getUrl().get("file");
		
		Constants.SYNC_FILE_UPLOAD_PATH = yamlConfig.getConfig().getPath().get("sync_file");
		Constants.SYNC_FILE_LOAD_URL = yamlConfig.getConfig().getUrl().get("sync_file");
		
		
		
		Constants.MAIL_ACCOUNT = yamlConfig.getConfig().getMail().get("mail_account");
		Constants.MAIL_PASSWORD = yamlConfig.getConfig().getMail().get("mail_pwd");
		Constants.MAIL_HOST = yamlConfig.getConfig().getMail().get("mail_host");
		Constants.MAIL_PORT = yamlConfig.getConfig().getMail().get("mail_host_port");
		Constants.MAIL_STARTTLS = yamlConfig.getConfig().getMail().get("mail_starttls");
		Constants.MAIL_FROM = yamlConfig.getConfig().getMail().get("mail_from");
		
		
		Constants.TUYA_CLIENT_ID = yamlConfig.getConfig().getTuya().get("client_id");
		Constants.TUYA_SECRET_KEY = yamlConfig.getConfig().getTuya().get("secret_key");
		Constants.TUYA_SCHEMA = yamlConfig.getConfig().getTuya().get("schema");
		
		
	

	}
	
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean(name="multipartResolver") 
	public CommonsMultipartResolver getResolver() throws IOException{
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		
		//Set the maximum allowed size (in bytes) for each individual file.
		resolver.setMaxUploadSizePerFile(52428800);//50MB
		
		//You may also set other available properties.
		return resolver;
	}

}