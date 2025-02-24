package spring.ff4j.toggle.controller;

import java.util.Map;
import org.ff4j.core.Feature;
import org.springframework.web.bind.annotation.*;
import spring.ff4j.toggle.repository.Ff4jFeatureRepository;

@RestController
@RequestMapping("/api/features")
public class FeatureToggleController {

    private final Ff4jFeatureRepository ff4jFeatureRepository;

    public FeatureToggleController(Ff4jFeatureRepository ff4jFeatureRepository) {
        this.ff4jFeatureRepository = ff4jFeatureRepository;
    }

    @GetMapping
    public Map<String, Feature> getAllFeatures() {
        return ff4jFeatureRepository.getAllFeatures();
    }

    @PutMapping("/{featureName}/enable")
    public void enableFeature(@PathVariable String featureName) {
        ff4jFeatureRepository.enable(featureName);
    }

    @PutMapping("/{featureName}/disable")
    public void disableFeature(@PathVariable String featureName) {
        ff4jFeatureRepository.disable(featureName);
    }

    @GetMapping("/{featureName}")
    public String checkFeature(@PathVariable String featureName) {
        if(ff4jFeatureRepository.isEnabled(featureName))
            return featureName + " is enabled";
        return featureName + " is disabled or not found";
    }
}
