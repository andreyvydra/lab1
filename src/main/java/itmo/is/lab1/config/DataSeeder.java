package itmo.is.lab1.config;

import itmo.is.lab1.models.location.Coordinates;
import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.person.Person;
import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.models.person.enums.Country;
import itmo.is.lab1.models.product.Address;
import itmo.is.lab1.models.product.Organization;
import itmo.is.lab1.models.product.Product;
import itmo.is.lab1.models.product.enums.OrganizationType;
import itmo.is.lab1.models.product.enums.UnitOfMeasure;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.models.user.enums.Role;
import itmo.is.lab1.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
public class DataSeeder implements ApplicationRunner {
    private final UserRepository userRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final OrganizationRepository organizationRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) {
        if (productRepository.count() > 0 || organizationRepository.count() > 0 ||
                addressRepository.count() > 0 || personRepository.count() > 0 ||
                locationRepository.count() > 0 || coordinatesRepository.count() > 0) {
            return;
        }

        final int N = 20;
        Random rnd = new Random(42);

        List<User> users = new ArrayList<>();
        User u = new User();
        u.setUsername("aboba");
        u.setPassword(passwordEncoder.encode("aboba"));
        u.setRole(Role.ROLE_USER);
        users.add(u);

        User ad = new User();
        ad.setUsername("admin");
        ad.setPassword(passwordEncoder.encode("admin"));
        ad.setRole(Role.ROLE_ADMIN);
        userRepository.save(ad);

        users = userRepository.saveAll(users);

        List<Coordinates> coords = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            Coordinates c = new Coordinates();
            c.setX(rnd.nextInt(469));
            c.setY(-451 + rnd.nextInt(1000));
            c.setIsChangeable(true);
            c.setUser(users.get(rnd.nextInt(users.size())));
            coords.add(c);
        }
        coords = coordinatesRepository.saveAll(coords);

        List<Location> locations = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            Location l = new Location();
            l.setX(rnd.nextInt(1000));
            l.setY((long) rnd.nextInt(1000));
            l.setZ(rnd.nextInt(1000));
            l.setName("Loc-" + i);
            l.setIsChangeable(true);
            l.setUser(users.get(rnd.nextInt(users.size())));
            locations.add(l);
        }
        locations = locationRepository.saveAll(locations);

        List<Person> persons = new ArrayList<>();
        Color[] colors = Color.values();
        Country[] countries = Country.values();
        for (int i = 1; i <= N; i++) {
            Person p = new Person();
            p.setName("Person-" + i);
            p.setHairColor(colors[rnd.nextInt(colors.length)]);
            p.setEyeColor(colors[rnd.nextInt(colors.length)]);
            p.setNationality(countries[rnd.nextInt(countries.length)]);
            p.setLocation(locations.get(rnd.nextInt(locations.size())));
            p.setPassportID("PASS-" + i);
            p.setIsChangeable(true);
            p.setUser(users.get(rnd.nextInt(users.size())));
            p.setHeight(5);
            persons.add(p);
        }
        persons = personRepository.saveAll(persons);

        List<Address> addresses = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            Address a = new Address();
            a.setStreet("Street-" + i);
            a.setTown(locations.get(rnd.nextInt(locations.size())));
            a.setIsChangeable(true);
            a.setUser(users.get(rnd.nextInt(users.size())));
            addresses.add(a);
        }
        addresses = addressRepository.saveAll(addresses);

        List<Organization> orgs = new ArrayList<>();
        OrganizationType[] orgTypes = OrganizationType.values();
        for (int i = 1; i <= N; i++) {
            Organization o = new Organization();
            o.setName("Org-" + i);
            o.setOfficialAddress(addresses.get(rnd.nextInt(addresses.size())));
            o.setAnnualTurnover(1.0 + rnd.nextInt(1000));
            o.setEmployeesCount(1 + rnd.nextInt(500));
            o.setFullName("Organization Full Name " + i);
            o.setRating(rnd.nextInt(100) + 1.0);
            o.setType(orgTypes[rnd.nextInt(orgTypes.length)]);
            o.setIsChangeable(true);
            o.setUser(users.get(rnd.nextInt(users.size())));
            orgs.add(o);
        }
        orgs = organizationRepository.saveAll(orgs);

        UnitOfMeasure[] units = UnitOfMeasure.values();
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            Product pr = new Product();
            pr.setName("Product-" + i);
            pr.setCoordinates(coords.get(rnd.nextInt(coords.size())));
            pr.setUnitOfMeasure(units[rnd.nextInt(units.length)]);
            pr.setManufacturer(orgs.get(rnd.nextInt(orgs.size())));
            pr.setPrice((long) (1 + rnd.nextInt(10_000)));
            pr.setManufactureCost(rnd.nextInt(10_000));
            pr.setRating(1 + rnd.nextInt(100));
            pr.setOwner(persons.get(rnd.nextInt(persons.size())));
            pr.setIsChangeable(true);
            pr.setUser(users.get(rnd.nextInt(users.size())));
            products.add(pr);
        }
        productRepository.saveAll(products);
    }
}

