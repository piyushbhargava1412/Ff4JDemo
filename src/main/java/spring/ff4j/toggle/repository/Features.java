package spring.ff4j.toggle.repository;

public enum Features {
    DUMMY_TOGGLE_1("DUMMY_TOGGLE_1"),
    DUMMY_TOGGLE_2("DUMMY_TOGGLE_2"),
    DUMMY_TOGGLE_3("DUMMY_TOGGLE_3");

    private final String name;

    Features(String name) {
        this.name = name;
    }

    public String getFeatureName() {
        return name;
    }
}
