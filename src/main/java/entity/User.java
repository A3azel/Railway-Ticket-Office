package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_id", nullable = false)
    private UserRole userRole;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_mail_id")
    private UserMail userMail;

    @Column(name = "user_phone", unique = true)
    private String userPhone;

    @Column(name = "user_email", unique = true)
    private String userEmail;
}
