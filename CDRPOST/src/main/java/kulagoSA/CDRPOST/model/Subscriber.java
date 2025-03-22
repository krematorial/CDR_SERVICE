package kulagoSA.CDRPOST.model;
import org.springframework.data.annotation.Id;

public class Subscriber {

    @Id
    private Long id;
    private String name;
    private String phoneNumber;

    // Геттеры и сеттеры
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

}
