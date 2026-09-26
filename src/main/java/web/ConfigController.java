package kz.iitu.springlab.web;

import kz.iitu.springlab.config.AppProperties;
import kz.iitu.springlab.config.EnvironmentBanner;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/lab3")
public class ConfigController {

    private final AppProperties props;
    private final EnvironmentBanner banner;
    private final Environment environment;

    public ConfigController(AppProperties props, EnvironmentBanner banner, Environment environment) {
        this.props = props;
        this.banner = banner;
        this.environment = environment;
    }

    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("owner", props.owner());
        result.put("group", props.group());
        result.put("mailFrom", props.mail().from());
        result.put("mailRetryCount", props.mail().retryCount());
        result.put("mailTimeout", props.mail().timeout().toString());
        result.put("mailEnabled", props.mail().enabled());
        result.put("serverPort", environment.getProperty("server.port"));
        result.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));
        result.put("banner", banner.describe());
        result.put("exportFormats", props.export().formats());
        result.put("exportMaxRows", props.export().maxRows());
        return result;
    }
}
