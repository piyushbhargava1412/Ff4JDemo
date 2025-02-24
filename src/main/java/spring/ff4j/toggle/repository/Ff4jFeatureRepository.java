package spring.ff4j.toggle.repository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.ff4j.exception.FeatureNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
public class Ff4jFeatureRepository {

    private final FF4j ff4j;

    public Ff4jFeatureRepository(FF4j ff4j) {
        this.ff4j = ff4j;
    }

    @PostConstruct
    public void checkAndCreateFeatures() {
        for (Features value : Features.values()) {
            if (!ff4j.exist(value.getFeatureName())) {
                ff4j.createFeature(new Feature(value.getFeatureName(), false));
                log.info("Feature '{}' created and disabled by default.", value.getFeatureName());
            }
        }
    }

    public boolean isEnabled(String featureName) {
        try {
            return ff4j.check(featureName);
        } catch (FeatureNotFoundException e) {
            log.error("Feature '{}' has been requested, but it does not exist", featureName);
            return false;
        }
    }

    public void enable(String featureName) {
        try {
            ff4j.enable(featureName);
            log.info("Feature '{}' enabled.", featureName);
        } catch (FeatureNotFoundException e) {
            log.error("Enable Failed :: Feature '{}' does not exist.", featureName);
        }
    }

    public void disable(String featureName) {
        try {
            ff4j.disable(featureName);
            log.info("Feature '{}' disabled.", featureName);
        } catch (FeatureNotFoundException e) {
            log.error("Disable Failed :: Feature '{}' does not exist.", featureName);
        }
    }

    public Map<String, Feature> getAllFeatures() {
        return ff4j.getFeatures();
    }
}

