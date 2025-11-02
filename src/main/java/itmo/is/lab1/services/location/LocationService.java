package itmo.is.lab1.services.location;

import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.product.Address;
import itmo.is.lab1.models.product.Organization;
import itmo.is.lab1.repositories.AddressRepository;
import itmo.is.lab1.repositories.LocationRepository;
import itmo.is.lab1.repositories.OrganizationRepository;
import itmo.is.lab1.repositories.PersonRepository;
import itmo.is.lab1.repositories.ProductRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.location.requests.LocationRequest;
import itmo.is.lab1.services.location.responses.LocationGetResponse;
import itmo.is.lab1.services.location.responses.LocationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService extends GeneralService<LocationRequest, LocationResponse, Location, LocationRepository> {

    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;
    private final OrganizationRepository organizationRepository;

    public LocationService(AddressRepository addressRepository,
                           PersonRepository personRepository,
                           ProductRepository productRepository,
                           OrganizationRepository organizationRepository) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.organizationRepository = organizationRepository;
    }

    protected LocationGetResponse buildResponse(Location location) {
        LocationGetResponse locationGetResponse = new LocationGetResponse();
        locationGetResponse.setValues(location);
        return locationGetResponse;
    }

    protected Location buildEntity(LocationRequest locationReq) {
        Location location = new Location();
        location.setValues(locationReq, userService.getCurrentUser());
        return location;
    }

    @Override
    @Transactional
    public itmo.is.lab1.services.common.responses.GeneralMessageResponse deleteById(Long id) {
        // 1) ?????? ???? ???????
        List<Address> addresses = addressRepository.findByTown_Id(id);
        // 1.1) ???????? officialAddress ? ???????????, ??????????? ?? ??? ??????
        List<Organization> toUpdate = new ArrayList<>();
        for (Address a : addresses) {
            List<Organization> orgs = organizationRepository.findByOfficialAddress_Id(a.getId());
            for (Organization o : orgs) {
                o.setOfficialAddress(null);
                toUpdate.add(o);
            }
        }
        if (!toUpdate.isEmpty()) organizationRepository.saveAll(toUpdate);
        // 1.2) ??????? ??????
        if (!addresses.isEmpty()) addressRepository.deleteAll(addresses);

        // 2) ??????? ?????? (? ?? ????????), ??? ??? Person.location ?? null ?? ??
        var persons = personRepository.findByLocation_Id(id);
        for (var p : persons) { productRepository.deleteByOwner_Id(p.getId()); }
        if (!persons.isEmpty()) personRepository.deleteAll(persons);

        // 3) ??????? ???? ???????
        return super.deleteById(id);
    }
}
