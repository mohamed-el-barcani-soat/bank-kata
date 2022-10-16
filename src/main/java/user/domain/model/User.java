package user.domain.model;

public record User (
     String id,
     String name) {
    public String getId() {
        return id;
    }
}
