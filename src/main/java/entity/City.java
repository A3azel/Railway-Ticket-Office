package entity;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "city_name")
    private String cityName;

    @OneToMany(mappedBy = "city_id")
    private Set<Station> stationSet;
}
