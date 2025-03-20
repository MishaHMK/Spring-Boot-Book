package book.project.bookstore.dto.internal.user;

public final class UserDto {
    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String shippingAddress;

    public UserDto(Long id, String email, String firstName, String lastName,
                   String shippingAddress) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shippingAddress = shippingAddress;
    }

    public Long id() {
        return id;
    }

    public String email() {
        return email;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String shippingAddress() {
        return shippingAddress;
    }
}
