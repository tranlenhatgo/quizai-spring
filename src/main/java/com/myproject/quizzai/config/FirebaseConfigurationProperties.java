package com.myproject.quizzai.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "com.properties.database")
public class FirebaseConfigurationProperties {
    @Valid
    private FireBase fireBase = new FireBase();

    @Getter
    @Setter
    public class FireBase {
        @NotBlank(message = "Path to service account key file must be configured")
        private String serviceAccountKeyPath;
    }

}
